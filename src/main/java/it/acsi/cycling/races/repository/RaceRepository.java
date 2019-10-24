package it.acsi.cycling.races.repository;

import it.acsi.cycling.races.domain.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data  repository for the Race entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {

    @Query(value = "select * from race" +
                   " where date >= :date" +
                   " order by date",
           countQuery = "select * from race" +
                        " where date > :date",
           nativeQuery=true)
    Page<Race> findByGreaterDateOrderedByDate(@Param("date")LocalDate date, Pageable pageable);

    @Query(value = "select * from race" +
        " where date >= :date" +
        " and acsi_team_id = :teamId" +
        " order by date",
        countQuery = "select * from race" +
            " where date >= :date" +
            " and acsi_team_id = :teamId",
        nativeQuery=true)
    Page<Race> findByGreaterEqualDateAndTeamIdOrderedByDate(
        @Param("date")LocalDate date, @Param("teamId") Long teamId, Pageable pageable);


}
