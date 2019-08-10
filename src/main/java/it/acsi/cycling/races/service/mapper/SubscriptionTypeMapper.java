package it.acsi.cycling.races.service.mapper;

import it.acsi.cycling.races.domain.*;
import it.acsi.cycling.races.service.dto.SubscriptionTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionType} and its DTO {@link SubscriptionTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {RaceMapper.class})
public interface SubscriptionTypeMapper extends EntityMapper<SubscriptionTypeDTO, SubscriptionType> {

    @Mapping(source = "race.id", target = "raceId")
    SubscriptionTypeDTO toDto(SubscriptionType subscriptionType);

    @Mapping(source = "raceId", target = "race")
    SubscriptionType toEntity(SubscriptionTypeDTO subscriptionTypeDTO);

    default SubscriptionType fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubscriptionType subscriptionType = new SubscriptionType();
        subscriptionType.setId(id);
        return subscriptionType;
    }
}
