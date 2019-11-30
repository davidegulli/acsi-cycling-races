package it.acsi.cycling.races.repository.search;

import it.acsi.cycling.races.domain.SubscriptionDiscount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link SubscriptionDiscount} entity.
 */
public interface SubscriptionDiscountSearchRepository extends ElasticsearchRepository<SubscriptionDiscount, Long> {
}
