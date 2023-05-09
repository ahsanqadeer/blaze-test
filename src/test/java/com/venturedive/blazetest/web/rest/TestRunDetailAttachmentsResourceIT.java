package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.TestRunDetailAttachments;
import com.venturedive.blazetest.domain.TestRunDetails;
import com.venturedive.blazetest.repository.TestRunDetailAttachmentsRepository;
import com.venturedive.blazetest.service.criteria.TestRunDetailAttachmentsCriteria;
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
 * Integration tests for the {@link TestRunDetailAttachmentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestRunDetailAttachmentsResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-run-detail-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestRunDetailAttachmentsRepository testRunDetailAttachmentsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestRunDetailAttachmentsMockMvc;

    private TestRunDetailAttachments testRunDetailAttachments;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestRunDetailAttachments createEntity(EntityManager em) {
        TestRunDetailAttachments testRunDetailAttachments = new TestRunDetailAttachments().url(DEFAULT_URL);
        // Add required entity
        TestRunDetails testRunDetails;
        if (TestUtil.findAll(em, TestRunDetails.class).isEmpty()) {
            testRunDetails = TestRunDetailsResourceIT.createEntity(em);
            em.persist(testRunDetails);
            em.flush();
        } else {
            testRunDetails = TestUtil.findAll(em, TestRunDetails.class).get(0);
        }
        testRunDetailAttachments.setTestRunDetail(testRunDetails);
        return testRunDetailAttachments;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestRunDetailAttachments createUpdatedEntity(EntityManager em) {
        TestRunDetailAttachments testRunDetailAttachments = new TestRunDetailAttachments().url(UPDATED_URL);
        // Add required entity
        TestRunDetails testRunDetails;
        if (TestUtil.findAll(em, TestRunDetails.class).isEmpty()) {
            testRunDetails = TestRunDetailsResourceIT.createUpdatedEntity(em);
            em.persist(testRunDetails);
            em.flush();
        } else {
            testRunDetails = TestUtil.findAll(em, TestRunDetails.class).get(0);
        }
        testRunDetailAttachments.setTestRunDetail(testRunDetails);
        return testRunDetailAttachments;
    }

    @BeforeEach
    public void initTest() {
        testRunDetailAttachments = createEntity(em);
    }

    @Test
    @Transactional
    void createTestRunDetailAttachments() throws Exception {
        int databaseSizeBeforeCreate = testRunDetailAttachmentsRepository.findAll().size();
        // Create the TestRunDetailAttachments
        restTestRunDetailAttachmentsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetailAttachments))
            )
            .andExpect(status().isCreated());

        // Validate the TestRunDetailAttachments in the database
        List<TestRunDetailAttachments> testRunDetailAttachmentsList = testRunDetailAttachmentsRepository.findAll();
        assertThat(testRunDetailAttachmentsList).hasSize(databaseSizeBeforeCreate + 1);
        TestRunDetailAttachments testTestRunDetailAttachments = testRunDetailAttachmentsList.get(testRunDetailAttachmentsList.size() - 1);
        assertThat(testTestRunDetailAttachments.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void createTestRunDetailAttachmentsWithExistingId() throws Exception {
        // Create the TestRunDetailAttachments with an existing ID
        testRunDetailAttachments.setId(1L);

        int databaseSizeBeforeCreate = testRunDetailAttachmentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestRunDetailAttachmentsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetailAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunDetailAttachments in the database
        List<TestRunDetailAttachments> testRunDetailAttachmentsList = testRunDetailAttachmentsRepository.findAll();
        assertThat(testRunDetailAttachmentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestRunDetailAttachments() throws Exception {
        // Initialize the database
        testRunDetailAttachmentsRepository.saveAndFlush(testRunDetailAttachments);

        // Get all the testRunDetailAttachmentsList
        restTestRunDetailAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testRunDetailAttachments.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    void getTestRunDetailAttachments() throws Exception {
        // Initialize the database
        testRunDetailAttachmentsRepository.saveAndFlush(testRunDetailAttachments);

        // Get the testRunDetailAttachments
        restTestRunDetailAttachmentsMockMvc
            .perform(get(ENTITY_API_URL_ID, testRunDetailAttachments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testRunDetailAttachments.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    void getTestRunDetailAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        testRunDetailAttachmentsRepository.saveAndFlush(testRunDetailAttachments);

        Long id = testRunDetailAttachments.getId();

        defaultTestRunDetailAttachmentsShouldBeFound("id.equals=" + id);
        defaultTestRunDetailAttachmentsShouldNotBeFound("id.notEquals=" + id);

        defaultTestRunDetailAttachmentsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestRunDetailAttachmentsShouldNotBeFound("id.greaterThan=" + id);

        defaultTestRunDetailAttachmentsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestRunDetailAttachmentsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestRunDetailAttachmentsByTestRunDetailIsEqualToSomething() throws Exception {
        TestRunDetails testRunDetail;
        if (TestUtil.findAll(em, TestRunDetails.class).isEmpty()) {
            testRunDetailAttachmentsRepository.saveAndFlush(testRunDetailAttachments);
            testRunDetail = TestRunDetailsResourceIT.createEntity(em);
        } else {
            testRunDetail = TestUtil.findAll(em, TestRunDetails.class).get(0);
        }
        em.persist(testRunDetail);
        em.flush();
        testRunDetailAttachments.setTestRunDetail(testRunDetail);
        testRunDetailAttachmentsRepository.saveAndFlush(testRunDetailAttachments);
        Long testRunDetailId = testRunDetail.getId();

        // Get all the testRunDetailAttachmentsList where testRunDetail equals to testRunDetailId
        defaultTestRunDetailAttachmentsShouldBeFound("testRunDetailId.equals=" + testRunDetailId);

        // Get all the testRunDetailAttachmentsList where testRunDetail equals to (testRunDetailId + 1)
        defaultTestRunDetailAttachmentsShouldNotBeFound("testRunDetailId.equals=" + (testRunDetailId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestRunDetailAttachmentsShouldBeFound(String filter) throws Exception {
        restTestRunDetailAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testRunDetailAttachments.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));

        // Check, that the count call also returns 1
        restTestRunDetailAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestRunDetailAttachmentsShouldNotBeFound(String filter) throws Exception {
        restTestRunDetailAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestRunDetailAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestRunDetailAttachments() throws Exception {
        // Get the testRunDetailAttachments
        restTestRunDetailAttachmentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestRunDetailAttachments() throws Exception {
        // Initialize the database
        testRunDetailAttachmentsRepository.saveAndFlush(testRunDetailAttachments);

        int databaseSizeBeforeUpdate = testRunDetailAttachmentsRepository.findAll().size();

        // Update the testRunDetailAttachments
        TestRunDetailAttachments updatedTestRunDetailAttachments = testRunDetailAttachmentsRepository
            .findById(testRunDetailAttachments.getId())
            .get();
        // Disconnect from session so that the updates on updatedTestRunDetailAttachments are not directly saved in db
        em.detach(updatedTestRunDetailAttachments);
        updatedTestRunDetailAttachments.url(UPDATED_URL);

        restTestRunDetailAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestRunDetailAttachments.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestRunDetailAttachments))
            )
            .andExpect(status().isOk());

        // Validate the TestRunDetailAttachments in the database
        List<TestRunDetailAttachments> testRunDetailAttachmentsList = testRunDetailAttachmentsRepository.findAll();
        assertThat(testRunDetailAttachmentsList).hasSize(databaseSizeBeforeUpdate);
        TestRunDetailAttachments testTestRunDetailAttachments = testRunDetailAttachmentsList.get(testRunDetailAttachmentsList.size() - 1);
        assertThat(testTestRunDetailAttachments.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void putNonExistingTestRunDetailAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testRunDetailAttachmentsRepository.findAll().size();
        testRunDetailAttachments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestRunDetailAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testRunDetailAttachments.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetailAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunDetailAttachments in the database
        List<TestRunDetailAttachments> testRunDetailAttachmentsList = testRunDetailAttachmentsRepository.findAll();
        assertThat(testRunDetailAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestRunDetailAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testRunDetailAttachmentsRepository.findAll().size();
        testRunDetailAttachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunDetailAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetailAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunDetailAttachments in the database
        List<TestRunDetailAttachments> testRunDetailAttachmentsList = testRunDetailAttachmentsRepository.findAll();
        assertThat(testRunDetailAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestRunDetailAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testRunDetailAttachmentsRepository.findAll().size();
        testRunDetailAttachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunDetailAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetailAttachments))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestRunDetailAttachments in the database
        List<TestRunDetailAttachments> testRunDetailAttachmentsList = testRunDetailAttachmentsRepository.findAll();
        assertThat(testRunDetailAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestRunDetailAttachmentsWithPatch() throws Exception {
        // Initialize the database
        testRunDetailAttachmentsRepository.saveAndFlush(testRunDetailAttachments);

        int databaseSizeBeforeUpdate = testRunDetailAttachmentsRepository.findAll().size();

        // Update the testRunDetailAttachments using partial update
        TestRunDetailAttachments partialUpdatedTestRunDetailAttachments = new TestRunDetailAttachments();
        partialUpdatedTestRunDetailAttachments.setId(testRunDetailAttachments.getId());

        partialUpdatedTestRunDetailAttachments.url(UPDATED_URL);

        restTestRunDetailAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestRunDetailAttachments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestRunDetailAttachments))
            )
            .andExpect(status().isOk());

        // Validate the TestRunDetailAttachments in the database
        List<TestRunDetailAttachments> testRunDetailAttachmentsList = testRunDetailAttachmentsRepository.findAll();
        assertThat(testRunDetailAttachmentsList).hasSize(databaseSizeBeforeUpdate);
        TestRunDetailAttachments testTestRunDetailAttachments = testRunDetailAttachmentsList.get(testRunDetailAttachmentsList.size() - 1);
        assertThat(testTestRunDetailAttachments.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void fullUpdateTestRunDetailAttachmentsWithPatch() throws Exception {
        // Initialize the database
        testRunDetailAttachmentsRepository.saveAndFlush(testRunDetailAttachments);

        int databaseSizeBeforeUpdate = testRunDetailAttachmentsRepository.findAll().size();

        // Update the testRunDetailAttachments using partial update
        TestRunDetailAttachments partialUpdatedTestRunDetailAttachments = new TestRunDetailAttachments();
        partialUpdatedTestRunDetailAttachments.setId(testRunDetailAttachments.getId());

        partialUpdatedTestRunDetailAttachments.url(UPDATED_URL);

        restTestRunDetailAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestRunDetailAttachments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestRunDetailAttachments))
            )
            .andExpect(status().isOk());

        // Validate the TestRunDetailAttachments in the database
        List<TestRunDetailAttachments> testRunDetailAttachmentsList = testRunDetailAttachmentsRepository.findAll();
        assertThat(testRunDetailAttachmentsList).hasSize(databaseSizeBeforeUpdate);
        TestRunDetailAttachments testTestRunDetailAttachments = testRunDetailAttachmentsList.get(testRunDetailAttachmentsList.size() - 1);
        assertThat(testTestRunDetailAttachments.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void patchNonExistingTestRunDetailAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testRunDetailAttachmentsRepository.findAll().size();
        testRunDetailAttachments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestRunDetailAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testRunDetailAttachments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetailAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunDetailAttachments in the database
        List<TestRunDetailAttachments> testRunDetailAttachmentsList = testRunDetailAttachmentsRepository.findAll();
        assertThat(testRunDetailAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestRunDetailAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testRunDetailAttachmentsRepository.findAll().size();
        testRunDetailAttachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunDetailAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetailAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunDetailAttachments in the database
        List<TestRunDetailAttachments> testRunDetailAttachmentsList = testRunDetailAttachmentsRepository.findAll();
        assertThat(testRunDetailAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestRunDetailAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testRunDetailAttachmentsRepository.findAll().size();
        testRunDetailAttachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunDetailAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRunDetailAttachments))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestRunDetailAttachments in the database
        List<TestRunDetailAttachments> testRunDetailAttachmentsList = testRunDetailAttachmentsRepository.findAll();
        assertThat(testRunDetailAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestRunDetailAttachments() throws Exception {
        // Initialize the database
        testRunDetailAttachmentsRepository.saveAndFlush(testRunDetailAttachments);

        int databaseSizeBeforeDelete = testRunDetailAttachmentsRepository.findAll().size();

        // Delete the testRunDetailAttachments
        restTestRunDetailAttachmentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, testRunDetailAttachments.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestRunDetailAttachments> testRunDetailAttachmentsList = testRunDetailAttachmentsRepository.findAll();
        assertThat(testRunDetailAttachmentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
