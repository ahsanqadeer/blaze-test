package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.Projects;
import com.venturedive.blazetest.domain.TestPlans;
import com.venturedive.blazetest.repository.TestPlansRepository;
import com.venturedive.blazetest.service.criteria.TestPlansCriteria;
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
 * Integration tests for the {@link TestPlansResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestPlansResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;
    private static final Integer SMALLER_CREATED_BY = 1 - 1;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/test-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestPlansRepository testPlansRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestPlansMockMvc;

    private TestPlans testPlans;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestPlans createEntity(EntityManager em) {
        TestPlans testPlans = new TestPlans()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT);
        return testPlans;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestPlans createUpdatedEntity(EntityManager em) {
        TestPlans testPlans = new TestPlans()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT);
        return testPlans;
    }

    @BeforeEach
    public void initTest() {
        testPlans = createEntity(em);
    }

    @Test
    @Transactional
    void createTestPlans() throws Exception {
        int databaseSizeBeforeCreate = testPlansRepository.findAll().size();
        // Create the TestPlans
        restTestPlansMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testPlans)))
            .andExpect(status().isCreated());

        // Validate the TestPlans in the database
        List<TestPlans> testPlansList = testPlansRepository.findAll();
        assertThat(testPlansList).hasSize(databaseSizeBeforeCreate + 1);
        TestPlans testTestPlans = testPlansList.get(testPlansList.size() - 1);
        assertThat(testTestPlans.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTestPlans.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTestPlans.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestPlans.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void createTestPlansWithExistingId() throws Exception {
        // Create the TestPlans with an existing ID
        testPlans.setId(1L);

        int databaseSizeBeforeCreate = testPlansRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestPlansMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testPlans)))
            .andExpect(status().isBadRequest());

        // Validate the TestPlans in the database
        List<TestPlans> testPlansList = testPlansRepository.findAll();
        assertThat(testPlansList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestPlans() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        // Get all the testPlansList
        restTestPlansMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testPlans.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getTestPlans() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        // Get the testPlans
        restTestPlansMockMvc
            .perform(get(ENTITY_API_URL_ID, testPlans.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testPlans.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getTestPlansByIdFiltering() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        Long id = testPlans.getId();

        defaultTestPlansShouldBeFound("id.equals=" + id);
        defaultTestPlansShouldNotBeFound("id.notEquals=" + id);

        defaultTestPlansShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestPlansShouldNotBeFound("id.greaterThan=" + id);

        defaultTestPlansShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestPlansShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestPlansByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        // Get all the testPlansList where name equals to DEFAULT_NAME
        defaultTestPlansShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the testPlansList where name equals to UPDATED_NAME
        defaultTestPlansShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestPlansByNameIsInShouldWork() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        // Get all the testPlansList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTestPlansShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the testPlansList where name equals to UPDATED_NAME
        defaultTestPlansShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestPlansByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        // Get all the testPlansList where name is not null
        defaultTestPlansShouldBeFound("name.specified=true");

        // Get all the testPlansList where name is null
        defaultTestPlansShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTestPlansByNameContainsSomething() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        // Get all the testPlansList where name contains DEFAULT_NAME
        defaultTestPlansShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the testPlansList where name contains UPDATED_NAME
        defaultTestPlansShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestPlansByNameNotContainsSomething() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        // Get all the testPlansList where name does not contain DEFAULT_NAME
        defaultTestPlansShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the testPlansList where name does not contain UPDATED_NAME
        defaultTestPlansShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        // Get all the testPlansList where createdBy equals to DEFAULT_CREATED_BY
        defaultTestPlansShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the testPlansList where createdBy equals to UPDATED_CREATED_BY
        defaultTestPlansShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        // Get all the testPlansList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultTestPlansShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the testPlansList where createdBy equals to UPDATED_CREATED_BY
        defaultTestPlansShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        // Get all the testPlansList where createdBy is not null
        defaultTestPlansShouldBeFound("createdBy.specified=true");

        // Get all the testPlansList where createdBy is null
        defaultTestPlansShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        // Get all the testPlansList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultTestPlansShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the testPlansList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultTestPlansShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        // Get all the testPlansList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultTestPlansShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the testPlansList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultTestPlansShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        // Get all the testPlansList where createdBy is less than DEFAULT_CREATED_BY
        defaultTestPlansShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the testPlansList where createdBy is less than UPDATED_CREATED_BY
        defaultTestPlansShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        // Get all the testPlansList where createdBy is greater than DEFAULT_CREATED_BY
        defaultTestPlansShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the testPlansList where createdBy is greater than SMALLER_CREATED_BY
        defaultTestPlansShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        // Get all the testPlansList where createdAt equals to DEFAULT_CREATED_AT
        defaultTestPlansShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the testPlansList where createdAt equals to UPDATED_CREATED_AT
        defaultTestPlansShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        // Get all the testPlansList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultTestPlansShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the testPlansList where createdAt equals to UPDATED_CREATED_AT
        defaultTestPlansShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTestPlansByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        // Get all the testPlansList where createdAt is not null
        defaultTestPlansShouldBeFound("createdAt.specified=true");

        // Get all the testPlansList where createdAt is null
        defaultTestPlansShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTestPlansByProjectIsEqualToSomething() throws Exception {
        Projects project;
        if (TestUtil.findAll(em, Projects.class).isEmpty()) {
            testPlansRepository.saveAndFlush(testPlans);
            project = ProjectsResourceIT.createEntity(em);
        } else {
            project = TestUtil.findAll(em, Projects.class).get(0);
        }
        em.persist(project);
        em.flush();
        testPlans.setProject(project);
        testPlansRepository.saveAndFlush(testPlans);
        Long projectId = project.getId();

        // Get all the testPlansList where project equals to projectId
        defaultTestPlansShouldBeFound("projectId.equals=" + projectId);

        // Get all the testPlansList where project equals to (projectId + 1)
        defaultTestPlansShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestPlansShouldBeFound(String filter) throws Exception {
        restTestPlansMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testPlans.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));

        // Check, that the count call also returns 1
        restTestPlansMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestPlansShouldNotBeFound(String filter) throws Exception {
        restTestPlansMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestPlansMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestPlans() throws Exception {
        // Get the testPlans
        restTestPlansMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestPlans() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        int databaseSizeBeforeUpdate = testPlansRepository.findAll().size();

        // Update the testPlans
        TestPlans updatedTestPlans = testPlansRepository.findById(testPlans.getId()).get();
        // Disconnect from session so that the updates on updatedTestPlans are not directly saved in db
        em.detach(updatedTestPlans);
        updatedTestPlans.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).createdBy(UPDATED_CREATED_BY).createdAt(UPDATED_CREATED_AT);

        restTestPlansMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestPlans.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestPlans))
            )
            .andExpect(status().isOk());

        // Validate the TestPlans in the database
        List<TestPlans> testPlansList = testPlansRepository.findAll();
        assertThat(testPlansList).hasSize(databaseSizeBeforeUpdate);
        TestPlans testTestPlans = testPlansList.get(testPlansList.size() - 1);
        assertThat(testTestPlans.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestPlans.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestPlans.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestPlans.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingTestPlans() throws Exception {
        int databaseSizeBeforeUpdate = testPlansRepository.findAll().size();
        testPlans.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestPlansMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testPlans.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testPlans))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestPlans in the database
        List<TestPlans> testPlansList = testPlansRepository.findAll();
        assertThat(testPlansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestPlans() throws Exception {
        int databaseSizeBeforeUpdate = testPlansRepository.findAll().size();
        testPlans.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestPlansMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testPlans))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestPlans in the database
        List<TestPlans> testPlansList = testPlansRepository.findAll();
        assertThat(testPlansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestPlans() throws Exception {
        int databaseSizeBeforeUpdate = testPlansRepository.findAll().size();
        testPlans.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestPlansMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testPlans)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestPlans in the database
        List<TestPlans> testPlansList = testPlansRepository.findAll();
        assertThat(testPlansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestPlansWithPatch() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        int databaseSizeBeforeUpdate = testPlansRepository.findAll().size();

        // Update the testPlans using partial update
        TestPlans partialUpdatedTestPlans = new TestPlans();
        partialUpdatedTestPlans.setId(testPlans.getId());

        partialUpdatedTestPlans.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).createdAt(UPDATED_CREATED_AT);

        restTestPlansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestPlans.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestPlans))
            )
            .andExpect(status().isOk());

        // Validate the TestPlans in the database
        List<TestPlans> testPlansList = testPlansRepository.findAll();
        assertThat(testPlansList).hasSize(databaseSizeBeforeUpdate);
        TestPlans testTestPlans = testPlansList.get(testPlansList.size() - 1);
        assertThat(testTestPlans.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestPlans.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestPlans.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTestPlans.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateTestPlansWithPatch() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        int databaseSizeBeforeUpdate = testPlansRepository.findAll().size();

        // Update the testPlans using partial update
        TestPlans partialUpdatedTestPlans = new TestPlans();
        partialUpdatedTestPlans.setId(testPlans.getId());

        partialUpdatedTestPlans
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT);

        restTestPlansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestPlans.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestPlans))
            )
            .andExpect(status().isOk());

        // Validate the TestPlans in the database
        List<TestPlans> testPlansList = testPlansRepository.findAll();
        assertThat(testPlansList).hasSize(databaseSizeBeforeUpdate);
        TestPlans testTestPlans = testPlansList.get(testPlansList.size() - 1);
        assertThat(testTestPlans.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestPlans.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestPlans.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTestPlans.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingTestPlans() throws Exception {
        int databaseSizeBeforeUpdate = testPlansRepository.findAll().size();
        testPlans.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestPlansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testPlans.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testPlans))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestPlans in the database
        List<TestPlans> testPlansList = testPlansRepository.findAll();
        assertThat(testPlansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestPlans() throws Exception {
        int databaseSizeBeforeUpdate = testPlansRepository.findAll().size();
        testPlans.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestPlansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testPlans))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestPlans in the database
        List<TestPlans> testPlansList = testPlansRepository.findAll();
        assertThat(testPlansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestPlans() throws Exception {
        int databaseSizeBeforeUpdate = testPlansRepository.findAll().size();
        testPlans.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestPlansMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testPlans))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestPlans in the database
        List<TestPlans> testPlansList = testPlansRepository.findAll();
        assertThat(testPlansList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestPlans() throws Exception {
        // Initialize the database
        testPlansRepository.saveAndFlush(testPlans);

        int databaseSizeBeforeDelete = testPlansRepository.findAll().size();

        // Delete the testPlans
        restTestPlansMockMvc
            .perform(delete(ENTITY_API_URL_ID, testPlans.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestPlans> testPlansList = testPlansRepository.findAll();
        assertThat(testPlansList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
