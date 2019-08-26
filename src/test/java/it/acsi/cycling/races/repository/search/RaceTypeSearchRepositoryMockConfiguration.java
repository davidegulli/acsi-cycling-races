package it.acsi.cycling.races.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link RaceTypeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RaceTypeSearchRepositoryMockConfiguration {

    @MockBean
    private RaceTypeSearchRepository mockRaceTypeSearchRepository;

}
