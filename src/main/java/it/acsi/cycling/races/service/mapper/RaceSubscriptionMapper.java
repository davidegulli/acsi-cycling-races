package it.acsi.cycling.races.service.mapper;

import it.acsi.cycling.races.domain.*;
import it.acsi.cycling.races.service.dto.RaceSubscriptionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link RaceSubscription} and its DTO {@link RaceSubscriptionDTO}.
 */
@Mapper(componentModel = "spring", uses = {RaceMapper.class})
public interface RaceSubscriptionMapper extends EntityMapper<RaceSubscriptionDTO, RaceSubscription> {

    @Mapping(source = "race.id", target = "raceId")
    RaceSubscriptionDTO toDto(RaceSubscription raceSubscription);

    @Mapping(source = "raceId", target = "race")
    RaceSubscription toEntity(RaceSubscriptionDTO raceSubscriptionDTO);

    default RaceSubscription fromId(Long id) {
        if (id == null) {
            return null;
        }
        RaceSubscription raceSubscription = new RaceSubscription();
        raceSubscription.setId(id);
        return raceSubscription;
    }
}
