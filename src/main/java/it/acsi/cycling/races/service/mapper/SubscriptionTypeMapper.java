package it.acsi.cycling.races.service.mapper;

import it.acsi.cycling.races.domain.*;
import it.acsi.cycling.races.service.dto.SubscriptionTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionType} and its DTO {@link SubscriptionTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubscriptionTypeMapper extends EntityMapper<SubscriptionTypeDTO, SubscriptionType> {



    default SubscriptionType fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubscriptionType subscriptionType = new SubscriptionType();
        subscriptionType.setId(id);
        return subscriptionType;
    }
}
