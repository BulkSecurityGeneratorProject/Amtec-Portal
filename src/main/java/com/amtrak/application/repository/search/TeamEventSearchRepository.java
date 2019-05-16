package com.amtrak.application.repository.search;

import com.amtrak.application.domain.TeamEvent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TeamEvent} entity.
 */
public interface TeamEventSearchRepository extends ElasticsearchRepository<TeamEvent, Long> {
}
