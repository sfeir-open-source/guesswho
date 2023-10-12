package be.vandenn3.quiestce.web.rest;

import be.vandenn3.quiestce.domain.*;
import be.vandenn3.quiestce.repository.*;
import be.vandenn3.quiestce.service.dto.GameCreationDTO;
import be.vandenn3.quiestce.service.dto.NewMessageDTO;
import be.vandenn3.quiestce.service.dto.PlayDTO;
import be.vandenn3.quiestce.service.dto.SelectCardDTO;
import be.vandenn3.quiestce.service.mapper.GameMapper;
import be.vandenn3.quiestce.service.mapper.MessageMapper;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static be.vandenn3.quiestce.session.CurrentPlayerManager.SESSION_KEY_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RoomResource} REST controller.
 */
class GameResourceIT extends IntegrationTestBase {

    private static final String ENTITY_API_URL = "/api/games";

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ThemeCardRepository themeCardRepository;

    @Autowired
    private GameCardRepository gameCardRepository;

    @Autowired
    private GameResource gameResource;

    @Autowired
    private MockMvc restGameMockMvc;

    @Autowired
    private GameMapper gameMapper;

    @Autowired
    private MessageMapper messageMapper;
    @Mock
    HttpSession mockHttpSession;

    private Room room;
    private Room otherRoom;
    private Game game;
    private Game otherGame;
    private Player otherPlayerInRoom;
    private Theme theme;
    private List<ThemeCard> themeCards;
    private List<GameCard> gameCards;

    @BeforeEach
    void setup() throws URISyntaxException {
        otherPlayerInRoom = createValidPlayer("player2");
        Player player3 = createValidPlayer("player3");

        Picture picture1 = pictureRepository.save(new Picture().path("picture1"));
        Picture picture2 = pictureRepository.save(new Picture().path("picture2"));
        Picture picture3 = pictureRepository.save(new Picture().path("picture2"));
        Picture picture4 = pictureRepository.save(new Picture().path("picture2"));
        theme = themeRepository.save(new Theme().name("test").main_picture(picture1));
        themeCards = new ArrayList<>();
        themeCards.add(themeCardRepository.save(new ThemeCard().name("card1").theme(theme).picture(picture2)));
        themeCards.add(themeCardRepository.save(new ThemeCard().name("card2").theme(theme).picture(picture3)));
        themeCards.add(themeCardRepository.save(new ThemeCard().name("card3").theme(theme).picture(picture4)));

        room = roomRepository.save(new Room().name("room1").player1(currentPlayer).player2(otherPlayerInRoom).code("tmp"));
        GameCreationDTO creationData = new GameCreationDTO();
        creationData.setRoomId(room.getId());
        creationData.setThemeId(theme.getId());
        when(mockHttpSession.getAttribute(SESSION_KEY_NAME)).thenReturn(currentPlayer);
        game = gameMapper.toEntity(gameResource.newGame(creationData, mockHttpSession).getBody());
        gameCards = gameCardRepository.findAllByGameId(game.getId());

        otherRoom = roomRepository.save(new Room().name("other-room").player1(otherPlayerInRoom).player2(player3).code("tmp2"));
        creationData.setRoomId(otherRoom.getId());
        when(mockHttpSession.getAttribute(SESSION_KEY_NAME)).thenReturn(otherPlayerInRoom);
        otherGame = gameMapper.toEntity(gameResource.newGame(creationData, mockHttpSession).getBody());
    }

    void startGame(int user1ChosenCard, int user2ChosenCard) {
        SelectCardDTO selectCardDTO = new SelectCardDTO();
        selectCardDTO.setGameCardId(gameCards.get(user1ChosenCard).getId());
        when(mockHttpSession.getAttribute(SESSION_KEY_NAME)).thenReturn(currentPlayer);
        gameResource.selectMyCard(game.getId(), selectCardDTO, mockHttpSession);
        when(mockHttpSession.getAttribute(SESSION_KEY_NAME)).thenReturn(otherPlayerInRoom);
        selectCardDTO.setGameCardId(gameCards.get(user2ChosenCard).getId());
        gameResource.selectMyCard(game.getId(), selectCardDTO, mockHttpSession);
        game = gameRepository.findById(game.getId()).orElseThrow();
    }

