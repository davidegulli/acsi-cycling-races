package it.acsi.cycling.races.repository;

import it.acsi.cycling.races.domain.RaceSubscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;


/**
 * Spring Data  repository for the RaceSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RaceSubscriptionRepository extends JpaRepository<RaceSubscription, Long> {

    Stream<RaceSubscription> findByRaceId(@Param("raceId") Long raceId);

    Page<RaceSubscription> findByRaceId(@Param("raceId") Long raceId, Pageable pageable);

}
