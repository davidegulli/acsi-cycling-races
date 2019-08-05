package it.acsi.cycling.races.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PathTypeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PathTypeSearchRepositoryMockConfiguration {

    @MockBean
    private PathTypeSearchRepository mockPathTypeSearchRepository;

}