    void playFirstMoves(int cardToDiscard) {
        PlayDTO playDTO = new PlayDTO();
        playDTO.setGameCardIds(Collections.singletonList(gameCards.get(cardToDiscard).getId()));
        if (game.getNextTurn().equals(currentPlayer)) {
            when(mockHttpSession.getAttribute(SESSION_KEY_NAME)).thenReturn(currentPlayer);
            gameResource.play(game.getId(), playDTO, mockHttpSession);
            when(mockHttpSession.getAttribute(SESSION_KEY_NAME)).thenReturn(otherPlayerInRoom);
            gameResource.play(game.getId(), playDTO, mockHttpSession);
        } else {
            when(mockHttpSession.getAttribute(SESSION_KEY_NAME)).thenReturn(otherPlayerInRoom);
            gameResource.play(game.getId(), playDTO, mockHttpSession);
            when(mockHttpSession.getAttribute(SESSION_KEY_NAME)).thenReturn(currentPlayer);
            gameResource.play(game.getId(), playDTO, mockHttpSession);
        }
    }

    void winGameAsCurrentPlayer() {
        List<GameCard> gameCards = gameCardRepository.findAllByGameId(game.getId());
        PlayDTO playDTO = new PlayDTO();

        if (game.getNextTurn().equals(currentPlayer)) {
            playDTO.setGameCardIds(List.of(gameCards.get(0).getId()));
            when(mockHttpSession.getAttribute(SESSION_KEY_NAME)).thenReturn(currentPlayer);
        } else {
            playDTO.setGameCardIds(List.of(gameCards.get(0).getId(), gameCards.get(1).getId()));
            when(mockHttpSession.getAttribute(SESSION_KEY_NAME)).thenReturn(otherPlayerInRoom);
        }
        gameResource.play(game.getId(), playDTO, mockHttpSession);
    }

    Message sendMessage(String content, Player user) {
        NewMessageDTO newMessageDTO = new NewMessageDTO();
        newMessageDTO.setContent(content);
        when(mockHttpSession.getAttribute(SESSION_KEY_NAME)).thenReturn(user);
        return messageMapper.toEntity(gameResource.sendMessage(game.getId(), newMessageDTO, mockHttpSession));
    }

    @Test
    @Transactional
    void getGamesByRoom() throws Exception {
        restGameMockMvc
            .perform(
                get(ENTITY_API_URL).sessionAttrs(sessionattr)
                    .queryParam("roomId", room.getId().toString())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$.[0].id", is(game.getId()), Long.class));
    }

    @Test
    @Transactional
    void getGamesByRoomWhenNoAccess() throws Exception {
        restGameMockMvc
            .perform(
                get(ENTITY_API_URL).sessionAttrs(sessionattr)
                    .queryParam("roomId", otherRoom.getId().toString())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void newGame() throws Exception {
        gameRepository.delete(game);
        GameCreationDTO creationData = new GameCreationDTO();
        creationData.setRoomId(room.getId());
        creationData.setThemeId(theme.getId());
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL).sessionAttrs(sessionattr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creationData))
            )
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.startDate", is(nullValue())))
            .andExpect(jsonPath("$.endDate", is(nullValue())))
            .andExpect(jsonPath("$.winner", is(nullValue())))
            .andExpect(jsonPath("$.nextTurn", is(nullValue())))
            .andExpect(jsonPath("$.room.id", is(room.getId()), Long.class))
            .andExpect(jsonPath("$.theme.id", is(theme.getId()), Long.class))
            .andExpect(jsonPath("$.gameCards", hasSize(3)))
            .andExpect(jsonPath("$.gameCards[0].themeCard.id", is(themeCards.get(0).getId()), Long.class))
            .andExpect(jsonPath("$.gameCards[1].themeCard.id", is(themeCards.get(1).getId()), Long.class))
            .andExpect(jsonPath("$.gameCards[2].themeCard.id", is(themeCards.get(2).getId()), Long.class));
    }

