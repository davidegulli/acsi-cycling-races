package it.acsi.cycling.races.repository;

import it.acsi.cycling.races.domain.File;
import it.acsi.cycling.races.domain.enumeration.EntityType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Set;


/**
 * Spring Data  repository for the File entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    Set<File> findByEntityTypeAndEntityId(EntityType entityType, Long entityId);
}
