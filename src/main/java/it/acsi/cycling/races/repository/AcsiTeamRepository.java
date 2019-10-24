package it.acsi.cycling.races.repository;

import it.acsi.cycling.races.domain.AcsiTeam;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the AcsiTeam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AcsiTeamRepository extends JpaRepository<AcsiTeam, Long> {

    Optional<AcsiTeam> findByUserId(String id);

    Optional<AcsiTeam> findByCode(String code);

    @Query(value = "select * " +
                    "from acsi_team " +
                    "where code like :codeInitial%",
           nativeQuery = true)
    List<AcsiTeam> searchByCodeInitial(@Param("codeInitial") String codeInitial);
}
