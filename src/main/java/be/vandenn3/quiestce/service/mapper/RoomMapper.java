package be.vandenn3.quiestce.service.mapper;

import be.vandenn3.quiestce.domain.Room;
import be.vandenn3.quiestce.service.dto.RoomDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Room} and its DTO {@link RoomDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoomMapper extends EntityMapper<RoomDTO, Room> {}
