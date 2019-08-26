package it.acsi.cycling.races.service.mapper;

import it.acsi.cycling.races.domain.*;
import it.acsi.cycling.races.service.dto.RaceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Race} and its DTO {@link RaceDTO}.
 */
@Mapper(componentModel = "spring", uses = {AcsiTeamMapper.class})
public interface RaceMapper extends EntityMapper<RaceDTO, Race> {

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
    @Mapping(target = "types", ignore = true)
    @Mapping(target = "removeType", ignore = true)
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
}
