package be.vandenn3.quiestce.service.mapper;

import be.vandenn3.quiestce.domain.UserAnonymous;
import be.vandenn3.quiestce.service.dto.UserAnonymousDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAnonymous} and its DTO {@link UserAnonymousDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAnonymousMapper extends EntityMapper<UserAnonymousDTO, UserAnonymous> {}
