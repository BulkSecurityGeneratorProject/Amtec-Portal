package com.amtrak.application.web.rest;

import com.amtrak.application.AmtecPortalApp;
import com.amtrak.application.domain.TeamEvent;
import com.amtrak.application.repository.TeamEventRepository;
import com.amtrak.application.repository.search.TeamEventSearchRepository;
import com.amtrak.application.service.TeamEventService;
import com.amtrak.application.web.rest.errors.ExceptionTranslator;
import com.amtrak.application.service.dto.TeamEventCriteria;
import com.amtrak.application.service.TeamEventQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static com.amtrak.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link TeamEventResource} REST controller.
 */
@SpringBootTest(classes = AmtecPortalApp.class)
public class TeamEventResourceIT {

    private static final Instant DEFAULT_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private TeamEventRepository teamEventRepository;

    @Autowired
    private TeamEventService teamEventService;

    /**
     * This repository is mocked in the com.amtrak.application.repository.search test package.
     *
     * @see com.amtrak.application.repository.search.TeamEventSearchRepositoryMockConfiguration
     */
    @Autowired
    private TeamEventSearchRepository mockTeamEventSearchRepository;

    @Autowired
    private TeamEventQueryService teamEventQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restTeamEventMockMvc;

    private TeamEvent teamEvent;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TeamEventResource teamEventResource = new TeamEventResource(teamEventService, teamEventQueryService);
        this.restTeamEventMockMvc = MockMvcBuilders.standaloneSetup(teamEventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeamEvent createEntity(EntityManager em) {
        TeamEvent teamEvent = new TeamEvent()
            .start(DEFAULT_START)
            .end(DEFAULT_END)
            .description(DEFAULT_DESCRIPTION);
        return teamEvent;
    }

    @BeforeEach
    public void initTest() {
        teamEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeamEvent() throws Exception {
        int databaseSizeBeforeCreate = teamEventRepository.findAll().size();

        // Create the TeamEvent
        restTeamEventMockMvc.perform(post("/api/team-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamEvent)))
            .andExpect(status().isCreated());

        // Validate the TeamEvent in the database
        List<TeamEvent> teamEventList = teamEventRepository.findAll();
        assertThat(teamEventList).hasSize(databaseSizeBeforeCreate + 1);
        TeamEvent testTeamEvent = teamEventList.get(teamEventList.size() - 1);
        assertThat(testTeamEvent.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testTeamEvent.getEnd()).isEqualTo(DEFAULT_END);
        assertThat(testTeamEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the TeamEvent in Elasticsearch
        verify(mockTeamEventSearchRepository, times(1)).save(testTeamEvent);
    }

    @Test
    @Transactional
    public void createTeamEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teamEventRepository.findAll().size();

        // Create the TeamEvent with an existing ID
        teamEvent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamEventMockMvc.perform(post("/api/team-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamEvent)))
            .andExpect(status().isBadRequest());

        // Validate the TeamEvent in the database
        List<TeamEvent> teamEventList = teamEventRepository.findAll();
        assertThat(teamEventList).hasSize(databaseSizeBeforeCreate);

        // Validate the TeamEvent in Elasticsearch
        verify(mockTeamEventSearchRepository, times(0)).save(teamEvent);
    }


    @Test
    @Transactional
    public void checkStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = teamEventRepository.findAll().size();
        // set the field null
        teamEvent.setStart(null);

        // Create the TeamEvent, which fails.

        restTeamEventMockMvc.perform(post("/api/team-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamEvent)))
            .andExpect(status().isBadRequest());

        List<TeamEvent> teamEventList = teamEventRepository.findAll();
        assertThat(teamEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTeamEvents() throws Exception {
        // Initialize the database
        teamEventRepository.saveAndFlush(teamEvent);

        // Get all the teamEventList
        restTeamEventMockMvc.perform(get("/api/team-events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getTeamEvent() throws Exception {
        // Initialize the database
        teamEventRepository.saveAndFlush(teamEvent);

        // Get the teamEvent
        restTeamEventMockMvc.perform(get("/api/team-events/{id}", teamEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(teamEvent.getId().intValue()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START.toString()))
            .andExpect(jsonPath("$.end").value(DEFAULT_END.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllTeamEventsByStartIsEqualToSomething() throws Exception {
        // Initialize the database
        teamEventRepository.saveAndFlush(teamEvent);

        // Get all the teamEventList where start equals to DEFAULT_START
        defaultTeamEventShouldBeFound("start.equals=" + DEFAULT_START);

        // Get all the teamEventList where start equals to UPDATED_START
        defaultTeamEventShouldNotBeFound("start.equals=" + UPDATED_START);
    }

    @Test
    @Transactional
    public void getAllTeamEventsByStartIsInShouldWork() throws Exception {
        // Initialize the database
        teamEventRepository.saveAndFlush(teamEvent);

        // Get all the teamEventList where start in DEFAULT_START or UPDATED_START
        defaultTeamEventShouldBeFound("start.in=" + DEFAULT_START + "," + UPDATED_START);

        // Get all the teamEventList where start equals to UPDATED_START
        defaultTeamEventShouldNotBeFound("start.in=" + UPDATED_START);
    }

    @Test
    @Transactional
    public void getAllTeamEventsByStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamEventRepository.saveAndFlush(teamEvent);

        // Get all the teamEventList where start is not null
        defaultTeamEventShouldBeFound("start.specified=true");

        // Get all the teamEventList where start is null
        defaultTeamEventShouldNotBeFound("start.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeamEventsByEndIsEqualToSomething() throws Exception {
        // Initialize the database
        teamEventRepository.saveAndFlush(teamEvent);

        // Get all the teamEventList where end equals to DEFAULT_END
        defaultTeamEventShouldBeFound("end.equals=" + DEFAULT_END);

        // Get all the teamEventList where end equals to UPDATED_END
        defaultTeamEventShouldNotBeFound("end.equals=" + UPDATED_END);
    }

    @Test
    @Transactional
    public void getAllTeamEventsByEndIsInShouldWork() throws Exception {
        // Initialize the database
        teamEventRepository.saveAndFlush(teamEvent);

        // Get all the teamEventList where end in DEFAULT_END or UPDATED_END
        defaultTeamEventShouldBeFound("end.in=" + DEFAULT_END + "," + UPDATED_END);

        // Get all the teamEventList where end equals to UPDATED_END
        defaultTeamEventShouldNotBeFound("end.in=" + UPDATED_END);
    }

    @Test
    @Transactional
    public void getAllTeamEventsByEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamEventRepository.saveAndFlush(teamEvent);

        // Get all the teamEventList where end is not null
        defaultTeamEventShouldBeFound("end.specified=true");

        // Get all the teamEventList where end is null
        defaultTeamEventShouldNotBeFound("end.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTeamEventShouldBeFound(String filter) throws Exception {
        restTeamEventMockMvc.perform(get("/api/team-events?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restTeamEventMockMvc.perform(get("/api/team-events/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTeamEventShouldNotBeFound(String filter) throws Exception {
        restTeamEventMockMvc.perform(get("/api/team-events?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTeamEventMockMvc.perform(get("/api/team-events/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTeamEvent() throws Exception {
        // Get the teamEvent
        restTeamEventMockMvc.perform(get("/api/team-events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeamEvent() throws Exception {
        // Initialize the database
        teamEventService.save(teamEvent);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockTeamEventSearchRepository);

        int databaseSizeBeforeUpdate = teamEventRepository.findAll().size();

        // Update the teamEvent
        TeamEvent updatedTeamEvent = teamEventRepository.findById(teamEvent.getId()).get();
        // Disconnect from session so that the updates on updatedTeamEvent are not directly saved in db
        em.detach(updatedTeamEvent);
        updatedTeamEvent
            .start(UPDATED_START)
            .end(UPDATED_END)
            .description(UPDATED_DESCRIPTION);

        restTeamEventMockMvc.perform(put("/api/team-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTeamEvent)))
            .andExpect(status().isOk());

        // Validate the TeamEvent in the database
        List<TeamEvent> teamEventList = teamEventRepository.findAll();
        assertThat(teamEventList).hasSize(databaseSizeBeforeUpdate);
        TeamEvent testTeamEvent = teamEventList.get(teamEventList.size() - 1);
        assertThat(testTeamEvent.getStart()).isEqualTo(UPDATED_START);
        assertThat(testTeamEvent.getEnd()).isEqualTo(UPDATED_END);
        assertThat(testTeamEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the TeamEvent in Elasticsearch
        verify(mockTeamEventSearchRepository, times(1)).save(testTeamEvent);
    }

    @Test
    @Transactional
    public void updateNonExistingTeamEvent() throws Exception {
        int databaseSizeBeforeUpdate = teamEventRepository.findAll().size();

        // Create the TeamEvent

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeamEventMockMvc.perform(put("/api/team-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamEvent)))
            .andExpect(status().isBadRequest());

        // Validate the TeamEvent in the database
        List<TeamEvent> teamEventList = teamEventRepository.findAll();
        assertThat(teamEventList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TeamEvent in Elasticsearch
        verify(mockTeamEventSearchRepository, times(0)).save(teamEvent);
    }

    @Test
    @Transactional
    public void deleteTeamEvent() throws Exception {
        // Initialize the database
        teamEventService.save(teamEvent);

        int databaseSizeBeforeDelete = teamEventRepository.findAll().size();

        // Delete the teamEvent
        restTeamEventMockMvc.perform(delete("/api/team-events/{id}", teamEvent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<TeamEvent> teamEventList = teamEventRepository.findAll();
        assertThat(teamEventList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TeamEvent in Elasticsearch
        verify(mockTeamEventSearchRepository, times(1)).deleteById(teamEvent.getId());
    }

    @Test
    @Transactional
    public void searchTeamEvent() throws Exception {
        // Initialize the database
        teamEventService.save(teamEvent);
        when(mockTeamEventSearchRepository.search(queryStringQuery("id:" + teamEvent.getId())))
            .thenReturn(Collections.singletonList(teamEvent));
        // Search the teamEvent
        restTeamEventMockMvc.perform(get("/api/_search/team-events?query=id:" + teamEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamEvent.class);
        TeamEvent teamEvent1 = new TeamEvent();
        teamEvent1.setId(1L);
        TeamEvent teamEvent2 = new TeamEvent();
        teamEvent2.setId(teamEvent1.getId());
        assertThat(teamEvent1).isEqualTo(teamEvent2);
        teamEvent2.setId(2L);
        assertThat(teamEvent1).isNotEqualTo(teamEvent2);
        teamEvent1.setId(null);
        assertThat(teamEvent1).isNotEqualTo(teamEvent2);
    }
}
