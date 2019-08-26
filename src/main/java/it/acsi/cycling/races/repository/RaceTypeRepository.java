package it.acsi.cycling.races.repository;

import it.acsi.cycling.races.domain.RaceType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RaceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RaceTypeRepository extends JpaRepository<RaceType, Long> {

}
