package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.TestRunStepDetailAttachments;
import com.venturedive.blazetest.domain.TestRunStepDetails;
import com.venturedive.blazetest.repository.TestRunStepDetailAttachmentsRepository;
import com.venturedive.blazetest.service.criteria.TestRunStepDetailAttachmentsCriteria;
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
 * Integration tests for the {@link TestRunStepDetailAttachmentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestRunStepDetailAttachmentsResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-run-step-detail-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestRunStepDetailAttachmentsRepository testRunStepDetailAttachmentsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestRunStepDetailAttachmentsMockMvc;

    private TestRunStepDetailAttachments testRunStepDetailAttachments;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestRunStepDetailAttachments createEntity(EntityManager em) {
        TestRunStepDetailAttachments testRunStepDetailAttachments = new TestRunStepDetailAttachments().url(DEFAULT_URL);
        // Add required entity
        TestRunStepDetails testRunStepDetails;
        if (TestUtil.findAll(em, TestRunStepDetails.class).isEmpty()) {
            testRunStepDetails = TestRunStepDetailsResourceIT.createEntity(em);
            em.persist(testRunStepDetails);
            em.flush();
        } else {
            testRunStepDetails = TestUtil.findAll(em, TestRunStepDetails.class).get(0);
        }
        testRunStepDetailAttachments.setTestRunStepDetail(testRunStepDetails);
        return testRunStepDetailAttachments;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestRunStepDetailAttachments createUpdatedEntity(EntityManager em) {
        TestRunStepDetailAttachments testRunStepDetailAttachments = new TestRunStepDetailAttachments().url(UPDATED_URL);
        // Add required entity
        TestRunStepDetails testRunStepDetails;
        if (TestUtil.findAll(em, TestRunStepDetails.class).isEmpty()) {
            testRunStepDetails = TestRunStepDetailsResourceIT.createUpdatedEntity(em);
            em.persist(testRunStepDetails);
            em.flush();
        } else {
            testRunStepDetails = TestUtil.findAll(em, TestRunStepDetails.class).get(0);
        }
        testRunStepDetailAttachments.setTestRunStepDetail(testRunStepDetails);
        return testRunStepDetailAttachments;
    }

    @BeforeEach
    public void initTest() {
        testRunStepDetailAttachments = createEntity(em);
    }

    @Test
    @Transactional
    void createTestRunStepDetailAttachments() throws Exception {
        int databaseSizeBeforeCreate = testRunStepDetailAttachmentsRepository.findAll().size();
        // Create the TestRunStepDetailAttachments
        restTestRunStepDetailAttachmentsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetailAttachments))
            )
            .andExpect(status().isCreated());

        // Validate the TestRunStepDetailAttachments in the database
        List<TestRunStepDetailAttachments> testRunStepDetailAttachmentsList = testRunStepDetailAttachmentsRepository.findAll();
        assertThat(testRunStepDetailAttachmentsList).hasSize(databaseSizeBeforeCreate + 1);
        TestRunStepDetailAttachments testTestRunStepDetailAttachments = testRunStepDetailAttachmentsList.get(
            testRunStepDetailAttachmentsList.size() - 1
        );
        assertThat(testTestRunStepDetailAttachments.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void createTestRunStepDetailAttachmentsWithExistingId() throws Exception {
        // Create the TestRunStepDetailAttachments with an existing ID
        testRunStepDetailAttachments.setId(1L);

        int databaseSizeBeforeCreate = testRunStepDetailAttachmentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestRunStepDetailAttachmentsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetailAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunStepDetailAttachments in the database
        List<TestRunStepDetailAttachments> testRunStepDetailAttachmentsList = testRunStepDetailAttachmentsRepository.findAll();
        assertThat(testRunStepDetailAttachmentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestRunStepDetailAttachments() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentsRepository.saveAndFlush(testRunStepDetailAttachments);

        // Get all the testRunStepDetailAttachmentsList
        restTestRunStepDetailAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testRunStepDetailAttachments.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    void getTestRunStepDetailAttachments() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentsRepository.saveAndFlush(testRunStepDetailAttachments);

        // Get the testRunStepDetailAttachments
        restTestRunStepDetailAttachmentsMockMvc
            .perform(get(ENTITY_API_URL_ID, testRunStepDetailAttachments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testRunStepDetailAttachments.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    void getTestRunStepDetailAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentsRepository.saveAndFlush(testRunStepDetailAttachments);

        Long id = testRunStepDetailAttachments.getId();

        defaultTestRunStepDetailAttachmentsShouldBeFound("id.equals=" + id);
        defaultTestRunStepDetailAttachmentsShouldNotBeFound("id.notEquals=" + id);

        defaultTestRunStepDetailAttachmentsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestRunStepDetailAttachmentsShouldNotBeFound("id.greaterThan=" + id);

        defaultTestRunStepDetailAttachmentsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestRunStepDetailAttachmentsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestRunStepDetailAttachmentsByTestRunStepDetailIsEqualToSomething() throws Exception {
        TestRunStepDetails testRunStepDetail;
        if (TestUtil.findAll(em, TestRunStepDetails.class).isEmpty()) {
            testRunStepDetailAttachmentsRepository.saveAndFlush(testRunStepDetailAttachments);
            testRunStepDetail = TestRunStepDetailsResourceIT.createEntity(em);
        } else {
            testRunStepDetail = TestUtil.findAll(em, TestRunStepDetails.class).get(0);
        }
        em.persist(testRunStepDetail);
        em.flush();
        testRunStepDetailAttachments.setTestRunStepDetail(testRunStepDetail);
        testRunStepDetailAttachmentsRepository.saveAndFlush(testRunStepDetailAttachments);
        Long testRunStepDetailId = testRunStepDetail.getId();

        // Get all the testRunStepDetailAttachmentsList where testRunStepDetail equals to testRunStepDetailId
        defaultTestRunStepDetailAttachmentsShouldBeFound("testRunStepDetailId.equals=" + testRunStepDetailId);

        // Get all the testRunStepDetailAttachmentsList where testRunStepDetail equals to (testRunStepDetailId + 1)
        defaultTestRunStepDetailAttachmentsShouldNotBeFound("testRunStepDetailId.equals=" + (testRunStepDetailId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestRunStepDetailAttachmentsShouldBeFound(String filter) throws Exception {
        restTestRunStepDetailAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testRunStepDetailAttachments.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));

        // Check, that the count call also returns 1
        restTestRunStepDetailAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestRunStepDetailAttachmentsShouldNotBeFound(String filter) throws Exception {
        restTestRunStepDetailAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestRunStepDetailAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestRunStepDetailAttachments() throws Exception {
        // Get the testRunStepDetailAttachments
        restTestRunStepDetailAttachmentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestRunStepDetailAttachments() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentsRepository.saveAndFlush(testRunStepDetailAttachments);

        int databaseSizeBeforeUpdate = testRunStepDetailAttachmentsRepository.findAll().size();

        // Update the testRunStepDetailAttachments
        TestRunStepDetailAttachments updatedTestRunStepDetailAttachments = testRunStepDetailAttachmentsRepository
            .findById(testRunStepDetailAttachments.getId())
            .get();
        // Disconnect from session so that the updates on updatedTestRunStepDetailAttachments are not directly saved in db
        em.detach(updatedTestRunStepDetailAttachments);
        updatedTestRunStepDetailAttachments.url(UPDATED_URL);

        restTestRunStepDetailAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestRunStepDetailAttachments.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestRunStepDetailAttachments))
            )
            .andExpect(status().isOk());

        // Validate the TestRunStepDetailAttachments in the database
        List<TestRunStepDetailAttachments> testRunStepDetailAttachmentsList = testRunStepDetailAttachmentsRepository.findAll();
        assertThat(testRunStepDetailAttachmentsList).hasSize(databaseSizeBeforeUpdate);
        TestRunStepDetailAttachments testTestRunStepDetailAttachments = testRunStepDetailAttachmentsList.get(
            testRunStepDetailAttachmentsList.size() - 1
        );
        assertThat(testTestRunStepDetailAttachments.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void putNonExistingTestRunStepDetailAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testRunStepDetailAttachmentsRepository.findAll().size();
        testRunStepDetailAttachments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestRunStepDetailAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testRunStepDetailAttachments.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetailAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunStepDetailAttachments in the database
        List<TestRunStepDetailAttachments> testRunStepDetailAttachmentsList = testRunStepDetailAttachmentsRepository.findAll();
        assertThat(testRunStepDetailAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestRunStepDetailAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testRunStepDetailAttachmentsRepository.findAll().size();
        testRunStepDetailAttachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunStepDetailAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetailAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunStepDetailAttachments in the database
        List<TestRunStepDetailAttachments> testRunStepDetailAttachmentsList = testRunStepDetailAttachmentsRepository.findAll();
        assertThat(testRunStepDetailAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestRunStepDetailAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testRunStepDetailAttachmentsRepository.findAll().size();
        testRunStepDetailAttachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunStepDetailAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetailAttachments))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestRunStepDetailAttachments in the database
        List<TestRunStepDetailAttachments> testRunStepDetailAttachmentsList = testRunStepDetailAttachmentsRepository.findAll();
        assertThat(testRunStepDetailAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestRunStepDetailAttachmentsWithPatch() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentsRepository.saveAndFlush(testRunStepDetailAttachments);

        int databaseSizeBeforeUpdate = testRunStepDetailAttachmentsRepository.findAll().size();

        // Update the testRunStepDetailAttachments using partial update
        TestRunStepDetailAttachments partialUpdatedTestRunStepDetailAttachments = new TestRunStepDetailAttachments();
        partialUpdatedTestRunStepDetailAttachments.setId(testRunStepDetailAttachments.getId());

        restTestRunStepDetailAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestRunStepDetailAttachments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestRunStepDetailAttachments))
            )
            .andExpect(status().isOk());

        // Validate the TestRunStepDetailAttachments in the database
        List<TestRunStepDetailAttachments> testRunStepDetailAttachmentsList = testRunStepDetailAttachmentsRepository.findAll();
        assertThat(testRunStepDetailAttachmentsList).hasSize(databaseSizeBeforeUpdate);
        TestRunStepDetailAttachments testTestRunStepDetailAttachments = testRunStepDetailAttachmentsList.get(
            testRunStepDetailAttachmentsList.size() - 1
        );
        assertThat(testTestRunStepDetailAttachments.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void fullUpdateTestRunStepDetailAttachmentsWithPatch() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentsRepository.saveAndFlush(testRunStepDetailAttachments);

        int databaseSizeBeforeUpdate = testRunStepDetailAttachmentsRepository.findAll().size();

        // Update the testRunStepDetailAttachments using partial update
        TestRunStepDetailAttachments partialUpdatedTestRunStepDetailAttachments = new TestRunStepDetailAttachments();
        partialUpdatedTestRunStepDetailAttachments.setId(testRunStepDetailAttachments.getId());

        partialUpdatedTestRunStepDetailAttachments.url(UPDATED_URL);

        restTestRunStepDetailAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestRunStepDetailAttachments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestRunStepDetailAttachments))
            )
            .andExpect(status().isOk());

        // Validate the TestRunStepDetailAttachments in the database
        List<TestRunStepDetailAttachments> testRunStepDetailAttachmentsList = testRunStepDetailAttachmentsRepository.findAll();
        assertThat(testRunStepDetailAttachmentsList).hasSize(databaseSizeBeforeUpdate);
        TestRunStepDetailAttachments testTestRunStepDetailAttachments = testRunStepDetailAttachmentsList.get(
            testRunStepDetailAttachmentsList.size() - 1
        );
        assertThat(testTestRunStepDetailAttachments.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void patchNonExistingTestRunStepDetailAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testRunStepDetailAttachmentsRepository.findAll().size();
        testRunStepDetailAttachments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestRunStepDetailAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testRunStepDetailAttachments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetailAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunStepDetailAttachments in the database
        List<TestRunStepDetailAttachments> testRunStepDetailAttachmentsList = testRunStepDetailAttachmentsRepository.findAll();
        assertThat(testRunStepDetailAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestRunStepDetailAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testRunStepDetailAttachmentsRepository.findAll().size();
        testRunStepDetailAttachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunStepDetailAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetailAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestRunStepDetailAttachments in the database
        List<TestRunStepDetailAttachments> testRunStepDetailAttachmentsList = testRunStepDetailAttachmentsRepository.findAll();
        assertThat(testRunStepDetailAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestRunStepDetailAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testRunStepDetailAttachmentsRepository.findAll().size();
        testRunStepDetailAttachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestRunStepDetailAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testRunStepDetailAttachments))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestRunStepDetailAttachments in the database
        List<TestRunStepDetailAttachments> testRunStepDetailAttachmentsList = testRunStepDetailAttachmentsRepository.findAll();
        assertThat(testRunStepDetailAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestRunStepDetailAttachments() throws Exception {
        // Initialize the database
        testRunStepDetailAttachmentsRepository.saveAndFlush(testRunStepDetailAttachments);

        int databaseSizeBeforeDelete = testRunStepDetailAttachmentsRepository.findAll().size();

        // Delete the testRunStepDetailAttachments
        restTestRunStepDetailAttachmentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, testRunStepDetailAttachments.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestRunStepDetailAttachments> testRunStepDetailAttachmentsList = testRunStepDetailAttachmentsRepository.findAll();
        assertThat(testRunStepDetailAttachmentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
