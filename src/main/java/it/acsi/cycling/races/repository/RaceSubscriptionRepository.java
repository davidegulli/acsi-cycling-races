package it.acsi.cycling.races.repository;

import it.acsi.cycling.races.domain.RaceSubscription;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RaceSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RaceSubscriptionRepository extends JpaRepository<RaceSubscription, Long> {

}
