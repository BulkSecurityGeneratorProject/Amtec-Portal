package com.amtrak.application.web.rest;

import com.amtrak.application.domain.Release;
import com.amtrak.application.exceptions.ReleaseException;
import com.amtrak.application.service.ReleaseService;
import com.amtrak.application.web.rest.errors.BadRequestAlertException;
import com.amtrak.application.service.dto.ReleaseCriteria;
import com.amtrak.application.service.ReleaseQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.amtrak.application.domain.Release}.
 */
@RestController
@RequestMapping("/api")
public class ReleaseResource {

    private final Logger log = LoggerFactory.getLogger(ReleaseResource.class);

    private static final String ENTITY_NAME = "release";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReleaseService releaseService;

    private final ReleaseQueryService releaseQueryService;

    public ReleaseResource(ReleaseService releaseService, ReleaseQueryService releaseQueryService) {
        this.releaseService = releaseService;
        this.releaseQueryService = releaseQueryService;
    }

    /**
     * {@code POST  /releases} : Create a new release.
     *
     * @param release the release to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new release, or with status {@code 400 (Bad Request)} if the release has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/releases")
    public ResponseEntity<Release> createRelease(@Valid @RequestBody Release release) throws URISyntaxException {
        log.debug("REST request to save Release : {}", release);
        if (release.getId() != null) {
            throw new BadRequestAlertException("A new release cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Release result = null;
        try {
            result = releaseService.createRelease(release);
        } catch (ReleaseException.ProductionReleaseExists productionReleaseExists) {
            throw new BadRequestAlertException(productionReleaseExists.getMessage(), ENTITY_NAME, "currentExists");
        } catch (ReleaseException.ProductionIsFuture productionIsFuture) {
            throw new BadRequestAlertException(productionIsFuture.getMessage(), ENTITY_NAME, "prodReleaseBad");
        }
        return ResponseEntity.created(new URI("/api/releases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /releases} : Updates an existing release.
     *
     * @param release the release to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated release,
     * or with status {@code 400 (Bad Request)} if the release is not valid,
     * or with status {@code 500 (Internal Server Error)} if the release couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/releases")
    public ResponseEntity<Release> updateRelease(@Valid @RequestBody Release release) throws URISyntaxException {
        log.debug("REST request to update Release : {}", release);
        if (release.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Release result = releaseService.save(release);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, release.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /releases} : get all the releases.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of releases in body.
     */
    @GetMapping("/releases")
    public ResponseEntity<List<Release>> getAllReleases(ReleaseCriteria criteria) {
        log.debug("REST request to get Releases by criteria: {}", criteria);
        List<Release> entityList = releaseQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /releases/current} : get all the current releases.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of current releases in body.
     */
    @GetMapping("/releases/current")
    public ResponseEntity<List<Release>> getCurrentReleases() {
        log.debug("REST request to get Current Releases");
        List<Release> entityList = releaseService.getCurrentReleases();
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /releases/future} : get all the future releases.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of future releases in body.
     */
    @GetMapping("/releases/future")
    public ResponseEntity<List<Release>> getFutureReleases() {
        log.debug("REST request to get Future Releases");
        List<Release> entityList = releaseService.findFutureReleases();
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /releases/count} : count all the releases.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/releases/count")
    public ResponseEntity<Long> countReleases(ReleaseCriteria criteria) {
        log.debug("REST request to count Releases by criteria: {}", criteria);
        return ResponseEntity.ok().body(releaseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /releases/:id} : get the "id" release.
     *
     * @param id the id of the release to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the release, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/releases/{id}")
    public ResponseEntity<Release> getRelease(@PathVariable Long id) {
        log.debug("REST request to get Release : {}", id);
        Optional<Release> release = releaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(release);
    }

    /**
     * {@code DELETE  /releases/:id} : delete the "id" release.
     *
     * @param id the id of the release to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/releases/{id}")
    public ResponseEntity<Void> deleteRelease(@PathVariable Long id) {
        log.debug("REST request to delete Release : {}", id);
        releaseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/releases?query=:query} : search for the release corresponding
     * to the query.
     *
     * @param query the query of the release search.
     * @return the result of the search.
     */
    @GetMapping("/_search/releases")
    public List<Release> searchReleases(@RequestParam String query) {
        log.debug("REST request to search Releases for query {}", query);
        return releaseService.search(query);
    }

}
