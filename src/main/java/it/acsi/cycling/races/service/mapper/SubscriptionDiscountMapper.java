package it.acsi.cycling.races.service.mapper;

import it.acsi.cycling.races.domain.*;
import it.acsi.cycling.races.service.dto.SubscriptionDiscountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionDiscount} and its DTO {@link SubscriptionDiscountDTO}.
 */
@Mapper(componentModel = "spring", uses = {SubscriptionTypeMapper.class})
public interface SubscriptionDiscountMapper extends EntityMapper<SubscriptionDiscountDTO, SubscriptionDiscount> {

    @Mapping(source = "subscriptionType.id", target = "subscriptionTypeId")
    SubscriptionDiscountDTO toDto(SubscriptionDiscount subscriptionDiscount);

    @Mapping(source = "subscriptionTypeId", target = "subscriptionType")
    SubscriptionDiscount toEntity(SubscriptionDiscountDTO subscriptionDiscountDTO);

    default SubscriptionDiscount fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubscriptionDiscount subscriptionDiscount = new SubscriptionDiscount();
        subscriptionDiscount.setId(id);
        return subscriptionDiscount;
    }
}
