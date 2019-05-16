package com.amtrak.application.web.rest;

import com.amtrak.application.AmtecPortalApp;
import com.amtrak.application.domain.Spr;
import com.amtrak.application.domain.User;
import com.amtrak.application.domain.Release;
import com.amtrak.application.repository.SprRepository;
import com.amtrak.application.repository.search.SprSearchRepository;
import com.amtrak.application.service.SprService;
import com.amtrak.application.web.rest.errors.ExceptionTranslator;
import com.amtrak.application.service.dto.SprCriteria;
import com.amtrak.application.service.SprQueryService;

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
import java.util.Collections;
import java.util.List;

import static com.amtrak.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.amtrak.application.domain.enumeration.Territory;
import com.amtrak.application.domain.enumeration.Priority;
import com.amtrak.application.domain.enumeration.Resolution;
/**
 * Integration tests for the {@Link SprResource} REST controller.
 */
@SpringBootTest(classes = AmtecPortalApp.class)
public class SprResourceIT {

    private static final Territory DEFAULT_TERRITORY = Territory.CETC_WEST;
    private static final Territory UPDATED_TERRITORY = Territory.HUDSON;

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    private static final String DEFAULT_FULL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Priority DEFAULT_PRIORITY = Priority.SHOWSTOPPER;
    private static final Priority UPDATED_PRIORITY = Priority.EMERGENCY;

    private static final Resolution DEFAULT_RESOLUTION = Resolution.NEW;
    private static final Resolution UPDATED_RESOLUTION = Resolution.RELEASED;

    private static final String DEFAULT_JIRA_LINK = "AAAAAAAAAA";
    private static final String UPDATED_JIRA_LINK = "BBBBBBBBBB";

    private static final Long DEFAULT_REVIEWER_ID = 1L;
    private static final Long UPDATED_REVIEWER_ID = 2L;

    @Autowired
    private SprRepository sprRepository;

    @Autowired
    private SprService sprService;

    /**
     * This repository is mocked in the com.amtrak.application.repository.search test package.
     *
     * @see com.amtrak.application.repository.search.SprSearchRepositoryMockConfiguration
     */
    @Autowired
    private SprSearchRepository mockSprSearchRepository;

    @Autowired
    private SprQueryService sprQueryService;

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

    private MockMvc restSprMockMvc;

    private Spr spr;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SprResource sprResource = new SprResource(sprService, sprQueryService);
        this.restSprMockMvc = MockMvcBuilders.standaloneSetup(sprResource)
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
    public static Spr createEntity(EntityManager em) {
        Spr spr = new Spr()
            .territory(DEFAULT_TERRITORY)
            .number(DEFAULT_NUMBER)
            .fullNumber(DEFAULT_FULL_NUMBER)
            .description(DEFAULT_DESCRIPTION)
            .priority(DEFAULT_PRIORITY)
            .resolution(DEFAULT_RESOLUTION)
            .jiraLink(DEFAULT_JIRA_LINK)
            .reviewerId(DEFAULT_REVIEWER_ID);
        return spr;
    }

