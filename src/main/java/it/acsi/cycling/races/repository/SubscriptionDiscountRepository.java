package it.acsi.cycling.races.repository;

import it.acsi.cycling.races.domain.SubscriptionDiscount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SubscriptionDiscount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionDiscountRepository extends JpaRepository<SubscriptionDiscount, Long> {

}
