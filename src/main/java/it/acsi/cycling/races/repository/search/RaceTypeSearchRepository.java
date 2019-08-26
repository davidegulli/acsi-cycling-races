package it.acsi.cycling.races.repository.search;

import it.acsi.cycling.races.domain.RaceType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link RaceType} entity.
 */
public interface RaceTypeSearchRepository extends ElasticsearchRepository<RaceType, Long> {
}
