package it.acsi.cycling.races.repository;

import it.acsi.cycling.races.domain.NonAcsiTeam;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the NonAcsiTeam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NonAcsiTeamRepository extends JpaRepository<NonAcsiTeam, Long> {

}
