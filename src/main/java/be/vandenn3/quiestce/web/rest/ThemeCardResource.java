package be.vandenn3.quiestce.web.rest;

import be.vandenn3.quiestce.repository.ThemeCardRepository;
import be.vandenn3.quiestce.service.ThemeCardService;
import be.vandenn3.quiestce.service.dto.ThemeCardDTO;
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
 * REST controller for managing {@link be.vandenn3.quiestce.domain.ThemeCard}.
 */
@RestController
@RequestMapping("/api")
public class ThemeCardResource {

    private final Logger log = LoggerFactory.getLogger(ThemeCardResource.class);

    private static final String ENTITY_NAME = "themeCard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ThemeCardService themeCardService;

    private final ThemeCardRepository themeCardRepository;

    public ThemeCardResource(ThemeCardService themeCardService, ThemeCardRepository themeCardRepository) {
        this.themeCardService = themeCardService;
        this.themeCardRepository = themeCardRepository;
    }

    /**
     * {@code POST  /theme-cards} : Create a new themeCard.
     *
     * @param themeCardDTO the themeCardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new themeCardDTO, or with status {@code 400 (Bad Request)} if the themeCard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/theme-cards")
    public ResponseEntity<ThemeCardDTO> createThemeCard(@Valid @RequestBody ThemeCardDTO themeCardDTO) throws URISyntaxException {
        log.debug("REST request to save ThemeCard : {}", themeCardDTO);
        if (themeCardDTO.getId() != null) {
            throw new BadRequestAlertException("A new themeCard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ThemeCardDTO result = themeCardService.save(themeCardDTO);
        return ResponseEntity
            .created(new URI("/api/theme-cards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /theme-cards/:id} : Updates an existing themeCard.
     *
     * @param id the id of the themeCardDTO to save.
     * @param themeCardDTO the themeCardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated themeCardDTO,
     * or with status {@code 400 (Bad Request)} if the themeCardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the themeCardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/theme-cards/{id}")
    public ResponseEntity<ThemeCardDTO> updateThemeCard(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ThemeCardDTO themeCardDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ThemeCard : {}, {}", id, themeCardDTO);
        if (themeCardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, themeCardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!themeCardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ThemeCardDTO result = themeCardService.update(themeCardDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, themeCardDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /theme-cards/:id} : Partial updates given fields of an existing themeCard, field will ignore if it is null
     *
     * @param id the id of the themeCardDTO to save.
     * @param themeCardDTO the themeCardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated themeCardDTO,
     * or with status {@code 400 (Bad Request)} if the themeCardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the themeCardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the themeCardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/theme-cards/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ThemeCardDTO> partialUpdateThemeCard(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ThemeCardDTO themeCardDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ThemeCard partially : {}, {}", id, themeCardDTO);
        if (themeCardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, themeCardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!themeCardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ThemeCardDTO> result = themeCardService.partialUpdate(themeCardDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, themeCardDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /theme-cards} : get all the themeCards.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of themeCards in body.
     */
    @GetMapping("/theme-cards")
    public List<ThemeCardDTO> getAllThemeCards(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ThemeCards");
        return themeCardService.findAll();
    }

    /**
     * {@code GET  /theme-cards/:id} : get the "id" themeCard.
     *
     * @param id the id of the themeCardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the themeCardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/theme-cards/{id}")
    public ResponseEntity<ThemeCardDTO> getThemeCard(@PathVariable Long id) {
        log.debug("REST request to get ThemeCard : {}", id);
        Optional<ThemeCardDTO> themeCardDTO = themeCardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(themeCardDTO);
    }

    /**
     * {@code DELETE  /theme-cards/:id} : delete the "id" themeCard.
     *
     * @param id the id of the themeCardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/theme-cards/{id}")
    public ResponseEntity<Void> deleteThemeCard(@PathVariable Long id) {
        log.debug("REST request to delete ThemeCard : {}", id);
        themeCardService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
