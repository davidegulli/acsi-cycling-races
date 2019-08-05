package it.acsi.cycling.races.repository;

import it.acsi.cycling.races.domain.SubscriptionType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SubscriptionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionType, Long> {

}
