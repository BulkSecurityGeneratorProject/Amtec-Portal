package com.amtrak.application.service;

import com.amtrak.application.domain.Release;
import com.amtrak.application.exceptions.ReleaseException;
import com.amtrak.application.repository.ReleaseRepository;
import com.amtrak.application.repository.search.ReleaseSearchRepository;
import com.amtrak.application.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Release}.
 */
@Service
@Transactional
public class ReleaseService {

    private final Logger log = LoggerFactory.getLogger(ReleaseService.class);

    private final ReleaseRepository releaseRepository;

    private final ReleaseSearchRepository releaseSearchRepository;

    public ReleaseService(ReleaseRepository releaseRepository, ReleaseSearchRepository releaseSearchRepository) {
        this.releaseRepository = releaseRepository;
        this.releaseSearchRepository = releaseSearchRepository;
    }

    /**
     * Save a release.
     *
     * @param release the entity to save.
     * @return the persisted entity.
     */
    public Release save(Release release) {
        log.debug("Request to save Release : {}", release);
        Release result = releaseRepository.save(release);
        releaseSearchRepository.save(result);
        return result;
    }


    public Release createRelease(Release release)
        throws ReleaseException.ProductionReleaseExists, ReleaseException.ProductionIsFuture {
        log.debug("Request to create Release: {}", release);
        if ( release.isCurrent() ) {
            List<Release> releases = releaseRepository.findAllByTerritoryAndCurrentIsTrue(release.getTerritory());
            if ( !releases.isEmpty() )
                throw new ReleaseException.ProductionReleaseExists("A release for this territory is already in production");
            if ( release.getDate().isAfter(Instant.now()))
                throw new ReleaseException.ProductionIsFuture("A release can not be in production at a date later than now");
        }
        return save(release);
    }

    /**
     * Get all the releases.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Release> findAll() {
        log.debug("Request to get all Releases");
        return releaseRepository.findAll();
    }


    /**
     * Get one release by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Release> findOne(Long id) {
        log.debug("Request to get Release : {}", id);
        return releaseRepository.findById(id);
    }

    /**
     * Delete the release by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Release : {}", id);
        releaseRepository.deleteById(id);
        releaseSearchRepository.deleteById(id);
    }

    /**
     * Search for the release corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Release> search(String query) {
        log.debug("Request to search Releases for query {}", query);
        return StreamSupport
            .stream(releaseSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    /**
     * Finds all current production releases
     *
     * @return List
     */
    @Transactional(readOnly = true)
    public List<Release> getCurrentReleases ()
    {
        log.debug("Request to find all current releases");
        return releaseRepository.findAllByCurrentIsTrue();
    }

    /**
     * Finds all future releases
     *
     * @return List
     */
    @Transactional(readOnly = true)
    public List<Release> findFutureReleases()
    {
        log.debug("Request to find all future releases");
        return releaseRepository.findAllByDateIsGreaterThanAndCurrent(Instant.now(), false);
    }
}
