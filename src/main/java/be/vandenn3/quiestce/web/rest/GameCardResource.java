package be.vandenn3.quiestce.web.rest;

import be.vandenn3.quiestce.repository.GameCardRepository;
import be.vandenn3.quiestce.service.GameCardService;
import be.vandenn3.quiestce.service.dto.GameCardDTO;
import be.vandenn3.quiestce.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link be.vandenn3.quiestce.domain.GameCard}.
 */
@RestController
@RequestMapping("/api")
public class GameCardResource {

    private final Logger log = LoggerFactory.getLogger(GameCardResource.class);

    private static final String ENTITY_NAME = "gameCard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameCardService gameCardService;

    private final GameCardRepository gameCardRepository;

    public GameCardResource(GameCardService gameCardService, GameCardRepository gameCardRepository) {
        this.gameCardService = gameCardService;
        this.gameCardRepository = gameCardRepository;
    }

    /**
     * {@code POST  /game-cards} : Create a new gameCard.
     *
     * @param gameCardDTO the gameCardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameCardDTO, or with status {@code 400 (Bad Request)} if the gameCard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/game-cards")
    public ResponseEntity<GameCardDTO> createGameCard(@Valid @RequestBody GameCardDTO gameCardDTO) throws URISyntaxException {
        log.debug("REST request to save GameCard : {}", gameCardDTO);
        if (gameCardDTO.getId() != null) {
            throw new BadRequestAlertException("A new gameCard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GameCardDTO result = gameCardService.save(gameCardDTO);
        return ResponseEntity
            .created(new URI("/api/game-cards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /game-cards/:id} : Updates an existing gameCard.
     *
     * @param id the id of the gameCardDTO to save.
     * @param gameCardDTO the gameCardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameCardDTO,
     * or with status {@code 400 (Bad Request)} if the gameCardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameCardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/game-cards/{id}")
    public ResponseEntity<GameCardDTO> updateGameCard(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GameCardDTO gameCardDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GameCard : {}, {}", id, gameCardDTO);
        if (gameCardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameCardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameCardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GameCardDTO result = gameCardService.update(gameCardDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameCardDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /game-cards/:id} : Partial updates given fields of an existing gameCard, field will ignore if it is null
     *
     * @param id the id of the gameCardDTO to save.
     * @param gameCardDTO the gameCardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameCardDTO,
     * or with status {@code 400 (Bad Request)} if the gameCardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gameCardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gameCardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/game-cards/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GameCardDTO> partialUpdateGameCard(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GameCardDTO gameCardDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GameCard partially : {}, {}", id, gameCardDTO);
        if (gameCardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameCardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameCardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GameCardDTO> result = gameCardService.partialUpdate(gameCardDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameCardDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /game-cards} : get all the gameCards.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gameCards in body.
     */
    @GetMapping("/game-cards")
    public List<GameCardDTO> getAllGameCards(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all GameCards");
        return gameCardService.findAll();
    }

    /**
     * {@code GET  /game-cards/:id} : get the "id" gameCard.
     *
     * @param id the id of the gameCardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameCardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/game-cards/{id}")
    public ResponseEntity<GameCardDTO> getGameCard(@PathVariable Long id) {
        log.debug("REST request to get GameCard : {}", id);
        Optional<GameCardDTO> gameCardDTO = gameCardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gameCardDTO);
    }

    /**
     * {@code DELETE  /game-cards/:id} : delete the "id" gameCard.
     *
     * @param id the id of the gameCardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/game-cards/{id}")
    public ResponseEntity<Void> deleteGameCard(@PathVariable Long id) {
        log.debug("REST request to delete GameCard : {}", id);
        gameCardService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
