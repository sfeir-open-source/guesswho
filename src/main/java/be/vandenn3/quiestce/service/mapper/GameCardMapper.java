package be.vandenn3.quiestce.service.mapper;

import be.vandenn3.quiestce.domain.Game;
import be.vandenn3.quiestce.domain.GameCard;
import be.vandenn3.quiestce.domain.ThemeCard;
import be.vandenn3.quiestce.service.dto.GameCardDTO;
import be.vandenn3.quiestce.service.dto.GameDTO;
import be.vandenn3.quiestce.service.dto.ThemeCardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GameCard} and its DTO {@link GameCardDTO}.
 */
@Mapper(componentModel = "spring")
public interface GameCardMapper extends EntityMapper<GameCardDTO, GameCard> {
    @Mapping(target = "game", source = "game", qualifiedByName = "gameId")
    @Mapping(target = "themeCard", source = "themeCard", qualifiedByName = "themeCardName")
    GameCardDTO toDto(GameCard s);

    @Named("gameId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GameDTO toDtoGameId(Game game);

    @Named("themeCardName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ThemeCardDTO toDtoThemeCardName(ThemeCard themeCard);
}
