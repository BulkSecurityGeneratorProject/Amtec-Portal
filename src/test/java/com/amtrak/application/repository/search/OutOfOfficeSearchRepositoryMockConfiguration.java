package com.amtrak.application.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link OutOfOfficeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class OutOfOfficeSearchRepositoryMockConfiguration {

    @MockBean
    private OutOfOfficeSearchRepository mockOutOfOfficeSearchRepository;

}
