package it.acsi.cycling.races.service.mapper;

import it.acsi.cycling.races.domain.*;
import it.acsi.cycling.races.service.dto.PathTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PathType} and its DTO {@link PathTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {RaceMapper.class})
public interface PathTypeMapper extends EntityMapper<PathTypeDTO, PathType> {

    @Mapping(source = "race.id", target = "raceId")
    PathTypeDTO toDto(PathType pathType);

    @Mapping(source = "raceId", target = "race")
    PathType toEntity(PathTypeDTO pathTypeDTO);

    default PathType fromId(Long id) {
        if (id == null) {
            return null;
        }
        PathType pathType = new PathType();
        pathType.setId(id);
        return pathType;
    }
}
