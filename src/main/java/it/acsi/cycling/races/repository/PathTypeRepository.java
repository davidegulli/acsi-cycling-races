package it.acsi.cycling.races.repository;

import it.acsi.cycling.races.domain.PathType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PathType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PathTypeRepository extends JpaRepository<PathType, Long> {

}
