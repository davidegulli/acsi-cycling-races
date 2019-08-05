package it.acsi.cycling.races.repository.search;

import it.acsi.cycling.races.domain.Race;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Race} entity.
 */
public interface RaceSearchRepository extends ElasticsearchRepository<Race, Long> {
}
