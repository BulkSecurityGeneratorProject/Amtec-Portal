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

import com.amtrak.application.domain.OutOfOffice;
import com.amtrak.application.domain.*; // for static metamodels
import com.amtrak.application.repository.OutOfOfficeRepository;
import com.amtrak.application.repository.search.OutOfOfficeSearchRepository;
import com.amtrak.application.service.dto.OutOfOfficeCriteria;

/**
 * Service for executing complex queries for {@link OutOfOffice} entities in the database.
 * The main input is a {@link OutOfOfficeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OutOfOffice} or a {@link Page} of {@link OutOfOffice} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OutOfOfficeQueryService extends QueryService<OutOfOffice> {

    private final Logger log = LoggerFactory.getLogger(OutOfOfficeQueryService.class);

    private final OutOfOfficeRepository outOfOfficeRepository;

    private final OutOfOfficeSearchRepository outOfOfficeSearchRepository;

    public OutOfOfficeQueryService(OutOfOfficeRepository outOfOfficeRepository, OutOfOfficeSearchRepository outOfOfficeSearchRepository) {
        this.outOfOfficeRepository = outOfOfficeRepository;
        this.outOfOfficeSearchRepository = outOfOfficeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link OutOfOffice} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OutOfOffice> findByCriteria(OutOfOfficeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OutOfOffice> specification = createSpecification(criteria);
        return outOfOfficeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OutOfOffice} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OutOfOffice> findByCriteria(OutOfOfficeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OutOfOffice> specification = createSpecification(criteria);
        return outOfOfficeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OutOfOfficeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OutOfOffice> specification = createSpecification(criteria);
        return outOfOfficeRepository.count(specification);
    }

    /**
     * Function to convert OutOfOfficeCriteria to a {@link Specification}.
     */
    private Specification<OutOfOffice> createSpecification(OutOfOfficeCriteria criteria) {
        Specification<OutOfOffice> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), OutOfOffice_.id));
            }
            if (criteria.getStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStart(), OutOfOffice_.start));
            }
            if (criteria.getEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEnd(), OutOfOffice_.end));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(OutOfOffice_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
