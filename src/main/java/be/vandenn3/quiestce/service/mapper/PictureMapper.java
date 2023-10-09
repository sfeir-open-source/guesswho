package be.vandenn3.quiestce.service.mapper;

import be.vandenn3.quiestce.domain.Picture;
import be.vandenn3.quiestce.service.dto.PictureDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Picture} and its DTO {@link PictureDTO}.
 */
@Mapper(componentModel = "spring")
public interface PictureMapper extends EntityMapper<PictureDTO, Picture> {}
