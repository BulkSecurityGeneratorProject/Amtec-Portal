package com.amtrak.application.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.amtrak.application.domain.TeamEvent;
import com.amtrak.application.domain.*; // for static metamodels
import com.amtrak.application.repository.TeamEventRepository;
import com.amtrak.application.repository.search.TeamEventSearchRepository;
import com.amtrak.application.service.dto.TeamEventCriteria;

/**
 * Service for executing complex queries for {@link TeamEvent} entities in the database.
 * The main input is a {@link TeamEventCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TeamEvent} or a {@link Page} of {@link TeamEvent} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TeamEventQueryService extends QueryService<TeamEvent> {

    private final Logger log = LoggerFactory.getLogger(TeamEventQueryService.class);

    private final TeamEventRepository teamEventRepository;

    private final TeamEventSearchRepository teamEventSearchRepository;

    public TeamEventQueryService(TeamEventRepository teamEventRepository, TeamEventSearchRepository teamEventSearchRepository) {
        this.teamEventRepository = teamEventRepository;
        this.teamEventSearchRepository = teamEventSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TeamEvent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TeamEvent> findByCriteria(TeamEventCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TeamEvent> specification = createSpecification(criteria);
        return teamEventRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TeamEvent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TeamEvent> findByCriteria(TeamEventCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TeamEvent> specification = createSpecification(criteria);
        return teamEventRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TeamEventCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TeamEvent> specification = createSpecification(criteria);
        return teamEventRepository.count(specification);
    }

    /**
     * Function to convert TeamEventCriteria to a {@link Specification}.
     */
    private Specification<TeamEvent> createSpecification(TeamEventCriteria criteria) {
        Specification<TeamEvent> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TeamEvent_.id));
            }
            if (criteria.getStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStart(), TeamEvent_.start));
            }
            if (criteria.getEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEnd(), TeamEvent_.end));
            }
        }
        return specification;
    }
}
