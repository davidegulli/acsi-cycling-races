package it.acsi.cycling.races.repository;

import it.acsi.cycling.races.domain.NonAcsiTeam;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the NonAcsiTeam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NonAcsiTeamRepository extends JpaRepository<NonAcsiTeam, Long> {

    Optional<NonAcsiTeam> findByCode(String code);

    @Query(value = "select * " +
                    "from non_acsi_team " +
                    "where code like :codeInitial%",
        nativeQuery = true)
    List<NonAcsiTeam> searchByCodeInitial(@Param("codeInitial") String codeInitial);

}
