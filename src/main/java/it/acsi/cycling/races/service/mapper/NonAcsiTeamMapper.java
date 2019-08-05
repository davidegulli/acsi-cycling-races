package it.acsi.cycling.races.service.mapper;

import it.acsi.cycling.races.domain.*;
import it.acsi.cycling.races.service.dto.NonAcsiTeamDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link NonAcsiTeam} and its DTO {@link NonAcsiTeamDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NonAcsiTeamMapper extends EntityMapper<NonAcsiTeamDTO, NonAcsiTeam> {



    default NonAcsiTeam fromId(Long id) {
        if (id == null) {
            return null;
        }
        NonAcsiTeam nonAcsiTeam = new NonAcsiTeam();
        nonAcsiTeam.setId(id);
        return nonAcsiTeam;
    }
}
