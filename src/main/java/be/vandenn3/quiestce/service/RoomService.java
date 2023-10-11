package be.vandenn3.quiestce.service;

import be.vandenn3.quiestce.domain.Player;
import be.vandenn3.quiestce.domain.Room;
import be.vandenn3.quiestce.repository.RoomRepository;
import be.vandenn3.quiestce.service.dto.RoomDTO;
import be.vandenn3.quiestce.service.mapper.RoomMapper;
import be.vandenn3.quiestce.util.StringUtils;
import jakarta.validation.constraints.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
// A five stars room service !
public class RoomService {

    private RoomRepository roomRepository;
    private RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    public RoomDTO createRoom(String name, Player currentPlayer) {
        final String roomCode = StringUtils.secureRandomAlphanum(8);
        final Room room = new Room().name(name).player1(currentPlayer).code(roomCode);
        return roomMapper.toDto(roomRepository.save(room));
    }

    public List<RoomDTO> getRoomsFor(Player currentPlayer) {
        final List<Room> rooms = roomRepository.findAllByPlayer1OrPlayer2(currentPlayer, currentPlayer);
        return roomMapper.toDto(rooms);
    }

    public RoomDTO joinRoom(String code, @NotNull Player currentPlayer) {
        final List<Room> rooms = roomRepository.findAllByCode(code);
        if (rooms.size() > 1) {
            throw new DataIntegrityViolationException("data integrity exception");
        }
        if (rooms.isEmpty()) {
            throw new AccessDeniedException("bad code");
        }
        Room room = rooms.get(0);
        Boolean player1IsCurrentUser = Objects.nonNull(room.getPlayer1()) && room.getPlayer1().equals(currentPlayer);
        Boolean player2IsCurrentUser = Objects.nonNull(room.getPlayer2()) && room.getPlayer2().equals(currentPlayer);
        if (player1IsCurrentUser || player2IsCurrentUser) {
            throw new AccessDeniedException("player is already in the room");
        }
        if (Objects.nonNull(room.getPlayer2())) {
            throw new AccessDeniedException("room is full");
        }

        room.setPlayer2(currentPlayer);
        room = roomRepository.save(room);
        return roomMapper.toDto(roomRepository.findOneWithEagerRelationships(room.getId()).orElseThrow());
    }
}
