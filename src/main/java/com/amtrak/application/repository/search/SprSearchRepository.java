package com.amtrak.application.repository.search;

import com.amtrak.application.domain.Spr;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Spr} entity.
 */
public interface SprSearchRepository extends ElasticsearchRepository<Spr, Long> {
}
