package com.amtrak.application.web.rest;

import com.amtrak.application.AmtecPortalApp;
import com.amtrak.application.domain.OutOfOffice;
import com.amtrak.application.domain.User;
import com.amtrak.application.repository.OutOfOfficeRepository;
import com.amtrak.application.repository.search.OutOfOfficeSearchRepository;
import com.amtrak.application.service.OutOfOfficeService;
import com.amtrak.application.web.rest.errors.ExceptionTranslator;
import com.amtrak.application.service.dto.OutOfOfficeCriteria;
import com.amtrak.application.service.OutOfOfficeQueryService;

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
 * Integration tests for the {@Link OutOfOfficeResource} REST controller.
 */
@SpringBootTest(classes = AmtecPortalApp.class)
public class OutOfOfficeResourceIT {

    private static final Instant DEFAULT_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private OutOfOfficeRepository outOfOfficeRepository;

    @Autowired
    private OutOfOfficeService outOfOfficeService;

    /**
     * This repository is mocked in the com.amtrak.application.repository.search test package.
     *
     * @see com.amtrak.application.repository.search.OutOfOfficeSearchRepositoryMockConfiguration
     */
    @Autowired
    private OutOfOfficeSearchRepository mockOutOfOfficeSearchRepository;

    @Autowired
    private OutOfOfficeQueryService outOfOfficeQueryService;

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

    private MockMvc restOutOfOfficeMockMvc;

    private OutOfOffice outOfOffice;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OutOfOfficeResource outOfOfficeResource = new OutOfOfficeResource(outOfOfficeService, outOfOfficeQueryService);
        this.restOutOfOfficeMockMvc = MockMvcBuilders.standaloneSetup(outOfOfficeResource)
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
    public static OutOfOffice createEntity(EntityManager em) {
        OutOfOffice outOfOffice = new OutOfOffice()
            .start(DEFAULT_START)
            .end(DEFAULT_END)
            .description(DEFAULT_DESCRIPTION);
        return outOfOffice;
    }

    @BeforeEach
    public void initTest() {
        outOfOffice = createEntity(em);
    }

