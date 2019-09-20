package it.acsi.cycling.races.repository;

import it.acsi.cycling.races.domain.SubscriptionType;
import org.apache.lucene.index.DocIDMerger;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the SubscriptionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionType, Long> {

    List<SubscriptionType> findByRaceId(Long raceId);

}
