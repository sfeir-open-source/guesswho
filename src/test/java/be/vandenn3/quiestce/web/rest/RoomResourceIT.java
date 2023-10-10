package be.vandenn3.quiestce.web.rest;

import be.vandenn3.quiestce.domain.Player;
import be.vandenn3.quiestce.domain.Room;
import be.vandenn3.quiestce.repository.RoomRepository;
import be.vandenn3.quiestce.service.dto.RoomCreationDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RoomResource} REST controller.
 */
class RoomResourceIT extends IntegrationTestBase {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rooms";
    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomMockMvc;

    private Room room;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Room createEntity(EntityManager em) {
        Room room = new Room().name(DEFAULT_NAME).code(DEFAULT_CODE);
        return room;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Room createUpdatedEntity(EntityManager em) {
        Room room = new Room().name(UPDATED_NAME).code(UPDATED_CODE);
        return room;
    }

    @BeforeEach
    public void initTest() {
        room = createEntity(em);
    }

    @Test
    @Transactional
    void createRoom() throws Exception {
        int databaseSizeBeforeCreate = roomRepository.findAll().size();
        // Create the Room
        RoomCreationDTO roomDTO = new RoomCreationDTO();
        roomDTO.setName(DEFAULT_NAME);
        restRoomMockMvc
            .perform(
                post(ENTITY_API_URL).sessionAttrs(sessionattr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeCreate + 1);
        Room testRoom = roomList.get(roomList.size() - 1);
        assertThat(testRoom.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRoom.getCode()).isNotBlank();
    }

    @Test
    @Transactional
    void createRoomWithEmptyName() throws Exception {
        RoomCreationDTO roomDTO = new RoomCreationDTO();
        roomDTO.setName("");
        restRoomMockMvc
            .perform(
                post(ENTITY_API_URL).sessionAttrs(sessionattr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomDTO))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void getMyRooms() throws Exception {
        Player player1 = createValidPlayer("player1");
        Player player2 = createValidPlayer("player2");
        roomRepository.save(new Room().name("room1").player1(player1).player2(player2).code("tmp"));
        Room room2 = roomRepository.save(new Room().name("room2").player1(player2).player2(currentPlayer).code("tmp"));
        Room room3 = roomRepository.save(new Room().name("room3").player1(currentPlayer).player2(player1).code("tmp"));
        Room room4 = roomRepository.save(new Room().name("room4").player1(currentPlayer).code("tmp"));
        Room room5 = roomRepository.save(new Room().name("room5").player2(currentPlayer).code("tmp"));
        roomRepository.save(new Room().name("room6").player1(player1).code("tmp"));
        roomRepository.save(new Room().name("room7").player2(player2).code("tmp"));

        restRoomMockMvc
            .perform(
                get(ENTITY_API_URL).sessionAttrs(sessionattr)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", hasSize(4)))
            .andExpect(jsonPath("$.[0].name", is(room2.getName())))
            .andExpect(jsonPath("$.[1].name", is(room3.getName())))
            .andExpect(jsonPath("$.[2].name", is(room4.getName())))
            .andExpect(jsonPath("$.[3].name", is(room5.getName())));
    }

    @Test
    void joinRoom() throws Exception {
        Player player1 = createValidPlayer("player1");
        Room room = roomRepository.save(new Room().name("room").player1(player1).code("tmp"));
        restRoomMockMvc
            .perform(
                post(ENTITY_API_URL + "/join/" + room.getCode()).sessionAttrs(sessionattr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    void joinFullRoom() throws Exception {
        Player player1 = createValidPlayer("player1");
        Player player2 = createValidPlayer("player2");
        Room room = roomRepository.save(new Room().name("room").player1(player1).player2(player2).code("tmp"));
        restRoomMockMvc
            .perform(
                post(ENTITY_API_URL + "/join/" + room.getCode()).sessionAttrs(sessionattr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isForbidden());
    }

    @Test
    void joinRoomWithBadCode() throws Exception {
        Player player1 = createValidPlayer("player1");
        Room room = roomRepository.save(new Room().name("room").player1(player1).code("tmp"));
        restRoomMockMvc
            .perform(
                post(ENTITY_API_URL + "/join/" + "bad-code").sessionAttrs(sessionattr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isForbidden());
    }

    @Test
    void joinRoomWhenAlreadyJoined() throws Exception {
        Room room = roomRepository.save(new Room().name("room").player1(currentPlayer).code("tmp"));
        restRoomMockMvc
            .perform(
                post(ENTITY_API_URL + "/join/" + room.getCode()).sessionAttrs(sessionattr)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isForbidden());
    }

}
