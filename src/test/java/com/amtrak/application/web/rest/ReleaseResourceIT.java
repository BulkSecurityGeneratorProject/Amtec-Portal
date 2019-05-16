package com.amtrak.application.web.rest;

import com.amtrak.application.AmtecPortalApp;
import com.amtrak.application.domain.Release;
import com.amtrak.application.domain.Spr;
import com.amtrak.application.repository.ReleaseRepository;
import com.amtrak.application.repository.search.ReleaseSearchRepository;
import com.amtrak.application.service.ReleaseService;
import com.amtrak.application.web.rest.errors.ExceptionTranslator;
import com.amtrak.application.service.dto.ReleaseCriteria;
import com.amtrak.application.service.ReleaseQueryService;

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

import com.amtrak.application.domain.enumeration.Territory;
/**
 * Integration tests for the {@Link ReleaseResource} REST controller.
 */
@SpringBootTest(classes = AmtecPortalApp.class)
public class ReleaseResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Territory DEFAULT_TERRITORY = Territory.CETC_WEST;
    private static final Territory UPDATED_TERRITORY = Territory.HUDSON;

    private static final Integer DEFAULT_BUILD = 1;
    private static final Integer UPDATED_BUILD = 2;

    private static final String DEFAULT_RELEASE_LETTER = "AAAAAAAAAA";
    private static final String UPDATED_RELEASE_LETTER = "BBBBBBBBBB";

    private static final String DEFAULT_PREFIX_LETTER = "AAAAAAAAAA";
    private static final String UPDATED_PREFIX_LETTER = "BBBBBBBBBB";

    private static final String DEFAULT_DATABASE_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_DATABASE_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_WS_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_WS_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_TMA_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_TMA_VERSION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PORT = 1;
    private static final Integer UPDATED_PORT = 2;

    private static final String DEFAULT_NEW_FEATURES = "AAAAAAAAAA";
    private static final String UPDATED_NEW_FEATURES = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_FEATURES = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_FEATURES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CURRENT = false;
    private static final Boolean UPDATED_CURRENT = true;

    @Autowired
    private ReleaseRepository releaseRepository;

    @Autowired
    private ReleaseService releaseService;

    /**
     * This repository is mocked in the com.amtrak.application.repository.search test package.
     *
     * @see com.amtrak.application.repository.search.ReleaseSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReleaseSearchRepository mockReleaseSearchRepository;

    @Autowired
    private ReleaseQueryService releaseQueryService;

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

    private MockMvc restReleaseMockMvc;

    private Release release;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReleaseResource releaseResource = new ReleaseResource(releaseService, releaseQueryService);
        this.restReleaseMockMvc = MockMvcBuilders.standaloneSetup(releaseResource)
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
    public static Release createEntity(EntityManager em) {
        Release release = new Release()
            .date(DEFAULT_DATE)
            .territory(DEFAULT_TERRITORY)
            .build(DEFAULT_BUILD)
            .releaseLetter(DEFAULT_RELEASE_LETTER)
            .prefixLetter(DEFAULT_PREFIX_LETTER)
            .databaseVersion(DEFAULT_DATABASE_VERSION)
            .wsVersion(DEFAULT_WS_VERSION)
            .tmaVersion(DEFAULT_TMA_VERSION)
            .port(DEFAULT_PORT)
            .newFeatures(DEFAULT_NEW_FEATURES)
            .updatedFeatures(DEFAULT_UPDATED_FEATURES)
            .current(DEFAULT_CURRENT);
        return release;
    }

    @BeforeEach
    public void initTest() {
        release = createEntity(em);
    }

    @Test
    @Transactional
    public void createRelease() throws Exception {
        int databaseSizeBeforeCreate = releaseRepository.findAll().size();

        // Create the Release
        restReleaseMockMvc.perform(post("/api/releases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(release)))
            .andExpect(status().isCreated());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeCreate + 1);
        Release testRelease = releaseList.get(releaseList.size() - 1);
        assertThat(testRelease.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRelease.getTerritory()).isEqualTo(DEFAULT_TERRITORY);
        assertThat(testRelease.getBuild()).isEqualTo(DEFAULT_BUILD);
        assertThat(testRelease.getReleaseLetter()).isEqualTo(DEFAULT_RELEASE_LETTER);
        assertThat(testRelease.getPrefixLetter()).isEqualTo(DEFAULT_PREFIX_LETTER);
        assertThat(testRelease.getDatabaseVersion()).isEqualTo(DEFAULT_DATABASE_VERSION);
        assertThat(testRelease.getWsVersion()).isEqualTo(DEFAULT_WS_VERSION);
        assertThat(testRelease.getTmaVersion()).isEqualTo(DEFAULT_TMA_VERSION);
        assertThat(testRelease.getPort()).isEqualTo(DEFAULT_PORT);
        assertThat(testRelease.getNewFeatures()).isEqualTo(DEFAULT_NEW_FEATURES);
        assertThat(testRelease.getUpdatedFeatures()).isEqualTo(DEFAULT_UPDATED_FEATURES);
        assertThat(testRelease.isCurrent()).isEqualTo(DEFAULT_CURRENT);

        // Validate the Release in Elasticsearch
        verify(mockReleaseSearchRepository, times(1)).save(testRelease);
    }

    @Test
    @Transactional
    public void createReleaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = releaseRepository.findAll().size();

        // Create the Release with an existing ID
        release.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReleaseMockMvc.perform(post("/api/releases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(release)))
            .andExpect(status().isBadRequest());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeCreate);

        // Validate the Release in Elasticsearch
        verify(mockReleaseSearchRepository, times(0)).save(release);
    }


    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = releaseRepository.findAll().size();
        // set the field null
        release.setDate(null);

        // Create the Release, which fails.

        restReleaseMockMvc.perform(post("/api/releases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(release)))
            .andExpect(status().isBadRequest());

        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTerritoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = releaseRepository.findAll().size();
        // set the field null
        release.setTerritory(null);

        // Create the Release, which fails.

        restReleaseMockMvc.perform(post("/api/releases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(release)))
            .andExpect(status().isBadRequest());

        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBuildIsRequired() throws Exception {
        int databaseSizeBeforeTest = releaseRepository.findAll().size();
        // set the field null
        release.setBuild(null);

        // Create the Release, which fails.

        restReleaseMockMvc.perform(post("/api/releases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(release)))
            .andExpect(status().isBadRequest());

        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReleaseLetterIsRequired() throws Exception {
        int databaseSizeBeforeTest = releaseRepository.findAll().size();
        // set the field null
        release.setReleaseLetter(null);

        // Create the Release, which fails.

        restReleaseMockMvc.perform(post("/api/releases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(release)))
            .andExpect(status().isBadRequest());

        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDatabaseVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = releaseRepository.findAll().size();
        // set the field null
        release.setDatabaseVersion(null);

        // Create the Release, which fails.

        restReleaseMockMvc.perform(post("/api/releases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(release)))
            .andExpect(status().isBadRequest());

        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrentIsRequired() throws Exception {
        int databaseSizeBeforeTest = releaseRepository.findAll().size();
        // set the field null
        release.setCurrent(null);

        // Create the Release, which fails.

        restReleaseMockMvc.perform(post("/api/releases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(release)))
            .andExpect(status().isBadRequest());

        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReleases() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList
        restReleaseMockMvc.perform(get("/api/releases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(release.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].territory").value(hasItem(DEFAULT_TERRITORY.toString())))
            .andExpect(jsonPath("$.[*].build").value(hasItem(DEFAULT_BUILD)))
            .andExpect(jsonPath("$.[*].releaseLetter").value(hasItem(DEFAULT_RELEASE_LETTER.toString())))
            .andExpect(jsonPath("$.[*].prefixLetter").value(hasItem(DEFAULT_PREFIX_LETTER.toString())))
            .andExpect(jsonPath("$.[*].databaseVersion").value(hasItem(DEFAULT_DATABASE_VERSION.toString())))
            .andExpect(jsonPath("$.[*].wsVersion").value(hasItem(DEFAULT_WS_VERSION.toString())))
            .andExpect(jsonPath("$.[*].tmaVersion").value(hasItem(DEFAULT_TMA_VERSION.toString())))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)))
            .andExpect(jsonPath("$.[*].newFeatures").value(hasItem(DEFAULT_NEW_FEATURES.toString())))
            .andExpect(jsonPath("$.[*].updatedFeatures").value(hasItem(DEFAULT_UPDATED_FEATURES.toString())))
            .andExpect(jsonPath("$.[*].current").value(hasItem(DEFAULT_CURRENT.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getRelease() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get the release
        restReleaseMockMvc.perform(get("/api/releases/{id}", release.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(release.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.territory").value(DEFAULT_TERRITORY.toString()))
            .andExpect(jsonPath("$.build").value(DEFAULT_BUILD))
            .andExpect(jsonPath("$.releaseLetter").value(DEFAULT_RELEASE_LETTER.toString()))
            .andExpect(jsonPath("$.prefixLetter").value(DEFAULT_PREFIX_LETTER.toString()))
            .andExpect(jsonPath("$.databaseVersion").value(DEFAULT_DATABASE_VERSION.toString()))
            .andExpect(jsonPath("$.wsVersion").value(DEFAULT_WS_VERSION.toString()))
            .andExpect(jsonPath("$.tmaVersion").value(DEFAULT_TMA_VERSION.toString()))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT))
            .andExpect(jsonPath("$.newFeatures").value(DEFAULT_NEW_FEATURES.toString()))
            .andExpect(jsonPath("$.updatedFeatures").value(DEFAULT_UPDATED_FEATURES.toString()))
            .andExpect(jsonPath("$.current").value(DEFAULT_CURRENT.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllReleasesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where date equals to DEFAULT_DATE
        defaultReleaseShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the releaseList where date equals to UPDATED_DATE
        defaultReleaseShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllReleasesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where date in DEFAULT_DATE or UPDATED_DATE
        defaultReleaseShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the releaseList where date equals to UPDATED_DATE
        defaultReleaseShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllReleasesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where date is not null
        defaultReleaseShouldBeFound("date.specified=true");

        // Get all the releaseList where date is null
        defaultReleaseShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllReleasesByTerritoryIsEqualToSomething() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where territory equals to DEFAULT_TERRITORY
        defaultReleaseShouldBeFound("territory.equals=" + DEFAULT_TERRITORY);

        // Get all the releaseList where territory equals to UPDATED_TERRITORY
        defaultReleaseShouldNotBeFound("territory.equals=" + UPDATED_TERRITORY);
    }

    @Test
    @Transactional
    public void getAllReleasesByTerritoryIsInShouldWork() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where territory in DEFAULT_TERRITORY or UPDATED_TERRITORY
        defaultReleaseShouldBeFound("territory.in=" + DEFAULT_TERRITORY + "," + UPDATED_TERRITORY);

        // Get all the releaseList where territory equals to UPDATED_TERRITORY
        defaultReleaseShouldNotBeFound("territory.in=" + UPDATED_TERRITORY);
    }

    @Test
    @Transactional
    public void getAllReleasesByTerritoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where territory is not null
        defaultReleaseShouldBeFound("territory.specified=true");

        // Get all the releaseList where territory is null
        defaultReleaseShouldNotBeFound("territory.specified=false");
    }

    @Test
    @Transactional
    public void getAllReleasesByBuildIsEqualToSomething() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where build equals to DEFAULT_BUILD
        defaultReleaseShouldBeFound("build.equals=" + DEFAULT_BUILD);

        // Get all the releaseList where build equals to UPDATED_BUILD
        defaultReleaseShouldNotBeFound("build.equals=" + UPDATED_BUILD);
    }

    @Test
    @Transactional
    public void getAllReleasesByBuildIsInShouldWork() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where build in DEFAULT_BUILD or UPDATED_BUILD
        defaultReleaseShouldBeFound("build.in=" + DEFAULT_BUILD + "," + UPDATED_BUILD);

        // Get all the releaseList where build equals to UPDATED_BUILD
        defaultReleaseShouldNotBeFound("build.in=" + UPDATED_BUILD);
    }

    @Test
    @Transactional
    public void getAllReleasesByBuildIsNullOrNotNull() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where build is not null
        defaultReleaseShouldBeFound("build.specified=true");

        // Get all the releaseList where build is null
        defaultReleaseShouldNotBeFound("build.specified=false");
    }

    @Test
    @Transactional
    public void getAllReleasesByBuildIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where build greater than or equals to DEFAULT_BUILD
        defaultReleaseShouldBeFound("build.greaterOrEqualThan=" + DEFAULT_BUILD);

        // Get all the releaseList where build greater than or equals to UPDATED_BUILD
        defaultReleaseShouldNotBeFound("build.greaterOrEqualThan=" + UPDATED_BUILD);
    }

    @Test
    @Transactional
    public void getAllReleasesByBuildIsLessThanSomething() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where build less than or equals to DEFAULT_BUILD
        defaultReleaseShouldNotBeFound("build.lessThan=" + DEFAULT_BUILD);

        // Get all the releaseList where build less than or equals to UPDATED_BUILD
        defaultReleaseShouldBeFound("build.lessThan=" + UPDATED_BUILD);
    }


    @Test
    @Transactional
    public void getAllReleasesByReleaseLetterIsEqualToSomething() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where releaseLetter equals to DEFAULT_RELEASE_LETTER
        defaultReleaseShouldBeFound("releaseLetter.equals=" + DEFAULT_RELEASE_LETTER);

        // Get all the releaseList where releaseLetter equals to UPDATED_RELEASE_LETTER
        defaultReleaseShouldNotBeFound("releaseLetter.equals=" + UPDATED_RELEASE_LETTER);
    }

    @Test
    @Transactional
    public void getAllReleasesByReleaseLetterIsInShouldWork() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where releaseLetter in DEFAULT_RELEASE_LETTER or UPDATED_RELEASE_LETTER
        defaultReleaseShouldBeFound("releaseLetter.in=" + DEFAULT_RELEASE_LETTER + "," + UPDATED_RELEASE_LETTER);

        // Get all the releaseList where releaseLetter equals to UPDATED_RELEASE_LETTER
        defaultReleaseShouldNotBeFound("releaseLetter.in=" + UPDATED_RELEASE_LETTER);
    }

    @Test
    @Transactional
    public void getAllReleasesByReleaseLetterIsNullOrNotNull() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where releaseLetter is not null
        defaultReleaseShouldBeFound("releaseLetter.specified=true");

        // Get all the releaseList where releaseLetter is null
        defaultReleaseShouldNotBeFound("releaseLetter.specified=false");
    }

    @Test
    @Transactional
    public void getAllReleasesByPrefixLetterIsEqualToSomething() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where prefixLetter equals to DEFAULT_PREFIX_LETTER
        defaultReleaseShouldBeFound("prefixLetter.equals=" + DEFAULT_PREFIX_LETTER);

        // Get all the releaseList where prefixLetter equals to UPDATED_PREFIX_LETTER
        defaultReleaseShouldNotBeFound("prefixLetter.equals=" + UPDATED_PREFIX_LETTER);
    }

    @Test
    @Transactional
    public void getAllReleasesByPrefixLetterIsInShouldWork() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where prefixLetter in DEFAULT_PREFIX_LETTER or UPDATED_PREFIX_LETTER
        defaultReleaseShouldBeFound("prefixLetter.in=" + DEFAULT_PREFIX_LETTER + "," + UPDATED_PREFIX_LETTER);

        // Get all the releaseList where prefixLetter equals to UPDATED_PREFIX_LETTER
        defaultReleaseShouldNotBeFound("prefixLetter.in=" + UPDATED_PREFIX_LETTER);
    }

    @Test
    @Transactional
    public void getAllReleasesByPrefixLetterIsNullOrNotNull() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where prefixLetter is not null
        defaultReleaseShouldBeFound("prefixLetter.specified=true");

        // Get all the releaseList where prefixLetter is null
        defaultReleaseShouldNotBeFound("prefixLetter.specified=false");
    }

    @Test
    @Transactional
    public void getAllReleasesByDatabaseVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where databaseVersion equals to DEFAULT_DATABASE_VERSION
        defaultReleaseShouldBeFound("databaseVersion.equals=" + DEFAULT_DATABASE_VERSION);

        // Get all the releaseList where databaseVersion equals to UPDATED_DATABASE_VERSION
        defaultReleaseShouldNotBeFound("databaseVersion.equals=" + UPDATED_DATABASE_VERSION);
    }

    @Test
    @Transactional
    public void getAllReleasesByDatabaseVersionIsInShouldWork() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where databaseVersion in DEFAULT_DATABASE_VERSION or UPDATED_DATABASE_VERSION
        defaultReleaseShouldBeFound("databaseVersion.in=" + DEFAULT_DATABASE_VERSION + "," + UPDATED_DATABASE_VERSION);

        // Get all the releaseList where databaseVersion equals to UPDATED_DATABASE_VERSION
        defaultReleaseShouldNotBeFound("databaseVersion.in=" + UPDATED_DATABASE_VERSION);
    }

    @Test
    @Transactional
    public void getAllReleasesByDatabaseVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where databaseVersion is not null
        defaultReleaseShouldBeFound("databaseVersion.specified=true");

        // Get all the releaseList where databaseVersion is null
        defaultReleaseShouldNotBeFound("databaseVersion.specified=false");
    }

    @Test
    @Transactional
    public void getAllReleasesByWsVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where wsVersion equals to DEFAULT_WS_VERSION
        defaultReleaseShouldBeFound("wsVersion.equals=" + DEFAULT_WS_VERSION);

        // Get all the releaseList where wsVersion equals to UPDATED_WS_VERSION
        defaultReleaseShouldNotBeFound("wsVersion.equals=" + UPDATED_WS_VERSION);
    }

    @Test
    @Transactional
    public void getAllReleasesByWsVersionIsInShouldWork() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where wsVersion in DEFAULT_WS_VERSION or UPDATED_WS_VERSION
        defaultReleaseShouldBeFound("wsVersion.in=" + DEFAULT_WS_VERSION + "," + UPDATED_WS_VERSION);

        // Get all the releaseList where wsVersion equals to UPDATED_WS_VERSION
        defaultReleaseShouldNotBeFound("wsVersion.in=" + UPDATED_WS_VERSION);
    }

    @Test
    @Transactional
    public void getAllReleasesByWsVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where wsVersion is not null
        defaultReleaseShouldBeFound("wsVersion.specified=true");

        // Get all the releaseList where wsVersion is null
        defaultReleaseShouldNotBeFound("wsVersion.specified=false");
    }

    @Test
    @Transactional
    public void getAllReleasesByTmaVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where tmaVersion equals to DEFAULT_TMA_VERSION
        defaultReleaseShouldBeFound("tmaVersion.equals=" + DEFAULT_TMA_VERSION);

        // Get all the releaseList where tmaVersion equals to UPDATED_TMA_VERSION
        defaultReleaseShouldNotBeFound("tmaVersion.equals=" + UPDATED_TMA_VERSION);
    }

    @Test
    @Transactional
    public void getAllReleasesByTmaVersionIsInShouldWork() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where tmaVersion in DEFAULT_TMA_VERSION or UPDATED_TMA_VERSION
        defaultReleaseShouldBeFound("tmaVersion.in=" + DEFAULT_TMA_VERSION + "," + UPDATED_TMA_VERSION);

        // Get all the releaseList where tmaVersion equals to UPDATED_TMA_VERSION
        defaultReleaseShouldNotBeFound("tmaVersion.in=" + UPDATED_TMA_VERSION);
    }

    @Test
    @Transactional
    public void getAllReleasesByTmaVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where tmaVersion is not null
        defaultReleaseShouldBeFound("tmaVersion.specified=true");

        // Get all the releaseList where tmaVersion is null
        defaultReleaseShouldNotBeFound("tmaVersion.specified=false");
    }

    @Test
    @Transactional
    public void getAllReleasesByPortIsEqualToSomething() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where port equals to DEFAULT_PORT
        defaultReleaseShouldBeFound("port.equals=" + DEFAULT_PORT);

        // Get all the releaseList where port equals to UPDATED_PORT
        defaultReleaseShouldNotBeFound("port.equals=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    public void getAllReleasesByPortIsInShouldWork() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where port in DEFAULT_PORT or UPDATED_PORT
        defaultReleaseShouldBeFound("port.in=" + DEFAULT_PORT + "," + UPDATED_PORT);

        // Get all the releaseList where port equals to UPDATED_PORT
        defaultReleaseShouldNotBeFound("port.in=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    public void getAllReleasesByPortIsNullOrNotNull() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where port is not null
        defaultReleaseShouldBeFound("port.specified=true");

        // Get all the releaseList where port is null
        defaultReleaseShouldNotBeFound("port.specified=false");
    }

    @Test
    @Transactional
    public void getAllReleasesByPortIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where port greater than or equals to DEFAULT_PORT
        defaultReleaseShouldBeFound("port.greaterOrEqualThan=" + DEFAULT_PORT);

        // Get all the releaseList where port greater than or equals to UPDATED_PORT
        defaultReleaseShouldNotBeFound("port.greaterOrEqualThan=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    public void getAllReleasesByPortIsLessThanSomething() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where port less than or equals to DEFAULT_PORT
        defaultReleaseShouldNotBeFound("port.lessThan=" + DEFAULT_PORT);

        // Get all the releaseList where port less than or equals to UPDATED_PORT
        defaultReleaseShouldBeFound("port.lessThan=" + UPDATED_PORT);
    }


    @Test
    @Transactional
    public void getAllReleasesByCurrentIsEqualToSomething() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where current equals to DEFAULT_CURRENT
        defaultReleaseShouldBeFound("current.equals=" + DEFAULT_CURRENT);

        // Get all the releaseList where current equals to UPDATED_CURRENT
        defaultReleaseShouldNotBeFound("current.equals=" + UPDATED_CURRENT);
    }

    @Test
    @Transactional
    public void getAllReleasesByCurrentIsInShouldWork() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where current in DEFAULT_CURRENT or UPDATED_CURRENT
        defaultReleaseShouldBeFound("current.in=" + DEFAULT_CURRENT + "," + UPDATED_CURRENT);

        // Get all the releaseList where current equals to UPDATED_CURRENT
        defaultReleaseShouldNotBeFound("current.in=" + UPDATED_CURRENT);
    }

    @Test
    @Transactional
    public void getAllReleasesByCurrentIsNullOrNotNull() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList where current is not null
        defaultReleaseShouldBeFound("current.specified=true");

        // Get all the releaseList where current is null
        defaultReleaseShouldNotBeFound("current.specified=false");
    }

    @Test
    @Transactional
    public void getAllReleasesBySprIsEqualToSomething() throws Exception {
        // Initialize the database
        Spr spr = SprResourceIT.createEntity(em);
        em.persist(spr);
        em.flush();
        release.addSpr(spr);
        releaseRepository.saveAndFlush(release);
        Long sprId = spr.getId();

        // Get all the releaseList where spr equals to sprId
        defaultReleaseShouldBeFound("sprId.equals=" + sprId);

        // Get all the releaseList where spr equals to sprId + 1
        defaultReleaseShouldNotBeFound("sprId.equals=" + (sprId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReleaseShouldBeFound(String filter) throws Exception {
        restReleaseMockMvc.perform(get("/api/releases?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(release.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].territory").value(hasItem(DEFAULT_TERRITORY.toString())))
            .andExpect(jsonPath("$.[*].build").value(hasItem(DEFAULT_BUILD)))
            .andExpect(jsonPath("$.[*].releaseLetter").value(hasItem(DEFAULT_RELEASE_LETTER)))
            .andExpect(jsonPath("$.[*].prefixLetter").value(hasItem(DEFAULT_PREFIX_LETTER)))
            .andExpect(jsonPath("$.[*].databaseVersion").value(hasItem(DEFAULT_DATABASE_VERSION)))
            .andExpect(jsonPath("$.[*].wsVersion").value(hasItem(DEFAULT_WS_VERSION)))
            .andExpect(jsonPath("$.[*].tmaVersion").value(hasItem(DEFAULT_TMA_VERSION)))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)))
            .andExpect(jsonPath("$.[*].newFeatures").value(hasItem(DEFAULT_NEW_FEATURES.toString())))
            .andExpect(jsonPath("$.[*].updatedFeatures").value(hasItem(DEFAULT_UPDATED_FEATURES.toString())))
            .andExpect(jsonPath("$.[*].current").value(hasItem(DEFAULT_CURRENT.booleanValue())));

        // Check, that the count call also returns 1
        restReleaseMockMvc.perform(get("/api/releases/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReleaseShouldNotBeFound(String filter) throws Exception {
        restReleaseMockMvc.perform(get("/api/releases?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReleaseMockMvc.perform(get("/api/releases/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRelease() throws Exception {
        // Get the release
        restReleaseMockMvc.perform(get("/api/releases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRelease() throws Exception {
        // Initialize the database
        releaseService.save(release);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockReleaseSearchRepository);

        int databaseSizeBeforeUpdate = releaseRepository.findAll().size();

        // Update the release
        Release updatedRelease = releaseRepository.findById(release.getId()).get();
        // Disconnect from session so that the updates on updatedRelease are not directly saved in db
        em.detach(updatedRelease);
        updatedRelease
            .date(UPDATED_DATE)
            .territory(UPDATED_TERRITORY)
            .build(UPDATED_BUILD)
            .releaseLetter(UPDATED_RELEASE_LETTER)
            .prefixLetter(UPDATED_PREFIX_LETTER)
            .databaseVersion(UPDATED_DATABASE_VERSION)
            .wsVersion(UPDATED_WS_VERSION)
            .tmaVersion(UPDATED_TMA_VERSION)
            .port(UPDATED_PORT)
            .newFeatures(UPDATED_NEW_FEATURES)
            .updatedFeatures(UPDATED_UPDATED_FEATURES)
            .current(UPDATED_CURRENT);

        restReleaseMockMvc.perform(put("/api/releases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRelease)))
            .andExpect(status().isOk());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeUpdate);
        Release testRelease = releaseList.get(releaseList.size() - 1);
        assertThat(testRelease.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRelease.getTerritory()).isEqualTo(UPDATED_TERRITORY);
        assertThat(testRelease.getBuild()).isEqualTo(UPDATED_BUILD);
        assertThat(testRelease.getReleaseLetter()).isEqualTo(UPDATED_RELEASE_LETTER);
        assertThat(testRelease.getPrefixLetter()).isEqualTo(UPDATED_PREFIX_LETTER);
        assertThat(testRelease.getDatabaseVersion()).isEqualTo(UPDATED_DATABASE_VERSION);
        assertThat(testRelease.getWsVersion()).isEqualTo(UPDATED_WS_VERSION);
        assertThat(testRelease.getTmaVersion()).isEqualTo(UPDATED_TMA_VERSION);
        assertThat(testRelease.getPort()).isEqualTo(UPDATED_PORT);
        assertThat(testRelease.getNewFeatures()).isEqualTo(UPDATED_NEW_FEATURES);
        assertThat(testRelease.getUpdatedFeatures()).isEqualTo(UPDATED_UPDATED_FEATURES);
        assertThat(testRelease.isCurrent()).isEqualTo(UPDATED_CURRENT);

        // Validate the Release in Elasticsearch
        verify(mockReleaseSearchRepository, times(1)).save(testRelease);
    }

    @Test
    @Transactional
    public void updateNonExistingRelease() throws Exception {
        int databaseSizeBeforeUpdate = releaseRepository.findAll().size();

        // Create the Release

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReleaseMockMvc.perform(put("/api/releases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(release)))
            .andExpect(status().isBadRequest());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Release in Elasticsearch
        verify(mockReleaseSearchRepository, times(0)).save(release);
    }

    @Test
    @Transactional
    public void deleteRelease() throws Exception {
        // Initialize the database
        releaseService.save(release);

        int databaseSizeBeforeDelete = releaseRepository.findAll().size();

        // Delete the release
        restReleaseMockMvc.perform(delete("/api/releases/{id}", release.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Release in Elasticsearch
        verify(mockReleaseSearchRepository, times(1)).deleteById(release.getId());
    }

    @Test
    @Transactional
    public void searchRelease() throws Exception {
        // Initialize the database
        releaseService.save(release);
        when(mockReleaseSearchRepository.search(queryStringQuery("id:" + release.getId())))
            .thenReturn(Collections.singletonList(release));
        // Search the release
        restReleaseMockMvc.perform(get("/api/_search/releases?query=id:" + release.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(release.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].territory").value(hasItem(DEFAULT_TERRITORY.toString())))
            .andExpect(jsonPath("$.[*].build").value(hasItem(DEFAULT_BUILD)))
            .andExpect(jsonPath("$.[*].releaseLetter").value(hasItem(DEFAULT_RELEASE_LETTER)))
            .andExpect(jsonPath("$.[*].prefixLetter").value(hasItem(DEFAULT_PREFIX_LETTER)))
            .andExpect(jsonPath("$.[*].databaseVersion").value(hasItem(DEFAULT_DATABASE_VERSION)))
            .andExpect(jsonPath("$.[*].wsVersion").value(hasItem(DEFAULT_WS_VERSION)))
            .andExpect(jsonPath("$.[*].tmaVersion").value(hasItem(DEFAULT_TMA_VERSION)))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)))
            .andExpect(jsonPath("$.[*].newFeatures").value(hasItem(DEFAULT_NEW_FEATURES.toString())))
            .andExpect(jsonPath("$.[*].updatedFeatures").value(hasItem(DEFAULT_UPDATED_FEATURES.toString())))
            .andExpect(jsonPath("$.[*].current").value(hasItem(DEFAULT_CURRENT.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Release.class);
        Release release1 = new Release();
        release1.setId(1L);
        Release release2 = new Release();
        release2.setId(release1.getId());
        assertThat(release1).isEqualTo(release2);
        release2.setId(2L);
        assertThat(release1).isNotEqualTo(release2);
        release1.setId(null);
        assertThat(release1).isNotEqualTo(release2);
    }
}
