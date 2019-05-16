package com.amtrak.application.web.rest;

import com.amtrak.application.domain.OutOfOffice;
import com.amtrak.application.service.OutOfOfficeService;
import com.amtrak.application.web.rest.errors.BadRequestAlertException;
import com.amtrak.application.service.dto.OutOfOfficeCriteria;
import com.amtrak.application.service.OutOfOfficeQueryService;

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
 * REST controller for managing {@link com.amtrak.application.domain.OutOfOffice}.
 */
@RestController
@RequestMapping("/api")
public class OutOfOfficeResource {

    private final Logger log = LoggerFactory.getLogger(OutOfOfficeResource.class);

    private static final String ENTITY_NAME = "outOfOffice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OutOfOfficeService outOfOfficeService;

    private final OutOfOfficeQueryService outOfOfficeQueryService;

    public OutOfOfficeResource(OutOfOfficeService outOfOfficeService, OutOfOfficeQueryService outOfOfficeQueryService) {
        this.outOfOfficeService = outOfOfficeService;
        this.outOfOfficeQueryService = outOfOfficeQueryService;
    }

    /**
     * {@code POST  /out-of-offices} : Create a new outOfOffice.
     *
     * @param outOfOffice the outOfOffice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new outOfOffice, or with status {@code 400 (Bad Request)} if the outOfOffice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/out-of-offices")
    public ResponseEntity<OutOfOffice> createOutOfOffice(@Valid @RequestBody OutOfOffice outOfOffice) throws URISyntaxException {
        log.debug("REST request to save OutOfOffice : {}", outOfOffice);
        if (outOfOffice.getId() != null) {
            throw new BadRequestAlertException("A new outOfOffice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OutOfOffice result = outOfOfficeService.save(outOfOffice);
        return ResponseEntity.created(new URI("/api/out-of-offices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /out-of-offices} : Updates an existing outOfOffice.
     *
     * @param outOfOffice the outOfOffice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outOfOffice,
     * or with status {@code 400 (Bad Request)} if the outOfOffice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the outOfOffice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/out-of-offices")
    public ResponseEntity<OutOfOffice> updateOutOfOffice(@Valid @RequestBody OutOfOffice outOfOffice) throws URISyntaxException {
        log.debug("REST request to update OutOfOffice : {}", outOfOffice);
        if (outOfOffice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OutOfOffice result = outOfOfficeService.save(outOfOffice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, outOfOffice.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /out-of-offices} : get all the outOfOffices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of outOfOffices in body.
     */
    @GetMapping("/out-of-offices")
    public ResponseEntity<List<OutOfOffice>> getAllOutOfOffices(OutOfOfficeCriteria criteria) {
        log.debug("REST request to get OutOfOffices by criteria: {}", criteria);
        List<OutOfOffice> entityList = outOfOfficeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /out-of-offices/count} : count all the outOfOffices.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/out-of-offices/count")
    public ResponseEntity<Long> countOutOfOffices(OutOfOfficeCriteria criteria) {
        log.debug("REST request to count OutOfOffices by criteria: {}", criteria);
        return ResponseEntity.ok().body(outOfOfficeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /out-of-offices/:id} : get the "id" outOfOffice.
     *
     * @param id the id of the outOfOffice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the outOfOffice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/out-of-offices/{id}")
    public ResponseEntity<OutOfOffice> getOutOfOffice(@PathVariable Long id) {
        log.debug("REST request to get OutOfOffice : {}", id);
        Optional<OutOfOffice> outOfOffice = outOfOfficeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(outOfOffice);
    }

    /**
     * {@code DELETE  /out-of-offices/:id} : delete the "id" outOfOffice.
     *
     * @param id the id of the outOfOffice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/out-of-offices/{id}")
    public ResponseEntity<Void> deleteOutOfOffice(@PathVariable Long id) {
        log.debug("REST request to delete OutOfOffice : {}", id);
        outOfOfficeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/out-of-offices?query=:query} : search for the outOfOffice corresponding
     * to the query.
     *
     * @param query the query of the outOfOffice search.
     * @return the result of the search.
     */
    @GetMapping("/_search/out-of-offices")
    public List<OutOfOffice> searchOutOfOffices(@RequestParam String query) {
        log.debug("REST request to search OutOfOffices for query {}", query);
        return outOfOfficeService.search(query);
    }

}
