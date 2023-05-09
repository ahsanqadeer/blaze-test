package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.Milestones;
import com.venturedive.blazetest.domain.TestLevels;
import com.venturedive.blazetest.domain.TestRunDetails;
import com.venturedive.blazetest.domain.TestRuns;
import com.venturedive.blazetest.repository.TestRunsRepository;
import com.venturedive.blazetest.service.criteria.TestRunsCriteria;
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

/**
 * Integration tests for the {@link TestRunsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestRunsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;
    private static final Integer SMALLER_CREATED_BY = 1 - 1;

    private static final String ENTITY_API_URL = "/api/test-runs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestRunsRepository testRunsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestRunsMockMvc;

    private TestRuns testRuns;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestRuns createEntity(EntityManager em) {
        TestRuns testRuns = new TestRuns()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .createdBy(DEFAULT_CREATED_BY);
        return testRuns;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestRuns createUpdatedEntity(EntityManager em) {
        TestRuns testRuns = new TestRuns()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY);
        return testRuns;
    }

    @BeforeEach
    public void initTest() {
        testRuns = createEntity(em);
    }

    @Test
    @Transactional
    void createTestRuns() throws Exception {
        int databaseSizeBeforeCreate = testRunsRepository.findAll().size();
        // Create the TestRuns
        restTestRunsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testRuns)))
            .andExpect(status().isCreated());

        // Validate the TestRuns in the database
        List<TestRuns> testRunsList = testRunsRepository.findAll();
        assertThat(testRunsList).hasSize(databaseSizeBeforeCreate + 1);
        TestRuns testTestRuns = testRunsList.get(testRunsList.size() - 1);
        assertThat(testTestRuns.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTestRuns.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTestRuns.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTestRuns.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void createTestRunsWithExistingId() throws Exception {
        // Create the TestRuns with an existing ID
        testRuns.setId(1L);

        int databaseSizeBeforeCreate = testRunsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestRunsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testRuns)))
            .andExpect(status().isBadRequest());

        // Validate the TestRuns in the database
        List<TestRuns> testRunsList = testRunsRepository.findAll();
        assertThat(testRunsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestRuns() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList
        restTestRunsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testRuns.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getTestRuns() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get the testRuns
        restTestRunsMockMvc
            .perform(get(ENTITY_API_URL_ID, testRuns.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testRuns.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getTestRunsByIdFiltering() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        Long id = testRuns.getId();

        defaultTestRunsShouldBeFound("id.equals=" + id);
        defaultTestRunsShouldNotBeFound("id.notEquals=" + id);

        defaultTestRunsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestRunsShouldNotBeFound("id.greaterThan=" + id);

        defaultTestRunsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestRunsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestRunsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where name equals to DEFAULT_NAME
        defaultTestRunsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the testRunsList where name equals to UPDATED_NAME
        defaultTestRunsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestRunsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTestRunsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the testRunsList where name equals to UPDATED_NAME
        defaultTestRunsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestRunsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where name is not null
        defaultTestRunsShouldBeFound("name.specified=true");

        // Get all the testRunsList where name is null
        defaultTestRunsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTestRunsByNameContainsSomething() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where name contains DEFAULT_NAME
        defaultTestRunsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the testRunsList where name contains UPDATED_NAME
        defaultTestRunsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestRunsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where name does not contain DEFAULT_NAME
        defaultTestRunsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the testRunsList where name does not contain UPDATED_NAME
        defaultTestRunsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestRunsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where description equals to DEFAULT_DESCRIPTION
        defaultTestRunsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the testRunsList where description equals to UPDATED_DESCRIPTION
        defaultTestRunsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestRunsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTestRunsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the testRunsList where description equals to UPDATED_DESCRIPTION
        defaultTestRunsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestRunsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where description is not null
        defaultTestRunsShouldBeFound("description.specified=true");

        // Get all the testRunsList where description is null
        defaultTestRunsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllTestRunsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where description contains DEFAULT_DESCRIPTION
        defaultTestRunsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the testRunsList where description contains UPDATED_DESCRIPTION
        defaultTestRunsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestRunsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where description does not contain DEFAULT_DESCRIPTION
        defaultTestRunsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the testRunsList where description does not contain UPDATED_DESCRIPTION
        defaultTestRunsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where createdAt equals to DEFAULT_CREATED_AT
        defaultTestRunsShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the testRunsList where createdAt equals to UPDATED_CREATED_AT
        defaultTestRunsShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultTestRunsShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the testRunsList where createdAt equals to UPDATED_CREATED_AT
        defaultTestRunsShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where createdAt is not null
        defaultTestRunsShouldBeFound("createdAt.specified=true");

        // Get all the testRunsList where createdAt is null
        defaultTestRunsShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where createdBy equals to DEFAULT_CREATED_BY
        defaultTestRunsShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the testRunsList where createdBy equals to UPDATED_CREATED_BY
        defaultTestRunsShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultTestRunsShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the testRunsList where createdBy equals to UPDATED_CREATED_BY
        defaultTestRunsShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where createdBy is not null
        defaultTestRunsShouldBeFound("createdBy.specified=true");

        // Get all the testRunsList where createdBy is null
        defaultTestRunsShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultTestRunsShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the testRunsList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultTestRunsShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultTestRunsShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the testRunsList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultTestRunsShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where createdBy is less than DEFAULT_CREATED_BY
        defaultTestRunsShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the testRunsList where createdBy is less than UPDATED_CREATED_BY
        defaultTestRunsShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunsByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        // Get all the testRunsList where createdBy is greater than DEFAULT_CREATED_BY
        defaultTestRunsShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the testRunsList where createdBy is greater than SMALLER_CREATED_BY
        defaultTestRunsShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestRunsByTestLevelIsEqualToSomething() throws Exception {
        TestLevels testLevel;
        if (TestUtil.findAll(em, TestLevels.class).isEmpty()) {
            testRunsRepository.saveAndFlush(testRuns);
            testLevel = TestLevelsResourceIT.createEntity(em);
        } else {
            testLevel = TestUtil.findAll(em, TestLevels.class).get(0);
        }
        em.persist(testLevel);
        em.flush();
        testRuns.setTestLevel(testLevel);
        testRunsRepository.saveAndFlush(testRuns);
        Long testLevelId = testLevel.getId();

        // Get all the testRunsList where testLevel equals to testLevelId
        defaultTestRunsShouldBeFound("testLevelId.equals=" + testLevelId);

        // Get all the testRunsList where testLevel equals to (testLevelId + 1)
        defaultTestRunsShouldNotBeFound("testLevelId.equals=" + (testLevelId + 1));
    }

    @Test
    @Transactional
    void getAllTestRunsByMileStoneIsEqualToSomething() throws Exception {
        Milestones mileStone;
        if (TestUtil.findAll(em, Milestones.class).isEmpty()) {
            testRunsRepository.saveAndFlush(testRuns);
            mileStone = MilestonesResourceIT.createEntity(em);
        } else {
            mileStone = TestUtil.findAll(em, Milestones.class).get(0);
        }
        em.persist(mileStone);
        em.flush();
        testRuns.setMileStone(mileStone);
        testRunsRepository.saveAndFlush(testRuns);
        Long mileStoneId = mileStone.getId();

        // Get all the testRunsList where mileStone equals to mileStoneId
        defaultTestRunsShouldBeFound("mileStoneId.equals=" + mileStoneId);

        // Get all the testRunsList where mileStone equals to (mileStoneId + 1)
        defaultTestRunsShouldNotBeFound("mileStoneId.equals=" + (mileStoneId + 1));
    }

    @Test
    @Transactional
    void getAllTestRunsByTestrundetailsTestrunIsEqualToSomething() throws Exception {
        TestRunDetails testrundetailsTestrun;
        if (TestUtil.findAll(em, TestRunDetails.class).isEmpty()) {
            testRunsRepository.saveAndFlush(testRuns);
            testrundetailsTestrun = TestRunDetailsResourceIT.createEntity(em);
        } else {
            testrundetailsTestrun = TestUtil.findAll(em, TestRunDetails.class).get(0);
        }
        em.persist(testrundetailsTestrun);
        em.flush();
        testRuns.addTestrundetailsTestrun(testrundetailsTestrun);
        testRunsRepository.saveAndFlush(testRuns);
        Long testrundetailsTestrunId = testrundetailsTestrun.getId();

        // Get all the testRunsList where testrundetailsTestrun equals to testrundetailsTestrunId
        defaultTestRunsShouldBeFound("testrundetailsTestrunId.equals=" + testrundetailsTestrunId);

        // Get all the testRunsList where testrundetailsTestrun equals to (testrundetailsTestrunId + 1)
        defaultTestRunsShouldNotBeFound("testrundetailsTestrunId.equals=" + (testrundetailsTestrunId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestRunsShouldBeFound(String filter) throws Exception {
        restTestRunsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testRuns.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restTestRunsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestRunsShouldNotBeFound(String filter) throws Exception {
        restTestRunsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestRunsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestRuns() throws Exception {
        // Get the testRuns
        restTestRunsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestRuns() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        int databaseSizeBeforeUpdate = testRunsRepository.findAll().size();

        // Update the testRuns
        TestRuns updatedTestRuns = testRunsRepository.findById(testRuns.getId()).get();
        // Disconnect from session so that the updates on updatedTestRuns are not directly saved in db
        em.detach(updatedTestRuns);
        updatedTestRuns.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).createdAt(UPDATED_CREATED_AT).createdBy(UPDATED_CREATED_BY);

        restTestRunsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestRuns.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestRuns))
            )
            .andExpect(status().isOk());

        // Validate the TestRuns in the database
        List<TestRuns> testRunsList = testRunsRepository.findAll();
        assertThat(testRunsList).hasSize(databaseSizeBeforeUpdate);
        TestRuns testTestRuns = testRunsList.get(testRunsList.size() - 1);
        assertThat(testTestRuns.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestRuns.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestRuns.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestRuns.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingTestRuns() throws Exception {
        int databaseSizeBeforeUpdate = testRunsRepository.findAll().size();
        testRuns.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestRunsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testRuns.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRuns))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRuns in the database
        List<TestRuns> testRunsList = testRunsRepository.findAll();
        assertThat(testRunsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestRuns() throws Exception {
        int databaseSizeBeforeUpdate = testRunsRepository.findAll().size();
        testRuns.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRuns))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRuns in the database
        List<TestRuns> testRunsList = testRunsRepository.findAll();
        assertThat(testRunsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestRuns() throws Exception {
        int databaseSizeBeforeUpdate = testRunsRepository.findAll().size();
        testRuns.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testRuns)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestRuns in the database
        List<TestRuns> testRunsList = testRunsRepository.findAll();
        assertThat(testRunsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestRunsWithPatch() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        int databaseSizeBeforeUpdate = testRunsRepository.findAll().size();

        // Update the testRuns using partial update
        TestRuns partialUpdatedTestRuns = new TestRuns();
        partialUpdatedTestRuns.setId(testRuns.getId());

        partialUpdatedTestRuns.createdAt(UPDATED_CREATED_AT).createdBy(UPDATED_CREATED_BY);

        restTestRunsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestRuns.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestRuns))
            )
            .andExpect(status().isOk());

        // Validate the TestRuns in the database
        List<TestRuns> testRunsList = testRunsRepository.findAll();
        assertThat(testRunsList).hasSize(databaseSizeBeforeUpdate);
        TestRuns testTestRuns = testRunsList.get(testRunsList.size() - 1);
        assertThat(testTestRuns.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTestRuns.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTestRuns.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestRuns.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateTestRunsWithPatch() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        int databaseSizeBeforeUpdate = testRunsRepository.findAll().size();

        // Update the testRuns using partial update
        TestRuns partialUpdatedTestRuns = new TestRuns();
        partialUpdatedTestRuns.setId(testRuns.getId());

        partialUpdatedTestRuns
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY);

        restTestRunsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestRuns.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestRuns))
            )
            .andExpect(status().isOk());

        // Validate the TestRuns in the database
        List<TestRuns> testRunsList = testRunsRepository.findAll();
        assertThat(testRunsList).hasSize(databaseSizeBeforeUpdate);
        TestRuns testTestRuns = testRunsList.get(testRunsList.size() - 1);
        assertThat(testTestRuns.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestRuns.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestRuns.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestRuns.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingTestRuns() throws Exception {
        int databaseSizeBeforeUpdate = testRunsRepository.findAll().size();
        testRuns.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestRunsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testRuns.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRuns))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRuns in the database
        List<TestRuns> testRunsList = testRunsRepository.findAll();
        assertThat(testRunsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestRuns() throws Exception {
        int databaseSizeBeforeUpdate = testRunsRepository.findAll().size();
        testRuns.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRuns))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRuns in the database
        List<TestRuns> testRunsList = testRunsRepository.findAll();
        assertThat(testRunsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestRuns() throws Exception {
        int databaseSizeBeforeUpdate = testRunsRepository.findAll().size();
        testRuns.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testRuns)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestRuns in the database
        List<TestRuns> testRunsList = testRunsRepository.findAll();
        assertThat(testRunsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestRuns() throws Exception {
        // Initialize the database
        testRunsRepository.saveAndFlush(testRuns);

        int databaseSizeBeforeDelete = testRunsRepository.findAll().size();

        // Delete the testRuns
        restTestRunsMockMvc
            .perform(delete(ENTITY_API_URL_ID, testRuns.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestRuns> testRunsList = testRunsRepository.findAll();
        assertThat(testRunsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
