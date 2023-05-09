package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.TestCaseAttachments;
import com.venturedive.blazetest.domain.TestCases;
import com.venturedive.blazetest.repository.TestCaseAttachmentsRepository;
import com.venturedive.blazetest.service.criteria.TestCaseAttachmentsCriteria;
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
 * Integration tests for the {@link TestCaseAttachmentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestCaseAttachmentsResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-case-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestCaseAttachmentsRepository testCaseAttachmentsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestCaseAttachmentsMockMvc;

    private TestCaseAttachments testCaseAttachments;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCaseAttachments createEntity(EntityManager em) {
        TestCaseAttachments testCaseAttachments = new TestCaseAttachments().url(DEFAULT_URL);
        // Add required entity
        TestCases testCases;
        if (TestUtil.findAll(em, TestCases.class).isEmpty()) {
            testCases = TestCasesResourceIT.createEntity(em);
            em.persist(testCases);
            em.flush();
        } else {
            testCases = TestUtil.findAll(em, TestCases.class).get(0);
        }
        testCaseAttachments.setTestCase(testCases);
        return testCaseAttachments;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCaseAttachments createUpdatedEntity(EntityManager em) {
        TestCaseAttachments testCaseAttachments = new TestCaseAttachments().url(UPDATED_URL);
        // Add required entity
        TestCases testCases;
        if (TestUtil.findAll(em, TestCases.class).isEmpty()) {
            testCases = TestCasesResourceIT.createUpdatedEntity(em);
            em.persist(testCases);
            em.flush();
        } else {
            testCases = TestUtil.findAll(em, TestCases.class).get(0);
        }
        testCaseAttachments.setTestCase(testCases);
        return testCaseAttachments;
    }

    @BeforeEach
    public void initTest() {
        testCaseAttachments = createEntity(em);
    }

    @Test
    @Transactional
    void createTestCaseAttachments() throws Exception {
        int databaseSizeBeforeCreate = testCaseAttachmentsRepository.findAll().size();
        // Create the TestCaseAttachments
        restTestCaseAttachmentsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseAttachments))
            )
            .andExpect(status().isCreated());

        // Validate the TestCaseAttachments in the database
        List<TestCaseAttachments> testCaseAttachmentsList = testCaseAttachmentsRepository.findAll();
        assertThat(testCaseAttachmentsList).hasSize(databaseSizeBeforeCreate + 1);
        TestCaseAttachments testTestCaseAttachments = testCaseAttachmentsList.get(testCaseAttachmentsList.size() - 1);
        assertThat(testTestCaseAttachments.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void createTestCaseAttachmentsWithExistingId() throws Exception {
        // Create the TestCaseAttachments with an existing ID
        testCaseAttachments.setId(1L);

        int databaseSizeBeforeCreate = testCaseAttachmentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCaseAttachmentsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseAttachments in the database
        List<TestCaseAttachments> testCaseAttachmentsList = testCaseAttachmentsRepository.findAll();
        assertThat(testCaseAttachmentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestCaseAttachments() throws Exception {
        // Initialize the database
        testCaseAttachmentsRepository.saveAndFlush(testCaseAttachments);

        // Get all the testCaseAttachmentsList
        restTestCaseAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCaseAttachments.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    void getTestCaseAttachments() throws Exception {
        // Initialize the database
        testCaseAttachmentsRepository.saveAndFlush(testCaseAttachments);

        // Get the testCaseAttachments
        restTestCaseAttachmentsMockMvc
            .perform(get(ENTITY_API_URL_ID, testCaseAttachments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testCaseAttachments.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    void getTestCaseAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        testCaseAttachmentsRepository.saveAndFlush(testCaseAttachments);

        Long id = testCaseAttachments.getId();

        defaultTestCaseAttachmentsShouldBeFound("id.equals=" + id);
        defaultTestCaseAttachmentsShouldNotBeFound("id.notEquals=" + id);

        defaultTestCaseAttachmentsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestCaseAttachmentsShouldNotBeFound("id.greaterThan=" + id);

        defaultTestCaseAttachmentsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestCaseAttachmentsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestCaseAttachmentsByTestCaseIsEqualToSomething() throws Exception {
        TestCases testCase;
        if (TestUtil.findAll(em, TestCases.class).isEmpty()) {
            testCaseAttachmentsRepository.saveAndFlush(testCaseAttachments);
            testCase = TestCasesResourceIT.createEntity(em);
        } else {
            testCase = TestUtil.findAll(em, TestCases.class).get(0);
        }
        em.persist(testCase);
        em.flush();
        testCaseAttachments.setTestCase(testCase);
        testCaseAttachmentsRepository.saveAndFlush(testCaseAttachments);
        Long testCaseId = testCase.getId();

        // Get all the testCaseAttachmentsList where testCase equals to testCaseId
        defaultTestCaseAttachmentsShouldBeFound("testCaseId.equals=" + testCaseId);

        // Get all the testCaseAttachmentsList where testCase equals to (testCaseId + 1)
        defaultTestCaseAttachmentsShouldNotBeFound("testCaseId.equals=" + (testCaseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestCaseAttachmentsShouldBeFound(String filter) throws Exception {
        restTestCaseAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCaseAttachments.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));

        // Check, that the count call also returns 1
        restTestCaseAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestCaseAttachmentsShouldNotBeFound(String filter) throws Exception {
        restTestCaseAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestCaseAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestCaseAttachments() throws Exception {
        // Get the testCaseAttachments
        restTestCaseAttachmentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestCaseAttachments() throws Exception {
        // Initialize the database
        testCaseAttachmentsRepository.saveAndFlush(testCaseAttachments);

        int databaseSizeBeforeUpdate = testCaseAttachmentsRepository.findAll().size();

        // Update the testCaseAttachments
        TestCaseAttachments updatedTestCaseAttachments = testCaseAttachmentsRepository.findById(testCaseAttachments.getId()).get();
        // Disconnect from session so that the updates on updatedTestCaseAttachments are not directly saved in db
        em.detach(updatedTestCaseAttachments);
        updatedTestCaseAttachments.url(UPDATED_URL);

        restTestCaseAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestCaseAttachments.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestCaseAttachments))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseAttachments in the database
        List<TestCaseAttachments> testCaseAttachmentsList = testCaseAttachmentsRepository.findAll();
        assertThat(testCaseAttachmentsList).hasSize(databaseSizeBeforeUpdate);
        TestCaseAttachments testTestCaseAttachments = testCaseAttachmentsList.get(testCaseAttachmentsList.size() - 1);
        assertThat(testTestCaseAttachments.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void putNonExistingTestCaseAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testCaseAttachmentsRepository.findAll().size();
        testCaseAttachments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCaseAttachments.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseAttachments in the database
        List<TestCaseAttachments> testCaseAttachmentsList = testCaseAttachmentsRepository.findAll();
        assertThat(testCaseAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestCaseAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testCaseAttachmentsRepository.findAll().size();
        testCaseAttachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseAttachments in the database
        List<TestCaseAttachments> testCaseAttachmentsList = testCaseAttachmentsRepository.findAll();
        assertThat(testCaseAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestCaseAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testCaseAttachmentsRepository.findAll().size();
        testCaseAttachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseAttachments))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCaseAttachments in the database
        List<TestCaseAttachments> testCaseAttachmentsList = testCaseAttachmentsRepository.findAll();
        assertThat(testCaseAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestCaseAttachmentsWithPatch() throws Exception {
        // Initialize the database
        testCaseAttachmentsRepository.saveAndFlush(testCaseAttachments);

        int databaseSizeBeforeUpdate = testCaseAttachmentsRepository.findAll().size();

        // Update the testCaseAttachments using partial update
        TestCaseAttachments partialUpdatedTestCaseAttachments = new TestCaseAttachments();
        partialUpdatedTestCaseAttachments.setId(testCaseAttachments.getId());

        partialUpdatedTestCaseAttachments.url(UPDATED_URL);

        restTestCaseAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCaseAttachments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCaseAttachments))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseAttachments in the database
        List<TestCaseAttachments> testCaseAttachmentsList = testCaseAttachmentsRepository.findAll();
        assertThat(testCaseAttachmentsList).hasSize(databaseSizeBeforeUpdate);
        TestCaseAttachments testTestCaseAttachments = testCaseAttachmentsList.get(testCaseAttachmentsList.size() - 1);
        assertThat(testTestCaseAttachments.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void fullUpdateTestCaseAttachmentsWithPatch() throws Exception {
        // Initialize the database
        testCaseAttachmentsRepository.saveAndFlush(testCaseAttachments);

        int databaseSizeBeforeUpdate = testCaseAttachmentsRepository.findAll().size();

        // Update the testCaseAttachments using partial update
        TestCaseAttachments partialUpdatedTestCaseAttachments = new TestCaseAttachments();
        partialUpdatedTestCaseAttachments.setId(testCaseAttachments.getId());

        partialUpdatedTestCaseAttachments.url(UPDATED_URL);

        restTestCaseAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCaseAttachments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCaseAttachments))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseAttachments in the database
        List<TestCaseAttachments> testCaseAttachmentsList = testCaseAttachmentsRepository.findAll();
        assertThat(testCaseAttachmentsList).hasSize(databaseSizeBeforeUpdate);
        TestCaseAttachments testTestCaseAttachments = testCaseAttachmentsList.get(testCaseAttachmentsList.size() - 1);
        assertThat(testTestCaseAttachments.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void patchNonExistingTestCaseAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testCaseAttachmentsRepository.findAll().size();
        testCaseAttachments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testCaseAttachments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCaseAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseAttachments in the database
        List<TestCaseAttachments> testCaseAttachmentsList = testCaseAttachmentsRepository.findAll();
        assertThat(testCaseAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestCaseAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testCaseAttachmentsRepository.findAll().size();
        testCaseAttachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCaseAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseAttachments in the database
        List<TestCaseAttachments> testCaseAttachmentsList = testCaseAttachmentsRepository.findAll();
        assertThat(testCaseAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestCaseAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testCaseAttachmentsRepository.findAll().size();
        testCaseAttachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCaseAttachments))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCaseAttachments in the database
        List<TestCaseAttachments> testCaseAttachmentsList = testCaseAttachmentsRepository.findAll();
        assertThat(testCaseAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestCaseAttachments() throws Exception {
        // Initialize the database
        testCaseAttachmentsRepository.saveAndFlush(testCaseAttachments);

        int databaseSizeBeforeDelete = testCaseAttachmentsRepository.findAll().size();

        // Delete the testCaseAttachments
        restTestCaseAttachmentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, testCaseAttachments.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestCaseAttachments> testCaseAttachmentsList = testCaseAttachmentsRepository.findAll();
        assertThat(testCaseAttachmentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
