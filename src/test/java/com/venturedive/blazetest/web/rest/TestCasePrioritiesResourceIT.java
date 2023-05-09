package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.TestCasePriorities;
import com.venturedive.blazetest.domain.TestCases;
import com.venturedive.blazetest.repository.TestCasePrioritiesRepository;
import com.venturedive.blazetest.service.criteria.TestCasePrioritiesCriteria;
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
 * Integration tests for the {@link TestCasePrioritiesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestCasePrioritiesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-case-priorities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestCasePrioritiesRepository testCasePrioritiesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestCasePrioritiesMockMvc;

    private TestCasePriorities testCasePriorities;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCasePriorities createEntity(EntityManager em) {
        TestCasePriorities testCasePriorities = new TestCasePriorities().name(DEFAULT_NAME);
        return testCasePriorities;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCasePriorities createUpdatedEntity(EntityManager em) {
        TestCasePriorities testCasePriorities = new TestCasePriorities().name(UPDATED_NAME);
        return testCasePriorities;
    }

    @BeforeEach
    public void initTest() {
        testCasePriorities = createEntity(em);
    }

    @Test
    @Transactional
    void createTestCasePriorities() throws Exception {
        int databaseSizeBeforeCreate = testCasePrioritiesRepository.findAll().size();
        // Create the TestCasePriorities
        restTestCasePrioritiesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCasePriorities))
            )
            .andExpect(status().isCreated());

        // Validate the TestCasePriorities in the database
        List<TestCasePriorities> testCasePrioritiesList = testCasePrioritiesRepository.findAll();
        assertThat(testCasePrioritiesList).hasSize(databaseSizeBeforeCreate + 1);
        TestCasePriorities testTestCasePriorities = testCasePrioritiesList.get(testCasePrioritiesList.size() - 1);
        assertThat(testTestCasePriorities.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTestCasePrioritiesWithExistingId() throws Exception {
        // Create the TestCasePriorities with an existing ID
        testCasePriorities.setId(1L);

        int databaseSizeBeforeCreate = testCasePrioritiesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCasePrioritiesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCasePriorities))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCasePriorities in the database
        List<TestCasePriorities> testCasePrioritiesList = testCasePrioritiesRepository.findAll();
        assertThat(testCasePrioritiesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = testCasePrioritiesRepository.findAll().size();
        // set the field null
        testCasePriorities.setName(null);

        // Create the TestCasePriorities, which fails.

        restTestCasePrioritiesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCasePriorities))
            )
            .andExpect(status().isBadRequest());

        List<TestCasePriorities> testCasePrioritiesList = testCasePrioritiesRepository.findAll();
        assertThat(testCasePrioritiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTestCasePriorities() throws Exception {
        // Initialize the database
        testCasePrioritiesRepository.saveAndFlush(testCasePriorities);

        // Get all the testCasePrioritiesList
        restTestCasePrioritiesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCasePriorities.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTestCasePriorities() throws Exception {
        // Initialize the database
        testCasePrioritiesRepository.saveAndFlush(testCasePriorities);

        // Get the testCasePriorities
        restTestCasePrioritiesMockMvc
            .perform(get(ENTITY_API_URL_ID, testCasePriorities.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testCasePriorities.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getTestCasePrioritiesByIdFiltering() throws Exception {
        // Initialize the database
        testCasePrioritiesRepository.saveAndFlush(testCasePriorities);

        Long id = testCasePriorities.getId();

        defaultTestCasePrioritiesShouldBeFound("id.equals=" + id);
        defaultTestCasePrioritiesShouldNotBeFound("id.notEquals=" + id);

        defaultTestCasePrioritiesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestCasePrioritiesShouldNotBeFound("id.greaterThan=" + id);

        defaultTestCasePrioritiesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestCasePrioritiesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestCasePrioritiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        testCasePrioritiesRepository.saveAndFlush(testCasePriorities);

        // Get all the testCasePrioritiesList where name equals to DEFAULT_NAME
        defaultTestCasePrioritiesShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the testCasePrioritiesList where name equals to UPDATED_NAME
        defaultTestCasePrioritiesShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestCasePrioritiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        testCasePrioritiesRepository.saveAndFlush(testCasePriorities);

        // Get all the testCasePrioritiesList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTestCasePrioritiesShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the testCasePrioritiesList where name equals to UPDATED_NAME
        defaultTestCasePrioritiesShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestCasePrioritiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCasePrioritiesRepository.saveAndFlush(testCasePriorities);

        // Get all the testCasePrioritiesList where name is not null
        defaultTestCasePrioritiesShouldBeFound("name.specified=true");

        // Get all the testCasePrioritiesList where name is null
        defaultTestCasePrioritiesShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCasePrioritiesByNameContainsSomething() throws Exception {
        // Initialize the database
        testCasePrioritiesRepository.saveAndFlush(testCasePriorities);

        // Get all the testCasePrioritiesList where name contains DEFAULT_NAME
        defaultTestCasePrioritiesShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the testCasePrioritiesList where name contains UPDATED_NAME
        defaultTestCasePrioritiesShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestCasePrioritiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        testCasePrioritiesRepository.saveAndFlush(testCasePriorities);

        // Get all the testCasePrioritiesList where name does not contain DEFAULT_NAME
        defaultTestCasePrioritiesShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the testCasePrioritiesList where name does not contain UPDATED_NAME
        defaultTestCasePrioritiesShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestCasePrioritiesByTestcasesPriorityIsEqualToSomething() throws Exception {
        TestCases testcasesPriority;
        if (TestUtil.findAll(em, TestCases.class).isEmpty()) {
            testCasePrioritiesRepository.saveAndFlush(testCasePriorities);
            testcasesPriority = TestCasesResourceIT.createEntity(em);
        } else {
            testcasesPriority = TestUtil.findAll(em, TestCases.class).get(0);
        }
        em.persist(testcasesPriority);
        em.flush();
        testCasePriorities.addTestcasesPriority(testcasesPriority);
        testCasePrioritiesRepository.saveAndFlush(testCasePriorities);
        Long testcasesPriorityId = testcasesPriority.getId();

        // Get all the testCasePrioritiesList where testcasesPriority equals to testcasesPriorityId
        defaultTestCasePrioritiesShouldBeFound("testcasesPriorityId.equals=" + testcasesPriorityId);

        // Get all the testCasePrioritiesList where testcasesPriority equals to (testcasesPriorityId + 1)
        defaultTestCasePrioritiesShouldNotBeFound("testcasesPriorityId.equals=" + (testcasesPriorityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestCasePrioritiesShouldBeFound(String filter) throws Exception {
        restTestCasePrioritiesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCasePriorities.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTestCasePrioritiesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestCasePrioritiesShouldNotBeFound(String filter) throws Exception {
        restTestCasePrioritiesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestCasePrioritiesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestCasePriorities() throws Exception {
        // Get the testCasePriorities
        restTestCasePrioritiesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestCasePriorities() throws Exception {
        // Initialize the database
        testCasePrioritiesRepository.saveAndFlush(testCasePriorities);

        int databaseSizeBeforeUpdate = testCasePrioritiesRepository.findAll().size();

        // Update the testCasePriorities
        TestCasePriorities updatedTestCasePriorities = testCasePrioritiesRepository.findById(testCasePriorities.getId()).get();
        // Disconnect from session so that the updates on updatedTestCasePriorities are not directly saved in db
        em.detach(updatedTestCasePriorities);
        updatedTestCasePriorities.name(UPDATED_NAME);

        restTestCasePrioritiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestCasePriorities.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestCasePriorities))
            )
            .andExpect(status().isOk());

        // Validate the TestCasePriorities in the database
        List<TestCasePriorities> testCasePrioritiesList = testCasePrioritiesRepository.findAll();
        assertThat(testCasePrioritiesList).hasSize(databaseSizeBeforeUpdate);
        TestCasePriorities testTestCasePriorities = testCasePrioritiesList.get(testCasePrioritiesList.size() - 1);
        assertThat(testTestCasePriorities.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTestCasePriorities() throws Exception {
        int databaseSizeBeforeUpdate = testCasePrioritiesRepository.findAll().size();
        testCasePriorities.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCasePrioritiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCasePriorities.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCasePriorities))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCasePriorities in the database
        List<TestCasePriorities> testCasePrioritiesList = testCasePrioritiesRepository.findAll();
        assertThat(testCasePrioritiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestCasePriorities() throws Exception {
        int databaseSizeBeforeUpdate = testCasePrioritiesRepository.findAll().size();
        testCasePriorities.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCasePrioritiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCasePriorities))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCasePriorities in the database
        List<TestCasePriorities> testCasePrioritiesList = testCasePrioritiesRepository.findAll();
        assertThat(testCasePrioritiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestCasePriorities() throws Exception {
        int databaseSizeBeforeUpdate = testCasePrioritiesRepository.findAll().size();
        testCasePriorities.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCasePrioritiesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCasePriorities))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCasePriorities in the database
        List<TestCasePriorities> testCasePrioritiesList = testCasePrioritiesRepository.findAll();
        assertThat(testCasePrioritiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestCasePrioritiesWithPatch() throws Exception {
        // Initialize the database
        testCasePrioritiesRepository.saveAndFlush(testCasePriorities);

        int databaseSizeBeforeUpdate = testCasePrioritiesRepository.findAll().size();

        // Update the testCasePriorities using partial update
        TestCasePriorities partialUpdatedTestCasePriorities = new TestCasePriorities();
        partialUpdatedTestCasePriorities.setId(testCasePriorities.getId());

        restTestCasePrioritiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCasePriorities.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCasePriorities))
            )
            .andExpect(status().isOk());

        // Validate the TestCasePriorities in the database
        List<TestCasePriorities> testCasePrioritiesList = testCasePrioritiesRepository.findAll();
        assertThat(testCasePrioritiesList).hasSize(databaseSizeBeforeUpdate);
        TestCasePriorities testTestCasePriorities = testCasePrioritiesList.get(testCasePrioritiesList.size() - 1);
        assertThat(testTestCasePriorities.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTestCasePrioritiesWithPatch() throws Exception {
        // Initialize the database
        testCasePrioritiesRepository.saveAndFlush(testCasePriorities);

        int databaseSizeBeforeUpdate = testCasePrioritiesRepository.findAll().size();

        // Update the testCasePriorities using partial update
        TestCasePriorities partialUpdatedTestCasePriorities = new TestCasePriorities();
        partialUpdatedTestCasePriorities.setId(testCasePriorities.getId());

        partialUpdatedTestCasePriorities.name(UPDATED_NAME);

        restTestCasePrioritiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCasePriorities.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCasePriorities))
            )
            .andExpect(status().isOk());

        // Validate the TestCasePriorities in the database
        List<TestCasePriorities> testCasePrioritiesList = testCasePrioritiesRepository.findAll();
        assertThat(testCasePrioritiesList).hasSize(databaseSizeBeforeUpdate);
        TestCasePriorities testTestCasePriorities = testCasePrioritiesList.get(testCasePrioritiesList.size() - 1);
        assertThat(testTestCasePriorities.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTestCasePriorities() throws Exception {
        int databaseSizeBeforeUpdate = testCasePrioritiesRepository.findAll().size();
        testCasePriorities.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCasePrioritiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testCasePriorities.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCasePriorities))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCasePriorities in the database
        List<TestCasePriorities> testCasePrioritiesList = testCasePrioritiesRepository.findAll();
        assertThat(testCasePrioritiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestCasePriorities() throws Exception {
        int databaseSizeBeforeUpdate = testCasePrioritiesRepository.findAll().size();
        testCasePriorities.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCasePrioritiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCasePriorities))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCasePriorities in the database
        List<TestCasePriorities> testCasePrioritiesList = testCasePrioritiesRepository.findAll();
        assertThat(testCasePrioritiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestCasePriorities() throws Exception {
        int databaseSizeBeforeUpdate = testCasePrioritiesRepository.findAll().size();
        testCasePriorities.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCasePrioritiesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCasePriorities))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCasePriorities in the database
        List<TestCasePriorities> testCasePrioritiesList = testCasePrioritiesRepository.findAll();
        assertThat(testCasePrioritiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestCasePriorities() throws Exception {
        // Initialize the database
        testCasePrioritiesRepository.saveAndFlush(testCasePriorities);

        int databaseSizeBeforeDelete = testCasePrioritiesRepository.findAll().size();

        // Delete the testCasePriorities
        restTestCasePrioritiesMockMvc
            .perform(delete(ENTITY_API_URL_ID, testCasePriorities.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestCasePriorities> testCasePrioritiesList = testCasePrioritiesRepository.findAll();
        assertThat(testCasePrioritiesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