    @Test
    @Transactional
    void newGameWhenNoAccess() throws Exception {
        GameCreationDTO creationData = new GameCreationDTO();
        creationData.setRoomId(otherRoom.getId());
        creationData.setThemeId(theme.getId());
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL).sessionAttrs(sessionattr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creationData))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void newGameWhenPreviousNotFinished() throws Exception {
        GameCreationDTO creationData = new GameCreationDTO();
        creationData.setRoomId(room.getId());
        creationData.setThemeId(theme.getId());
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL).sessionAttrs(sessionattr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creationData))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void newGameWhenBothPlayersNotThere() throws Exception {
        Room room = roomRepository.save(new Room().name("room1").player1(currentPlayer).code("room3-code"));
        GameCreationDTO creationData = new GameCreationDTO();
        creationData.setRoomId(room.getId());
        creationData.setThemeId(theme.getId());
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL).sessionAttrs(sessionattr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creationData))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void getGameDetails() throws Exception {
        startGame(0, 1);
        playFirstMoves(2);
        restGameMockMvc
            .perform(
                get(ENTITY_API_URL + "/" + game.getId()).sessionAttrs(sessionattr)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.startDate", is(notNullValue())))
            .andExpect(jsonPath("$.endDate", is(nullValue())))
            .andExpect(jsonPath("$.winner", is( nullValue())))
            .andExpect(jsonPath("$.nextTurn.id", is(in(List.of(currentPlayer.getId(), otherPlayerInRoom.getId()))), Long.class))
            .andExpect(jsonPath("$.room.id", is(room.getId()), Long.class))
            .andExpect(jsonPath("$.theme.id", is(theme.getId()), Long.class))
            .andExpect(jsonPath("$.gameCards", hasSize(3)))
            .andExpect(jsonPath("$.gameCards[*].player2_chosen", is(List.of(false, false, false))))
            .andExpect(jsonPath("$.gameCards[*].player2_discarded", is(List.of(false, false, false))));
    }

    @Test
    @Transactional
    void getGameDetailsWhenNoAccess() throws Exception {
        Map<String, Object> playerNotInRoomAttrs = Collections.singletonMap(SESSION_KEY_NAME, createValidPlayer("player3"));
        restGameMockMvc
            .perform(
                get(ENTITY_API_URL + "/" + game.getId()).sessionAttrs(playerNotInRoomAttrs)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void getGameDetailsWhenWon() throws Exception {
        startGame(0, 1);
        playFirstMoves(2);
        winGameAsCurrentPlayer();
        restGameMockMvc
            .perform(
                get(ENTITY_API_URL + "/" + game.getId()).sessionAttrs(sessionattr)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.startDate", is(notNullValue())))
            .andExpect(jsonPath("$.endDate", is(notNullValue())))
            .andExpect(jsonPath("$.winner.id", is(currentPlayer.getId()), Long.class))
            .andExpect(jsonPath("$.nextTurn.id", is(in(List.of(currentPlayer.getId(), otherPlayerInRoom.getId()))), Long.class))
            .andExpect(jsonPath("$.room.id", is(room.getId()), Long.class))
            .andExpect(jsonPath("$.theme.id", is(theme.getId()), Long.class))
            .andExpect(jsonPath("$.gameCards", hasSize(3)))
            .andExpect(jsonPath("$.gameCards[1].player2_chosen", is(true)))
            .andExpect(jsonPath("$.gameCards[2].player2_discarded", is(true)));
    }

    @Test
    @Transactional
    void selectMyCardAsFirstPlayer() throws Exception {
        SelectCardDTO selectCardDTO = new SelectCardDTO();
        selectCardDTO.setGameCardId(gameCards.get(0).getId());
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL + "/" + game.getId() + "/select-card").sessionAttrs(sessionattr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(selectCardDTO))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.startDate", is(nullValue())))
            .andExpect(jsonPath("$.endDate", is(nullValue())))
            .andExpect(jsonPath("$.winner", is(nullValue())))
            .andExpect(jsonPath("$.nextTurn", is(nullValue())))
            .andExpect(jsonPath("$.room.id", is(room.getId()), Long.class))
            .andExpect(jsonPath("$.theme.id", is(theme.getId()), Long.class))
            .andExpect(jsonPath("$.gameCards", hasSize(3)))
            .andExpect(jsonPath("$.gameCards[*].player2_chosen", is(List.of(false, false, false))))
            .andExpect(jsonPath("$.gameCards[*].player2_discarded", is(List.of(false, false, false))));
    }

    @Test
    @Transactional
    void selectMyCardAsSecondPlayer() throws Exception {
        // other player select his card:
        SelectCardDTO selectCardDTO = new SelectCardDTO();
        selectCardDTO.setGameCardId(gameCards.get(1).getId());
        when(mockHttpSession.getAttribute(SESSION_KEY_NAME)).thenReturn(otherPlayerInRoom);
        gameResource.selectMyCard(game.getId(), selectCardDTO, mockHttpSession);

        // current player select his card:
        selectCardDTO.setGameCardId(gameCards.get(0).getId());
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL + "/" + game.getId() + "/select-card").sessionAttrs(sessionattr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(selectCardDTO))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.startDate", is(notNullValue())))
            .andExpect(jsonPath("$.endDate", is(nullValue())))
            .andExpect(jsonPath("$.winner", is(nullValue())))
            .andExpect(jsonPath("$.nextTurn.id", is(in(List.of(currentPlayer.getId(), otherPlayerInRoom.getId()))), Long.class))
            .andExpect(jsonPath("$.room.id", is(room.getId()), Long.class))
            .andExpect(jsonPath("$.theme.id", is(theme.getId()), Long.class))
            .andExpect(jsonPath("$.gameCards", hasSize(3)))
            .andExpect(jsonPath("$.gameCards[*].player2_chosen", is(List.of(false, false, false))))
            .andExpect(jsonPath("$.gameCards[*].player2_discarded", is(List.of(false, false, false))));
    }

    @Test
    @Transactional
    void selectMyCardWhenNoAccess() throws Exception {
        Map<String, Object> playerNotInRoomAttrs = Collections.singletonMap(SESSION_KEY_NAME, createValidPlayer("player3"));
        SelectCardDTO selectCardDTO = new SelectCardDTO();
        selectCardDTO.setGameCardId(gameCards.get(0).getId());
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL + "/" + game.getId() + "/select-card").sessionAttrs(playerNotInRoomAttrs)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(selectCardDTO))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void selectMyCardWhenGameStarted() throws Exception {
        startGame(0, 1);
        SelectCardDTO selectCardDTO = new SelectCardDTO();
        selectCardDTO.setGameCardId(gameCards.get(0).getId());
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL + "/" + game.getId() + "/select-card").sessionAttrs(sessionattr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(selectCardDTO))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void selectMyCardWhenAlreadyChosen() throws Exception {
        selectMyCardAsFirstPlayer();
        SelectCardDTO selectCardDTO = new SelectCardDTO();
        selectCardDTO.setGameCardId(gameCards.get(0).getId());
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL + "/" + game.getId() + "/select-card").sessionAttrs(sessionattr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(selectCardDTO))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void selectMyCardWhenCardFromOtherGame() throws Exception {
        selectMyCardAsFirstPlayer();
        SelectCardDTO selectCardDTO = new SelectCardDTO();
        selectCardDTO.setGameCardId(gameCardRepository.findAllByGameId(otherGame.getId()).get(0).getId());
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL + "/" + game.getId() + "/select-card").sessionAttrs(sessionattr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(selectCardDTO))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void play() throws Exception {
        startGame(0, 1);

        Map<String, Object> localSessionAttr = sessionattr;
        int thisUserIndex = 1;
        int otherUserIndex = 2;
        if (!game.getNextTurn().equals(currentPlayer)) {
            localSessionAttr = Collections.singletonMap(SESSION_KEY_NAME, otherPlayerInRoom);
            thisUserIndex = 2;
            otherUserIndex = 1;
        }

        PlayDTO playDTO = new PlayDTO();
        playDTO.setGameCardIds(List.of(gameCards.get(2).getId()));
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL + "/" + game.getId() + "/play").sessionAttrs(localSessionAttr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playDTO))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.startDate", is(notNullValue())))
            .andExpect(jsonPath("$.endDate", is(nullValue())))
            .andExpect(jsonPath("$.winner", is(nullValue())))
            .andExpect(jsonPath("$.nextTurn.id", is(in(List.of(currentPlayer.getId(), otherPlayerInRoom.getId()))), Long.class))
            .andExpect(jsonPath("$.room.id", is(room.getId()), Long.class))
            .andExpect(jsonPath("$.theme.id", is(theme.getId()), Long.class))
            .andExpect(jsonPath("$.gameCards", hasSize(3)))
            .andExpect(jsonPath("$.gameCards[*].player"+otherUserIndex+"_chosen", is(List.of(false, false, false))))
            .andExpect(jsonPath("$.gameCards[*].player"+otherUserIndex+"_discarded", is(List.of(false, false, false))))
            .andExpect(jsonPath("$.gameCards[0].player"+thisUserIndex+"_discarded", is(false)))
            .andExpect(jsonPath("$.gameCards[1].player"+thisUserIndex+"_discarded", is(false)))
            .andExpect(jsonPath("$.gameCards[2].player"+thisUserIndex+"_discarded", is(true)));
    }

    @Test
    @Transactional
    void playWhenNoAccess() throws Exception {
        startGame(0, 1);

        Map<String, Object> playerNotInRoomAttrs = Collections.singletonMap(SESSION_KEY_NAME, createValidPlayer("player3"));

        PlayDTO playDTO = new PlayDTO();
        playDTO.setGameCardIds(List.of(gameCards.get(2).getId()));
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL + "/" + game.getId() + "/play").sessionAttrs(playerNotInRoomAttrs)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playDTO))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void playWhenNotYourTurn() throws Exception {
        startGame(0, 1);

        Map<String, Object> localSessionAttr = sessionattr;
        if (game.getNextTurn().equals(currentPlayer)) {
            localSessionAttr = Collections.singletonMap(SESSION_KEY_NAME, otherPlayerInRoom);
        }

        PlayDTO playDTO = new PlayDTO();
        playDTO.setGameCardIds(List.of(gameCards.get(2).getId()));
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL + "/" + game.getId() + "/play").sessionAttrs(localSessionAttr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playDTO))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void playWithAlreadyDiscardedCard() throws Exception {
        startGame(0, 1);
        int cardToTry = 2;
        playFirstMoves(cardToTry);

        Map<String, Object> localSessionAttr = sessionattr;
        if (!game.getNextTurn().equals(currentPlayer)) {
            localSessionAttr = Collections.singletonMap(SESSION_KEY_NAME, otherPlayerInRoom);
        }

        PlayDTO playDTO = new PlayDTO();
        playDTO.setGameCardIds(List.of(gameCards.get(cardToTry).getId()));
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL + "/" + game.getId() + "/play").sessionAttrs(localSessionAttr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playDTO))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void playAndWin() throws Exception {
        startGame(0, 1);

        Map<String, Object> localSessionAttr = sessionattr;
        List<Long> cardIds = List.of(gameCards.get(0).getId(), gameCards.get(2).getId());
        Long winnerId = currentPlayer.getId();
        if (!game.getNextTurn().equals(currentPlayer)) {
            localSessionAttr = Collections.singletonMap(SESSION_KEY_NAME, otherPlayerInRoom);
            cardIds = List.of(gameCards.get(1).getId(), gameCards.get(2).getId());
            winnerId = otherPlayerInRoom.getId();
        }

        PlayDTO playDTO = new PlayDTO();
        playDTO.setGameCardIds(cardIds);
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL + "/" + game.getId() + "/play").sessionAttrs(localSessionAttr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playDTO))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.startDate", is(notNullValue())))
            .andExpect(jsonPath("$.endDate", is(notNullValue())))
            .andExpect(jsonPath("$.winner.id", is(winnerId), Long.class))
            .andExpect(jsonPath("$.nextTurn.id", is(in(List.of(currentPlayer.getId(), otherPlayerInRoom.getId()))), Long.class))
            .andExpect(jsonPath("$.room.id", is(room.getId()), Long.class))
            .andExpect(jsonPath("$.theme.id", is(theme.getId()), Long.class))
            .andExpect(jsonPath("$.gameCards", hasSize(3)))
            .andExpect(jsonPath("$.gameCards[0].player1_chosen", is(true)))
            .andExpect(jsonPath("$.gameCards[1].player2_chosen", is(true)));
    }

    @Test
    @Transactional
    void playAndLoose() throws Exception {
        startGame(0, 1);

        Map<String, Object> localSessionAttr = sessionattr;
        List<Long> cardIds = List.of(gameCards.get(1).getId(), gameCards.get(2).getId());
        Long winnerId = otherPlayerInRoom.getId();
        if (!game.getNextTurn().equals(currentPlayer)) {
            localSessionAttr = Collections.singletonMap(SESSION_KEY_NAME, otherPlayerInRoom);
            cardIds = List.of(gameCards.get(0).getId(), gameCards.get(2).getId());
            winnerId = currentPlayer.getId();
        }

        PlayDTO playDTO = new PlayDTO();
        playDTO.setGameCardIds(cardIds);
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL + "/" + game.getId() + "/play").sessionAttrs(localSessionAttr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playDTO))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.startDate", is(notNullValue())))
            .andExpect(jsonPath("$.endDate", is(notNullValue())))
            .andExpect(jsonPath("$.winner.id", is(winnerId), Long.class))
            .andExpect(jsonPath("$.nextTurn.id", is(in(List.of(currentPlayer.getId(), otherPlayerInRoom.getId()))), Long.class))
            .andExpect(jsonPath("$.room.id", is(room.getId()), Long.class))
            .andExpect(jsonPath("$.theme.id", is(theme.getId()), Long.class))
            .andExpect(jsonPath("$.gameCards", hasSize(3)))
            .andExpect(jsonPath("$.gameCards[0].player1_chosen", is(true)))
            .andExpect(jsonPath("$.gameCards[1].player2_chosen", is(true)));
    }

    @Test
    @Transactional
    void playAndLooseWhenNoCardLeft() throws Exception {
        startGame(0, 1);

        Map<String, Object> localSessionAttr = sessionattr;
        List<Long> cardIds = List.of(gameCards.get(0).getId(), gameCards.get(1).getId(), gameCards.get(2).getId());
        Long winnerId = otherPlayerInRoom.getId();
        if (!game.getNextTurn().equals(currentPlayer)) {
            localSessionAttr = Collections.singletonMap(SESSION_KEY_NAME, otherPlayerInRoom);
            winnerId = currentPlayer.getId();
        }

        PlayDTO playDTO = new PlayDTO();
        playDTO.setGameCardIds(cardIds);
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL + "/" + game.getId() + "/play").sessionAttrs(localSessionAttr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playDTO))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.startDate", is(notNullValue())))
            .andExpect(jsonPath("$.endDate", is(notNullValue())))
            .andExpect(jsonPath("$.winner.id", is(winnerId), Long.class))
            .andExpect(jsonPath("$.nextTurn.id", is(in(List.of(currentPlayer.getId(), otherPlayerInRoom.getId()))), Long.class))
            .andExpect(jsonPath("$.room.id", is(room.getId()), Long.class))
            .andExpect(jsonPath("$.theme.id", is(theme.getId()), Long.class))
            .andExpect(jsonPath("$.gameCards", hasSize(3)))
            .andExpect(jsonPath("$.gameCards[0].player1_chosen", is(true)))
            .andExpect(jsonPath("$.gameCards[1].player2_chosen", is(true)));
    }

    @Test
    @Transactional
    void playWhenAlreadyWon() throws Exception {
        startGame(0, 1);
        playFirstMoves(2);
        winGameAsCurrentPlayer();

        Map<String, Object> localSessionAttr = sessionattr;
        int cardToPlay = 1;
        if (!game.getNextTurn().equals(currentPlayer)) {
            localSessionAttr = Collections.singletonMap(SESSION_KEY_NAME, otherPlayerInRoom);
            cardToPlay = 0;
        }

        PlayDTO playDTO = new PlayDTO();
        playDTO.setGameCardIds(List.of(gameCards.get(cardToPlay).getId()));
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL + "/" + game.getId() + "/play").sessionAttrs(localSessionAttr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playDTO))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void newMessage() throws Exception {
        NewMessageDTO newMessageDTO = new NewMessageDTO();
        newMessageDTO.setContent("test message");
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL + "/" + game.getId() + "/message").sessionAttrs(sessionattr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(newMessageDTO))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.creationDate", is(notNullValue())))
            .andExpect(jsonPath("$.content", is(newMessageDTO.getContent())))
            .andExpect(jsonPath("$.game.id", is(game.getId()), Long.class))
            .andExpect(jsonPath("$.author.id", is(currentPlayer.getId()), Long.class));
    }

    @Test
    @Transactional
    void newMessageWhenNoAccess() throws Exception {
        NewMessageDTO newMessageDTO = new NewMessageDTO();
        newMessageDTO.setContent("test message");
        Map<String, Object> somePlayerSessAttrs = Collections.singletonMap(SESSION_KEY_NAME, createValidPlayer("player3"));
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL + "/" + game.getId() + "/message").sessionAttrs(somePlayerSessAttrs)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(newMessageDTO))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void getMessages() throws Exception {
        Message m1 = sendMessage("first message", currentPlayer);
        Message m2 = sendMessage("second message", currentPlayer);
        Message m3 = sendMessage("third message", otherPlayerInRoom);

        restGameMockMvc
            .perform(
                get(ENTITY_API_URL + "/" + game.getId() + "/message").sessionAttrs(sessionattr)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$.[0].content", is(m1.getContent())))
            .andExpect(jsonPath("$.[0].creationDate", is(notNullValue())))
            .andExpect(jsonPath("$.[0].author.id", is(currentPlayer.getId()), Long.class))
            .andExpect(jsonPath("$.[0].game.id", is(game.getId()), Long.class))
            .andExpect(jsonPath("$.[1].content", is(m2.getContent())))
            .andExpect(jsonPath("$.[1].creationDate", is(notNullValue())))
            .andExpect(jsonPath("$.[1].author.id", is(currentPlayer.getId()), Long.class))
            .andExpect(jsonPath("$.[1].game.id", is(game.getId()), Long.class))
            .andExpect(jsonPath("$.[2].content", is(m3.getContent())))
            .andExpect(jsonPath("$.[2].creationDate", is(notNullValue())))
            .andExpect(jsonPath("$.[2].author.id", is(otherPlayerInRoom.getId()), Long.class))
            .andExpect(jsonPath("$.[2].game.id", is(game.getId()), Long.class));
    }

    @Test
    @Transactional
    void getMessagesWhenNoAccess() throws Exception {
        sendMessage("first message", currentPlayer);
        sendMessage("second message", currentPlayer);
        sendMessage("third message", otherPlayerInRoom);
        Map<String, Object> somePlayerSessAttrs = Collections.singletonMap(SESSION_KEY_NAME, createValidPlayer("player3"));

        restGameMockMvc
            .perform(
                get(ENTITY_API_URL + "/" + game.getId() + "/message").sessionAttrs(somePlayerSessAttrs)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void getMessagesAfterId() throws Exception {
        sendMessage("first message", currentPlayer);
        Message m2 = sendMessage("second message", currentPlayer);
        Message m3 = sendMessage("third message", otherPlayerInRoom);

        restGameMockMvc
            .perform(
                get(ENTITY_API_URL + "/" + game.getId() + "/message")
                    .sessionAttrs(sessionattr).queryParam("lastMessageId", m2.getId().toString())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$.[0].content", is(m3.getContent())))
            .andExpect(jsonPath("$.[0].creationDate", is(notNullValue())))
            .andExpect(jsonPath("$.[0].author.id", is(otherPlayerInRoom.getId()), Long.class))
            .andExpect(jsonPath("$.[0].game.id", is(game.getId()), Long.class));
    }

}
