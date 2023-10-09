package be.vandenn3.quiestce.service.mapper;

import be.vandenn3.quiestce.domain.Game;
import be.vandenn3.quiestce.domain.Player;
import be.vandenn3.quiestce.domain.Room;
import be.vandenn3.quiestce.domain.Theme;
import be.vandenn3.quiestce.service.dto.GameDTO;
import be.vandenn3.quiestce.service.dto.PlayerDTO;
import be.vandenn3.quiestce.service.dto.RoomDTO;
import be.vandenn3.quiestce.service.dto.ThemeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Game} and its DTO {@link GameDTO}.
 */
@Mapper(componentModel = "spring")
public interface GameMapper extends EntityMapper<GameDTO, Game> {
    @Mapping(target = "room", source = "room", qualifiedByName = "roomName")
    @Mapping(target = "theme", source = "theme", qualifiedByName = "themeName")
    @Mapping(target = "winner", source = "winner", qualifiedByName = "playerPseudo")
    @Mapping(target = "nextTurn", source = "nextTurn", qualifiedByName = "playerPseudo")
    GameDTO toDto(Game s);

    @Named("roomName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RoomDTO toDtoRoomName(Room room);

    @Named("themeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ThemeDTO toDtoThemeName(Theme theme);

    @Named("playerPseudo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "pseudo", source = "pseudo")
    PlayerDTO toDtoPlayerPseudo(Player player);
}
