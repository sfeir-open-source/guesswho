package be.vandenn3.quiestce.service.mapper;

import be.vandenn3.quiestce.domain.Player;
import be.vandenn3.quiestce.service.dto.PlayerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Player} and its DTO {@link PlayerDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlayerMapper extends EntityMapper<PlayerDTO, Player> {}
