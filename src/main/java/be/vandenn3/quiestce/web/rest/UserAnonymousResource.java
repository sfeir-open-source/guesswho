package be.vandenn3.quiestce.web.rest;

import be.vandenn3.quiestce.repository.UserAnonymousRepository;
import be.vandenn3.quiestce.service.UserAnonymousService;
import be.vandenn3.quiestce.service.dto.UserAnonymousDTO;
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
 * REST controller for managing {@link be.vandenn3.quiestce.domain.UserAnonymous}.
 */
@RestController
@RequestMapping("/api")
public class UserAnonymousResource {

    private final Logger log = LoggerFactory.getLogger(UserAnonymousResource.class);

    private static final String ENTITY_NAME = "userAnonymous";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAnonymousService userAnonymousService;

    private final UserAnonymousRepository userAnonymousRepository;

    public UserAnonymousResource(UserAnonymousService userAnonymousService, UserAnonymousRepository userAnonymousRepository) {
        this.userAnonymousService = userAnonymousService;
        this.userAnonymousRepository = userAnonymousRepository;
    }

    /**
     * {@code POST  /user-anonymous} : Create a new userAnonymous.
     *
     * @param userAnonymousDTO the userAnonymousDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAnonymousDTO, or with status {@code 400 (Bad Request)} if the userAnonymous has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-anonymous")
    public ResponseEntity<UserAnonymousDTO> createUserAnonymous(@Valid @RequestBody UserAnonymousDTO userAnonymousDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserAnonymous : {}", userAnonymousDTO);
        if (userAnonymousDTO.getId() != null) {
            throw new BadRequestAlertException("A new userAnonymous cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserAnonymousDTO result = userAnonymousService.save(userAnonymousDTO);
        return ResponseEntity
            .created(new URI("/api/user-anonymous/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-anonymous/:id} : Updates an existing userAnonymous.
     *
     * @param id the id of the userAnonymousDTO to save.
     * @param userAnonymousDTO the userAnonymousDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAnonymousDTO,
     * or with status {@code 400 (Bad Request)} if the userAnonymousDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAnonymousDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-anonymous/{id}")
    public ResponseEntity<UserAnonymousDTO> updateUserAnonymous(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserAnonymousDTO userAnonymousDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserAnonymous : {}, {}", id, userAnonymousDTO);
        if (userAnonymousDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAnonymousDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAnonymousRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserAnonymousDTO result = userAnonymousService.update(userAnonymousDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAnonymousDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-anonymous/:id} : Partial updates given fields of an existing userAnonymous, field will ignore if it is null
     *
     * @param id the id of the userAnonymousDTO to save.
     * @param userAnonymousDTO the userAnonymousDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAnonymousDTO,
     * or with status {@code 400 (Bad Request)} if the userAnonymousDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userAnonymousDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAnonymousDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-anonymous/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAnonymousDTO> partialUpdateUserAnonymous(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserAnonymousDTO userAnonymousDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserAnonymous partially : {}, {}", id, userAnonymousDTO);
        if (userAnonymousDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAnonymousDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAnonymousRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAnonymousDTO> result = userAnonymousService.partialUpdate(userAnonymousDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAnonymousDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-anonymous} : get all the userAnonymous.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAnonymous in body.
     */
    @GetMapping("/user-anonymous")
    public List<UserAnonymousDTO> getAllUserAnonymous() {
        log.debug("REST request to get all UserAnonymous");
        return userAnonymousService.findAll();
    }

    /**
     * {@code GET  /user-anonymous/:id} : get the "id" userAnonymous.
     *
     * @param id the id of the userAnonymousDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAnonymousDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-anonymous/{id}")
    public ResponseEntity<UserAnonymousDTO> getUserAnonymous(@PathVariable Long id) {
        log.debug("REST request to get UserAnonymous : {}", id);
        Optional<UserAnonymousDTO> userAnonymousDTO = userAnonymousService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAnonymousDTO);
    }

    /**
     * {@code DELETE  /user-anonymous/:id} : delete the "id" userAnonymous.
     *
     * @param id the id of the userAnonymousDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-anonymous/{id}")
    public ResponseEntity<Void> deleteUserAnonymous(@PathVariable Long id) {
        log.debug("REST request to delete UserAnonymous : {}", id);
        userAnonymousService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
