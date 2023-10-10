package be.vandenn3.quiestce.web.rest;

import be.vandenn3.quiestce.service.RoomService;
import be.vandenn3.quiestce.service.dto.RoomCreationDTO;
import be.vandenn3.quiestce.service.dto.RoomDTO;
import be.vandenn3.quiestce.session.CurrentPlayerManager;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing {@link be.vandenn3.quiestce.domain.Room}.
 */
@RestController
@RequestMapping("/api/rooms")
public class RoomResource {

    private final Logger log = LoggerFactory.getLogger(RoomResource.class);

    private static final String ENTITY_NAME = "room";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoomService roomService;
    private final CurrentPlayerManager currentPlayerManager;

    public RoomResource(RoomService roomService, CurrentPlayerManager currentPlayerManager) {
        this.roomService = roomService;
        this.currentPlayerManager = currentPlayerManager;
    }

    @PostMapping("")
    public ResponseEntity<RoomDTO> createRoom(@Valid @RequestBody RoomCreationDTO roomData, HttpSession httpSession) throws URISyntaxException {
        log.debug("REST request to create Room : {}", roomData.getName());

        RoomDTO result = roomService.createRoom(roomData.getName(), currentPlayerManager.getCurrentPlayer(httpSession));
        return ResponseEntity
            .created(new URI("/api/rooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("")
    public List<RoomDTO> getMyRooms(HttpSession httpSession) {
        log.debug("REST request to get all user's Rooms");
        return roomService.getRoomsFor(currentPlayerManager.getCurrentPlayer(httpSession));
    }

    @PostMapping("/join/{code}")
    public RoomDTO joinRoom(@PathVariable String code, HttpSession httpSession) throws URISyntaxException {
        log.debug("REST request to join Room with code starting by: {}", code.substring(0, 2));

        return roomService.joinRoom(code, currentPlayerManager.getCurrentPlayer(httpSession));
    }
}
