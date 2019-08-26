package it.acsi.cycling.races.service.mapper;

import it.acsi.cycling.races.domain.*;
import it.acsi.cycling.races.service.dto.RaceTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link RaceType} and its DTO {@link RaceTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {RaceMapper.class})
public interface RaceTypeMapper extends EntityMapper<RaceTypeDTO, RaceType> {

    @Mapping(source = "race.id", target = "raceId")
    RaceTypeDTO toDto(RaceType raceType);

    @Mapping(source = "raceId", target = "race")
    RaceType toEntity(RaceTypeDTO raceTypeDTO);

    default RaceType fromId(Long id) {
        if (id == null) {
            return null;
        }
        RaceType raceType = new RaceType();
        raceType.setId(id);
        return raceType;
    }
}
