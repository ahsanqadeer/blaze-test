package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.TestRunDetails;
import com.venturedive.blazetest.domain.TestRunStepDetails;
import com.venturedive.blazetest.domain.TestStatuses;
import com.venturedive.blazetest.repository.TestStatusesRepository;
import com.venturedive.blazetest.service.criteria.TestStatusesCriteria;
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
 * Integration tests for the {@link TestStatusesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestStatusesResourceIT {

    private static final String DEFAULT_STATUS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestStatusesRepository testStatusesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestStatusesMockMvc;

    private TestStatuses testStatuses;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestStatuses createEntity(EntityManager em) {
        TestStatuses testStatuses = new TestStatuses().statusName(DEFAULT_STATUS_NAME);
        return testStatuses;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestStatuses createUpdatedEntity(EntityManager em) {
        TestStatuses testStatuses = new TestStatuses().statusName(UPDATED_STATUS_NAME);
        return testStatuses;
    }

    @BeforeEach
    public void initTest() {
        testStatuses = createEntity(em);
    }

    @Test
    @Transactional
    void createTestStatuses() throws Exception {
        int databaseSizeBeforeCreate = testStatusesRepository.findAll().size();
        // Create the TestStatuses
        restTestStatusesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testStatuses)))
            .andExpect(status().isCreated());

        // Validate the TestStatuses in the database
        List<TestStatuses> testStatusesList = testStatusesRepository.findAll();
        assertThat(testStatusesList).hasSize(databaseSizeBeforeCreate + 1);
        TestStatuses testTestStatuses = testStatusesList.get(testStatusesList.size() - 1);
        assertThat(testTestStatuses.getStatusName()).isEqualTo(DEFAULT_STATUS_NAME);
    }

    @Test
    @Transactional
    void createTestStatusesWithExistingId() throws Exception {
        // Create the TestStatuses with an existing ID
        testStatuses.setId(1L);

        int databaseSizeBeforeCreate = testStatusesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestStatusesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testStatuses)))
            .andExpect(status().isBadRequest());

        // Validate the TestStatuses in the database
        List<TestStatuses> testStatusesList = testStatusesRepository.findAll();
        assertThat(testStatusesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestStatuses() throws Exception {
        // Initialize the database
        testStatusesRepository.saveAndFlush(testStatuses);

        // Get all the testStatusesList
        restTestStatusesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testStatuses.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusName").value(hasItem(DEFAULT_STATUS_NAME)));
    }

    @Test
    @Transactional
    void getTestStatuses() throws Exception {
        // Initialize the database
        testStatusesRepository.saveAndFlush(testStatuses);

        // Get the testStatuses
        restTestStatusesMockMvc
            .perform(get(ENTITY_API_URL_ID, testStatuses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testStatuses.getId().intValue()))
            .andExpect(jsonPath("$.statusName").value(DEFAULT_STATUS_NAME));
    }

    @Test
    @Transactional
    void getTestStatusesByIdFiltering() throws Exception {
        // Initialize the database
        testStatusesRepository.saveAndFlush(testStatuses);

        Long id = testStatuses.getId();

        defaultTestStatusesShouldBeFound("id.equals=" + id);
        defaultTestStatusesShouldNotBeFound("id.notEquals=" + id);

        defaultTestStatusesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestStatusesShouldNotBeFound("id.greaterThan=" + id);

        defaultTestStatusesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestStatusesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestStatusesByStatusNameIsEqualToSomething() throws Exception {
        // Initialize the database
        testStatusesRepository.saveAndFlush(testStatuses);

        // Get all the testStatusesList where statusName equals to DEFAULT_STATUS_NAME
        defaultTestStatusesShouldBeFound("statusName.equals=" + DEFAULT_STATUS_NAME);

        // Get all the testStatusesList where statusName equals to UPDATED_STATUS_NAME
        defaultTestStatusesShouldNotBeFound("statusName.equals=" + UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllTestStatusesByStatusNameIsInShouldWork() throws Exception {
        // Initialize the database
        testStatusesRepository.saveAndFlush(testStatuses);

        // Get all the testStatusesList where statusName in DEFAULT_STATUS_NAME or UPDATED_STATUS_NAME
        defaultTestStatusesShouldBeFound("statusName.in=" + DEFAULT_STATUS_NAME + "," + UPDATED_STATUS_NAME);

        // Get all the testStatusesList where statusName equals to UPDATED_STATUS_NAME
        defaultTestStatusesShouldNotBeFound("statusName.in=" + UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllTestStatusesByStatusNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        testStatusesRepository.saveAndFlush(testStatuses);

        // Get all the testStatusesList where statusName is not null
        defaultTestStatusesShouldBeFound("statusName.specified=true");

        // Get all the testStatusesList where statusName is null
        defaultTestStatusesShouldNotBeFound("statusName.specified=false");
    }

    @Test
    @Transactional
    void getAllTestStatusesByStatusNameContainsSomething() throws Exception {
        // Initialize the database
        testStatusesRepository.saveAndFlush(testStatuses);

        // Get all the testStatusesList where statusName contains DEFAULT_STATUS_NAME
        defaultTestStatusesShouldBeFound("statusName.contains=" + DEFAULT_STATUS_NAME);

        // Get all the testStatusesList where statusName contains UPDATED_STATUS_NAME
        defaultTestStatusesShouldNotBeFound("statusName.contains=" + UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllTestStatusesByStatusNameNotContainsSomething() throws Exception {
        // Initialize the database
        testStatusesRepository.saveAndFlush(testStatuses);

        // Get all the testStatusesList where statusName does not contain DEFAULT_STATUS_NAME
        defaultTestStatusesShouldNotBeFound("statusName.doesNotContain=" + DEFAULT_STATUS_NAME);

        // Get all the testStatusesList where statusName does not contain UPDATED_STATUS_NAME
        defaultTestStatusesShouldBeFound("statusName.doesNotContain=" + UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllTestStatusesByTestrundetailsStatusIsEqualToSomething() throws Exception {
        TestRunDetails testrundetailsStatus;
        if (TestUtil.findAll(em, TestRunDetails.class).isEmpty()) {
            testStatusesRepository.saveAndFlush(testStatuses);
            testrundetailsStatus = TestRunDetailsResourceIT.createEntity(em);
        } else {
            testrundetailsStatus = TestUtil.findAll(em, TestRunDetails.class).get(0);
        }
        em.persist(testrundetailsStatus);
        em.flush();
        testStatuses.addTestrundetailsStatus(testrundetailsStatus);
        testStatusesRepository.saveAndFlush(testStatuses);
        Long testrundetailsStatusId = testrundetailsStatus.getId();

        // Get all the testStatusesList where testrundetailsStatus equals to testrundetailsStatusId
        defaultTestStatusesShouldBeFound("testrundetailsStatusId.equals=" + testrundetailsStatusId);

        // Get all the testStatusesList where testrundetailsStatus equals to (testrundetailsStatusId + 1)
        defaultTestStatusesShouldNotBeFound("testrundetailsStatusId.equals=" + (testrundetailsStatusId + 1));
    }

    @Test
    @Transactional
    void getAllTestStatusesByTestrunstepdetailsStatusIsEqualToSomething() throws Exception {
        TestRunStepDetails testrunstepdetailsStatus;
        if (TestUtil.findAll(em, TestRunStepDetails.class).isEmpty()) {
            testStatusesRepository.saveAndFlush(testStatuses);
            testrunstepdetailsStatus = TestRunStepDetailsResourceIT.createEntity(em);
        } else {
            testrunstepdetailsStatus = TestUtil.findAll(em, TestRunStepDetails.class).get(0);
        }
        em.persist(testrunstepdetailsStatus);
        em.flush();
        testStatuses.addTestrunstepdetailsStatus(testrunstepdetailsStatus);
        testStatusesRepository.saveAndFlush(testStatuses);
        Long testrunstepdetailsStatusId = testrunstepdetailsStatus.getId();

        // Get all the testStatusesList where testrunstepdetailsStatus equals to testrunstepdetailsStatusId
        defaultTestStatusesShouldBeFound("testrunstepdetailsStatusId.equals=" + testrunstepdetailsStatusId);

        // Get all the testStatusesList where testrunstepdetailsStatus equals to (testrunstepdetailsStatusId + 1)
        defaultTestStatusesShouldNotBeFound("testrunstepdetailsStatusId.equals=" + (testrunstepdetailsStatusId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestStatusesShouldBeFound(String filter) throws Exception {
        restTestStatusesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testStatuses.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusName").value(hasItem(DEFAULT_STATUS_NAME)));

        // Check, that the count call also returns 1
        restTestStatusesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestStatusesShouldNotBeFound(String filter) throws Exception {
        restTestStatusesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestStatusesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestStatuses() throws Exception {
        // Get the testStatuses
        restTestStatusesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestStatuses() throws Exception {
        // Initialize the database
        testStatusesRepository.saveAndFlush(testStatuses);

        int databaseSizeBeforeUpdate = testStatusesRepository.findAll().size();

        // Update the testStatuses
        TestStatuses updatedTestStatuses = testStatusesRepository.findById(testStatuses.getId()).get();
        // Disconnect from session so that the updates on updatedTestStatuses are not directly saved in db
        em.detach(updatedTestStatuses);
        updatedTestStatuses.statusName(UPDATED_STATUS_NAME);

        restTestStatusesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestStatuses.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestStatuses))
            )
            .andExpect(status().isOk());

        // Validate the TestStatuses in the database
        List<TestStatuses> testStatusesList = testStatusesRepository.findAll();
        assertThat(testStatusesList).hasSize(databaseSizeBeforeUpdate);
        TestStatuses testTestStatuses = testStatusesList.get(testStatusesList.size() - 1);
        assertThat(testTestStatuses.getStatusName()).isEqualTo(UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTestStatuses() throws Exception {
        int databaseSizeBeforeUpdate = testStatusesRepository.findAll().size();
        testStatuses.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestStatusesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testStatuses.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testStatuses))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestStatuses in the database
        List<TestStatuses> testStatusesList = testStatusesRepository.findAll();
        assertThat(testStatusesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestStatuses() throws Exception {
        int databaseSizeBeforeUpdate = testStatusesRepository.findAll().size();
        testStatuses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestStatusesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testStatuses))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestStatuses in the database
        List<TestStatuses> testStatusesList = testStatusesRepository.findAll();
        assertThat(testStatusesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestStatuses() throws Exception {
        int databaseSizeBeforeUpdate = testStatusesRepository.findAll().size();
        testStatuses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestStatusesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testStatuses)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestStatuses in the database
        List<TestStatuses> testStatusesList = testStatusesRepository.findAll();
        assertThat(testStatusesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestStatusesWithPatch() throws Exception {
        // Initialize the database
        testStatusesRepository.saveAndFlush(testStatuses);

        int databaseSizeBeforeUpdate = testStatusesRepository.findAll().size();

        // Update the testStatuses using partial update
        TestStatuses partialUpdatedTestStatuses = new TestStatuses();
        partialUpdatedTestStatuses.setId(testStatuses.getId());

        restTestStatusesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestStatuses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestStatuses))
            )
            .andExpect(status().isOk());

        // Validate the TestStatuses in the database
        List<TestStatuses> testStatusesList = testStatusesRepository.findAll();
        assertThat(testStatusesList).hasSize(databaseSizeBeforeUpdate);
        TestStatuses testTestStatuses = testStatusesList.get(testStatusesList.size() - 1);
        assertThat(testTestStatuses.getStatusName()).isEqualTo(DEFAULT_STATUS_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTestStatusesWithPatch() throws Exception {
        // Initialize the database
        testStatusesRepository.saveAndFlush(testStatuses);

        int databaseSizeBeforeUpdate = testStatusesRepository.findAll().size();

        // Update the testStatuses using partial update
        TestStatuses partialUpdatedTestStatuses = new TestStatuses();
        partialUpdatedTestStatuses.setId(testStatuses.getId());

        partialUpdatedTestStatuses.statusName(UPDATED_STATUS_NAME);

        restTestStatusesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestStatuses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestStatuses))
            )
            .andExpect(status().isOk());

        // Validate the TestStatuses in the database
        List<TestStatuses> testStatusesList = testStatusesRepository.findAll();
        assertThat(testStatusesList).hasSize(databaseSizeBeforeUpdate);
        TestStatuses testTestStatuses = testStatusesList.get(testStatusesList.size() - 1);
        assertThat(testTestStatuses.getStatusName()).isEqualTo(UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTestStatuses() throws Exception {
        int databaseSizeBeforeUpdate = testStatusesRepository.findAll().size();
        testStatuses.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestStatusesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testStatuses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testStatuses))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestStatuses in the database
        List<TestStatuses> testStatusesList = testStatusesRepository.findAll();
        assertThat(testStatusesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestStatuses() throws Exception {
        int databaseSizeBeforeUpdate = testStatusesRepository.findAll().size();
        testStatuses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestStatusesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testStatuses))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestStatuses in the database
        List<TestStatuses> testStatusesList = testStatusesRepository.findAll();
        assertThat(testStatusesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestStatuses() throws Exception {
        int databaseSizeBeforeUpdate = testStatusesRepository.findAll().size();
        testStatuses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestStatusesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testStatuses))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestStatuses in the database
        List<TestStatuses> testStatusesList = testStatusesRepository.findAll();
        assertThat(testStatusesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestStatuses() throws Exception {
        // Initialize the database
        testStatusesRepository.saveAndFlush(testStatuses);

        int databaseSizeBeforeDelete = testStatusesRepository.findAll().size();

        // Delete the testStatuses
        restTestStatusesMockMvc
            .perform(delete(ENTITY_API_URL_ID, testStatuses.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestStatuses> testStatusesList = testStatusesRepository.findAll();
        assertThat(testStatusesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
