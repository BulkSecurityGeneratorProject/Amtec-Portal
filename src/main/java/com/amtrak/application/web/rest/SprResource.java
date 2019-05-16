package com.amtrak.application.web.rest;

import com.amtrak.application.domain.Spr;
import com.amtrak.application.domain.User;
import com.amtrak.application.domain.enumeration.Territory;
import com.amtrak.application.service.SprService;
import com.amtrak.application.web.rest.errors.BadRequestAlertException;
import com.amtrak.application.service.dto.SprCriteria;
import com.amtrak.application.service.SprQueryService;

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
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.amtrak.application.domain.Spr}.
 */
@RestController
@RequestMapping("/api")
public class SprResource {

    private final Logger log = LoggerFactory.getLogger(SprResource.class);

    private static final String ENTITY_NAME = "spr";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SprService sprService;

    private final SprQueryService sprQueryService;

    public SprResource(SprService sprService, SprQueryService sprQueryService) {
        this.sprService = sprService;
        this.sprQueryService = sprQueryService;
    }

    /**
     * {@code POST  /sprs} : Create a new spr.
     *
     * @param spr the spr to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spr, or with status {@code 400 (Bad Request)} if the spr has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sprs")
    public ResponseEntity<Spr> createSpr(@Valid @RequestBody Spr spr) throws URISyntaxException {
        log.debug("REST request to save Spr : {}", spr);
        if (spr.getId() != null) {
            throw new BadRequestAlertException("A new spr cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Spr result = sprService.save(spr);
        return ResponseEntity.created(new URI("/api/sprs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sprs} : Updates an existing spr.
     *
     * @param spr the spr to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spr,
     * or with status {@code 400 (Bad Request)} if the spr is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spr couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sprs")
    public ResponseEntity<Spr> updateSpr(@Valid @RequestBody Spr spr) throws URISyntaxException {
        log.debug("REST request to update Spr : {}", spr);
        if (spr.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Spr result = sprService.save(spr);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spr.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sprs} : get all the sprs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sprs in body.
     */
    @GetMapping("/sprs")
    public ResponseEntity<List<Spr>> getAllSprs(SprCriteria criteria) {
        log.debug("REST request to get Sprs by criteria: {}", criteria);
        List<Spr> entityList = sprQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    @GetMapping("/sprs/user=:user")
    public ResponseEntity<List<Spr>> getAllUserSprs(User user)
    {
        log.debug("REST request to get Sprs for User: {}", user.getLogin());
        List<Spr> entityList = sprService.getAllUsersSprs(user);
        return ResponseEntity.ok().body(entityList);
    }

    @GetMapping("/sprs/open/user:user")
    public ResponseEntity<List<Spr>> getAllOpenUserSprs(User user)
    {
        log.debug("REST request to get open Sprs for User: {}", user.getLogin());
        List<Spr> entityList = sprService.getAllOpenSprsForUser(user);
        return ResponseEntity.ok().body(entityList);
    }

    @GetMapping("/sprs/reviewed")
    public ResponseEntity<List<Spr>> getReviewedSprs()
    {
        log.debug("REST request to get all reviewed SPRs");
        List<Spr> entityList = sprService.getAllReviewedSprs();
        return ResponseEntity.ok().body(entityList);
    }

    @GetMapping("/sprs/open/count/user:user")
    public ResponseEntity<List<Spr>> getUserOpenSprCount(User user)
    {
        log.debug("REST request to get open SPR count for User: {}", user.getLogin());
        List<Spr> entityList = sprService.getAllOpenSprsForUser(user);
        return ResponseEntity.ok().body(entityList);
    }

    @GetMapping("/sprs/open")
    public ResponseEntity<List<Spr>> getAllOpenSprs()
    {
        log.debug("REST request to get open SPR count");
        List<Spr> entityList = sprService.getAllOpenSprs();
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /sprs/count} : count all the sprs.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/sprs/count")
    public ResponseEntity<Long> countSprs(SprCriteria criteria) {
        log.debug("REST request to count Sprs by criteria: {}", criteria);
        return ResponseEntity.ok().body(sprQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sprs/:id} : get the "id" spr.
     *
     * @param id the id of the spr to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spr, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sprs/{id}")
    public ResponseEntity<Spr> getSpr(@PathVariable Long id) {
        log.debug("REST request to get Spr : {}", id);
        Optional<Spr> spr = sprService.findOne(id);
        return ResponseUtil.wrapOrNotFound(spr);
    }

    /**
     * {@code DELETE  /sprs/:id} : delete the "id" spr.
     *
     * @param id the id of the spr to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sprs/{id}")
    public ResponseEntity<Void> deleteSpr(@PathVariable Long id) {
        log.debug("REST request to delete Spr : {}", id);
        sprService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/sprs?query=:query} : search for the spr corresponding
     * to the query.
     *
     * @param query the query of the spr search.
     * @return the result of the search.
     */
    @GetMapping("/_search/sprs")
    public List<Spr> searchSprs(@RequestParam String query) {
        log.debug("REST request to search Sprs for query {}", query);
        return sprService.search(query);
    }

}
