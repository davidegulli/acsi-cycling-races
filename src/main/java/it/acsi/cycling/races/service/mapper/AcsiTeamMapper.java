package it.acsi.cycling.races.service.mapper;

import it.acsi.cycling.races.domain.*;
import it.acsi.cycling.races.service.dto.AcsiTeamDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AcsiTeam} and its DTO {@link AcsiTeamDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AcsiTeamMapper extends EntityMapper<AcsiTeamDTO, AcsiTeam> {


    @Mapping(target = "races", ignore = true)
    @Mapping(target = "removeRace", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    @Mapping(target = "removeContact", ignore = true)
    AcsiTeam toEntity(AcsiTeamDTO acsiTeamDTO);

    default AcsiTeam fromId(Long id) {
        if (id == null) {
            return null;
        }
        AcsiTeam acsiTeam = new AcsiTeam();
        acsiTeam.setId(id);
        return acsiTeam;
    }
}
