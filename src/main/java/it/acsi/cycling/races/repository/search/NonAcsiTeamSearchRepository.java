package it.acsi.cycling.races.repository.search;

import it.acsi.cycling.races.domain.NonAcsiTeam;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link NonAcsiTeam} entity.
 */
public interface NonAcsiTeamSearchRepository extends ElasticsearchRepository<NonAcsiTeam, Long> {
}
