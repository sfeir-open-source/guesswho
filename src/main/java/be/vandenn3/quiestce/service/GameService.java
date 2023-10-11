package be.vandenn3.quiestce.service;

import be.vandenn3.quiestce.domain.*;
import be.vandenn3.quiestce.repository.*;
import be.vandenn3.quiestce.service.dto.GameCardDTO;
import be.vandenn3.quiestce.service.dto.GameDTO;
import be.vandenn3.quiestce.service.dto.MessageDTO;
import be.vandenn3.quiestce.service.mapper.GameCardMapper;
import be.vandenn3.quiestce.service.mapper.GameMapper;
import be.vandenn3.quiestce.service.mapper.MessageMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@Transactional
public class GameService {
    private final GameRepository gameRepository;
    private final GameCardRepository gameCardRepository;
    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final GameMapper gameMapper;
    private final GameCardMapper gameCardMapper;

    private final MessageMapper messageMapper;
    private final ThemeRepository themeRepository;
    private final ThemeCardRepository themeCardRepository;

    public GameService(GameRepository gameRepository, GameCardRepository gameCardRepository,
                       MessageRepository messageRepository, RoomRepository roomRepository,
                       GameMapper gameMapper, GameCardMapper gameCardMapper, MessageMapper messageMapper,
                       ThemeRepository themeRepository, ThemeCardRepository themeCardRepository) {
        this.gameRepository = gameRepository;
        this.gameCardRepository = gameCardRepository;
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.gameMapper = gameMapper;
        this.gameCardMapper = gameCardMapper;
        this.messageMapper = messageMapper;
        this.themeRepository = themeRepository;
        this.themeCardRepository = themeCardRepository;
    }

    public List<GameDTO> getAllByRoom(Long roomId, Player currentPlayer) {
        checkAccessAndGetRoom(roomId, currentPlayer);
        return gameMapper.toDto(gameRepository.findAllByRoomId(roomId));
    }

    public GameDTO newGame(Long roomId, Long themeId, Player currentPlayer) {
        Room room = checkAccessAndGetRoom(roomId, currentPlayer);
        if (!isPreviousGameFinished(roomId)) {
            throw new AccessDeniedException("cannot create a new game while the previous one is not finished");
        }
        if (!areBothPlayersThere(room)) {
            throw new AccessDeniedException("cannot create a new game while there is not two users in the room");
        }

        Theme theme = themeRepository.findById(themeId).orElseThrow(() -> new IllegalArgumentException("theme does not exist"));
        List<ThemeCard> themeCards = themeCardRepository.findAllByThemeId(themeId);

        Game game = gameRepository.save(new Game().room(room).theme(theme));
        gameCardRepository.saveAll(themeCards.stream().map(themeCard -> new GameCard(game, themeCard)).toList());

        return getGameData(game, currentPlayer);
    }

    public GameDTO getGameDetails(Long gameId, Player currentPlayer) {
        Game game = checkAccessAndGetGame(gameId, currentPlayer);
        return getGameData(game, currentPlayer);
    }

    public GameDTO chooseCard(Long gameId, Long gameCardId, Player currentPlayer) {
        Game game = checkAccessAndGetGame(gameId, currentPlayer);
        if (hasGameBegun(game)) {
            throw new AccessDeniedException("cannot choose card - game has already started");
        }

        List<GameCard> gameCards = gameCardRepository.findAllByGameId(game.getId());
        GameCard selectedCard = gameCards.stream().filter(card -> gameCardId.equals(card.getId())).findAny()
            .orElseThrow(() -> new AccessDeniedException("invalid card ID"));

        boolean player1HasChosen = gameCards.stream().anyMatch(GameCard::getPlayer1_chosen);
        boolean player2HasChosen = gameCards.stream().anyMatch(GameCard::getPlayer2_chosen);
        boolean otherPlayerHasChosen;

        if (game.getRoom().isPlayer1(currentPlayer)) {
            if (player1HasChosen) {
                throw new AccessDeniedException("this player has already chosen a card");
            }
            selectedCard.setPlayer1_chosen(true);
            otherPlayerHasChosen = player2HasChosen;
        } else if (game.getRoom().isPlayer2(currentPlayer)) {
            if (player2HasChosen) {
                throw new AccessDeniedException("this player has already chosen a card");
            }
            selectedCard.setPlayer2_chosen(true);
            otherPlayerHasChosen = player1HasChosen;
        } else {
            throw new IllegalStateException("current player not part of the game");
        }

        gameCardRepository.save(selectedCard);

        if (otherPlayerHasChosen) {
            game.setStartDate(ZonedDateTime.now());
            game.setNextTurn(new Random().nextBoolean() ? game.getRoom().getPlayer1() : game.getRoom().getPlayer2());
        }

        return getGameData(game, currentPlayer);
    }

