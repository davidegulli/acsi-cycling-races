package it.acsi.cycling.races.repository.search;

import it.acsi.cycling.races.domain.PathType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PathType} entity.
 */
public interface PathTypeSearchRepository extends ElasticsearchRepository<PathType, Long> {
}
