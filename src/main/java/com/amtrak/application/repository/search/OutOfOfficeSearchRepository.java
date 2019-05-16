package com.amtrak.application.repository.search;

import com.amtrak.application.domain.OutOfOffice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link OutOfOffice} entity.
 */
public interface OutOfOfficeSearchRepository extends ElasticsearchRepository<OutOfOffice, Long> {
}
