package com.amtrak.application.service;

import com.amtrak.application.domain.OutOfOffice;
import com.amtrak.application.repository.OutOfOfficeRepository;
import com.amtrak.application.repository.search.OutOfOfficeSearchRepository;
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
 * Service Implementation for managing {@link OutOfOffice}.
 */
@Service
@Transactional
public class OutOfOfficeService {

    private final Logger log = LoggerFactory.getLogger(OutOfOfficeService.class);

    private final OutOfOfficeRepository outOfOfficeRepository;

    private final OutOfOfficeSearchRepository outOfOfficeSearchRepository;

    public OutOfOfficeService(OutOfOfficeRepository outOfOfficeRepository, OutOfOfficeSearchRepository outOfOfficeSearchRepository) {
        this.outOfOfficeRepository = outOfOfficeRepository;
        this.outOfOfficeSearchRepository = outOfOfficeSearchRepository;
    }

    /**
     * Save a outOfOffice.
     *
     * @param outOfOffice the entity to save.
     * @return the persisted entity.
     */
    public OutOfOffice save(OutOfOffice outOfOffice) {
        log.debug("Request to save OutOfOffice : {}", outOfOffice);
        OutOfOffice result = outOfOfficeRepository.save(outOfOffice);
        outOfOfficeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the outOfOffices.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OutOfOffice> findAll() {
        log.debug("Request to get all OutOfOffices");
        return outOfOfficeRepository.findAll();
    }


    /**
     * Get one outOfOffice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OutOfOffice> findOne(Long id) {
        log.debug("Request to get OutOfOffice : {}", id);
        return outOfOfficeRepository.findById(id);
    }

    /**
     * Delete the outOfOffice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OutOfOffice : {}", id);
        outOfOfficeRepository.deleteById(id);
        outOfOfficeSearchRepository.deleteById(id);
    }

    /**
     * Search for the outOfOffice corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OutOfOffice> search(String query) {
        log.debug("Request to search OutOfOffices for query {}", query);
        return StreamSupport
            .stream(outOfOfficeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
