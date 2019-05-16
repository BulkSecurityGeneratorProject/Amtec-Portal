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

import com.amtrak.application.domain.Release;
import com.amtrak.application.domain.*; // for static metamodels
import com.amtrak.application.repository.ReleaseRepository;
import com.amtrak.application.repository.search.ReleaseSearchRepository;
import com.amtrak.application.service.dto.ReleaseCriteria;

/**
 * Service for executing complex queries for {@link Release} entities in the database.
 * The main input is a {@link ReleaseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Release} or a {@link Page} of {@link Release} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReleaseQueryService extends QueryService<Release> {

    private final Logger log = LoggerFactory.getLogger(ReleaseQueryService.class);

    private final ReleaseRepository releaseRepository;

    private final ReleaseSearchRepository releaseSearchRepository;

    public ReleaseQueryService(ReleaseRepository releaseRepository, ReleaseSearchRepository releaseSearchRepository) {
        this.releaseRepository = releaseRepository;
        this.releaseSearchRepository = releaseSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Release} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Release> findByCriteria(ReleaseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Release> specification = createSpecification(criteria);
        return releaseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Release} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Release> findByCriteria(ReleaseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Release> specification = createSpecification(criteria);
        return releaseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReleaseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Release> specification = createSpecification(criteria);
        return releaseRepository.count(specification);
    }

    /**
     * Function to convert ReleaseCriteria to a {@link Specification}.
     */
    private Specification<Release> createSpecification(ReleaseCriteria criteria) {
        Specification<Release> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Release_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Release_.date));
            }
            if (criteria.getTerritory() != null) {
                specification = specification.and(buildSpecification(criteria.getTerritory(), Release_.territory));
            }
            if (criteria.getBuild() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBuild(), Release_.build));
            }
            if (criteria.getReleaseLetter() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReleaseLetter(), Release_.releaseLetter));
            }
            if (criteria.getPrefixLetter() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrefixLetter(), Release_.prefixLetter));
            }
            if (criteria.getDatabaseVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDatabaseVersion(), Release_.databaseVersion));
            }
            if (criteria.getWsVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWsVersion(), Release_.wsVersion));
            }
            if (criteria.getTmaVersion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTmaVersion(), Release_.tmaVersion));
            }
            if (criteria.getPort() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPort(), Release_.port));
            }
            if (criteria.getCurrent() != null) {
                specification = specification.and(buildSpecification(criteria.getCurrent(), Release_.current));
            }
            if (criteria.getSprId() != null) {
                specification = specification.and(buildSpecification(criteria.getSprId(),
                    root -> root.join(Release_.sprs, JoinType.LEFT).get(Spr_.id)));
            }
        }
        return specification;
    }
}
