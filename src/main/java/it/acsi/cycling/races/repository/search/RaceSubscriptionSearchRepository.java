package it.acsi.cycling.races.repository.search;

import it.acsi.cycling.races.domain.RaceSubscription;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link RaceSubscription} entity.
 */
public interface RaceSubscriptionSearchRepository extends ElasticsearchRepository<RaceSubscription, Long> {
}
