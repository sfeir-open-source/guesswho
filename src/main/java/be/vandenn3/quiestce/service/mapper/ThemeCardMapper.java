package be.vandenn3.quiestce.service.mapper;

import be.vandenn3.quiestce.domain.Picture;
import be.vandenn3.quiestce.domain.Theme;
import be.vandenn3.quiestce.domain.ThemeCard;
import be.vandenn3.quiestce.service.dto.PictureDTO;
import be.vandenn3.quiestce.service.dto.ThemeCardDTO;
import be.vandenn3.quiestce.service.dto.ThemeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ThemeCard} and its DTO {@link ThemeCardDTO}.
 */
@Mapper(componentModel = "spring")
public interface ThemeCardMapper extends EntityMapper<ThemeCardDTO, ThemeCard> {
    @Mapping(target = "theme", source = "theme", qualifiedByName = "themeName")
    @Mapping(target = "picture", source = "picture", qualifiedByName = "pictureId")
    ThemeCardDTO toDto(ThemeCard s);

    @Named("themeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ThemeDTO toDtoThemeName(Theme theme);

    @Named("pictureId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PictureDTO toDtoPictureId(Picture picture);
}