    @Test
    @Transactional
    public void createOutOfOffice() throws Exception {
        int databaseSizeBeforeCreate = outOfOfficeRepository.findAll().size();

        // Create the OutOfOffice
        restOutOfOfficeMockMvc.perform(post("/api/out-of-offices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(outOfOffice)))
            .andExpect(status().isCreated());

        // Validate the OutOfOffice in the database
        List<OutOfOffice> outOfOfficeList = outOfOfficeRepository.findAll();
        assertThat(outOfOfficeList).hasSize(databaseSizeBeforeCreate + 1);
        OutOfOffice testOutOfOffice = outOfOfficeList.get(outOfOfficeList.size() - 1);
        assertThat(testOutOfOffice.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testOutOfOffice.getEnd()).isEqualTo(DEFAULT_END);
        assertThat(testOutOfOffice.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the OutOfOffice in Elasticsearch
        verify(mockOutOfOfficeSearchRepository, times(1)).save(testOutOfOffice);
    }

    @Test
    @Transactional
    public void createOutOfOfficeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = outOfOfficeRepository.findAll().size();

        // Create the OutOfOffice with an existing ID
        outOfOffice.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOutOfOfficeMockMvc.perform(post("/api/out-of-offices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(outOfOffice)))
            .andExpect(status().isBadRequest());

        // Validate the OutOfOffice in the database
        List<OutOfOffice> outOfOfficeList = outOfOfficeRepository.findAll();
        assertThat(outOfOfficeList).hasSize(databaseSizeBeforeCreate);

        // Validate the OutOfOffice in Elasticsearch
        verify(mockOutOfOfficeSearchRepository, times(0)).save(outOfOffice);
    }


    @Test
    @Transactional
    public void checkStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = outOfOfficeRepository.findAll().size();
        // set the field null
        outOfOffice.setStart(null);

        // Create the OutOfOffice, which fails.

        restOutOfOfficeMockMvc.perform(post("/api/out-of-offices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(outOfOffice)))
            .andExpect(status().isBadRequest());

        List<OutOfOffice> outOfOfficeList = outOfOfficeRepository.findAll();
        assertThat(outOfOfficeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndIsRequired() throws Exception {
        int databaseSizeBeforeTest = outOfOfficeRepository.findAll().size();
        // set the field null
        outOfOffice.setEnd(null);

        // Create the OutOfOffice, which fails.

        restOutOfOfficeMockMvc.perform(post("/api/out-of-offices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(outOfOffice)))
            .andExpect(status().isBadRequest());

        List<OutOfOffice> outOfOfficeList = outOfOfficeRepository.findAll();
        assertThat(outOfOfficeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOutOfOffices() throws Exception {
        // Initialize the database
        outOfOfficeRepository.saveAndFlush(outOfOffice);

        // Get all the outOfOfficeList
        restOutOfOfficeMockMvc.perform(get("/api/out-of-offices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outOfOffice.getId().intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getOutOfOffice() throws Exception {
        // Initialize the database
        outOfOfficeRepository.saveAndFlush(outOfOffice);

        // Get the outOfOffice
        restOutOfOfficeMockMvc.perform(get("/api/out-of-offices/{id}", outOfOffice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(outOfOffice.getId().intValue()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START.toString()))
            .andExpect(jsonPath("$.end").value(DEFAULT_END.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllOutOfOfficesByStartIsEqualToSomething() throws Exception {
        // Initialize the database
        outOfOfficeRepository.saveAndFlush(outOfOffice);

        // Get all the outOfOfficeList where start equals to DEFAULT_START
        defaultOutOfOfficeShouldBeFound("start.equals=" + DEFAULT_START);

        // Get all the outOfOfficeList where start equals to UPDATED_START
        defaultOutOfOfficeShouldNotBeFound("start.equals=" + UPDATED_START);
    }

    @Test
    @Transactional
    public void getAllOutOfOfficesByStartIsInShouldWork() throws Exception {
        // Initialize the database
        outOfOfficeRepository.saveAndFlush(outOfOffice);

        // Get all the outOfOfficeList where start in DEFAULT_START or UPDATED_START
        defaultOutOfOfficeShouldBeFound("start.in=" + DEFAULT_START + "," + UPDATED_START);

        // Get all the outOfOfficeList where start equals to UPDATED_START
        defaultOutOfOfficeShouldNotBeFound("start.in=" + UPDATED_START);
    }

    @Test
    @Transactional
    public void getAllOutOfOfficesByStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        outOfOfficeRepository.saveAndFlush(outOfOffice);

        // Get all the outOfOfficeList where start is not null
        defaultOutOfOfficeShouldBeFound("start.specified=true");

        // Get all the outOfOfficeList where start is null
        defaultOutOfOfficeShouldNotBeFound("start.specified=false");
    }

    @Test
    @Transactional
    public void getAllOutOfOfficesByEndIsEqualToSomething() throws Exception {
        // Initialize the database
        outOfOfficeRepository.saveAndFlush(outOfOffice);

        // Get all the outOfOfficeList where end equals to DEFAULT_END
        defaultOutOfOfficeShouldBeFound("end.equals=" + DEFAULT_END);

        // Get all the outOfOfficeList where end equals to UPDATED_END
        defaultOutOfOfficeShouldNotBeFound("end.equals=" + UPDATED_END);
    }

    @Test
    @Transactional
    public void getAllOutOfOfficesByEndIsInShouldWork() throws Exception {
        // Initialize the database
        outOfOfficeRepository.saveAndFlush(outOfOffice);

        // Get all the outOfOfficeList where end in DEFAULT_END or UPDATED_END
        defaultOutOfOfficeShouldBeFound("end.in=" + DEFAULT_END + "," + UPDATED_END);

        // Get all the outOfOfficeList where end equals to UPDATED_END
        defaultOutOfOfficeShouldNotBeFound("end.in=" + UPDATED_END);
    }

    @Test
    @Transactional
    public void getAllOutOfOfficesByEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        outOfOfficeRepository.saveAndFlush(outOfOffice);

        // Get all the outOfOfficeList where end is not null
        defaultOutOfOfficeShouldBeFound("end.specified=true");

        // Get all the outOfOfficeList where end is null
        defaultOutOfOfficeShouldNotBeFound("end.specified=false");
    }

    @Test
    @Transactional
    public void getAllOutOfOfficesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        outOfOffice.setUser(user);
        outOfOfficeRepository.saveAndFlush(outOfOffice);
        Long userId = user.getId();

        // Get all the outOfOfficeList where user equals to userId
        defaultOutOfOfficeShouldBeFound("userId.equals=" + userId);

        // Get all the outOfOfficeList where user equals to userId + 1
        defaultOutOfOfficeShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOutOfOfficeShouldBeFound(String filter) throws Exception {
        restOutOfOfficeMockMvc.perform(get("/api/out-of-offices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outOfOffice.getId().intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restOutOfOfficeMockMvc.perform(get("/api/out-of-offices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOutOfOfficeShouldNotBeFound(String filter) throws Exception {
        restOutOfOfficeMockMvc.perform(get("/api/out-of-offices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOutOfOfficeMockMvc.perform(get("/api/out-of-offices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOutOfOffice() throws Exception {
        // Get the outOfOffice
        restOutOfOfficeMockMvc.perform(get("/api/out-of-offices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOutOfOffice() throws Exception {
        // Initialize the database
        outOfOfficeService.save(outOfOffice);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockOutOfOfficeSearchRepository);

        int databaseSizeBeforeUpdate = outOfOfficeRepository.findAll().size();

        // Update the outOfOffice
        OutOfOffice updatedOutOfOffice = outOfOfficeRepository.findById(outOfOffice.getId()).get();
        // Disconnect from session so that the updates on updatedOutOfOffice are not directly saved in db
        em.detach(updatedOutOfOffice);
        updatedOutOfOffice
            .start(UPDATED_START)
            .end(UPDATED_END)
            .description(UPDATED_DESCRIPTION);

        restOutOfOfficeMockMvc.perform(put("/api/out-of-offices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOutOfOffice)))
            .andExpect(status().isOk());

        // Validate the OutOfOffice in the database
        List<OutOfOffice> outOfOfficeList = outOfOfficeRepository.findAll();
        assertThat(outOfOfficeList).hasSize(databaseSizeBeforeUpdate);
        OutOfOffice testOutOfOffice = outOfOfficeList.get(outOfOfficeList.size() - 1);
        assertThat(testOutOfOffice.getStart()).isEqualTo(UPDATED_START);
        assertThat(testOutOfOffice.getEnd()).isEqualTo(UPDATED_END);
        assertThat(testOutOfOffice.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the OutOfOffice in Elasticsearch
        verify(mockOutOfOfficeSearchRepository, times(1)).save(testOutOfOffice);
    }

    @Test
    @Transactional
    public void updateNonExistingOutOfOffice() throws Exception {
        int databaseSizeBeforeUpdate = outOfOfficeRepository.findAll().size();

        // Create the OutOfOffice

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutOfOfficeMockMvc.perform(put("/api/out-of-offices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(outOfOffice)))
            .andExpect(status().isBadRequest());

        // Validate the OutOfOffice in the database
        List<OutOfOffice> outOfOfficeList = outOfOfficeRepository.findAll();
        assertThat(outOfOfficeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OutOfOffice in Elasticsearch
        verify(mockOutOfOfficeSearchRepository, times(0)).save(outOfOffice);
    }

    @Test
    @Transactional
    public void deleteOutOfOffice() throws Exception {
        // Initialize the database
        outOfOfficeService.save(outOfOffice);

        int databaseSizeBeforeDelete = outOfOfficeRepository.findAll().size();

        // Delete the outOfOffice
        restOutOfOfficeMockMvc.perform(delete("/api/out-of-offices/{id}", outOfOffice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<OutOfOffice> outOfOfficeList = outOfOfficeRepository.findAll();
        assertThat(outOfOfficeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OutOfOffice in Elasticsearch
        verify(mockOutOfOfficeSearchRepository, times(1)).deleteById(outOfOffice.getId());
    }

    @Test
    @Transactional
    public void searchOutOfOffice() throws Exception {
        // Initialize the database
        outOfOfficeService.save(outOfOffice);
        when(mockOutOfOfficeSearchRepository.search(queryStringQuery("id:" + outOfOffice.getId())))
            .thenReturn(Collections.singletonList(outOfOffice));
        // Search the outOfOffice
        restOutOfOfficeMockMvc.perform(get("/api/_search/out-of-offices?query=id:" + outOfOffice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outOfOffice.getId().intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutOfOffice.class);
        OutOfOffice outOfOffice1 = new OutOfOffice();
        outOfOffice1.setId(1L);
        OutOfOffice outOfOffice2 = new OutOfOffice();
        outOfOffice2.setId(outOfOffice1.getId());
        assertThat(outOfOffice1).isEqualTo(outOfOffice2);
        outOfOffice2.setId(2L);
        assertThat(outOfOffice1).isNotEqualTo(outOfOffice2);
        outOfOffice1.setId(null);
        assertThat(outOfOffice1).isNotEqualTo(outOfOffice2);
    }
}
