package it.acsi.cycling.races.service.mapper;

import it.acsi.cycling.races.domain.*;
import it.acsi.cycling.races.service.dto.FileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link File} and its DTO {@link FileDTO}.
 */
@Mapper(componentModel = "spring", uses = {RaceMapper.class})
public interface FileMapper extends EntityMapper<FileDTO, File> {

    @Mapping(source = "race.id", target = "raceId")
    FileDTO toDto(File file);

    @Mapping(source = "raceId", target = "race")
    File toEntity(FileDTO fileDTO);

    default File fromId(Long id) {
        if (id == null) {
            return null;
        }
        File file = new File();
        file.setId(id);
        return file;
    }
}
