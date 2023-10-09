package be.vandenn3.quiestce.service.mapper;

import be.vandenn3.quiestce.domain.Game;
import be.vandenn3.quiestce.domain.Message;
import be.vandenn3.quiestce.domain.Player;
import be.vandenn3.quiestce.service.dto.GameDTO;
import be.vandenn3.quiestce.service.dto.MessageDTO;
import be.vandenn3.quiestce.service.dto.PlayerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Message} and its DTO {@link MessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {
    @Mapping(target = "game", source = "game", qualifiedByName = "gameId")
    @Mapping(target = "author", source = "author", qualifiedByName = "playerPseudo")
    MessageDTO toDto(Message s);

    @Named("gameId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GameDTO toDtoGameId(Game game);

    @Named("playerPseudo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "pseudo", source = "pseudo")
    PlayerDTO toDtoPlayerPseudo(Player player);
}
