package be.vandenn3.quiestce.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.vandenn3.quiestce.IntegrationTest;
import be.vandenn3.quiestce.domain.UserAnonymous;
import be.vandenn3.quiestce.repository.UserAnonymousRepository;
import be.vandenn3.quiestce.service.dto.UserAnonymousDTO;
import be.vandenn3.quiestce.service.mapper.UserAnonymousMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserAnonymousResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAnonymousResourceIT {

    private static final String DEFAULT_PSEUDO = "AAAAAAAAAA";
    private static final String UPDATED_PSEUDO = "BBBBBBBBBB";

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-anonymous";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserAnonymousRepository userAnonymousRepository;

    @Autowired
    private UserAnonymousMapper userAnonymousMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAnonymousMockMvc;

    private UserAnonymous userAnonymous;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAnonymous createEntity(EntityManager em) {
        UserAnonymous userAnonymous = new UserAnonymous().pseudo(DEFAULT_PSEUDO).token(DEFAULT_TOKEN);
        return userAnonymous;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAnonymous createUpdatedEntity(EntityManager em) {
        UserAnonymous userAnonymous = new UserAnonymous().pseudo(UPDATED_PSEUDO).token(UPDATED_TOKEN);
        return userAnonymous;
    }

    @BeforeEach
    public void initTest() {
        userAnonymous = createEntity(em);
    }

    @Test
    @Transactional
    void createUserAnonymous() throws Exception {
        int databaseSizeBeforeCreate = userAnonymousRepository.findAll().size();
        // Create the UserAnonymous
        UserAnonymousDTO userAnonymousDTO = userAnonymousMapper.toDto(userAnonymous);
        restUserAnonymousMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAnonymousDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserAnonymous in the database
        List<UserAnonymous> userAnonymousList = userAnonymousRepository.findAll();
        assertThat(userAnonymousList).hasSize(databaseSizeBeforeCreate + 1);
        UserAnonymous testUserAnonymous = userAnonymousList.get(userAnonymousList.size() - 1);
        assertThat(testUserAnonymous.getPseudo()).isEqualTo(DEFAULT_PSEUDO);
        assertThat(testUserAnonymous.getToken()).isEqualTo(DEFAULT_TOKEN);
    }

    @Test
    @Transactional
    void createUserAnonymousWithExistingId() throws Exception {
        // Create the UserAnonymous with an existing ID
        userAnonymous.setId(1L);
        UserAnonymousDTO userAnonymousDTO = userAnonymousMapper.toDto(userAnonymous);

        int databaseSizeBeforeCreate = userAnonymousRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAnonymousMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAnonymousDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAnonymous in the database
        List<UserAnonymous> userAnonymousList = userAnonymousRepository.findAll();
        assertThat(userAnonymousList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPseudoIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAnonymousRepository.findAll().size();
        // set the field null
        userAnonymous.setPseudo(null);

        // Create the UserAnonymous, which fails.
        UserAnonymousDTO userAnonymousDTO = userAnonymousMapper.toDto(userAnonymous);

        restUserAnonymousMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAnonymousDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserAnonymous> userAnonymousList = userAnonymousRepository.findAll();
        assertThat(userAnonymousList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAnonymousRepository.findAll().size();
        // set the field null
        userAnonymous.setToken(null);

        // Create the UserAnonymous, which fails.
        UserAnonymousDTO userAnonymousDTO = userAnonymousMapper.toDto(userAnonymous);

        restUserAnonymousMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAnonymousDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserAnonymous> userAnonymousList = userAnonymousRepository.findAll();
        assertThat(userAnonymousList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserAnonymous() throws Exception {
        // Initialize the database
        userAnonymousRepository.saveAndFlush(userAnonymous);

        // Get all the userAnonymousList
        restUserAnonymousMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAnonymous.getId().intValue())))
            .andExpect(jsonPath("$.[*].pseudo").value(hasItem(DEFAULT_PSEUDO)))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)));
    }

    @Test
    @Transactional
    void getUserAnonymous() throws Exception {
        // Initialize the database
        userAnonymousRepository.saveAndFlush(userAnonymous);

        // Get the userAnonymous
        restUserAnonymousMockMvc
            .perform(get(ENTITY_API_URL_ID, userAnonymous.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAnonymous.getId().intValue()))
            .andExpect(jsonPath("$.pseudo").value(DEFAULT_PSEUDO))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN));
    }

    @Test
    @Transactional
    void getNonExistingUserAnonymous() throws Exception {
        // Get the userAnonymous
        restUserAnonymousMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserAnonymous() throws Exception {
        // Initialize the database
        userAnonymousRepository.saveAndFlush(userAnonymous);

        int databaseSizeBeforeUpdate = userAnonymousRepository.findAll().size();

        // Update the userAnonymous
        UserAnonymous updatedUserAnonymous = userAnonymousRepository.findById(userAnonymous.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserAnonymous are not directly saved in db
        em.detach(updatedUserAnonymous);
        updatedUserAnonymous.pseudo(UPDATED_PSEUDO).token(UPDATED_TOKEN);
        UserAnonymousDTO userAnonymousDTO = userAnonymousMapper.toDto(updatedUserAnonymous);

        restUserAnonymousMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAnonymousDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAnonymousDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserAnonymous in the database
        List<UserAnonymous> userAnonymousList = userAnonymousRepository.findAll();
        assertThat(userAnonymousList).hasSize(databaseSizeBeforeUpdate);
        UserAnonymous testUserAnonymous = userAnonymousList.get(userAnonymousList.size() - 1);
        assertThat(testUserAnonymous.getPseudo()).isEqualTo(UPDATED_PSEUDO);
        assertThat(testUserAnonymous.getToken()).isEqualTo(UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void putNonExistingUserAnonymous() throws Exception {
        int databaseSizeBeforeUpdate = userAnonymousRepository.findAll().size();
        userAnonymous.setId(count.incrementAndGet());

        // Create the UserAnonymous
        UserAnonymousDTO userAnonymousDTO = userAnonymousMapper.toDto(userAnonymous);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAnonymousMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAnonymousDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAnonymousDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAnonymous in the database
        List<UserAnonymous> userAnonymousList = userAnonymousRepository.findAll();
        assertThat(userAnonymousList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAnonymous() throws Exception {
        int databaseSizeBeforeUpdate = userAnonymousRepository.findAll().size();
        userAnonymous.setId(count.incrementAndGet());

        // Create the UserAnonymous
        UserAnonymousDTO userAnonymousDTO = userAnonymousMapper.toDto(userAnonymous);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAnonymousMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAnonymousDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAnonymous in the database
        List<UserAnonymous> userAnonymousList = userAnonymousRepository.findAll();
        assertThat(userAnonymousList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAnonymous() throws Exception {
        int databaseSizeBeforeUpdate = userAnonymousRepository.findAll().size();
        userAnonymous.setId(count.incrementAndGet());

        // Create the UserAnonymous
        UserAnonymousDTO userAnonymousDTO = userAnonymousMapper.toDto(userAnonymous);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAnonymousMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAnonymousDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAnonymous in the database
        List<UserAnonymous> userAnonymousList = userAnonymousRepository.findAll();
        assertThat(userAnonymousList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAnonymousWithPatch() throws Exception {
        // Initialize the database
        userAnonymousRepository.saveAndFlush(userAnonymous);

        int databaseSizeBeforeUpdate = userAnonymousRepository.findAll().size();

        // Update the userAnonymous using partial update
        UserAnonymous partialUpdatedUserAnonymous = new UserAnonymous();
        partialUpdatedUserAnonymous.setId(userAnonymous.getId());

        partialUpdatedUserAnonymous.token(UPDATED_TOKEN);

        restUserAnonymousMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAnonymous.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAnonymous))
            )
            .andExpect(status().isOk());

        // Validate the UserAnonymous in the database
        List<UserAnonymous> userAnonymousList = userAnonymousRepository.findAll();
        assertThat(userAnonymousList).hasSize(databaseSizeBeforeUpdate);
        UserAnonymous testUserAnonymous = userAnonymousList.get(userAnonymousList.size() - 1);
        assertThat(testUserAnonymous.getPseudo()).isEqualTo(DEFAULT_PSEUDO);
        assertThat(testUserAnonymous.getToken()).isEqualTo(UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void fullUpdateUserAnonymousWithPatch() throws Exception {
        // Initialize the database
        userAnonymousRepository.saveAndFlush(userAnonymous);

        int databaseSizeBeforeUpdate = userAnonymousRepository.findAll().size();

        // Update the userAnonymous using partial update
        UserAnonymous partialUpdatedUserAnonymous = new UserAnonymous();
        partialUpdatedUserAnonymous.setId(userAnonymous.getId());

        partialUpdatedUserAnonymous.pseudo(UPDATED_PSEUDO).token(UPDATED_TOKEN);

        restUserAnonymousMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAnonymous.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAnonymous))
            )
            .andExpect(status().isOk());

        // Validate the UserAnonymous in the database
        List<UserAnonymous> userAnonymousList = userAnonymousRepository.findAll();
        assertThat(userAnonymousList).hasSize(databaseSizeBeforeUpdate);
        UserAnonymous testUserAnonymous = userAnonymousList.get(userAnonymousList.size() - 1);
        assertThat(testUserAnonymous.getPseudo()).isEqualTo(UPDATED_PSEUDO);
        assertThat(testUserAnonymous.getToken()).isEqualTo(UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void patchNonExistingUserAnonymous() throws Exception {
        int databaseSizeBeforeUpdate = userAnonymousRepository.findAll().size();
        userAnonymous.setId(count.incrementAndGet());

        // Create the UserAnonymous
        UserAnonymousDTO userAnonymousDTO = userAnonymousMapper.toDto(userAnonymous);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAnonymousMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAnonymousDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAnonymousDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAnonymous in the database
        List<UserAnonymous> userAnonymousList = userAnonymousRepository.findAll();
        assertThat(userAnonymousList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAnonymous() throws Exception {
        int databaseSizeBeforeUpdate = userAnonymousRepository.findAll().size();
        userAnonymous.setId(count.incrementAndGet());

        // Create the UserAnonymous
        UserAnonymousDTO userAnonymousDTO = userAnonymousMapper.toDto(userAnonymous);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAnonymousMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAnonymousDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAnonymous in the database
        List<UserAnonymous> userAnonymousList = userAnonymousRepository.findAll();
        assertThat(userAnonymousList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAnonymous() throws Exception {
        int databaseSizeBeforeUpdate = userAnonymousRepository.findAll().size();
        userAnonymous.setId(count.incrementAndGet());

        // Create the UserAnonymous
        UserAnonymousDTO userAnonymousDTO = userAnonymousMapper.toDto(userAnonymous);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAnonymousMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAnonymousDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAnonymous in the database
        List<UserAnonymous> userAnonymousList = userAnonymousRepository.findAll();
        assertThat(userAnonymousList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAnonymous() throws Exception {
        // Initialize the database
        userAnonymousRepository.saveAndFlush(userAnonymous);

        int databaseSizeBeforeDelete = userAnonymousRepository.findAll().size();

        // Delete the userAnonymous
        restUserAnonymousMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAnonymous.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserAnonymous> userAnonymousList = userAnonymousRepository.findAll();
        assertThat(userAnonymousList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