    public GameDTO play(Long gameId, List<Long> gameCardIds, Player currentPlayer) {
        Game game = checkAccessAndGetGame(gameId, currentPlayer);
        int currentPlayerIndex = currentPlayer.getIndexFor(game.getRoom());
        if (!hasGameBegun(game)) {
            throw new AccessDeniedException("cannot play - game has not started yet");
        }

        List<GameCard> allGameCards = gameCardRepository.findAllByGameId(game.getId());
        if (gameCardIds.stream().anyMatch(id -> allGameCards.stream().map(GameCard::getId).noneMatch(id::equals))) {
            throw new AccessDeniedException("some card ids are invalid");
        }
        List<GameCard> selectedGameCards = allGameCards.stream().filter(card -> gameCardIds.stream().anyMatch(cardId -> card.getId().equals(cardId))).toList();

        if (!currentPlayer.equals(game.getNextTurn())) {
            throw new AccessDeniedException("it is not your turn");
        }

        if (gameCardIds.stream().anyMatch(id -> allGameCards.stream().filter(card
                -> card.getId().equals(id)).findAny().orElseThrow().hasPlayerDiscarded(currentPlayerIndex))) {
            throw new AccessDeniedException("some card ids are already discarded");
        }

        selectedGameCards.forEach(card -> card.setPlayerDiscarded(currentPlayerIndex, true));
        gameCardRepository.saveAll(selectedGameCards);

        List<GameCard> updatedList = gameCardRepository.findAllByGameId(game.getId());
        List<GameCard> leftCards = updatedList.stream().filter(card -> !card.hasPlayerDiscarded(currentPlayerIndex)).toList();
        if (leftCards.size() <= 1) {
            GameCard otherPlayerChosenCard = updatedList.stream().filter(card -> card.hasPlayerChosen(currentPlayerIndex)).findAny().orElseThrow();
            if (leftCards.isEmpty() || leftCards.get(0).equals(otherPlayerChosenCard)) {
                setWinner(currentPlayer, game);
            } else {
                setWinner(game.getRoom().getOtherPlayer(currentPlayer), game);
            }
        } else {
            game.setNextTurn(game.getRoom().getOtherPlayer(currentPlayer));
        }
        gameRepository.save(game);

        return getGameData(game, currentPlayer);
    }

    public MessageDTO newMessage(Long gameId, String content, Player currentPlayer) {
        Game game = checkAccessAndGetGame(gameId, currentPlayer);
        Message message = new Message().game(game).content(content).creationDate(ZonedDateTime.now()).author(currentPlayer);
        return messageMapper.toDto(messageRepository.save(message));
    }

    public List<MessageDTO> getMessages(Long gameId, Long lastMessageId, Player currentPlayer) {
        checkAccessAndGetGame(gameId, currentPlayer);
        Long afterMessageId = Objects.isNull(lastMessageId) ? 0 : lastMessageId;
        return messageMapper.toDto(messageRepository.findAllByGameIdAndIdGreaterThan(gameId, afterMessageId));
    }

    private GameDTO getGameData(Game game, Player currentPlayer) {
        Room room = game.getRoom();
        game = gameRepository.findById(game.getId()).orElseThrow();
        GameDTO gameDTO = gameMapper.toDto(game);
        List<GameCardDTO> cards = gameCardMapper.toDto(gameCardRepository.findAllByGameId(game.getId()));
        if (Objects.isNull(game.getWinner())) {
            hideOtherPlayerData(cards, currentPlayer, room);
        }
        gameDTO.setGameCards(cards);
        return gameDTO;
    }

    private void setWinner(Player player, Game game) {
        game.setWinner(player);
        game.setEndDate(ZonedDateTime.now());
    }

    private void hideOtherPlayerData(List<GameCardDTO> gameCardDTOs, Player currentPlayer, Room room) {
        gameCardDTOs.forEach(card -> {
            if (currentPlayer.equals(room.getPlayer1())) {
                card.setPlayer2_chosen(false);
                card.setPlayer2_discarded(false);
            } else {
                card.setPlayer1_chosen(false);
                card.setPlayer1_discarded(false);
            }
        });
    }

    private boolean hasGameBegun(Game game) {
        return Objects.nonNull(game.getNextTurn());
    }

    private boolean areBothPlayersThere(Room room) {
        return Objects.nonNull(room.getPlayer1()) && Objects.nonNull(room.getPlayer2());
    }

    private boolean isPreviousGameFinished(Long roomId) {
        return gameRepository.findAllByRoomIdAndWinnerIsNull(roomId).isEmpty();
    }

    private Game checkAccessAndGetGame(Long gameId, Player currentPlayer) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new AccessDeniedException("user has not access to this room"));
        game.setRoom(checkAccessAndGetRoom(game.getRoom().getId(), currentPlayer));
        return game;
    }

    private Room checkAccessAndGetRoom(Long roomId, Player currentPlayer) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new AccessDeniedException("user has not access to this room"));
        boolean currentPlayerIsPlayer1 = Objects.nonNull(room.getPlayer1()) && room.getPlayer1().equals(currentPlayer);
        boolean currentPlayerIsPlayer2 = Objects.nonNull(room.getPlayer2()) && room.getPlayer2().equals(currentPlayer);
        if (!currentPlayerIsPlayer1 && !currentPlayerIsPlayer2) {
            throw new AccessDeniedException("user has not access to this room");
        }
        return room;
    }
}
