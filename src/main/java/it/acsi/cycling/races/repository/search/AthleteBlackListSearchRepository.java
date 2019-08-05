package it.acsi.cycling.races.repository.search;

import it.acsi.cycling.races.domain.AthleteBlackList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AthleteBlackList} entity.
 */
public interface AthleteBlackListSearchRepository extends ElasticsearchRepository<AthleteBlackList, Long> {
}
