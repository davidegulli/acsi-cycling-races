package it.acsi.cycling.races.repository.search;

import it.acsi.cycling.races.domain.File;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link File} entity.
 */
public interface FileSearchRepository extends ElasticsearchRepository<File, Long> {
}
