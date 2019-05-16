package com.amtrak.application.web.rest;

import com.amtrak.application.domain.TeamEvent;
import com.amtrak.application.service.TeamEventService;
import com.amtrak.application.web.rest.errors.BadRequestAlertException;
import com.amtrak.application.service.dto.TeamEventCriteria;
import com.amtrak.application.service.TeamEventQueryService;

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
 * REST controller for managing {@link com.amtrak.application.domain.TeamEvent}.
 */
@RestController
@RequestMapping("/api")
public class TeamEventResource {

    private final Logger log = LoggerFactory.getLogger(TeamEventResource.class);

    private static final String ENTITY_NAME = "teamEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeamEventService teamEventService;

    private final TeamEventQueryService teamEventQueryService;

    public TeamEventResource(TeamEventService teamEventService, TeamEventQueryService teamEventQueryService) {
        this.teamEventService = teamEventService;
        this.teamEventQueryService = teamEventQueryService;
    }

    /**
     * {@code POST  /team-events} : Create a new teamEvent.
     *
     * @param teamEvent the teamEvent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new teamEvent, or with status {@code 400 (Bad Request)} if the teamEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/team-events")
    public ResponseEntity<TeamEvent> createTeamEvent(@Valid @RequestBody TeamEvent teamEvent) throws URISyntaxException {
        log.debug("REST request to save TeamEvent : {}", teamEvent);
        if (teamEvent.getId() != null) {
            throw new BadRequestAlertException("A new teamEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TeamEvent result = teamEventService.save(teamEvent);
        return ResponseEntity.created(new URI("/api/team-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /team-events} : Updates an existing teamEvent.
     *
     * @param teamEvent the teamEvent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teamEvent,
     * or with status {@code 400 (Bad Request)} if the teamEvent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teamEvent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/team-events")
    public ResponseEntity<TeamEvent> updateTeamEvent(@Valid @RequestBody TeamEvent teamEvent) throws URISyntaxException {
        log.debug("REST request to update TeamEvent : {}", teamEvent);
        if (teamEvent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TeamEvent result = teamEventService.save(teamEvent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, teamEvent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /team-events} : get all the teamEvents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teamEvents in body.
     */
    @GetMapping("/team-events")
    public ResponseEntity<List<TeamEvent>> getAllTeamEvents(TeamEventCriteria criteria) {
        log.debug("REST request to get TeamEvents by criteria: {}", criteria);
        List<TeamEvent> entityList = teamEventQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /team-events/count} : count all the teamEvents.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/team-events/count")
    public ResponseEntity<Long> countTeamEvents(TeamEventCriteria criteria) {
        log.debug("REST request to count TeamEvents by criteria: {}", criteria);
        return ResponseEntity.ok().body(teamEventQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /team-events/:id} : get the "id" teamEvent.
     *
     * @param id the id of the teamEvent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teamEvent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/team-events/{id}")
    public ResponseEntity<TeamEvent> getTeamEvent(@PathVariable Long id) {
        log.debug("REST request to get TeamEvent : {}", id);
        Optional<TeamEvent> teamEvent = teamEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teamEvent);
    }

    /**
     * {@code DELETE  /team-events/:id} : delete the "id" teamEvent.
     *
     * @param id the id of the teamEvent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/team-events/{id}")
    public ResponseEntity<Void> deleteTeamEvent(@PathVariable Long id) {
        log.debug("REST request to delete TeamEvent : {}", id);
        teamEventService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/team-events?query=:query} : search for the teamEvent corresponding
     * to the query.
     *
     * @param query the query of the teamEvent search.
     * @return the result of the search.
     */
    @GetMapping("/_search/team-events")
    public List<TeamEvent> searchTeamEvents(@RequestParam String query) {
        log.debug("REST request to search TeamEvents for query {}", query);
        return teamEventService.search(query);
    }

}
