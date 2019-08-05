package it.acsi.cycling.races.service.mapper;

import it.acsi.cycling.races.domain.*;
import it.acsi.cycling.races.service.dto.AthleteBlackListDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AthleteBlackList} and its DTO {@link AthleteBlackListDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AthleteBlackListMapper extends EntityMapper<AthleteBlackListDTO, AthleteBlackList> {



    default AthleteBlackList fromId(Long id) {
        if (id == null) {
            return null;
        }
        AthleteBlackList athleteBlackList = new AthleteBlackList();
        athleteBlackList.setId(id);
        return athleteBlackList;
    }
}
