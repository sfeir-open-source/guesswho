package be.vandenn3.quiestce.web.rest;

import be.vandenn3.quiestce.repository.ThemeRepository;
import be.vandenn3.quiestce.service.ThemeService;
import be.vandenn3.quiestce.service.dto.ThemeDTO;
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
 * REST controller for managing {@link be.vandenn3.quiestce.domain.Theme}.
 */
@RestController
@RequestMapping("/api")
public class ThemeResource {

    private final Logger log = LoggerFactory.getLogger(ThemeResource.class);

    private static final String ENTITY_NAME = "theme";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ThemeService themeService;

    private final ThemeRepository themeRepository;

    public ThemeResource(ThemeService themeService, ThemeRepository themeRepository) {
        this.themeService = themeService;
        this.themeRepository = themeRepository;
    }

    /**
     * {@code POST  /themes} : Create a new theme.
     *
     * @param themeDTO the themeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new themeDTO, or with status {@code 400 (Bad Request)} if the theme has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/themes")
    public ResponseEntity<ThemeDTO> createTheme(@Valid @RequestBody ThemeDTO themeDTO) throws URISyntaxException {
        log.debug("REST request to save Theme : {}", themeDTO);
        if (themeDTO.getId() != null) {
            throw new BadRequestAlertException("A new theme cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ThemeDTO result = themeService.save(themeDTO);
        return ResponseEntity
            .created(new URI("/api/themes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /themes/:id} : Updates an existing theme.
     *
     * @param id the id of the themeDTO to save.
     * @param themeDTO the themeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated themeDTO,
     * or with status {@code 400 (Bad Request)} if the themeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the themeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/themes/{id}")
    public ResponseEntity<ThemeDTO> updateTheme(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ThemeDTO themeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Theme : {}, {}", id, themeDTO);
        if (themeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, themeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!themeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ThemeDTO result = themeService.update(themeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, themeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /themes/:id} : Partial updates given fields of an existing theme, field will ignore if it is null
     *
     * @param id the id of the themeDTO to save.
     * @param themeDTO the themeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated themeDTO,
     * or with status {@code 400 (Bad Request)} if the themeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the themeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the themeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/themes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ThemeDTO> partialUpdateTheme(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ThemeDTO themeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Theme partially : {}, {}", id, themeDTO);
        if (themeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, themeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!themeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ThemeDTO> result = themeService.partialUpdate(themeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, themeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /themes} : get all the themes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of themes in body.
     */
    @GetMapping("/themes")
    public List<ThemeDTO> getAllThemes() {
        log.debug("REST request to get all Themes");
        return themeService.findAll();
    }

    /**
     * {@code GET  /themes/:id} : get the "id" theme.
     *
     * @param id the id of the themeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the themeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/themes/{id}")
    public ResponseEntity<ThemeDTO> getTheme(@PathVariable Long id) {
        log.debug("REST request to get Theme : {}", id);
        Optional<ThemeDTO> themeDTO = themeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(themeDTO);
    }

    /**
     * {@code DELETE  /themes/:id} : delete the "id" theme.
     *
     * @param id the id of the themeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        log.debug("REST request to delete Theme : {}", id);
        themeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
