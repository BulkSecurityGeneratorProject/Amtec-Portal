package com.amtrak.application.service;

import com.amtrak.application.domain.TeamEvent;
import com.amtrak.application.repository.TeamEventRepository;
import com.amtrak.application.repository.search.TeamEventSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link TeamEvent}.
 */
@Service
@Transactional
public class TeamEventService {

    private final Logger log = LoggerFactory.getLogger(TeamEventService.class);

    private final TeamEventRepository teamEventRepository;

    private final TeamEventSearchRepository teamEventSearchRepository;

    public TeamEventService(TeamEventRepository teamEventRepository, TeamEventSearchRepository teamEventSearchRepository) {
        this.teamEventRepository = teamEventRepository;
        this.teamEventSearchRepository = teamEventSearchRepository;
    }

    /**
     * Save a teamEvent.
     *
     * @param teamEvent the entity to save.
     * @return the persisted entity.
     */
    public TeamEvent save(TeamEvent teamEvent) {
        log.debug("Request to save TeamEvent : {}", teamEvent);
        TeamEvent result = teamEventRepository.save(teamEvent);
        teamEventSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the teamEvents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TeamEvent> findAll() {
        log.debug("Request to get all TeamEvents");
        return teamEventRepository.findAll();
    }


    /**
     * Get one teamEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TeamEvent> findOne(Long id) {
        log.debug("Request to get TeamEvent : {}", id);
        return teamEventRepository.findById(id);
    }

    /**
     * Delete the teamEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TeamEvent : {}", id);
        teamEventRepository.deleteById(id);
        teamEventSearchRepository.deleteById(id);
    }

    /**
     * Search for the teamEvent corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TeamEvent> search(String query) {
        log.debug("Request to search TeamEvents for query {}", query);
        return StreamSupport
            .stream(teamEventSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
