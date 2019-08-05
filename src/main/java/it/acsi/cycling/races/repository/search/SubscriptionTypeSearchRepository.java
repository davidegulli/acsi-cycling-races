package it.acsi.cycling.races.repository.search;

import it.acsi.cycling.races.domain.SubscriptionType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link SubscriptionType} entity.
 */
public interface SubscriptionTypeSearchRepository extends ElasticsearchRepository<SubscriptionType, Long> {
}
