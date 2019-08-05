package it.acsi.cycling.races.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link RaceSubscriptionSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RaceSubscriptionSearchRepositoryMockConfiguration {

    @MockBean
    private RaceSubscriptionSearchRepository mockRaceSubscriptionSearchRepository;

}
