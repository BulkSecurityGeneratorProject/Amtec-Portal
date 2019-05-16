package com.amtrak.application.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link SprSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SprSearchRepositoryMockConfiguration {

    @MockBean
    private SprSearchRepository mockSprSearchRepository;

}
