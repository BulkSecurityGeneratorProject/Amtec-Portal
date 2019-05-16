package com.amtrak.application.service;

import com.amtrak.application.domain.Spr;
import com.amtrak.application.domain.User;
import com.amtrak.application.domain.enumeration.Resolution;
import com.amtrak.application.domain.enumeration.Territory;
import com.amtrak.application.repository.SprRepository;
import com.amtrak.application.repository.UserRepository;
import com.amtrak.application.repository.search.SprSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Spr}.
 */
@Service
@Transactional
@SuppressWarnings("Duplicates")
public class SprService {

    private final Logger log = LoggerFactory.getLogger(SprService.class);

    private final SprRepository sprRepository;

    private final SprSearchRepository sprSearchRepository;

    private final UserRepository userRepository;

    public SprService(SprRepository sprRepository, SprSearchRepository sprSearchRepository,
                      UserRepository userRepository) {
        this.sprRepository = sprRepository;
        this.sprSearchRepository = sprSearchRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a spr.
     *
     * @param spr the entity to save.
     * @return the persisted entity.
     */
    public Spr save(Spr spr) {
        log.debug("Request to save Spr : {}", spr);
        Spr result = sprRepository.save(spr);
        sprSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the sprs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = false)
    public List<Spr> findAll() {
        log.debug("Request to get all Sprs");
        return sprRepository.findAll();
    }


    /**
     * Get one spr by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Spr> findOne(Long id) {
        log.debug("Request to get Spr : {}", id);
        return sprRepository.findById(id);
    }

    /**
     * Delete the spr by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Spr : {}", id);
        sprRepository.deleteById(id);
        sprSearchRepository.deleteById(id);
    }

    /**
     * Search for the spr corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Spr> search(String query) {
        log.debug("Request to search Sprs for query {}", query);
        return StreamSupport
            .stream(sprSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    /**
     * Finds all (open/closed) SPRs for a User
     *
     * @param user User to search
     * @return List
     */
    @Transactional(readOnly = true)
    public List<Spr> getAllUsersSprs(User user)
    {
        log.debug("Request to find all Sprs for User={}", user.getLogin());
        return sprRepository.findAllByUser(user);
    }


    /**
     * Finds all SPRs for a User which are still considered open
     *
     * @param user User
     * @return List
     */
    @Transactional(readOnly = true)
    public List<Spr> getAllOpenSprsForUser(User user)
    {
        log.debug("Request to find all OPEN SPRs for User={}", user.getLogin());
        List<Spr> allSprs =  sprRepository.findAllByUser(user);

        for (Iterator<Spr> iterator = allSprs.iterator(); iterator.hasNext(); ) {
            Spr spr = iterator.next();
            switch(spr.getResolution())
            {
                case RELEASED:
                case CANNOT_REPRODUCE:
                case TESTED:
                case REVIEWED:
                case FIXED:
                    iterator.remove();
                    break;
            }
        }
        return allSprs;
    }

    @Transactional(readOnly = true)
    public List<Spr> getAllReviewedSprs()
    {
        log.debug("Request to find all reviewed SPRs");
        return sprRepository.findAllByResolution(Resolution.REVIEWED);
    }

    /**
     * Gets the SPR count for all users
     *
     * @return Map
     */
    @Transactional(readOnly = true)
    public Map<User, Integer> getAllUserOpenSprCount()
    {
        log.debug("Request to find all open SPR count for all users");
        List<Spr> sprs = sprRepository.findAll();
        List<User> users = userRepository.findAll();
        Map<User, Integer> sprMap = new HashMap<>();

        // Add users, initialize to 0
        for ( User user : users )
            sprMap.put(user, 0);

        // Filter the SPRS
        for (Iterator<Spr> iterator = sprs.iterator(); iterator.hasNext(); ) {
            Spr spr = iterator.next();
            switch(spr.getResolution())
            {
                case RELEASED:
                case CANNOT_REPRODUCE:
                case TESTED:
                case REVIEWED:
                case FIXED:
                    iterator.remove();
                    break;
            }
        }

        // Tally up SPRs for each user
        for ( Spr spr : sprs )
            sprMap.replace(spr.getUser(), sprMap.get( spr.getUser() ) + 1);

        return sprMap;
    }

    /**
     * Gets the SPR count for all users
     *
     * @return Map
     */
    @Transactional(readOnly = true)
    public List<Spr> getAllOpenSprs()
    {
        log.debug("Request to find all open SPR count for all users");
        List<Spr> sprs = sprRepository.findAll();

        for (Iterator<Spr> iterator = sprs.iterator(); iterator.hasNext(); ) {
            Spr spr = iterator.next();
            switch(spr.getResolution())
            {
                case RELEASED:
                case CANNOT_REPRODUCE:
                case TESTED:
                case REVIEWED:
                case FIXED:
                    iterator.remove();
                    break;
            }
        }
        return sprs;
    }
}
