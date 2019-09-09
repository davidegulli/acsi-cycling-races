package it.acsi.cycling.races.repository;

import it.acsi.cycling.races.domain.AcsiTeam;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the AcsiTeam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AcsiTeamRepository extends JpaRepository<AcsiTeam, Long> {

    Optional<AcsiTeam> findByUserId(String id);
}
