package it.acsi.cycling.races.service.mapper;

import it.acsi.cycling.races.domain.*;
import it.acsi.cycling.races.domain.enumeration.FileType;
import it.acsi.cycling.races.service.dto.RaceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Race} and its DTO {@link RaceDTO}.
 */
@Mapper(componentModel = "spring", uses = {RaceTypeMapper.class, AcsiTeamMapper.class})
public interface RaceMapper extends EntityMapper<RaceDTO, Race> {

    @Mapping(source = "type.id", target = "typeId")
    @Mapping(source = "type.name", target = "typeName")
    @Mapping(source = "acsiTeam.id", target = "acsiTeamId")
    RaceDTO toDto(Race race);

    @Mapping(target = "contacts", ignore = true)
    @Mapping(target = "removeContact", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "removeAttachment", ignore = true)
    @Mapping(target = "pathTypes", ignore = true)
    @Mapping(target = "removePathType", ignore = true)
    @Mapping(target = "subscriptionTypes", ignore = true)
    @Mapping(target = "removeSubscriptionType", ignore = true)
    @Mapping(target = "subscriptions", ignore = true)
    @Mapping(target = "removeSubscription", ignore = true)
    @Mapping(source = "typeId", target = "type")
    @Mapping(source = "acsiTeamId", target = "acsiTeam")
    Race toEntity(RaceDTO raceDTO);

    default Race fromId(Long id) {
        if (id == null) {
            return null;
        }
        Race race = new Race();
        race.setId(id);
        return race;
    }

    @Named("toDtoWithChildRelation")
    default RaceDTO toDtoWithChildRelation(Race race) {

        RaceDTO dto = toDto(race);
        race.getContacts()
            .stream()
            .findFirst()
            .ifPresent(c -> {
                dto.setContactName(c.getName());
                dto.setContactEmail(c.getEmail());
                dto.setContactPhone(c.getPhone());
            });

        race.getAttachments()
            .stream()
            .filter(f -> f.getType().equals(FileType.LOGO_IMAGE))
            .forEach(f -> {
                dto.setBinaryLogoUrl(f.getUrl());
            });

        race.getAttachments()
            .stream()
            .filter(f -> f.getType().equals(FileType.COVER_IMAGE))
            .forEach(f -> {
                dto.setBinaryCoverUrl(f.getUrl());
            });

        race.getAttachments()
            .stream()
            .filter(f -> f.getType().equals(FileType.PATH_IMAGE))
            .forEach(f -> {
                dto.setBinaryPathMapUrl(f.getUrl());
            });

        return dto;
    }
}
