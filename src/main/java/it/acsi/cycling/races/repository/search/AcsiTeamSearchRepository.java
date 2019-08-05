package it.acsi.cycling.races.repository.search;

import it.acsi.cycling.races.domain.AcsiTeam;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AcsiTeam} entity.
 */
public interface AcsiTeamSearchRepository extends ElasticsearchRepository<AcsiTeam, Long> {
}
