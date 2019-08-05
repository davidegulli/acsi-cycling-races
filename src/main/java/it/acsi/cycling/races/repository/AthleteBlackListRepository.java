package it.acsi.cycling.races.repository;

import it.acsi.cycling.races.domain.AthleteBlackList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AthleteBlackList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AthleteBlackListRepository extends JpaRepository<AthleteBlackList, Long> {

}
