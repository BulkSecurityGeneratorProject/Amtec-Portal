package com.amtrak.application.repository.search;

import com.amtrak.application.domain.Release;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Release} entity.
 */
public interface ReleaseSearchRepository extends ElasticsearchRepository<Release, Long> {
}
