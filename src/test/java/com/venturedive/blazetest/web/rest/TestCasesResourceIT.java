package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.Milestones;
import com.venturedive.blazetest.domain.Sections;
import com.venturedive.blazetest.domain.Templates;
import com.venturedive.blazetest.domain.TestCaseAttachments;
import com.venturedive.blazetest.domain.TestCaseFields;
import com.venturedive.blazetest.domain.TestCasePriorities;
import com.venturedive.blazetest.domain.TestCases;
import com.venturedive.blazetest.domain.TestLevels;
import com.venturedive.blazetest.domain.TestRunDetails;
import com.venturedive.blazetest.domain.TestSuites;
import com.venturedive.blazetest.repository.TestCasesRepository;
import com.venturedive.blazetest.service.TestCasesService;
import com.venturedive.blazetest.service.criteria.TestCasesCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TestCasesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TestCasesResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ESTIMATE = "AAAAAAAAAA";
    private static final String UPDATED_ESTIMATE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;
    private static final Integer SMALLER_CREATED_BY = 1 - 1;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_UPDATED_BY = 1;
    private static final Integer UPDATED_UPDATED_BY = 2;
    private static final Integer SMALLER_UPDATED_BY = 1 - 1;

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PRECONDITION = "AAAAAAAAAA";
    private static final String UPDATED_PRECONDITION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_AUTOMATED = false;
    private static final Boolean UPDATED_IS_AUTOMATED = true;

    private static final String ENTITY_API_URL = "/api/test-cases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestCasesRepository testCasesRepository;

    @Mock
    private TestCasesRepository testCasesRepositoryMock;

    @Mock
    private TestCasesService testCasesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestCasesMockMvc;

    private TestCases testCases;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCases createEntity(EntityManager em) {
        TestCases testCases = new TestCases()
            .title(DEFAULT_TITLE)
            .estimate(DEFAULT_ESTIMATE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT)
            .precondition(DEFAULT_PRECONDITION)
            .description(DEFAULT_DESCRIPTION)
            .isAutomated(DEFAULT_IS_AUTOMATED);
        // Add required entity
        TestCasePriorities testCasePriorities;
        if (TestUtil.findAll(em, TestCasePriorities.class).isEmpty()) {
            testCasePriorities = TestCasePrioritiesResourceIT.createEntity(em);
            em.persist(testCasePriorities);
            em.flush();
        } else {
            testCasePriorities = TestUtil.findAll(em, TestCasePriorities.class).get(0);
        }
        testCases.setPriority(testCasePriorities);
        return testCases;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCases createUpdatedEntity(EntityManager em) {
        TestCases testCases = new TestCases()
            .title(UPDATED_TITLE)
            .estimate(UPDATED_ESTIMATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .precondition(UPDATED_PRECONDITION)
            .description(UPDATED_DESCRIPTION)
            .isAutomated(UPDATED_IS_AUTOMATED);
        // Add required entity
        TestCasePriorities testCasePriorities;
        if (TestUtil.findAll(em, TestCasePriorities.class).isEmpty()) {
            testCasePriorities = TestCasePrioritiesResourceIT.createUpdatedEntity(em);
            em.persist(testCasePriorities);
            em.flush();
        } else {
            testCasePriorities = TestUtil.findAll(em, TestCasePriorities.class).get(0);
        }
        testCases.setPriority(testCasePriorities);
        return testCases;
    }

    @BeforeEach
    public void initTest() {
        testCases = createEntity(em);
    }

    @Test
    @Transactional
    void createTestCases() throws Exception {
        int databaseSizeBeforeCreate = testCasesRepository.findAll().size();
        // Create the TestCases
        restTestCasesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCases)))
            .andExpect(status().isCreated());

        // Validate the TestCases in the database
        List<TestCases> testCasesList = testCasesRepository.findAll();
        assertThat(testCasesList).hasSize(databaseSizeBeforeCreate + 1);
        TestCases testTestCases = testCasesList.get(testCasesList.size() - 1);
        assertThat(testTestCases.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTestCases.getEstimate()).isEqualTo(DEFAULT_ESTIMATE);
        assertThat(testTestCases.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestCases.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTestCases.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testTestCases.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testTestCases.getPrecondition()).isEqualTo(DEFAULT_PRECONDITION);
        assertThat(testTestCases.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTestCases.getIsAutomated()).isEqualTo(DEFAULT_IS_AUTOMATED);
    }

    @Test
    @Transactional
    void createTestCasesWithExistingId() throws Exception {
        // Create the TestCases with an existing ID
        testCases.setId(1L);

        int databaseSizeBeforeCreate = testCasesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCasesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCases)))
            .andExpect(status().isBadRequest());

        // Validate the TestCases in the database
        List<TestCases> testCasesList = testCasesRepository.findAll();
        assertThat(testCasesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestCases() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList
        restTestCasesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCases.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].estimate").value(hasItem(DEFAULT_ESTIMATE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].precondition").value(hasItem(DEFAULT_PRECONDITION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].isAutomated").value(hasItem(DEFAULT_IS_AUTOMATED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTestCasesWithEagerRelationshipsIsEnabled() throws Exception {
        when(testCasesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTestCasesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(testCasesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTestCasesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(testCasesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTestCasesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(testCasesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTestCases() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get the testCases
        restTestCasesMockMvc
            .perform(get(ENTITY_API_URL_ID, testCases.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testCases.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.estimate").value(DEFAULT_ESTIMATE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.precondition").value(DEFAULT_PRECONDITION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.isAutomated").value(DEFAULT_IS_AUTOMATED.booleanValue()));
    }

    @Test
    @Transactional
    void getTestCasesByIdFiltering() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        Long id = testCases.getId();

        defaultTestCasesShouldBeFound("id.equals=" + id);
        defaultTestCasesShouldNotBeFound("id.notEquals=" + id);

        defaultTestCasesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestCasesShouldNotBeFound("id.greaterThan=" + id);

        defaultTestCasesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestCasesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestCasesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where title equals to DEFAULT_TITLE
        defaultTestCasesShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the testCasesList where title equals to UPDATED_TITLE
        defaultTestCasesShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTestCasesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultTestCasesShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the testCasesList where title equals to UPDATED_TITLE
        defaultTestCasesShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTestCasesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where title is not null
        defaultTestCasesShouldBeFound("title.specified=true");

        // Get all the testCasesList where title is null
        defaultTestCasesShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasesByTitleContainsSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where title contains DEFAULT_TITLE
        defaultTestCasesShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the testCasesList where title contains UPDATED_TITLE
        defaultTestCasesShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTestCasesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where title does not contain DEFAULT_TITLE
        defaultTestCasesShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the testCasesList where title does not contain UPDATED_TITLE
        defaultTestCasesShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTestCasesByEstimateIsEqualToSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where estimate equals to DEFAULT_ESTIMATE
        defaultTestCasesShouldBeFound("estimate.equals=" + DEFAULT_ESTIMATE);

        // Get all the testCasesList where estimate equals to UPDATED_ESTIMATE
        defaultTestCasesShouldNotBeFound("estimate.equals=" + UPDATED_ESTIMATE);
    }

    @Test
    @Transactional
    void getAllTestCasesByEstimateIsInShouldWork() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where estimate in DEFAULT_ESTIMATE or UPDATED_ESTIMATE
        defaultTestCasesShouldBeFound("estimate.in=" + DEFAULT_ESTIMATE + "," + UPDATED_ESTIMATE);

        // Get all the testCasesList where estimate equals to UPDATED_ESTIMATE
        defaultTestCasesShouldNotBeFound("estimate.in=" + UPDATED_ESTIMATE);
    }

    @Test
    @Transactional
    void getAllTestCasesByEstimateIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where estimate is not null
        defaultTestCasesShouldBeFound("estimate.specified=true");

        // Get all the testCasesList where estimate is null
        defaultTestCasesShouldNotBeFound("estimate.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasesByEstimateContainsSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where estimate contains DEFAULT_ESTIMATE
        defaultTestCasesShouldBeFound("estimate.contains=" + DEFAULT_ESTIMATE);

        // Get all the testCasesList where estimate contains UPDATED_ESTIMATE
        defaultTestCasesShouldNotBeFound("estimate.contains=" + UPDATED_ESTIMATE);
    }

    @Test
    @Transactional
    void getAllTestCasesByEstimateNotContainsSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where estimate does not contain DEFAULT_ESTIMATE
        defaultTestCasesShouldNotBeFound("estimate.doesNotContain=" + DEFAULT_ESTIMATE);

        // Get all the testCasesList where estimate does not contain UPDATED_ESTIMATE
        defaultTestCasesShouldBeFound("estimate.doesNotContain=" + UPDATED_ESTIMATE);
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where createdBy equals to DEFAULT_CREATED_BY
        defaultTestCasesShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the testCasesList where createdBy equals to UPDATED_CREATED_BY
        defaultTestCasesShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultTestCasesShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the testCasesList where createdBy equals to UPDATED_CREATED_BY
        defaultTestCasesShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where createdBy is not null
        defaultTestCasesShouldBeFound("createdBy.specified=true");

        // Get all the testCasesList where createdBy is null
        defaultTestCasesShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultTestCasesShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the testCasesList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultTestCasesShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultTestCasesShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the testCasesList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultTestCasesShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where createdBy is less than DEFAULT_CREATED_BY
        defaultTestCasesShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the testCasesList where createdBy is less than UPDATED_CREATED_BY
        defaultTestCasesShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where createdBy is greater than DEFAULT_CREATED_BY
        defaultTestCasesShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the testCasesList where createdBy is greater than SMALLER_CREATED_BY
        defaultTestCasesShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where createdAt equals to DEFAULT_CREATED_AT
        defaultTestCasesShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the testCasesList where createdAt equals to UPDATED_CREATED_AT
        defaultTestCasesShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultTestCasesShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the testCasesList where createdAt equals to UPDATED_CREATED_AT
        defaultTestCasesShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTestCasesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where createdAt is not null
        defaultTestCasesShouldBeFound("createdAt.specified=true");

        // Get all the testCasesList where createdAt is null
        defaultTestCasesShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where updatedBy equals to DEFAULT_UPDATED_BY
        defaultTestCasesShouldBeFound("updatedBy.equals=" + DEFAULT_UPDATED_BY);

        // Get all the testCasesList where updatedBy equals to UPDATED_UPDATED_BY
        defaultTestCasesShouldNotBeFound("updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where updatedBy in DEFAULT_UPDATED_BY or UPDATED_UPDATED_BY
        defaultTestCasesShouldBeFound("updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY);

        // Get all the testCasesList where updatedBy equals to UPDATED_UPDATED_BY
        defaultTestCasesShouldNotBeFound("updatedBy.in=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where updatedBy is not null
        defaultTestCasesShouldBeFound("updatedBy.specified=true");

        // Get all the testCasesList where updatedBy is null
        defaultTestCasesShouldNotBeFound("updatedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where updatedBy is greater than or equal to DEFAULT_UPDATED_BY
        defaultTestCasesShouldBeFound("updatedBy.greaterThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the testCasesList where updatedBy is greater than or equal to UPDATED_UPDATED_BY
        defaultTestCasesShouldNotBeFound("updatedBy.greaterThanOrEqual=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where updatedBy is less than or equal to DEFAULT_UPDATED_BY
        defaultTestCasesShouldBeFound("updatedBy.lessThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the testCasesList where updatedBy is less than or equal to SMALLER_UPDATED_BY
        defaultTestCasesShouldNotBeFound("updatedBy.lessThanOrEqual=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where updatedBy is less than DEFAULT_UPDATED_BY
        defaultTestCasesShouldNotBeFound("updatedBy.lessThan=" + DEFAULT_UPDATED_BY);

        // Get all the testCasesList where updatedBy is less than UPDATED_UPDATED_BY
        defaultTestCasesShouldBeFound("updatedBy.lessThan=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where updatedBy is greater than DEFAULT_UPDATED_BY
        defaultTestCasesShouldNotBeFound("updatedBy.greaterThan=" + DEFAULT_UPDATED_BY);

        // Get all the testCasesList where updatedBy is greater than SMALLER_UPDATED_BY
        defaultTestCasesShouldBeFound("updatedBy.greaterThan=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultTestCasesShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the testCasesList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTestCasesShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultTestCasesShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the testCasesList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTestCasesShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTestCasesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where updatedAt is not null
        defaultTestCasesShouldBeFound("updatedAt.specified=true");

        // Get all the testCasesList where updatedAt is null
        defaultTestCasesShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasesByPreconditionIsEqualToSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where precondition equals to DEFAULT_PRECONDITION
        defaultTestCasesShouldBeFound("precondition.equals=" + DEFAULT_PRECONDITION);

        // Get all the testCasesList where precondition equals to UPDATED_PRECONDITION
        defaultTestCasesShouldNotBeFound("precondition.equals=" + UPDATED_PRECONDITION);
    }

    @Test
    @Transactional
    void getAllTestCasesByPreconditionIsInShouldWork() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where precondition in DEFAULT_PRECONDITION or UPDATED_PRECONDITION
        defaultTestCasesShouldBeFound("precondition.in=" + DEFAULT_PRECONDITION + "," + UPDATED_PRECONDITION);

        // Get all the testCasesList where precondition equals to UPDATED_PRECONDITION
        defaultTestCasesShouldNotBeFound("precondition.in=" + UPDATED_PRECONDITION);
    }

    @Test
    @Transactional
    void getAllTestCasesByPreconditionIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where precondition is not null
        defaultTestCasesShouldBeFound("precondition.specified=true");

        // Get all the testCasesList where precondition is null
        defaultTestCasesShouldNotBeFound("precondition.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasesByPreconditionContainsSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where precondition contains DEFAULT_PRECONDITION
        defaultTestCasesShouldBeFound("precondition.contains=" + DEFAULT_PRECONDITION);

        // Get all the testCasesList where precondition contains UPDATED_PRECONDITION
        defaultTestCasesShouldNotBeFound("precondition.contains=" + UPDATED_PRECONDITION);
    }

    @Test
    @Transactional
    void getAllTestCasesByPreconditionNotContainsSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where precondition does not contain DEFAULT_PRECONDITION
        defaultTestCasesShouldNotBeFound("precondition.doesNotContain=" + DEFAULT_PRECONDITION);

        // Get all the testCasesList where precondition does not contain UPDATED_PRECONDITION
        defaultTestCasesShouldBeFound("precondition.doesNotContain=" + UPDATED_PRECONDITION);
    }

    @Test
    @Transactional
    void getAllTestCasesByIsAutomatedIsEqualToSomething() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where isAutomated equals to DEFAULT_IS_AUTOMATED
        defaultTestCasesShouldBeFound("isAutomated.equals=" + DEFAULT_IS_AUTOMATED);

        // Get all the testCasesList where isAutomated equals to UPDATED_IS_AUTOMATED
        defaultTestCasesShouldNotBeFound("isAutomated.equals=" + UPDATED_IS_AUTOMATED);
    }

    @Test
    @Transactional
    void getAllTestCasesByIsAutomatedIsInShouldWork() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where isAutomated in DEFAULT_IS_AUTOMATED or UPDATED_IS_AUTOMATED
        defaultTestCasesShouldBeFound("isAutomated.in=" + DEFAULT_IS_AUTOMATED + "," + UPDATED_IS_AUTOMATED);

        // Get all the testCasesList where isAutomated equals to UPDATED_IS_AUTOMATED
        defaultTestCasesShouldNotBeFound("isAutomated.in=" + UPDATED_IS_AUTOMATED);
    }

    @Test
    @Transactional
    void getAllTestCasesByIsAutomatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        // Get all the testCasesList where isAutomated is not null
        defaultTestCasesShouldBeFound("isAutomated.specified=true");

        // Get all the testCasesList where isAutomated is null
        defaultTestCasesShouldNotBeFound("isAutomated.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasesByTestSuiteIsEqualToSomething() throws Exception {
        TestSuites testSuite;
        if (TestUtil.findAll(em, TestSuites.class).isEmpty()) {
            testCasesRepository.saveAndFlush(testCases);
            testSuite = TestSuitesResourceIT.createEntity(em);
        } else {
            testSuite = TestUtil.findAll(em, TestSuites.class).get(0);
        }
        em.persist(testSuite);
        em.flush();
        testCases.setTestSuite(testSuite);
        testCasesRepository.saveAndFlush(testCases);
        Long testSuiteId = testSuite.getId();

        // Get all the testCasesList where testSuite equals to testSuiteId
        defaultTestCasesShouldBeFound("testSuiteId.equals=" + testSuiteId);

        // Get all the testCasesList where testSuite equals to (testSuiteId + 1)
        defaultTestCasesShouldNotBeFound("testSuiteId.equals=" + (testSuiteId + 1));
    }

    @Test
    @Transactional
    void getAllTestCasesBySectionIsEqualToSomething() throws Exception {
        Sections section;
        if (TestUtil.findAll(em, Sections.class).isEmpty()) {
            testCasesRepository.saveAndFlush(testCases);
            section = SectionsResourceIT.createEntity(em);
        } else {
            section = TestUtil.findAll(em, Sections.class).get(0);
        }
        em.persist(section);
        em.flush();
        testCases.setSection(section);
        testCasesRepository.saveAndFlush(testCases);
        Long sectionId = section.getId();

        // Get all the testCasesList where section equals to sectionId
        defaultTestCasesShouldBeFound("sectionId.equals=" + sectionId);

        // Get all the testCasesList where section equals to (sectionId + 1)
        defaultTestCasesShouldNotBeFound("sectionId.equals=" + (sectionId + 1));
    }

    @Test
    @Transactional
    void getAllTestCasesByPriorityIsEqualToSomething() throws Exception {
        TestCasePriorities priority;
        if (TestUtil.findAll(em, TestCasePriorities.class).isEmpty()) {
            testCasesRepository.saveAndFlush(testCases);
            priority = TestCasePrioritiesResourceIT.createEntity(em);
        } else {
            priority = TestUtil.findAll(em, TestCasePriorities.class).get(0);
        }
        em.persist(priority);
        em.flush();
        testCases.setPriority(priority);
        testCasesRepository.saveAndFlush(testCases);
        Long priorityId = priority.getId();

        // Get all the testCasesList where priority equals to priorityId
        defaultTestCasesShouldBeFound("priorityId.equals=" + priorityId);

        // Get all the testCasesList where priority equals to (priorityId + 1)
        defaultTestCasesShouldNotBeFound("priorityId.equals=" + (priorityId + 1));
    }

    @Test
    @Transactional
    void getAllTestCasesByTemplateIsEqualToSomething() throws Exception {
        Templates template;
        if (TestUtil.findAll(em, Templates.class).isEmpty()) {
            testCasesRepository.saveAndFlush(testCases);
            template = TemplatesResourceIT.createEntity(em);
        } else {
            template = TestUtil.findAll(em, Templates.class).get(0);
        }
        em.persist(template);
        em.flush();
        testCases.setTemplate(template);
        testCasesRepository.saveAndFlush(testCases);
        Long templateId = template.getId();

        // Get all the testCasesList where template equals to templateId
        defaultTestCasesShouldBeFound("templateId.equals=" + templateId);

        // Get all the testCasesList where template equals to (templateId + 1)
        defaultTestCasesShouldNotBeFound("templateId.equals=" + (templateId + 1));
    }

    @Test
    @Transactional
    void getAllTestCasesByMilestoneIsEqualToSomething() throws Exception {
        Milestones milestone;
        if (TestUtil.findAll(em, Milestones.class).isEmpty()) {
            testCasesRepository.saveAndFlush(testCases);
            milestone = MilestonesResourceIT.createEntity(em);
        } else {
            milestone = TestUtil.findAll(em, Milestones.class).get(0);
        }
        em.persist(milestone);
        em.flush();
        testCases.setMilestone(milestone);
        testCasesRepository.saveAndFlush(testCases);
        Long milestoneId = milestone.getId();

        // Get all the testCasesList where milestone equals to milestoneId
        defaultTestCasesShouldBeFound("milestoneId.equals=" + milestoneId);

        // Get all the testCasesList where milestone equals to (milestoneId + 1)
        defaultTestCasesShouldNotBeFound("milestoneId.equals=" + (milestoneId + 1));
    }

    @Test
    @Transactional
    void getAllTestCasesByTestLevelIsEqualToSomething() throws Exception {
        TestLevels testLevel;
        if (TestUtil.findAll(em, TestLevels.class).isEmpty()) {
            testCasesRepository.saveAndFlush(testCases);
            testLevel = TestLevelsResourceIT.createEntity(em);
        } else {
            testLevel = TestUtil.findAll(em, TestLevels.class).get(0);
        }
        em.persist(testLevel);
        em.flush();
        testCases.addTestLevel(testLevel);
        testCasesRepository.saveAndFlush(testCases);
        Long testLevelId = testLevel.getId();

        // Get all the testCasesList where testLevel equals to testLevelId
        defaultTestCasesShouldBeFound("testLevelId.equals=" + testLevelId);

        // Get all the testCasesList where testLevel equals to (testLevelId + 1)
        defaultTestCasesShouldNotBeFound("testLevelId.equals=" + (testLevelId + 1));
    }

    @Test
    @Transactional
    void getAllTestCasesByTestcaseattachmentsTestcaseIsEqualToSomething() throws Exception {
        TestCaseAttachments testcaseattachmentsTestcase;
        if (TestUtil.findAll(em, TestCaseAttachments.class).isEmpty()) {
            testCasesRepository.saveAndFlush(testCases);
            testcaseattachmentsTestcase = TestCaseAttachmentsResourceIT.createEntity(em);
        } else {
            testcaseattachmentsTestcase = TestUtil.findAll(em, TestCaseAttachments.class).get(0);
        }
        em.persist(testcaseattachmentsTestcase);
        em.flush();
        testCases.addTestcaseattachmentsTestcase(testcaseattachmentsTestcase);
        testCasesRepository.saveAndFlush(testCases);
        Long testcaseattachmentsTestcaseId = testcaseattachmentsTestcase.getId();

        // Get all the testCasesList where testcaseattachmentsTestcase equals to testcaseattachmentsTestcaseId
        defaultTestCasesShouldBeFound("testcaseattachmentsTestcaseId.equals=" + testcaseattachmentsTestcaseId);

        // Get all the testCasesList where testcaseattachmentsTestcase equals to (testcaseattachmentsTestcaseId + 1)
        defaultTestCasesShouldNotBeFound("testcaseattachmentsTestcaseId.equals=" + (testcaseattachmentsTestcaseId + 1));
    }

    @Test
    @Transactional
    void getAllTestCasesByTestcasefieldsTestcaseIsEqualToSomething() throws Exception {
        TestCaseFields testcasefieldsTestcase;
        if (TestUtil.findAll(em, TestCaseFields.class).isEmpty()) {
            testCasesRepository.saveAndFlush(testCases);
            testcasefieldsTestcase = TestCaseFieldsResourceIT.createEntity(em);
        } else {
            testcasefieldsTestcase = TestUtil.findAll(em, TestCaseFields.class).get(0);
        }
        em.persist(testcasefieldsTestcase);
        em.flush();
        testCases.addTestcasefieldsTestcase(testcasefieldsTestcase);
        testCasesRepository.saveAndFlush(testCases);
        Long testcasefieldsTestcaseId = testcasefieldsTestcase.getId();

        // Get all the testCasesList where testcasefieldsTestcase equals to testcasefieldsTestcaseId
        defaultTestCasesShouldBeFound("testcasefieldsTestcaseId.equals=" + testcasefieldsTestcaseId);

        // Get all the testCasesList where testcasefieldsTestcase equals to (testcasefieldsTestcaseId + 1)
        defaultTestCasesShouldNotBeFound("testcasefieldsTestcaseId.equals=" + (testcasefieldsTestcaseId + 1));
    }

    @Test
    @Transactional
    void getAllTestCasesByTestrundetailsTestcaseIsEqualToSomething() throws Exception {
        TestRunDetails testrundetailsTestcase;
        if (TestUtil.findAll(em, TestRunDetails.class).isEmpty()) {
            testCasesRepository.saveAndFlush(testCases);
            testrundetailsTestcase = TestRunDetailsResourceIT.createEntity(em);
        } else {
            testrundetailsTestcase = TestUtil.findAll(em, TestRunDetails.class).get(0);
        }
        em.persist(testrundetailsTestcase);
        em.flush();
        testCases.addTestrundetailsTestcase(testrundetailsTestcase);
        testCasesRepository.saveAndFlush(testCases);
        Long testrundetailsTestcaseId = testrundetailsTestcase.getId();

        // Get all the testCasesList where testrundetailsTestcase equals to testrundetailsTestcaseId
        defaultTestCasesShouldBeFound("testrundetailsTestcaseId.equals=" + testrundetailsTestcaseId);

        // Get all the testCasesList where testrundetailsTestcase equals to (testrundetailsTestcaseId + 1)
        defaultTestCasesShouldNotBeFound("testrundetailsTestcaseId.equals=" + (testrundetailsTestcaseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestCasesShouldBeFound(String filter) throws Exception {
        restTestCasesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCases.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].estimate").value(hasItem(DEFAULT_ESTIMATE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].precondition").value(hasItem(DEFAULT_PRECONDITION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].isAutomated").value(hasItem(DEFAULT_IS_AUTOMATED.booleanValue())));

        // Check, that the count call also returns 1
        restTestCasesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestCasesShouldNotBeFound(String filter) throws Exception {
        restTestCasesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestCasesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestCases() throws Exception {
        // Get the testCases
        restTestCasesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestCases() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        int databaseSizeBeforeUpdate = testCasesRepository.findAll().size();

        // Update the testCases
        TestCases updatedTestCases = testCasesRepository.findById(testCases.getId()).get();
        // Disconnect from session so that the updates on updatedTestCases are not directly saved in db
        em.detach(updatedTestCases);
        updatedTestCases
            .title(UPDATED_TITLE)
            .estimate(UPDATED_ESTIMATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .precondition(UPDATED_PRECONDITION)
            .description(UPDATED_DESCRIPTION)
            .isAutomated(UPDATED_IS_AUTOMATED);

        restTestCasesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestCases.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestCases))
            )
            .andExpect(status().isOk());

        // Validate the TestCases in the database
        List<TestCases> testCasesList = testCasesRepository.findAll();
        assertThat(testCasesList).hasSize(databaseSizeBeforeUpdate);
        TestCases testTestCases = testCasesList.get(testCasesList.size() - 1);
        assertThat(testTestCases.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTestCases.getEstimate()).isEqualTo(UPDATED_ESTIMATE);
        assertThat(testTestCases.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestCases.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestCases.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTestCases.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testTestCases.getPrecondition()).isEqualTo(UPDATED_PRECONDITION);
        assertThat(testTestCases.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestCases.getIsAutomated()).isEqualTo(UPDATED_IS_AUTOMATED);
    }

    @Test
    @Transactional
    void putNonExistingTestCases() throws Exception {
        int databaseSizeBeforeUpdate = testCasesRepository.findAll().size();
        testCases.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCasesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCases.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCases))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCases in the database
        List<TestCases> testCasesList = testCasesRepository.findAll();
        assertThat(testCasesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestCases() throws Exception {
        int databaseSizeBeforeUpdate = testCasesRepository.findAll().size();
        testCases.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCasesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCases))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCases in the database
        List<TestCases> testCasesList = testCasesRepository.findAll();
        assertThat(testCasesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestCases() throws Exception {
        int databaseSizeBeforeUpdate = testCasesRepository.findAll().size();
        testCases.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCasesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCases)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCases in the database
        List<TestCases> testCasesList = testCasesRepository.findAll();
        assertThat(testCasesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestCasesWithPatch() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        int databaseSizeBeforeUpdate = testCasesRepository.findAll().size();

        // Update the testCases using partial update
        TestCases partialUpdatedTestCases = new TestCases();
        partialUpdatedTestCases.setId(testCases.getId());

        partialUpdatedTestCases
            .title(UPDATED_TITLE)
            .estimate(UPDATED_ESTIMATE)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .precondition(UPDATED_PRECONDITION)
            .isAutomated(UPDATED_IS_AUTOMATED);

        restTestCasesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCases.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCases))
            )
            .andExpect(status().isOk());

        // Validate the TestCases in the database
        List<TestCases> testCasesList = testCasesRepository.findAll();
        assertThat(testCasesList).hasSize(databaseSizeBeforeUpdate);
        TestCases testTestCases = testCasesList.get(testCasesList.size() - 1);
        assertThat(testTestCases.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTestCases.getEstimate()).isEqualTo(UPDATED_ESTIMATE);
        assertThat(testTestCases.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestCases.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTestCases.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testTestCases.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testTestCases.getPrecondition()).isEqualTo(UPDATED_PRECONDITION);
        assertThat(testTestCases.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTestCases.getIsAutomated()).isEqualTo(UPDATED_IS_AUTOMATED);
    }

    @Test
    @Transactional
    void fullUpdateTestCasesWithPatch() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        int databaseSizeBeforeUpdate = testCasesRepository.findAll().size();

        // Update the testCases using partial update
        TestCases partialUpdatedTestCases = new TestCases();
        partialUpdatedTestCases.setId(testCases.getId());

        partialUpdatedTestCases
            .title(UPDATED_TITLE)
            .estimate(UPDATED_ESTIMATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .precondition(UPDATED_PRECONDITION)
            .description(UPDATED_DESCRIPTION)
            .isAutomated(UPDATED_IS_AUTOMATED);

        restTestCasesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCases.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCases))
            )
            .andExpect(status().isOk());

        // Validate the TestCases in the database
        List<TestCases> testCasesList = testCasesRepository.findAll();
        assertThat(testCasesList).hasSize(databaseSizeBeforeUpdate);
        TestCases testTestCases = testCasesList.get(testCasesList.size() - 1);
        assertThat(testTestCases.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTestCases.getEstimate()).isEqualTo(UPDATED_ESTIMATE);
        assertThat(testTestCases.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestCases.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestCases.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTestCases.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testTestCases.getPrecondition()).isEqualTo(UPDATED_PRECONDITION);
        assertThat(testTestCases.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestCases.getIsAutomated()).isEqualTo(UPDATED_IS_AUTOMATED);
    }

    @Test
    @Transactional
    void patchNonExistingTestCases() throws Exception {
        int databaseSizeBeforeUpdate = testCasesRepository.findAll().size();
        testCases.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCasesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testCases.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCases))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCases in the database
        List<TestCases> testCasesList = testCasesRepository.findAll();
        assertThat(testCasesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestCases() throws Exception {
        int databaseSizeBeforeUpdate = testCasesRepository.findAll().size();
        testCases.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCasesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCases))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCases in the database
        List<TestCases> testCasesList = testCasesRepository.findAll();
        assertThat(testCasesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestCases() throws Exception {
        int databaseSizeBeforeUpdate = testCasesRepository.findAll().size();
        testCases.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCasesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testCases))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCases in the database
        List<TestCases> testCasesList = testCasesRepository.findAll();
        assertThat(testCasesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestCases() throws Exception {
        // Initialize the database
        testCasesRepository.saveAndFlush(testCases);

        int databaseSizeBeforeDelete = testCasesRepository.findAll().size();

        // Delete the testCases
        restTestCasesMockMvc
            .perform(delete(ENTITY_API_URL_ID, testCases.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestCases> testCasesList = testCasesRepository.findAll();
        assertThat(testCasesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
