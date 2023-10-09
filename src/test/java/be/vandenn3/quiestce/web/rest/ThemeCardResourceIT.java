package be.vandenn3.quiestce.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.vandenn3.quiestce.IntegrationTest;
import be.vandenn3.quiestce.domain.Picture;
import be.vandenn3.quiestce.domain.Theme;
import be.vandenn3.quiestce.domain.ThemeCard;
import be.vandenn3.quiestce.repository.ThemeCardRepository;
import be.vandenn3.quiestce.service.ThemeCardService;
import be.vandenn3.quiestce.service.dto.ThemeCardDTO;
import be.vandenn3.quiestce.service.mapper.ThemeCardMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ThemeCardResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ThemeCardResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/theme-cards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ThemeCardRepository themeCardRepository;

    @Mock
    private ThemeCardRepository themeCardRepositoryMock;

    @Autowired
    private ThemeCardMapper themeCardMapper;

    @Mock
    private ThemeCardService themeCardServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restThemeCardMockMvc;

    private ThemeCard themeCard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ThemeCard createEntity(EntityManager em) {
        ThemeCard themeCard = new ThemeCard().name(DEFAULT_NAME);
        // Add required entity
        Theme theme;
        if (TestUtil.findAll(em, Theme.class).isEmpty()) {
            theme = ThemeResourceIT.createEntity(em);
            em.persist(theme);
            em.flush();
        } else {
            theme = TestUtil.findAll(em, Theme.class).get(0);
        }
        themeCard.setTheme(theme);
        // Add required entity
        Picture picture;
        if (TestUtil.findAll(em, Picture.class).isEmpty()) {
            picture = PictureResourceIT.createEntity(em);
            em.persist(picture);
            em.flush();
        } else {
            picture = TestUtil.findAll(em, Picture.class).get(0);
        }
        themeCard.setPicture(picture);
        return themeCard;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ThemeCard createUpdatedEntity(EntityManager em) {
        ThemeCard themeCard = new ThemeCard().name(UPDATED_NAME);
        // Add required entity
        Theme theme;
        if (TestUtil.findAll(em, Theme.class).isEmpty()) {
            theme = ThemeResourceIT.createUpdatedEntity(em);
            em.persist(theme);
            em.flush();
        } else {
            theme = TestUtil.findAll(em, Theme.class).get(0);
        }
        themeCard.setTheme(theme);
        // Add required entity
        Picture picture;
        if (TestUtil.findAll(em, Picture.class).isEmpty()) {
            picture = PictureResourceIT.createUpdatedEntity(em);
            em.persist(picture);
            em.flush();
        } else {
            picture = TestUtil.findAll(em, Picture.class).get(0);
        }
        themeCard.setPicture(picture);
        return themeCard;
    }

    @BeforeEach
    public void initTest() {
        themeCard = createEntity(em);
    }

    @Test
    @Transactional
    void createThemeCard() throws Exception {
        int databaseSizeBeforeCreate = themeCardRepository.findAll().size();
        // Create the ThemeCard
        ThemeCardDTO themeCardDTO = themeCardMapper.toDto(themeCard);
        restThemeCardMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeCardDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ThemeCard in the database
        List<ThemeCard> themeCardList = themeCardRepository.findAll();
        assertThat(themeCardList).hasSize(databaseSizeBeforeCreate + 1);
        ThemeCard testThemeCard = themeCardList.get(themeCardList.size() - 1);
        assertThat(testThemeCard.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createThemeCardWithExistingId() throws Exception {
        // Create the ThemeCard with an existing ID
        themeCard.setId(1L);
        ThemeCardDTO themeCardDTO = themeCardMapper.toDto(themeCard);

        int databaseSizeBeforeCreate = themeCardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restThemeCardMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeCard in the database
        List<ThemeCard> themeCardList = themeCardRepository.findAll();
        assertThat(themeCardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = themeCardRepository.findAll().size();
        // set the field null
        themeCard.setName(null);

        // Create the ThemeCard, which fails.
        ThemeCardDTO themeCardDTO = themeCardMapper.toDto(themeCard);

        restThemeCardMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeCardDTO))
            )
            .andExpect(status().isBadRequest());

        List<ThemeCard> themeCardList = themeCardRepository.findAll();
        assertThat(themeCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllThemeCards() throws Exception {
        // Initialize the database
        themeCardRepository.saveAndFlush(themeCard);

        // Get all the themeCardList
        restThemeCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(themeCard.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllThemeCardsWithEagerRelationshipsIsEnabled() throws Exception {
        when(themeCardServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restThemeCardMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(themeCardServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllThemeCardsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(themeCardServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restThemeCardMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(themeCardRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getThemeCard() throws Exception {
        // Initialize the database
        themeCardRepository.saveAndFlush(themeCard);

        // Get the themeCard
        restThemeCardMockMvc
            .perform(get(ENTITY_API_URL_ID, themeCard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(themeCard.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingThemeCard() throws Exception {
        // Get the themeCard
        restThemeCardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingThemeCard() throws Exception {
        // Initialize the database
        themeCardRepository.saveAndFlush(themeCard);

        int databaseSizeBeforeUpdate = themeCardRepository.findAll().size();

        // Update the themeCard
        ThemeCard updatedThemeCard = themeCardRepository.findById(themeCard.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedThemeCard are not directly saved in db
        em.detach(updatedThemeCard);
        updatedThemeCard.name(UPDATED_NAME);
        ThemeCardDTO themeCardDTO = themeCardMapper.toDto(updatedThemeCard);

        restThemeCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, themeCardDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeCardDTO))
            )
            .andExpect(status().isOk());

        // Validate the ThemeCard in the database
        List<ThemeCard> themeCardList = themeCardRepository.findAll();
        assertThat(themeCardList).hasSize(databaseSizeBeforeUpdate);
        ThemeCard testThemeCard = themeCardList.get(themeCardList.size() - 1);
        assertThat(testThemeCard.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingThemeCard() throws Exception {
        int databaseSizeBeforeUpdate = themeCardRepository.findAll().size();
        themeCard.setId(count.incrementAndGet());

        // Create the ThemeCard
        ThemeCardDTO themeCardDTO = themeCardMapper.toDto(themeCard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThemeCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, themeCardDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeCard in the database
        List<ThemeCard> themeCardList = themeCardRepository.findAll();
        assertThat(themeCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchThemeCard() throws Exception {
        int databaseSizeBeforeUpdate = themeCardRepository.findAll().size();
        themeCard.setId(count.incrementAndGet());

        // Create the ThemeCard
        ThemeCardDTO themeCardDTO = themeCardMapper.toDto(themeCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeCard in the database
        List<ThemeCard> themeCardList = themeCardRepository.findAll();
        assertThat(themeCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamThemeCard() throws Exception {
        int databaseSizeBeforeUpdate = themeCardRepository.findAll().size();
        themeCard.setId(count.incrementAndGet());

        // Create the ThemeCard
        ThemeCardDTO themeCardDTO = themeCardMapper.toDto(themeCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeCardMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(themeCardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ThemeCard in the database
        List<ThemeCard> themeCardList = themeCardRepository.findAll();
        assertThat(themeCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateThemeCardWithPatch() throws Exception {
        // Initialize the database
        themeCardRepository.saveAndFlush(themeCard);

        int databaseSizeBeforeUpdate = themeCardRepository.findAll().size();

        // Update the themeCard using partial update
        ThemeCard partialUpdatedThemeCard = new ThemeCard();
        partialUpdatedThemeCard.setId(themeCard.getId());

        partialUpdatedThemeCard.name(UPDATED_NAME);

        restThemeCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedThemeCard.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedThemeCard))
            )
            .andExpect(status().isOk());

        // Validate the ThemeCard in the database
        List<ThemeCard> themeCardList = themeCardRepository.findAll();
        assertThat(themeCardList).hasSize(databaseSizeBeforeUpdate);
        ThemeCard testThemeCard = themeCardList.get(themeCardList.size() - 1);
        assertThat(testThemeCard.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateThemeCardWithPatch() throws Exception {
        // Initialize the database
        themeCardRepository.saveAndFlush(themeCard);

        int databaseSizeBeforeUpdate = themeCardRepository.findAll().size();

        // Update the themeCard using partial update
        ThemeCard partialUpdatedThemeCard = new ThemeCard();
        partialUpdatedThemeCard.setId(themeCard.getId());

        partialUpdatedThemeCard.name(UPDATED_NAME);

        restThemeCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedThemeCard.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedThemeCard))
            )
            .andExpect(status().isOk());

        // Validate the ThemeCard in the database
        List<ThemeCard> themeCardList = themeCardRepository.findAll();
        assertThat(themeCardList).hasSize(databaseSizeBeforeUpdate);
        ThemeCard testThemeCard = themeCardList.get(themeCardList.size() - 1);
        assertThat(testThemeCard.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingThemeCard() throws Exception {
        int databaseSizeBeforeUpdate = themeCardRepository.findAll().size();
        themeCard.setId(count.incrementAndGet());

        // Create the ThemeCard
        ThemeCardDTO themeCardDTO = themeCardMapper.toDto(themeCard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThemeCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, themeCardDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(themeCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeCard in the database
        List<ThemeCard> themeCardList = themeCardRepository.findAll();
        assertThat(themeCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchThemeCard() throws Exception {
        int databaseSizeBeforeUpdate = themeCardRepository.findAll().size();
        themeCard.setId(count.incrementAndGet());

        // Create the ThemeCard
        ThemeCardDTO themeCardDTO = themeCardMapper.toDto(themeCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(themeCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ThemeCard in the database
        List<ThemeCard> themeCardList = themeCardRepository.findAll();
        assertThat(themeCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamThemeCard() throws Exception {
        int databaseSizeBeforeUpdate = themeCardRepository.findAll().size();
        themeCard.setId(count.incrementAndGet());

        // Create the ThemeCard
        ThemeCardDTO themeCardDTO = themeCardMapper.toDto(themeCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThemeCardMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(themeCardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ThemeCard in the database
        List<ThemeCard> themeCardList = themeCardRepository.findAll();
        assertThat(themeCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteThemeCard() throws Exception {
        // Initialize the database
        themeCardRepository.saveAndFlush(themeCard);

        int databaseSizeBeforeDelete = themeCardRepository.findAll().size();

        // Delete the themeCard
        restThemeCardMockMvc
            .perform(delete(ENTITY_API_URL_ID, themeCard.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ThemeCard> themeCardList = themeCardRepository.findAll();
        assertThat(themeCardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
