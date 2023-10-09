package be.vandenn3.quiestce.service.mapper;

import be.vandenn3.quiestce.domain.Player;
import be.vandenn3.quiestce.domain.Room;
import be.vandenn3.quiestce.service.dto.PlayerDTO;
import be.vandenn3.quiestce.service.dto.RoomDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Room} and its DTO {@link RoomDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoomMapper extends EntityMapper<RoomDTO, Room> {
    @Mapping(target = "player1", source = "player1", qualifiedByName = "playerPseudo")
    @Mapping(target = "player2", source = "player2", qualifiedByName = "playerPseudo")
    RoomDTO toDto(Room s);

    @Named("playerPseudo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "pseudo", source = "pseudo")
    PlayerDTO toDtoPlayerPseudo(Player player);
}
