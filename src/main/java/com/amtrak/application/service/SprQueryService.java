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

import com.amtrak.application.domain.Spr;
import com.amtrak.application.domain.*; // for static metamodels
import com.amtrak.application.repository.SprRepository;
import com.amtrak.application.repository.search.SprSearchRepository;
import com.amtrak.application.service.dto.SprCriteria;

/**
 * Service for executing complex queries for {@link Spr} entities in the database.
 * The main input is a {@link SprCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Spr} or a {@link Page} of {@link Spr} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SprQueryService extends QueryService<Spr> {

    private final Logger log = LoggerFactory.getLogger(SprQueryService.class);

    private final SprRepository sprRepository;

    private final SprSearchRepository sprSearchRepository;

    public SprQueryService(SprRepository sprRepository, SprSearchRepository sprSearchRepository) {
        this.sprRepository = sprRepository;
        this.sprSearchRepository = sprSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Spr} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Spr> findByCriteria(SprCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Spr> specification = createSpecification(criteria);
        return sprRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Spr} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Spr> findByCriteria(SprCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Spr> specification = createSpecification(criteria);
        return sprRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SprCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Spr> specification = createSpecification(criteria);
        return sprRepository.count(specification);
    }

    /**
     * Function to convert SprCriteria to a {@link Specification}.
     */
    private Specification<Spr> createSpecification(SprCriteria criteria) {
        Specification<Spr> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Spr_.id));
            }
            if (criteria.getTerritory() != null) {
                specification = specification.and(buildSpecification(criteria.getTerritory(), Spr_.territory));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumber(), Spr_.number));
            }
            if (criteria.getFullNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullNumber(), Spr_.fullNumber));
            }
            if (criteria.getPriority() != null) {
                specification = specification.and(buildSpecification(criteria.getPriority(), Spr_.priority));
            }
            if (criteria.getResolution() != null) {
                specification = specification.and(buildSpecification(criteria.getResolution(), Spr_.resolution));
            }
            if (criteria.getJiraLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJiraLink(), Spr_.jiraLink));
            }
            if (criteria.getReviewerId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewerId(), Spr_.reviewerId));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Spr_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getReleaseId() != null) {
                specification = specification.and(buildSpecification(criteria.getReleaseId(),
                    root -> root.join(Spr_.release, JoinType.LEFT).get(Release_.id)));
            }
        }
        return specification;
    }
}
