package be.vandenn3.quiestce.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.vandenn3.quiestce.IntegrationTest;
import be.vandenn3.quiestce.domain.Game;
import be.vandenn3.quiestce.domain.GameCard;
import be.vandenn3.quiestce.domain.ThemeCard;
import be.vandenn3.quiestce.repository.GameCardRepository;
import be.vandenn3.quiestce.service.GameCardService;
import be.vandenn3.quiestce.service.dto.GameCardDTO;
import be.vandenn3.quiestce.service.mapper.GameCardMapper;
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
 * Integration tests for the {@link GameCardResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GameCardResourceIT {

    private static final Boolean DEFAULT_PLAYER_1_CHOSEN = false;
    private static final Boolean UPDATED_PLAYER_1_CHOSEN = true;

    private static final Boolean DEFAULT_PLAYER_2_CHOSEN = false;
    private static final Boolean UPDATED_PLAYER_2_CHOSEN = true;

    private static final Boolean DEFAULT_PLAYER_1_DISCARDED = false;
    private static final Boolean UPDATED_PLAYER_1_DISCARDED = true;

    private static final Boolean DEFAULT_PLAYER_2_DISCARDED = false;
    private static final Boolean UPDATED_PLAYER_2_DISCARDED = true;

    private static final String ENTITY_API_URL = "/api/game-cards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GameCardRepository gameCardRepository;

    @Mock
    private GameCardRepository gameCardRepositoryMock;

    @Autowired
    private GameCardMapper gameCardMapper;

    @Mock
    private GameCardService gameCardServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameCardMockMvc;

    private GameCard gameCard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameCard createEntity(EntityManager em) {
        GameCard gameCard = new GameCard()
            .player1_chosen(DEFAULT_PLAYER_1_CHOSEN)
            .player2_chosen(DEFAULT_PLAYER_2_CHOSEN)
            .player1_discarded(DEFAULT_PLAYER_1_DISCARDED)
            .player2_discarded(DEFAULT_PLAYER_2_DISCARDED);
        // Add required entity
        Game game;
        if (TestUtil.findAll(em, Game.class).isEmpty()) {
            game = GameResourceIT.createEntity(em);
            em.persist(game);
            em.flush();
        } else {
            game = TestUtil.findAll(em, Game.class).get(0);
        }
        gameCard.setGame(game);
        // Add required entity
        ThemeCard themeCard;
        if (TestUtil.findAll(em, ThemeCard.class).isEmpty()) {
            themeCard = ThemeCardResourceIT.createEntity(em);
            em.persist(themeCard);
            em.flush();
        } else {
            themeCard = TestUtil.findAll(em, ThemeCard.class).get(0);
        }
        gameCard.setThemeCard(themeCard);
        return gameCard;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameCard createUpdatedEntity(EntityManager em) {
        GameCard gameCard = new GameCard()
            .player1_chosen(UPDATED_PLAYER_1_CHOSEN)
            .player2_chosen(UPDATED_PLAYER_2_CHOSEN)
            .player1_discarded(UPDATED_PLAYER_1_DISCARDED)
            .player2_discarded(UPDATED_PLAYER_2_DISCARDED);
        // Add required entity
        Game game;
        if (TestUtil.findAll(em, Game.class).isEmpty()) {
            game = GameResourceIT.createUpdatedEntity(em);
            em.persist(game);
            em.flush();
        } else {
            game = TestUtil.findAll(em, Game.class).get(0);
        }
        gameCard.setGame(game);
        // Add required entity
        ThemeCard themeCard;
        if (TestUtil.findAll(em, ThemeCard.class).isEmpty()) {
            themeCard = ThemeCardResourceIT.createUpdatedEntity(em);
            em.persist(themeCard);
            em.flush();
        } else {
            themeCard = TestUtil.findAll(em, ThemeCard.class).get(0);
        }
        gameCard.setThemeCard(themeCard);
        return gameCard;
    }

    @BeforeEach
    public void initTest() {
        gameCard = createEntity(em);
    }

    @Test
    @Transactional
    void createGameCard() throws Exception {
        int databaseSizeBeforeCreate = gameCardRepository.findAll().size();
        // Create the GameCard
        GameCardDTO gameCardDTO = gameCardMapper.toDto(gameCard);
        restGameCardMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameCardDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GameCard in the database
        List<GameCard> gameCardList = gameCardRepository.findAll();
        assertThat(gameCardList).hasSize(databaseSizeBeforeCreate + 1);
        GameCard testGameCard = gameCardList.get(gameCardList.size() - 1);
        assertThat(testGameCard.getPlayer1_chosen()).isEqualTo(DEFAULT_PLAYER_1_CHOSEN);
        assertThat(testGameCard.getPlayer2_chosen()).isEqualTo(DEFAULT_PLAYER_2_CHOSEN);
        assertThat(testGameCard.getPlayer1_discarded()).isEqualTo(DEFAULT_PLAYER_1_DISCARDED);
        assertThat(testGameCard.getPlayer2_discarded()).isEqualTo(DEFAULT_PLAYER_2_DISCARDED);
    }

    @Test
    @Transactional
    void createGameCardWithExistingId() throws Exception {
        // Create the GameCard with an existing ID
        gameCard.setId(1L);
        GameCardDTO gameCardDTO = gameCardMapper.toDto(gameCard);

        int databaseSizeBeforeCreate = gameCardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameCardMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameCard in the database
        List<GameCard> gameCardList = gameCardRepository.findAll();
        assertThat(gameCardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPlayer1_chosenIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameCardRepository.findAll().size();
        // set the field null
        gameCard.setPlayer1_chosen(null);

        // Create the GameCard, which fails.
        GameCardDTO gameCardDTO = gameCardMapper.toDto(gameCard);

        restGameCardMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameCardDTO))
            )
            .andExpect(status().isBadRequest());

        List<GameCard> gameCardList = gameCardRepository.findAll();
        assertThat(gameCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlayer2_chosenIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameCardRepository.findAll().size();
        // set the field null
        gameCard.setPlayer2_chosen(null);

        // Create the GameCard, which fails.
        GameCardDTO gameCardDTO = gameCardMapper.toDto(gameCard);

        restGameCardMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameCardDTO))
            )
            .andExpect(status().isBadRequest());

        List<GameCard> gameCardList = gameCardRepository.findAll();
        assertThat(gameCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlayer1_discardedIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameCardRepository.findAll().size();
        // set the field null
        gameCard.setPlayer1_discarded(null);

        // Create the GameCard, which fails.
        GameCardDTO gameCardDTO = gameCardMapper.toDto(gameCard);

        restGameCardMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameCardDTO))
            )
            .andExpect(status().isBadRequest());

        List<GameCard> gameCardList = gameCardRepository.findAll();
        assertThat(gameCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlayer2_discardedIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameCardRepository.findAll().size();
        // set the field null
        gameCard.setPlayer2_discarded(null);

        // Create the GameCard, which fails.
        GameCardDTO gameCardDTO = gameCardMapper.toDto(gameCard);

        restGameCardMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameCardDTO))
            )
            .andExpect(status().isBadRequest());

        List<GameCard> gameCardList = gameCardRepository.findAll();
        assertThat(gameCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGameCards() throws Exception {
        // Initialize the database
        gameCardRepository.saveAndFlush(gameCard);

        // Get all the gameCardList
        restGameCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameCard.getId().intValue())))
            .andExpect(jsonPath("$.[*].player1_chosen").value(hasItem(DEFAULT_PLAYER_1_CHOSEN.booleanValue())))
            .andExpect(jsonPath("$.[*].player2_chosen").value(hasItem(DEFAULT_PLAYER_2_CHOSEN.booleanValue())))
            .andExpect(jsonPath("$.[*].player1_discarded").value(hasItem(DEFAULT_PLAYER_1_DISCARDED.booleanValue())))
            .andExpect(jsonPath("$.[*].player2_discarded").value(hasItem(DEFAULT_PLAYER_2_DISCARDED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGameCardsWithEagerRelationshipsIsEnabled() throws Exception {
        when(gameCardServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGameCardMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(gameCardServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGameCardsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(gameCardServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGameCardMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(gameCardRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getGameCard() throws Exception {
        // Initialize the database
        gameCardRepository.saveAndFlush(gameCard);

        // Get the gameCard
        restGameCardMockMvc
            .perform(get(ENTITY_API_URL_ID, gameCard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gameCard.getId().intValue()))
            .andExpect(jsonPath("$.player1_chosen").value(DEFAULT_PLAYER_1_CHOSEN.booleanValue()))
            .andExpect(jsonPath("$.player2_chosen").value(DEFAULT_PLAYER_2_CHOSEN.booleanValue()))
            .andExpect(jsonPath("$.player1_discarded").value(DEFAULT_PLAYER_1_DISCARDED.booleanValue()))
            .andExpect(jsonPath("$.player2_discarded").value(DEFAULT_PLAYER_2_DISCARDED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingGameCard() throws Exception {
        // Get the gameCard
        restGameCardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGameCard() throws Exception {
        // Initialize the database
        gameCardRepository.saveAndFlush(gameCard);

        int databaseSizeBeforeUpdate = gameCardRepository.findAll().size();

        // Update the gameCard
        GameCard updatedGameCard = gameCardRepository.findById(gameCard.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGameCard are not directly saved in db
        em.detach(updatedGameCard);
        updatedGameCard
            .player1_chosen(UPDATED_PLAYER_1_CHOSEN)
            .player2_chosen(UPDATED_PLAYER_2_CHOSEN)
            .player1_discarded(UPDATED_PLAYER_1_DISCARDED)
            .player2_discarded(UPDATED_PLAYER_2_DISCARDED);
        GameCardDTO gameCardDTO = gameCardMapper.toDto(updatedGameCard);

        restGameCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameCardDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameCardDTO))
            )
            .andExpect(status().isOk());

        // Validate the GameCard in the database
        List<GameCard> gameCardList = gameCardRepository.findAll();
        assertThat(gameCardList).hasSize(databaseSizeBeforeUpdate);
        GameCard testGameCard = gameCardList.get(gameCardList.size() - 1);
        assertThat(testGameCard.getPlayer1_chosen()).isEqualTo(UPDATED_PLAYER_1_CHOSEN);
        assertThat(testGameCard.getPlayer2_chosen()).isEqualTo(UPDATED_PLAYER_2_CHOSEN);
        assertThat(testGameCard.getPlayer1_discarded()).isEqualTo(UPDATED_PLAYER_1_DISCARDED);
        assertThat(testGameCard.getPlayer2_discarded()).isEqualTo(UPDATED_PLAYER_2_DISCARDED);
    }

    @Test
    @Transactional
    void putNonExistingGameCard() throws Exception {
        int databaseSizeBeforeUpdate = gameCardRepository.findAll().size();
        gameCard.setId(count.incrementAndGet());

        // Create the GameCard
        GameCardDTO gameCardDTO = gameCardMapper.toDto(gameCard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameCardDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameCard in the database
        List<GameCard> gameCardList = gameCardRepository.findAll();
        assertThat(gameCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGameCard() throws Exception {
        int databaseSizeBeforeUpdate = gameCardRepository.findAll().size();
        gameCard.setId(count.incrementAndGet());

        // Create the GameCard
        GameCardDTO gameCardDTO = gameCardMapper.toDto(gameCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameCard in the database
        List<GameCard> gameCardList = gameCardRepository.findAll();
        assertThat(gameCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGameCard() throws Exception {
        int databaseSizeBeforeUpdate = gameCardRepository.findAll().size();
        gameCard.setId(count.incrementAndGet());

        // Create the GameCard
        GameCardDTO gameCardDTO = gameCardMapper.toDto(gameCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameCardMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameCardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameCard in the database
        List<GameCard> gameCardList = gameCardRepository.findAll();
        assertThat(gameCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameCardWithPatch() throws Exception {
        // Initialize the database
        gameCardRepository.saveAndFlush(gameCard);

        int databaseSizeBeforeUpdate = gameCardRepository.findAll().size();

        // Update the gameCard using partial update
        GameCard partialUpdatedGameCard = new GameCard();
        partialUpdatedGameCard.setId(gameCard.getId());

        partialUpdatedGameCard
            .player2_chosen(UPDATED_PLAYER_2_CHOSEN)
            .player1_discarded(UPDATED_PLAYER_1_DISCARDED)
            .player2_discarded(UPDATED_PLAYER_2_DISCARDED);

        restGameCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameCard.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameCard))
            )
            .andExpect(status().isOk());

        // Validate the GameCard in the database
        List<GameCard> gameCardList = gameCardRepository.findAll();
        assertThat(gameCardList).hasSize(databaseSizeBeforeUpdate);
        GameCard testGameCard = gameCardList.get(gameCardList.size() - 1);
        assertThat(testGameCard.getPlayer1_chosen()).isEqualTo(DEFAULT_PLAYER_1_CHOSEN);
        assertThat(testGameCard.getPlayer2_chosen()).isEqualTo(UPDATED_PLAYER_2_CHOSEN);
        assertThat(testGameCard.getPlayer1_discarded()).isEqualTo(UPDATED_PLAYER_1_DISCARDED);
        assertThat(testGameCard.getPlayer2_discarded()).isEqualTo(UPDATED_PLAYER_2_DISCARDED);
    }

    @Test
    @Transactional
    void fullUpdateGameCardWithPatch() throws Exception {
        // Initialize the database
        gameCardRepository.saveAndFlush(gameCard);

        int databaseSizeBeforeUpdate = gameCardRepository.findAll().size();

        // Update the gameCard using partial update
        GameCard partialUpdatedGameCard = new GameCard();
        partialUpdatedGameCard.setId(gameCard.getId());

        partialUpdatedGameCard
            .player1_chosen(UPDATED_PLAYER_1_CHOSEN)
            .player2_chosen(UPDATED_PLAYER_2_CHOSEN)
            .player1_discarded(UPDATED_PLAYER_1_DISCARDED)
            .player2_discarded(UPDATED_PLAYER_2_DISCARDED);

        restGameCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameCard.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameCard))
            )
            .andExpect(status().isOk());

        // Validate the GameCard in the database
        List<GameCard> gameCardList = gameCardRepository.findAll();
        assertThat(gameCardList).hasSize(databaseSizeBeforeUpdate);
        GameCard testGameCard = gameCardList.get(gameCardList.size() - 1);
        assertThat(testGameCard.getPlayer1_chosen()).isEqualTo(UPDATED_PLAYER_1_CHOSEN);
        assertThat(testGameCard.getPlayer2_chosen()).isEqualTo(UPDATED_PLAYER_2_CHOSEN);
        assertThat(testGameCard.getPlayer1_discarded()).isEqualTo(UPDATED_PLAYER_1_DISCARDED);
        assertThat(testGameCard.getPlayer2_discarded()).isEqualTo(UPDATED_PLAYER_2_DISCARDED);
    }

    @Test
    @Transactional
    void patchNonExistingGameCard() throws Exception {
        int databaseSizeBeforeUpdate = gameCardRepository.findAll().size();
        gameCard.setId(count.incrementAndGet());

        // Create the GameCard
        GameCardDTO gameCardDTO = gameCardMapper.toDto(gameCard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gameCardDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameCard in the database
        List<GameCard> gameCardList = gameCardRepository.findAll();
        assertThat(gameCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGameCard() throws Exception {
        int databaseSizeBeforeUpdate = gameCardRepository.findAll().size();
        gameCard.setId(count.incrementAndGet());

        // Create the GameCard
        GameCardDTO gameCardDTO = gameCardMapper.toDto(gameCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameCard in the database
        List<GameCard> gameCardList = gameCardRepository.findAll();
        assertThat(gameCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGameCard() throws Exception {
        int databaseSizeBeforeUpdate = gameCardRepository.findAll().size();
        gameCard.setId(count.incrementAndGet());

        // Create the GameCard
        GameCardDTO gameCardDTO = gameCardMapper.toDto(gameCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameCardMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameCardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameCard in the database
        List<GameCard> gameCardList = gameCardRepository.findAll();
        assertThat(gameCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGameCard() throws Exception {
        // Initialize the database
        gameCardRepository.saveAndFlush(gameCard);

        int databaseSizeBeforeDelete = gameCardRepository.findAll().size();

        // Delete the gameCard
        restGameCardMockMvc
            .perform(delete(ENTITY_API_URL_ID, gameCard.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GameCard> gameCardList = gameCardRepository.findAll();
        assertThat(gameCardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
