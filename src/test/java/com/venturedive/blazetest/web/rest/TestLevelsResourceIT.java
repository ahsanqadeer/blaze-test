package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.TestCases;
import com.venturedive.blazetest.domain.TestLevels;
import com.venturedive.blazetest.domain.TestRuns;
import com.venturedive.blazetest.repository.TestLevelsRepository;
import com.venturedive.blazetest.service.criteria.TestLevelsCriteria;
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
 * Integration tests for the {@link TestLevelsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestLevelsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-levels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestLevelsRepository testLevelsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestLevelsMockMvc;

    private TestLevels testLevels;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestLevels createEntity(EntityManager em) {
        TestLevels testLevels = new TestLevels().name(DEFAULT_NAME);
        return testLevels;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestLevels createUpdatedEntity(EntityManager em) {
        TestLevels testLevels = new TestLevels().name(UPDATED_NAME);
        return testLevels;
    }

    @BeforeEach
    public void initTest() {
        testLevels = createEntity(em);
    }

    @Test
    @Transactional
    void createTestLevels() throws Exception {
        int databaseSizeBeforeCreate = testLevelsRepository.findAll().size();
        // Create the TestLevels
        restTestLevelsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testLevels)))
            .andExpect(status().isCreated());

        // Validate the TestLevels in the database
        List<TestLevels> testLevelsList = testLevelsRepository.findAll();
        assertThat(testLevelsList).hasSize(databaseSizeBeforeCreate + 1);
        TestLevels testTestLevels = testLevelsList.get(testLevelsList.size() - 1);
        assertThat(testTestLevels.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTestLevelsWithExistingId() throws Exception {
        // Create the TestLevels with an existing ID
        testLevels.setId(1L);

        int databaseSizeBeforeCreate = testLevelsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestLevelsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testLevels)))
            .andExpect(status().isBadRequest());

        // Validate the TestLevels in the database
        List<TestLevels> testLevelsList = testLevelsRepository.findAll();
        assertThat(testLevelsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestLevels() throws Exception {
        // Initialize the database
        testLevelsRepository.saveAndFlush(testLevels);

        // Get all the testLevelsList
        restTestLevelsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testLevels.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTestLevels() throws Exception {
        // Initialize the database
        testLevelsRepository.saveAndFlush(testLevels);

        // Get the testLevels
        restTestLevelsMockMvc
            .perform(get(ENTITY_API_URL_ID, testLevels.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testLevels.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getTestLevelsByIdFiltering() throws Exception {
        // Initialize the database
        testLevelsRepository.saveAndFlush(testLevels);

        Long id = testLevels.getId();

        defaultTestLevelsShouldBeFound("id.equals=" + id);
        defaultTestLevelsShouldNotBeFound("id.notEquals=" + id);

        defaultTestLevelsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestLevelsShouldNotBeFound("id.greaterThan=" + id);

        defaultTestLevelsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestLevelsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestLevelsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        testLevelsRepository.saveAndFlush(testLevels);

        // Get all the testLevelsList where name equals to DEFAULT_NAME
        defaultTestLevelsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the testLevelsList where name equals to UPDATED_NAME
        defaultTestLevelsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestLevelsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        testLevelsRepository.saveAndFlush(testLevels);

        // Get all the testLevelsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTestLevelsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the testLevelsList where name equals to UPDATED_NAME
        defaultTestLevelsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestLevelsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        testLevelsRepository.saveAndFlush(testLevels);

        // Get all the testLevelsList where name is not null
        defaultTestLevelsShouldBeFound("name.specified=true");

        // Get all the testLevelsList where name is null
        defaultTestLevelsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTestLevelsByNameContainsSomething() throws Exception {
        // Initialize the database
        testLevelsRepository.saveAndFlush(testLevels);

        // Get all the testLevelsList where name contains DEFAULT_NAME
        defaultTestLevelsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the testLevelsList where name contains UPDATED_NAME
        defaultTestLevelsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestLevelsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        testLevelsRepository.saveAndFlush(testLevels);

        // Get all the testLevelsList where name does not contain DEFAULT_NAME
        defaultTestLevelsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the testLevelsList where name does not contain UPDATED_NAME
        defaultTestLevelsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTestLevelsByTestrunsTestlevelIsEqualToSomething() throws Exception {
        TestRuns testrunsTestlevel;
        if (TestUtil.findAll(em, TestRuns.class).isEmpty()) {
            testLevelsRepository.saveAndFlush(testLevels);
            testrunsTestlevel = TestRunsResourceIT.createEntity(em);
        } else {
            testrunsTestlevel = TestUtil.findAll(em, TestRuns.class).get(0);
        }
        em.persist(testrunsTestlevel);
        em.flush();
        testLevels.addTestrunsTestlevel(testrunsTestlevel);
        testLevelsRepository.saveAndFlush(testLevels);
        Long testrunsTestlevelId = testrunsTestlevel.getId();

        // Get all the testLevelsList where testrunsTestlevel equals to testrunsTestlevelId
        defaultTestLevelsShouldBeFound("testrunsTestlevelId.equals=" + testrunsTestlevelId);

        // Get all the testLevelsList where testrunsTestlevel equals to (testrunsTestlevelId + 1)
        defaultTestLevelsShouldNotBeFound("testrunsTestlevelId.equals=" + (testrunsTestlevelId + 1));
    }

    @Test
    @Transactional
    void getAllTestLevelsByTestCaseIsEqualToSomething() throws Exception {
        TestCases testCase;
        if (TestUtil.findAll(em, TestCases.class).isEmpty()) {
            testLevelsRepository.saveAndFlush(testLevels);
            testCase = TestCasesResourceIT.createEntity(em);
        } else {
            testCase = TestUtil.findAll(em, TestCases.class).get(0);
        }
        em.persist(testCase);
        em.flush();
        testLevels.addTestCase(testCase);
        testLevelsRepository.saveAndFlush(testLevels);
        Long testCaseId = testCase.getId();

        // Get all the testLevelsList where testCase equals to testCaseId
        defaultTestLevelsShouldBeFound("testCaseId.equals=" + testCaseId);

        // Get all the testLevelsList where testCase equals to (testCaseId + 1)
        defaultTestLevelsShouldNotBeFound("testCaseId.equals=" + (testCaseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestLevelsShouldBeFound(String filter) throws Exception {
        restTestLevelsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testLevels.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTestLevelsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestLevelsShouldNotBeFound(String filter) throws Exception {
        restTestLevelsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestLevelsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestLevels() throws Exception {
        // Get the testLevels
        restTestLevelsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestLevels() throws Exception {
        // Initialize the database
        testLevelsRepository.saveAndFlush(testLevels);

        int databaseSizeBeforeUpdate = testLevelsRepository.findAll().size();

        // Update the testLevels
        TestLevels updatedTestLevels = testLevelsRepository.findById(testLevels.getId()).get();
        // Disconnect from session so that the updates on updatedTestLevels are not directly saved in db
        em.detach(updatedTestLevels);
        updatedTestLevels.name(UPDATED_NAME);

        restTestLevelsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestLevels.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestLevels))
            )
            .andExpect(status().isOk());

        // Validate the TestLevels in the database
        List<TestLevels> testLevelsList = testLevelsRepository.findAll();
        assertThat(testLevelsList).hasSize(databaseSizeBeforeUpdate);
        TestLevels testTestLevels = testLevelsList.get(testLevelsList.size() - 1);
        assertThat(testTestLevels.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTestLevels() throws Exception {
        int databaseSizeBeforeUpdate = testLevelsRepository.findAll().size();
        testLevels.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestLevelsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testLevels.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testLevels))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestLevels in the database
        List<TestLevels> testLevelsList = testLevelsRepository.findAll();
        assertThat(testLevelsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestLevels() throws Exception {
        int databaseSizeBeforeUpdate = testLevelsRepository.findAll().size();
        testLevels.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestLevelsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testLevels))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestLevels in the database
        List<TestLevels> testLevelsList = testLevelsRepository.findAll();
        assertThat(testLevelsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestLevels() throws Exception {
        int databaseSizeBeforeUpdate = testLevelsRepository.findAll().size();
        testLevels.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestLevelsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testLevels)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestLevels in the database
        List<TestLevels> testLevelsList = testLevelsRepository.findAll();
        assertThat(testLevelsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestLevelsWithPatch() throws Exception {
        // Initialize the database
        testLevelsRepository.saveAndFlush(testLevels);

        int databaseSizeBeforeUpdate = testLevelsRepository.findAll().size();

        // Update the testLevels using partial update
        TestLevels partialUpdatedTestLevels = new TestLevels();
        partialUpdatedTestLevels.setId(testLevels.getId());

        restTestLevelsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestLevels.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestLevels))
            )
            .andExpect(status().isOk());

        // Validate the TestLevels in the database
        List<TestLevels> testLevelsList = testLevelsRepository.findAll();
        assertThat(testLevelsList).hasSize(databaseSizeBeforeUpdate);
        TestLevels testTestLevels = testLevelsList.get(testLevelsList.size() - 1);
        assertThat(testTestLevels.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTestLevelsWithPatch() throws Exception {
        // Initialize the database
        testLevelsRepository.saveAndFlush(testLevels);

        int databaseSizeBeforeUpdate = testLevelsRepository.findAll().size();

        // Update the testLevels using partial update
        TestLevels partialUpdatedTestLevels = new TestLevels();
        partialUpdatedTestLevels.setId(testLevels.getId());

        partialUpdatedTestLevels.name(UPDATED_NAME);

        restTestLevelsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestLevels.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestLevels))
            )
            .andExpect(status().isOk());

        // Validate the TestLevels in the database
        List<TestLevels> testLevelsList = testLevelsRepository.findAll();
        assertThat(testLevelsList).hasSize(databaseSizeBeforeUpdate);
        TestLevels testTestLevels = testLevelsList.get(testLevelsList.size() - 1);
        assertThat(testTestLevels.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTestLevels() throws Exception {
        int databaseSizeBeforeUpdate = testLevelsRepository.findAll().size();
        testLevels.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestLevelsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testLevels.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testLevels))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestLevels in the database
        List<TestLevels> testLevelsList = testLevelsRepository.findAll();
        assertThat(testLevelsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestLevels() throws Exception {
        int databaseSizeBeforeUpdate = testLevelsRepository.findAll().size();
        testLevels.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestLevelsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testLevels))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestLevels in the database
        List<TestLevels> testLevelsList = testLevelsRepository.findAll();
        assertThat(testLevelsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestLevels() throws Exception {
        int databaseSizeBeforeUpdate = testLevelsRepository.findAll().size();
        testLevels.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestLevelsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testLevels))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestLevels in the database
        List<TestLevels> testLevelsList = testLevelsRepository.findAll();
        assertThat(testLevelsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestLevels() throws Exception {
        // Initialize the database
        testLevelsRepository.saveAndFlush(testLevels);

        int databaseSizeBeforeDelete = testLevelsRepository.findAll().size();

        // Delete the testLevels
        restTestLevelsMockMvc
            .perform(delete(ENTITY_API_URL_ID, testLevels.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestLevels> testLevelsList = testLevelsRepository.findAll();
        assertThat(testLevelsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
