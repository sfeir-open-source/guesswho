package be.vandenn3.quiestce.web.rest;

import be.vandenn3.quiestce.service.GameService;
import be.vandenn3.quiestce.service.dto.*;
import be.vandenn3.quiestce.session.CurrentPlayerManager;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameResource {
    private final Logger log = LoggerFactory.getLogger(GameResource.class);

    private static final String ENTITY_NAME = "game";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameService gameService;
    private final CurrentPlayerManager currentPlayerManager;

    public GameResource(GameService gameService, CurrentPlayerManager currentPlayerManager) {
        this.gameService = gameService;
        this.currentPlayerManager = currentPlayerManager;
    }

    @GetMapping("")
    public List<GameDTO> getGamesByRoom(@RequestParam Long roomId, HttpSession httpSession) {
        return gameService.getAllByRoom(roomId, currentPlayerManager.getCurrentPlayer(httpSession));
    }

    @PostMapping("")
    public ResponseEntity<GameDTO> newGame(@Valid @RequestBody GameCreationDTO gameData, HttpSession httpSession) throws URISyntaxException {
        log.debug("REST request to create Game in room {}", gameData.getRoomId());

        GameDTO result = gameService.newGame(gameData.getRoomId(), gameData.getThemeId(), currentPlayerManager.getCurrentPlayer(httpSession));
        return ResponseEntity
            .created(new URI("/api/game/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/{id}")
    public GameDTO getDetails(@PathVariable Long id, HttpSession httpSession) {
        return gameService.getGameDetails(id, currentPlayerManager.getCurrentPlayer(httpSession));
    }

    @PostMapping("/{id}/select-card")
    public GameDTO selectMyCard(@PathVariable Long id, @Valid @RequestBody SelectCardDTO selectCardDTO, HttpSession httpSession) {
        return gameService.chooseCard(id, selectCardDTO.getGameCardId(), currentPlayerManager.getCurrentPlayer(httpSession));
    }

    @PostMapping("/{id}/play")
    public GameDTO selectMyCard(@PathVariable Long id, @Valid @RequestBody PlayDTO playDTO, HttpSession httpSession) {
        return gameService.play(id, playDTO.getGameCardIds(), currentPlayerManager.getCurrentPlayer(httpSession));
    }

    @PostMapping("/{id}/message")
    public MessageDTO sendMessage(@PathVariable Long id, @Valid @RequestBody NewMessageDTO newMessageDTO, HttpSession httpSession) {
        return gameService.newMessage(id, newMessageDTO.getContent(), currentPlayerManager.getCurrentPlayer(httpSession));
    }

    @GetMapping("/{id}/message")
    public List<MessageDTO> getMessages(@PathVariable Long id, @RequestParam(required = false) Long lastMessageId, HttpSession httpSession) {
        return gameService.getMessages(id, lastMessageId, currentPlayerManager.getCurrentPlayer(httpSession));
    }

}
