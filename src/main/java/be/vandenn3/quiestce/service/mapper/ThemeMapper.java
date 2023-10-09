package be.vandenn3.quiestce.service.mapper;

import be.vandenn3.quiestce.domain.Picture;
import be.vandenn3.quiestce.domain.Theme;
import be.vandenn3.quiestce.service.dto.PictureDTO;
import be.vandenn3.quiestce.service.dto.ThemeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Theme} and its DTO {@link ThemeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ThemeMapper extends EntityMapper<ThemeDTO, Theme> {
    @Mapping(target = "main_picture", source = "main_picture", qualifiedByName = "pictureId")
    ThemeDTO toDto(Theme s);

    @Named("pictureId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PictureDTO toDtoPictureId(Picture picture);
}
