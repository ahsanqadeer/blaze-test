package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.Projects;
import com.venturedive.blazetest.domain.Sections;
import com.venturedive.blazetest.domain.TestCases;
import com.venturedive.blazetest.domain.TestSuites;
import com.venturedive.blazetest.repository.TestSuitesRepository;
import com.venturedive.blazetest.service.criteria.TestSuitesCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TestSuitesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestSuitesResourceIT {

    private static final String DEFAULT_TEST_SUITE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEST_SUITE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/test-suites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestSuitesRepository testSuitesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestSuitesMockMvc;

    private TestSuites testSuites;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestSuites createEntity(EntityManager em) {
        TestSuites testSuites = new TestSuites()
            .testSuiteName(DEFAULT_TEST_SUITE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT);
        return testSuites;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestSuites createUpdatedEntity(EntityManager em) {
        TestSuites testSuites = new TestSuites()
            .testSuiteName(UPDATED_TEST_SUITE_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);
        return testSuites;
    }

    @BeforeEach
    public void initTest() {
        testSuites = createEntity(em);
    }

    @Test
    @Transactional
    void createTestSuites() throws Exception {
        int databaseSizeBeforeCreate = testSuitesRepository.findAll().size();
        // Create the TestSuites
        restTestSuitesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testSuites)))
            .andExpect(status().isCreated());

        // Validate the TestSuites in the database
        List<TestSuites> testSuitesList = testSuitesRepository.findAll();
        assertThat(testSuitesList).hasSize(databaseSizeBeforeCreate + 1);
        TestSuites testTestSuites = testSuitesList.get(testSuitesList.size() - 1);
        assertThat(testTestSuites.getTestSuiteName()).isEqualTo(DEFAULT_TEST_SUITE_NAME);
        assertThat(testTestSuites.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTestSuites.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestSuites.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTestSuites.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testTestSuites.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createTestSuitesWithExistingId() throws Exception {
        // Create the TestSuites with an existing ID
        testSuites.setId(1L);

        int databaseSizeBeforeCreate = testSuitesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestSuitesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testSuites)))
            .andExpect(status().isBadRequest());

        // Validate the TestSuites in the database
        List<TestSuites> testSuitesList = testSuitesRepository.findAll();
        assertThat(testSuitesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestSuites() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList
        restTestSuitesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testSuites.getId().intValue())))
            .andExpect(jsonPath("$.[*].testSuiteName").value(hasItem(DEFAULT_TEST_SUITE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getTestSuites() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get the testSuites
        restTestSuitesMockMvc
            .perform(get(ENTITY_API_URL_ID, testSuites.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testSuites.getId().intValue()))
            .andExpect(jsonPath("$.testSuiteName").value(DEFAULT_TEST_SUITE_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getTestSuitesByIdFiltering() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        Long id = testSuites.getId();

        defaultTestSuitesShouldBeFound("id.equals=" + id);
        defaultTestSuitesShouldNotBeFound("id.notEquals=" + id);

        defaultTestSuitesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestSuitesShouldNotBeFound("id.greaterThan=" + id);

        defaultTestSuitesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestSuitesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestSuitesByTestSuiteNameIsEqualToSomething() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where testSuiteName equals to DEFAULT_TEST_SUITE_NAME
        defaultTestSuitesShouldBeFound("testSuiteName.equals=" + DEFAULT_TEST_SUITE_NAME);

        // Get all the testSuitesList where testSuiteName equals to UPDATED_TEST_SUITE_NAME
        defaultTestSuitesShouldNotBeFound("testSuiteName.equals=" + UPDATED_TEST_SUITE_NAME);
    }

    @Test
    @Transactional
    void getAllTestSuitesByTestSuiteNameIsInShouldWork() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where testSuiteName in DEFAULT_TEST_SUITE_NAME or UPDATED_TEST_SUITE_NAME
        defaultTestSuitesShouldBeFound("testSuiteName.in=" + DEFAULT_TEST_SUITE_NAME + "," + UPDATED_TEST_SUITE_NAME);

        // Get all the testSuitesList where testSuiteName equals to UPDATED_TEST_SUITE_NAME
        defaultTestSuitesShouldNotBeFound("testSuiteName.in=" + UPDATED_TEST_SUITE_NAME);
    }

    @Test
    @Transactional
    void getAllTestSuitesByTestSuiteNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where testSuiteName is not null
        defaultTestSuitesShouldBeFound("testSuiteName.specified=true");

        // Get all the testSuitesList where testSuiteName is null
        defaultTestSuitesShouldNotBeFound("testSuiteName.specified=false");
    }

    @Test
    @Transactional
    void getAllTestSuitesByTestSuiteNameContainsSomething() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where testSuiteName contains DEFAULT_TEST_SUITE_NAME
        defaultTestSuitesShouldBeFound("testSuiteName.contains=" + DEFAULT_TEST_SUITE_NAME);

        // Get all the testSuitesList where testSuiteName contains UPDATED_TEST_SUITE_NAME
        defaultTestSuitesShouldNotBeFound("testSuiteName.contains=" + UPDATED_TEST_SUITE_NAME);
    }

    @Test
    @Transactional
    void getAllTestSuitesByTestSuiteNameNotContainsSomething() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where testSuiteName does not contain DEFAULT_TEST_SUITE_NAME
        defaultTestSuitesShouldNotBeFound("testSuiteName.doesNotContain=" + DEFAULT_TEST_SUITE_NAME);

        // Get all the testSuitesList where testSuiteName does not contain UPDATED_TEST_SUITE_NAME
        defaultTestSuitesShouldBeFound("testSuiteName.doesNotContain=" + UPDATED_TEST_SUITE_NAME);
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where createdBy equals to DEFAULT_CREATED_BY
        defaultTestSuitesShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the testSuitesList where createdBy equals to UPDATED_CREATED_BY
        defaultTestSuitesShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultTestSuitesShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the testSuitesList where createdBy equals to UPDATED_CREATED_BY
        defaultTestSuitesShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where createdBy is not null
        defaultTestSuitesShouldBeFound("createdBy.specified=true");

        // Get all the testSuitesList where createdBy is null
        defaultTestSuitesShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultTestSuitesShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the testSuitesList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultTestSuitesShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultTestSuitesShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the testSuitesList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultTestSuitesShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where createdBy is less than DEFAULT_CREATED_BY
        defaultTestSuitesShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the testSuitesList where createdBy is less than UPDATED_CREATED_BY
        defaultTestSuitesShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where createdBy is greater than DEFAULT_CREATED_BY
        defaultTestSuitesShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the testSuitesList where createdBy is greater than SMALLER_CREATED_BY
        defaultTestSuitesShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where createdAt equals to DEFAULT_CREATED_AT
        defaultTestSuitesShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the testSuitesList where createdAt equals to UPDATED_CREATED_AT
        defaultTestSuitesShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultTestSuitesShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the testSuitesList where createdAt equals to UPDATED_CREATED_AT
        defaultTestSuitesShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTestSuitesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where createdAt is not null
        defaultTestSuitesShouldBeFound("createdAt.specified=true");

        // Get all the testSuitesList where createdAt is null
        defaultTestSuitesShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where updatedBy equals to DEFAULT_UPDATED_BY
        defaultTestSuitesShouldBeFound("updatedBy.equals=" + DEFAULT_UPDATED_BY);

        // Get all the testSuitesList where updatedBy equals to UPDATED_UPDATED_BY
        defaultTestSuitesShouldNotBeFound("updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where updatedBy in DEFAULT_UPDATED_BY or UPDATED_UPDATED_BY
        defaultTestSuitesShouldBeFound("updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY);

        // Get all the testSuitesList where updatedBy equals to UPDATED_UPDATED_BY
        defaultTestSuitesShouldNotBeFound("updatedBy.in=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where updatedBy is not null
        defaultTestSuitesShouldBeFound("updatedBy.specified=true");

        // Get all the testSuitesList where updatedBy is null
        defaultTestSuitesShouldNotBeFound("updatedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where updatedBy is greater than or equal to DEFAULT_UPDATED_BY
        defaultTestSuitesShouldBeFound("updatedBy.greaterThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the testSuitesList where updatedBy is greater than or equal to UPDATED_UPDATED_BY
        defaultTestSuitesShouldNotBeFound("updatedBy.greaterThanOrEqual=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where updatedBy is less than or equal to DEFAULT_UPDATED_BY
        defaultTestSuitesShouldBeFound("updatedBy.lessThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the testSuitesList where updatedBy is less than or equal to SMALLER_UPDATED_BY
        defaultTestSuitesShouldNotBeFound("updatedBy.lessThanOrEqual=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where updatedBy is less than DEFAULT_UPDATED_BY
        defaultTestSuitesShouldNotBeFound("updatedBy.lessThan=" + DEFAULT_UPDATED_BY);

        // Get all the testSuitesList where updatedBy is less than UPDATED_UPDATED_BY
        defaultTestSuitesShouldBeFound("updatedBy.lessThan=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where updatedBy is greater than DEFAULT_UPDATED_BY
        defaultTestSuitesShouldNotBeFound("updatedBy.greaterThan=" + DEFAULT_UPDATED_BY);

        // Get all the testSuitesList where updatedBy is greater than SMALLER_UPDATED_BY
        defaultTestSuitesShouldBeFound("updatedBy.greaterThan=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultTestSuitesShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the testSuitesList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTestSuitesShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultTestSuitesShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the testSuitesList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTestSuitesShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTestSuitesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        // Get all the testSuitesList where updatedAt is not null
        defaultTestSuitesShouldBeFound("updatedAt.specified=true");

        // Get all the testSuitesList where updatedAt is null
        defaultTestSuitesShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTestSuitesByProjectIsEqualToSomething() throws Exception {
        Projects project;
        if (TestUtil.findAll(em, Projects.class).isEmpty()) {
            testSuitesRepository.saveAndFlush(testSuites);
            project = ProjectsResourceIT.createEntity(em);
        } else {
            project = TestUtil.findAll(em, Projects.class).get(0);
        }
        em.persist(project);
        em.flush();
        testSuites.setProject(project);
        testSuitesRepository.saveAndFlush(testSuites);
        Long projectId = project.getId();

        // Get all the testSuitesList where project equals to projectId
        defaultTestSuitesShouldBeFound("projectId.equals=" + projectId);

        // Get all the testSuitesList where project equals to (projectId + 1)
        defaultTestSuitesShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    @Test
    @Transactional
    void getAllTestSuitesBySectionsTestsuiteIsEqualToSomething() throws Exception {
        Sections sectionsTestsuite;
        if (TestUtil.findAll(em, Sections.class).isEmpty()) {
            testSuitesRepository.saveAndFlush(testSuites);
            sectionsTestsuite = SectionsResourceIT.createEntity(em);
        } else {
            sectionsTestsuite = TestUtil.findAll(em, Sections.class).get(0);
        }
        em.persist(sectionsTestsuite);
        em.flush();
        testSuites.addSectionsTestsuite(sectionsTestsuite);
        testSuitesRepository.saveAndFlush(testSuites);
        Long sectionsTestsuiteId = sectionsTestsuite.getId();

        // Get all the testSuitesList where sectionsTestsuite equals to sectionsTestsuiteId
        defaultTestSuitesShouldBeFound("sectionsTestsuiteId.equals=" + sectionsTestsuiteId);

        // Get all the testSuitesList where sectionsTestsuite equals to (sectionsTestsuiteId + 1)
        defaultTestSuitesShouldNotBeFound("sectionsTestsuiteId.equals=" + (sectionsTestsuiteId + 1));
    }

    @Test
    @Transactional
    void getAllTestSuitesByTestcasesTestsuiteIsEqualToSomething() throws Exception {
        TestCases testcasesTestsuite;
        if (TestUtil.findAll(em, TestCases.class).isEmpty()) {
            testSuitesRepository.saveAndFlush(testSuites);
            testcasesTestsuite = TestCasesResourceIT.createEntity(em);
        } else {
            testcasesTestsuite = TestUtil.findAll(em, TestCases.class).get(0);
        }
        em.persist(testcasesTestsuite);
        em.flush();
        testSuites.addTestcasesTestsuite(testcasesTestsuite);
        testSuitesRepository.saveAndFlush(testSuites);
        Long testcasesTestsuiteId = testcasesTestsuite.getId();

        // Get all the testSuitesList where testcasesTestsuite equals to testcasesTestsuiteId
        defaultTestSuitesShouldBeFound("testcasesTestsuiteId.equals=" + testcasesTestsuiteId);

        // Get all the testSuitesList where testcasesTestsuite equals to (testcasesTestsuiteId + 1)
        defaultTestSuitesShouldNotBeFound("testcasesTestsuiteId.equals=" + (testcasesTestsuiteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestSuitesShouldBeFound(String filter) throws Exception {
        restTestSuitesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testSuites.getId().intValue())))
            .andExpect(jsonPath("$.[*].testSuiteName").value(hasItem(DEFAULT_TEST_SUITE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restTestSuitesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestSuitesShouldNotBeFound(String filter) throws Exception {
        restTestSuitesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestSuitesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestSuites() throws Exception {
        // Get the testSuites
        restTestSuitesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestSuites() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        int databaseSizeBeforeUpdate = testSuitesRepository.findAll().size();

        // Update the testSuites
        TestSuites updatedTestSuites = testSuitesRepository.findById(testSuites.getId()).get();
        // Disconnect from session so that the updates on updatedTestSuites are not directly saved in db
        em.detach(updatedTestSuites);
        updatedTestSuites
            .testSuiteName(UPDATED_TEST_SUITE_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restTestSuitesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestSuites.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestSuites))
            )
            .andExpect(status().isOk());

        // Validate the TestSuites in the database
        List<TestSuites> testSuitesList = testSuitesRepository.findAll();
        assertThat(testSuitesList).hasSize(databaseSizeBeforeUpdate);
        TestSuites testTestSuites = testSuitesList.get(testSuitesList.size() - 1);
        assertThat(testTestSuites.getTestSuiteName()).isEqualTo(UPDATED_TEST_SUITE_NAME);
        assertThat(testTestSuites.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestSuites.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestSuites.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestSuites.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTestSuites.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingTestSuites() throws Exception {
        int databaseSizeBeforeUpdate = testSuitesRepository.findAll().size();
        testSuites.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestSuitesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testSuites.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testSuites))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestSuites in the database
        List<TestSuites> testSuitesList = testSuitesRepository.findAll();
        assertThat(testSuitesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestSuites() throws Exception {
        int databaseSizeBeforeUpdate = testSuitesRepository.findAll().size();
        testSuites.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestSuitesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testSuites))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestSuites in the database
        List<TestSuites> testSuitesList = testSuitesRepository.findAll();
        assertThat(testSuitesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestSuites() throws Exception {
        int databaseSizeBeforeUpdate = testSuitesRepository.findAll().size();
        testSuites.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestSuitesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testSuites)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestSuites in the database
        List<TestSuites> testSuitesList = testSuitesRepository.findAll();
        assertThat(testSuitesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestSuitesWithPatch() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        int databaseSizeBeforeUpdate = testSuitesRepository.findAll().size();

        // Update the testSuites using partial update
        TestSuites partialUpdatedTestSuites = new TestSuites();
        partialUpdatedTestSuites.setId(testSuites.getId());

        partialUpdatedTestSuites.description(UPDATED_DESCRIPTION).createdBy(UPDATED_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);

        restTestSuitesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestSuites.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestSuites))
            )
            .andExpect(status().isOk());

        // Validate the TestSuites in the database
        List<TestSuites> testSuitesList = testSuitesRepository.findAll();
        assertThat(testSuitesList).hasSize(databaseSizeBeforeUpdate);
        TestSuites testTestSuites = testSuitesList.get(testSuitesList.size() - 1);
        assertThat(testTestSuites.getTestSuiteName()).isEqualTo(DEFAULT_TEST_SUITE_NAME);
        assertThat(testTestSuites.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestSuites.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestSuites.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTestSuites.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTestSuites.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateTestSuitesWithPatch() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        int databaseSizeBeforeUpdate = testSuitesRepository.findAll().size();

        // Update the testSuites using partial update
        TestSuites partialUpdatedTestSuites = new TestSuites();
        partialUpdatedTestSuites.setId(testSuites.getId());

        partialUpdatedTestSuites
            .testSuiteName(UPDATED_TEST_SUITE_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restTestSuitesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestSuites.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestSuites))
            )
            .andExpect(status().isOk());

        // Validate the TestSuites in the database
        List<TestSuites> testSuitesList = testSuitesRepository.findAll();
        assertThat(testSuitesList).hasSize(databaseSizeBeforeUpdate);
        TestSuites testTestSuites = testSuitesList.get(testSuitesList.size() - 1);
        assertThat(testTestSuites.getTestSuiteName()).isEqualTo(UPDATED_TEST_SUITE_NAME);
        assertThat(testTestSuites.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestSuites.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestSuites.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestSuites.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testTestSuites.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingTestSuites() throws Exception {
        int databaseSizeBeforeUpdate = testSuitesRepository.findAll().size();
        testSuites.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestSuitesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testSuites.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testSuites))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestSuites in the database
        List<TestSuites> testSuitesList = testSuitesRepository.findAll();
        assertThat(testSuitesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestSuites() throws Exception {
        int databaseSizeBeforeUpdate = testSuitesRepository.findAll().size();
        testSuites.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestSuitesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testSuites))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestSuites in the database
        List<TestSuites> testSuitesList = testSuitesRepository.findAll();
        assertThat(testSuitesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestSuites() throws Exception {
        int databaseSizeBeforeUpdate = testSuitesRepository.findAll().size();
        testSuites.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestSuitesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testSuites))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestSuites in the database
        List<TestSuites> testSuitesList = testSuitesRepository.findAll();
        assertThat(testSuitesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestSuites() throws Exception {
        // Initialize the database
        testSuitesRepository.saveAndFlush(testSuites);

        int databaseSizeBeforeDelete = testSuitesRepository.findAll().size();

        // Delete the testSuites
        restTestSuitesMockMvc
            .perform(delete(ENTITY_API_URL_ID, testSuites.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestSuites> testSuitesList = testSuitesRepository.findAll();
        assertThat(testSuitesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