    @BeforeEach
    public void initTest() {
        spr = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpr() throws Exception {
        int databaseSizeBeforeCreate = sprRepository.findAll().size();

        // Create the Spr
        restSprMockMvc.perform(post("/api/sprs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spr)))
            .andExpect(status().isCreated());

        // Validate the Spr in the database
        List<Spr> sprList = sprRepository.findAll();
        assertThat(sprList).hasSize(databaseSizeBeforeCreate + 1);
        Spr testSpr = sprList.get(sprList.size() - 1);
        assertThat(testSpr.getTerritory()).isEqualTo(DEFAULT_TERRITORY);
        assertThat(testSpr.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testSpr.getFullNumber()).isEqualTo(DEFAULT_FULL_NUMBER);
        assertThat(testSpr.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSpr.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testSpr.getResolution()).isEqualTo(DEFAULT_RESOLUTION);
        assertThat(testSpr.getJiraLink()).isEqualTo(DEFAULT_JIRA_LINK);
        assertThat(testSpr.getReviewerId()).isEqualTo(DEFAULT_REVIEWER_ID);

        // Validate the Spr in Elasticsearch
        verify(mockSprSearchRepository, times(1)).save(testSpr);
    }

    @Test
    @Transactional
    public void createSprWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sprRepository.findAll().size();

        // Create the Spr with an existing ID
        spr.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSprMockMvc.perform(post("/api/sprs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spr)))
            .andExpect(status().isBadRequest());

        // Validate the Spr in the database
        List<Spr> sprList = sprRepository.findAll();
        assertThat(sprList).hasSize(databaseSizeBeforeCreate);

        // Validate the Spr in Elasticsearch
        verify(mockSprSearchRepository, times(0)).save(spr);
    }


    @Test
    @Transactional
    public void checkTerritoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = sprRepository.findAll().size();
        // set the field null
        spr.setTerritory(null);

        // Create the Spr, which fails.

        restSprMockMvc.perform(post("/api/sprs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spr)))
            .andExpect(status().isBadRequest());

        List<Spr> sprList = sprRepository.findAll();
        assertThat(sprList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = sprRepository.findAll().size();
        // set the field null
        spr.setNumber(null);

        // Create the Spr, which fails.

        restSprMockMvc.perform(post("/api/sprs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spr)))
            .andExpect(status().isBadRequest());

        List<Spr> sprList = sprRepository.findAll();
        assertThat(sprList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFullNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = sprRepository.findAll().size();
        // set the field null
        spr.setFullNumber(null);

        // Create the Spr, which fails.

        restSprMockMvc.perform(post("/api/sprs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spr)))
            .andExpect(status().isBadRequest());

        List<Spr> sprList = sprRepository.findAll();
        assertThat(sprList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSprs() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList
        restSprMockMvc.perform(get("/api/sprs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spr.getId().intValue())))
            .andExpect(jsonPath("$.[*].territory").value(hasItem(DEFAULT_TERRITORY.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].fullNumber").value(hasItem(DEFAULT_FULL_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].resolution").value(hasItem(DEFAULT_RESOLUTION.toString())))
            .andExpect(jsonPath("$.[*].jiraLink").value(hasItem(DEFAULT_JIRA_LINK.toString())))
            .andExpect(jsonPath("$.[*].reviewerId").value(hasItem(DEFAULT_REVIEWER_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getSpr() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get the spr
        restSprMockMvc.perform(get("/api/sprs/{id}", spr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(spr.getId().intValue()))
            .andExpect(jsonPath("$.territory").value(DEFAULT_TERRITORY.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.fullNumber").value(DEFAULT_FULL_NUMBER.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.resolution").value(DEFAULT_RESOLUTION.toString()))
            .andExpect(jsonPath("$.jiraLink").value(DEFAULT_JIRA_LINK.toString()))
            .andExpect(jsonPath("$.reviewerId").value(DEFAULT_REVIEWER_ID.intValue()));
    }

    @Test
    @Transactional
    public void getAllSprsByTerritoryIsEqualToSomething() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where territory equals to DEFAULT_TERRITORY
        defaultSprShouldBeFound("territory.equals=" + DEFAULT_TERRITORY);

        // Get all the sprList where territory equals to UPDATED_TERRITORY
        defaultSprShouldNotBeFound("territory.equals=" + UPDATED_TERRITORY);
    }

    @Test
    @Transactional
    public void getAllSprsByTerritoryIsInShouldWork() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where territory in DEFAULT_TERRITORY or UPDATED_TERRITORY
        defaultSprShouldBeFound("territory.in=" + DEFAULT_TERRITORY + "," + UPDATED_TERRITORY);

        // Get all the sprList where territory equals to UPDATED_TERRITORY
        defaultSprShouldNotBeFound("territory.in=" + UPDATED_TERRITORY);
    }

    @Test
    @Transactional
    public void getAllSprsByTerritoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where territory is not null
        defaultSprShouldBeFound("territory.specified=true");

        // Get all the sprList where territory is null
        defaultSprShouldNotBeFound("territory.specified=false");
    }

    @Test
    @Transactional
    public void getAllSprsByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where number equals to DEFAULT_NUMBER
        defaultSprShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the sprList where number equals to UPDATED_NUMBER
        defaultSprShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSprsByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultSprShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the sprList where number equals to UPDATED_NUMBER
        defaultSprShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSprsByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where number is not null
        defaultSprShouldBeFound("number.specified=true");

        // Get all the sprList where number is null
        defaultSprShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    public void getAllSprsByNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where number greater than or equals to DEFAULT_NUMBER
        defaultSprShouldBeFound("number.greaterOrEqualThan=" + DEFAULT_NUMBER);

        // Get all the sprList where number greater than or equals to UPDATED_NUMBER
        defaultSprShouldNotBeFound("number.greaterOrEqualThan=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSprsByNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where number less than or equals to DEFAULT_NUMBER
        defaultSprShouldNotBeFound("number.lessThan=" + DEFAULT_NUMBER);

        // Get all the sprList where number less than or equals to UPDATED_NUMBER
        defaultSprShouldBeFound("number.lessThan=" + UPDATED_NUMBER);
    }


    @Test
    @Transactional
    public void getAllSprsByFullNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where fullNumber equals to DEFAULT_FULL_NUMBER
        defaultSprShouldBeFound("fullNumber.equals=" + DEFAULT_FULL_NUMBER);

        // Get all the sprList where fullNumber equals to UPDATED_FULL_NUMBER
        defaultSprShouldNotBeFound("fullNumber.equals=" + UPDATED_FULL_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSprsByFullNumberIsInShouldWork() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where fullNumber in DEFAULT_FULL_NUMBER or UPDATED_FULL_NUMBER
        defaultSprShouldBeFound("fullNumber.in=" + DEFAULT_FULL_NUMBER + "," + UPDATED_FULL_NUMBER);

        // Get all the sprList where fullNumber equals to UPDATED_FULL_NUMBER
        defaultSprShouldNotBeFound("fullNumber.in=" + UPDATED_FULL_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSprsByFullNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where fullNumber is not null
        defaultSprShouldBeFound("fullNumber.specified=true");

        // Get all the sprList where fullNumber is null
        defaultSprShouldNotBeFound("fullNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllSprsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where priority equals to DEFAULT_PRIORITY
        defaultSprShouldBeFound("priority.equals=" + DEFAULT_PRIORITY);

        // Get all the sprList where priority equals to UPDATED_PRIORITY
        defaultSprShouldNotBeFound("priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    public void getAllSprsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where priority in DEFAULT_PRIORITY or UPDATED_PRIORITY
        defaultSprShouldBeFound("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY);

        // Get all the sprList where priority equals to UPDATED_PRIORITY
        defaultSprShouldNotBeFound("priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    public void getAllSprsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where priority is not null
        defaultSprShouldBeFound("priority.specified=true");

        // Get all the sprList where priority is null
        defaultSprShouldNotBeFound("priority.specified=false");
    }

    @Test
    @Transactional
    public void getAllSprsByResolutionIsEqualToSomething() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where resolution equals to DEFAULT_RESOLUTION
        defaultSprShouldBeFound("resolution.equals=" + DEFAULT_RESOLUTION);

        // Get all the sprList where resolution equals to UPDATED_RESOLUTION
        defaultSprShouldNotBeFound("resolution.equals=" + UPDATED_RESOLUTION);
    }

    @Test
    @Transactional
    public void getAllSprsByResolutionIsInShouldWork() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where resolution in DEFAULT_RESOLUTION or UPDATED_RESOLUTION
        defaultSprShouldBeFound("resolution.in=" + DEFAULT_RESOLUTION + "," + UPDATED_RESOLUTION);

        // Get all the sprList where resolution equals to UPDATED_RESOLUTION
        defaultSprShouldNotBeFound("resolution.in=" + UPDATED_RESOLUTION);
    }

    @Test
    @Transactional
    public void getAllSprsByResolutionIsNullOrNotNull() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where resolution is not null
        defaultSprShouldBeFound("resolution.specified=true");

        // Get all the sprList where resolution is null
        defaultSprShouldNotBeFound("resolution.specified=false");
    }

    @Test
    @Transactional
    public void getAllSprsByJiraLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where jiraLink equals to DEFAULT_JIRA_LINK
        defaultSprShouldBeFound("jiraLink.equals=" + DEFAULT_JIRA_LINK);

        // Get all the sprList where jiraLink equals to UPDATED_JIRA_LINK
        defaultSprShouldNotBeFound("jiraLink.equals=" + UPDATED_JIRA_LINK);
    }

    @Test
    @Transactional
    public void getAllSprsByJiraLinkIsInShouldWork() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where jiraLink in DEFAULT_JIRA_LINK or UPDATED_JIRA_LINK
        defaultSprShouldBeFound("jiraLink.in=" + DEFAULT_JIRA_LINK + "," + UPDATED_JIRA_LINK);

        // Get all the sprList where jiraLink equals to UPDATED_JIRA_LINK
        defaultSprShouldNotBeFound("jiraLink.in=" + UPDATED_JIRA_LINK);
    }

    @Test
    @Transactional
    public void getAllSprsByJiraLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where jiraLink is not null
        defaultSprShouldBeFound("jiraLink.specified=true");

        // Get all the sprList where jiraLink is null
        defaultSprShouldNotBeFound("jiraLink.specified=false");
    }

    @Test
    @Transactional
    public void getAllSprsByReviewerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where reviewerId equals to DEFAULT_REVIEWER_ID
        defaultSprShouldBeFound("reviewerId.equals=" + DEFAULT_REVIEWER_ID);

        // Get all the sprList where reviewerId equals to UPDATED_REVIEWER_ID
        defaultSprShouldNotBeFound("reviewerId.equals=" + UPDATED_REVIEWER_ID);
    }

    @Test
    @Transactional
    public void getAllSprsByReviewerIdIsInShouldWork() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where reviewerId in DEFAULT_REVIEWER_ID or UPDATED_REVIEWER_ID
        defaultSprShouldBeFound("reviewerId.in=" + DEFAULT_REVIEWER_ID + "," + UPDATED_REVIEWER_ID);

        // Get all the sprList where reviewerId equals to UPDATED_REVIEWER_ID
        defaultSprShouldNotBeFound("reviewerId.in=" + UPDATED_REVIEWER_ID);
    }

    @Test
    @Transactional
    public void getAllSprsByReviewerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where reviewerId is not null
        defaultSprShouldBeFound("reviewerId.specified=true");

        // Get all the sprList where reviewerId is null
        defaultSprShouldNotBeFound("reviewerId.specified=false");
    }

    @Test
    @Transactional
    public void getAllSprsByReviewerIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where reviewerId greater than or equals to DEFAULT_REVIEWER_ID
        defaultSprShouldBeFound("reviewerId.greaterOrEqualThan=" + DEFAULT_REVIEWER_ID);

        // Get all the sprList where reviewerId greater than or equals to UPDATED_REVIEWER_ID
        defaultSprShouldNotBeFound("reviewerId.greaterOrEqualThan=" + UPDATED_REVIEWER_ID);
    }

    @Test
    @Transactional
    public void getAllSprsByReviewerIdIsLessThanSomething() throws Exception {
        // Initialize the database
        sprRepository.saveAndFlush(spr);

        // Get all the sprList where reviewerId less than or equals to DEFAULT_REVIEWER_ID
        defaultSprShouldNotBeFound("reviewerId.lessThan=" + DEFAULT_REVIEWER_ID);

        // Get all the sprList where reviewerId less than or equals to UPDATED_REVIEWER_ID
        defaultSprShouldBeFound("reviewerId.lessThan=" + UPDATED_REVIEWER_ID);
    }


    @Test
    @Transactional
    public void getAllSprsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        spr.setUser(user);
        sprRepository.saveAndFlush(spr);
        Long userId = user.getId();

        // Get all the sprList where user equals to userId
        defaultSprShouldBeFound("userId.equals=" + userId);

        // Get all the sprList where user equals to userId + 1
        defaultSprShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllSprsByReleaseIsEqualToSomething() throws Exception {
        // Initialize the database
        Release release = ReleaseResourceIT.createEntity(em);
        em.persist(release);
        em.flush();
        spr.setRelease(release);
        sprRepository.saveAndFlush(spr);
        Long releaseId = release.getId();

        // Get all the sprList where release equals to releaseId
        defaultSprShouldBeFound("releaseId.equals=" + releaseId);

        // Get all the sprList where release equals to releaseId + 1
        defaultSprShouldNotBeFound("releaseId.equals=" + (releaseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSprShouldBeFound(String filter) throws Exception {
        restSprMockMvc.perform(get("/api/sprs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spr.getId().intValue())))
            .andExpect(jsonPath("$.[*].territory").value(hasItem(DEFAULT_TERRITORY.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].fullNumber").value(hasItem(DEFAULT_FULL_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].resolution").value(hasItem(DEFAULT_RESOLUTION.toString())))
            .andExpect(jsonPath("$.[*].jiraLink").value(hasItem(DEFAULT_JIRA_LINK)))
            .andExpect(jsonPath("$.[*].reviewerId").value(hasItem(DEFAULT_REVIEWER_ID.intValue())));

        // Check, that the count call also returns 1
        restSprMockMvc.perform(get("/api/sprs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSprShouldNotBeFound(String filter) throws Exception {
        restSprMockMvc.perform(get("/api/sprs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSprMockMvc.perform(get("/api/sprs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSpr() throws Exception {
        // Get the spr
        restSprMockMvc.perform(get("/api/sprs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpr() throws Exception {
        // Initialize the database
        sprService.save(spr);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockSprSearchRepository);

        int databaseSizeBeforeUpdate = sprRepository.findAll().size();

        // Update the spr
        Spr updatedSpr = sprRepository.findById(spr.getId()).get();
        // Disconnect from session so that the updates on updatedSpr are not directly saved in db
        em.detach(updatedSpr);
        updatedSpr
            .territory(UPDATED_TERRITORY)
            .number(UPDATED_NUMBER)
            .fullNumber(UPDATED_FULL_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .resolution(UPDATED_RESOLUTION)
            .jiraLink(UPDATED_JIRA_LINK)
            .reviewerId(UPDATED_REVIEWER_ID);

        restSprMockMvc.perform(put("/api/sprs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSpr)))
            .andExpect(status().isOk());

        // Validate the Spr in the database
        List<Spr> sprList = sprRepository.findAll();
        assertThat(sprList).hasSize(databaseSizeBeforeUpdate);
        Spr testSpr = sprList.get(sprList.size() - 1);
        assertThat(testSpr.getTerritory()).isEqualTo(UPDATED_TERRITORY);
        assertThat(testSpr.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testSpr.getFullNumber()).isEqualTo(UPDATED_FULL_NUMBER);
        assertThat(testSpr.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpr.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testSpr.getResolution()).isEqualTo(UPDATED_RESOLUTION);
        assertThat(testSpr.getJiraLink()).isEqualTo(UPDATED_JIRA_LINK);
        assertThat(testSpr.getReviewerId()).isEqualTo(UPDATED_REVIEWER_ID);

        // Validate the Spr in Elasticsearch
        verify(mockSprSearchRepository, times(1)).save(testSpr);
    }

    @Test
    @Transactional
    public void updateNonExistingSpr() throws Exception {
        int databaseSizeBeforeUpdate = sprRepository.findAll().size();

        // Create the Spr

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSprMockMvc.perform(put("/api/sprs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spr)))
            .andExpect(status().isBadRequest());

        // Validate the Spr in the database
        List<Spr> sprList = sprRepository.findAll();
        assertThat(sprList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Spr in Elasticsearch
        verify(mockSprSearchRepository, times(0)).save(spr);
    }

    @Test
    @Transactional
    public void deleteSpr() throws Exception {
        // Initialize the database
        sprService.save(spr);

        int databaseSizeBeforeDelete = sprRepository.findAll().size();

        // Delete the spr
        restSprMockMvc.perform(delete("/api/sprs/{id}", spr.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Spr> sprList = sprRepository.findAll();
        assertThat(sprList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Spr in Elasticsearch
        verify(mockSprSearchRepository, times(1)).deleteById(spr.getId());
    }

    @Test
    @Transactional
    public void searchSpr() throws Exception {
        // Initialize the database
        sprService.save(spr);
        when(mockSprSearchRepository.search(queryStringQuery("id:" + spr.getId())))
            .thenReturn(Collections.singletonList(spr));
        // Search the spr
        restSprMockMvc.perform(get("/api/_search/sprs?query=id:" + spr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spr.getId().intValue())))
            .andExpect(jsonPath("$.[*].territory").value(hasItem(DEFAULT_TERRITORY.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].fullNumber").value(hasItem(DEFAULT_FULL_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].resolution").value(hasItem(DEFAULT_RESOLUTION.toString())))
            .andExpect(jsonPath("$.[*].jiraLink").value(hasItem(DEFAULT_JIRA_LINK)))
            .andExpect(jsonPath("$.[*].reviewerId").value(hasItem(DEFAULT_REVIEWER_ID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Spr.class);
        Spr spr1 = new Spr();
        spr1.setId(1L);
        Spr spr2 = new Spr();
        spr2.setId(spr1.getId());
        assertThat(spr1).isEqualTo(spr2);
        spr2.setId(2L);
        assertThat(spr1).isNotEqualTo(spr2);
        spr1.setId(null);
        assertThat(spr1).isNotEqualTo(spr2);
    }
}
