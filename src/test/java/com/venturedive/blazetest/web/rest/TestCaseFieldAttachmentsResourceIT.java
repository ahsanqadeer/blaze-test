package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.TestCaseFieldAttachments;
import com.venturedive.blazetest.domain.TestCaseFields;
import com.venturedive.blazetest.repository.TestCaseFieldAttachmentsRepository;
import com.venturedive.blazetest.service.criteria.TestCaseFieldAttachmentsCriteria;
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
 * Integration tests for the {@link TestCaseFieldAttachmentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestCaseFieldAttachmentsResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-case-field-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestCaseFieldAttachmentsRepository testCaseFieldAttachmentsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestCaseFieldAttachmentsMockMvc;

    private TestCaseFieldAttachments testCaseFieldAttachments;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCaseFieldAttachments createEntity(EntityManager em) {
        TestCaseFieldAttachments testCaseFieldAttachments = new TestCaseFieldAttachments().url(DEFAULT_URL);
        // Add required entity
        TestCaseFields testCaseFields;
        if (TestUtil.findAll(em, TestCaseFields.class).isEmpty()) {
            testCaseFields = TestCaseFieldsResourceIT.createEntity(em);
            em.persist(testCaseFields);
            em.flush();
        } else {
            testCaseFields = TestUtil.findAll(em, TestCaseFields.class).get(0);
        }
        testCaseFieldAttachments.setTestCaseField(testCaseFields);
        return testCaseFieldAttachments;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCaseFieldAttachments createUpdatedEntity(EntityManager em) {
        TestCaseFieldAttachments testCaseFieldAttachments = new TestCaseFieldAttachments().url(UPDATED_URL);
        // Add required entity
        TestCaseFields testCaseFields;
        if (TestUtil.findAll(em, TestCaseFields.class).isEmpty()) {
            testCaseFields = TestCaseFieldsResourceIT.createUpdatedEntity(em);
            em.persist(testCaseFields);
            em.flush();
        } else {
            testCaseFields = TestUtil.findAll(em, TestCaseFields.class).get(0);
        }
        testCaseFieldAttachments.setTestCaseField(testCaseFields);
        return testCaseFieldAttachments;
    }

    @BeforeEach
    public void initTest() {
        testCaseFieldAttachments = createEntity(em);
    }

    @Test
    @Transactional
    void createTestCaseFieldAttachments() throws Exception {
        int databaseSizeBeforeCreate = testCaseFieldAttachmentsRepository.findAll().size();
        // Create the TestCaseFieldAttachments
        restTestCaseFieldAttachmentsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFieldAttachments))
            )
            .andExpect(status().isCreated());

        // Validate the TestCaseFieldAttachments in the database
        List<TestCaseFieldAttachments> testCaseFieldAttachmentsList = testCaseFieldAttachmentsRepository.findAll();
        assertThat(testCaseFieldAttachmentsList).hasSize(databaseSizeBeforeCreate + 1);
        TestCaseFieldAttachments testTestCaseFieldAttachments = testCaseFieldAttachmentsList.get(testCaseFieldAttachmentsList.size() - 1);
        assertThat(testTestCaseFieldAttachments.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void createTestCaseFieldAttachmentsWithExistingId() throws Exception {
        // Create the TestCaseFieldAttachments with an existing ID
        testCaseFieldAttachments.setId(1L);

        int databaseSizeBeforeCreate = testCaseFieldAttachmentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCaseFieldAttachmentsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFieldAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseFieldAttachments in the database
        List<TestCaseFieldAttachments> testCaseFieldAttachmentsList = testCaseFieldAttachmentsRepository.findAll();
        assertThat(testCaseFieldAttachmentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldAttachments() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentsRepository.saveAndFlush(testCaseFieldAttachments);

        // Get all the testCaseFieldAttachmentsList
        restTestCaseFieldAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCaseFieldAttachments.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    void getTestCaseFieldAttachments() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentsRepository.saveAndFlush(testCaseFieldAttachments);

        // Get the testCaseFieldAttachments
        restTestCaseFieldAttachmentsMockMvc
            .perform(get(ENTITY_API_URL_ID, testCaseFieldAttachments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testCaseFieldAttachments.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    void getTestCaseFieldAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentsRepository.saveAndFlush(testCaseFieldAttachments);

        Long id = testCaseFieldAttachments.getId();

        defaultTestCaseFieldAttachmentsShouldBeFound("id.equals=" + id);
        defaultTestCaseFieldAttachmentsShouldNotBeFound("id.notEquals=" + id);

        defaultTestCaseFieldAttachmentsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestCaseFieldAttachmentsShouldNotBeFound("id.greaterThan=" + id);

        defaultTestCaseFieldAttachmentsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestCaseFieldAttachmentsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldAttachmentsByTestCaseFieldIsEqualToSomething() throws Exception {
        TestCaseFields testCaseField;
        if (TestUtil.findAll(em, TestCaseFields.class).isEmpty()) {
            testCaseFieldAttachmentsRepository.saveAndFlush(testCaseFieldAttachments);
            testCaseField = TestCaseFieldsResourceIT.createEntity(em);
        } else {
            testCaseField = TestUtil.findAll(em, TestCaseFields.class).get(0);
        }
        em.persist(testCaseField);
        em.flush();
        testCaseFieldAttachments.setTestCaseField(testCaseField);
        testCaseFieldAttachmentsRepository.saveAndFlush(testCaseFieldAttachments);
        Long testCaseFieldId = testCaseField.getId();

        // Get all the testCaseFieldAttachmentsList where testCaseField equals to testCaseFieldId
        defaultTestCaseFieldAttachmentsShouldBeFound("testCaseFieldId.equals=" + testCaseFieldId);

        // Get all the testCaseFieldAttachmentsList where testCaseField equals to (testCaseFieldId + 1)
        defaultTestCaseFieldAttachmentsShouldNotBeFound("testCaseFieldId.equals=" + (testCaseFieldId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestCaseFieldAttachmentsShouldBeFound(String filter) throws Exception {
        restTestCaseFieldAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCaseFieldAttachments.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));

        // Check, that the count call also returns 1
        restTestCaseFieldAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestCaseFieldAttachmentsShouldNotBeFound(String filter) throws Exception {
        restTestCaseFieldAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestCaseFieldAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestCaseFieldAttachments() throws Exception {
        // Get the testCaseFieldAttachments
        restTestCaseFieldAttachmentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestCaseFieldAttachments() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentsRepository.saveAndFlush(testCaseFieldAttachments);

        int databaseSizeBeforeUpdate = testCaseFieldAttachmentsRepository.findAll().size();

        // Update the testCaseFieldAttachments
        TestCaseFieldAttachments updatedTestCaseFieldAttachments = testCaseFieldAttachmentsRepository
            .findById(testCaseFieldAttachments.getId())
            .get();
        // Disconnect from session so that the updates on updatedTestCaseFieldAttachments are not directly saved in db
        em.detach(updatedTestCaseFieldAttachments);
        updatedTestCaseFieldAttachments.url(UPDATED_URL);

        restTestCaseFieldAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestCaseFieldAttachments.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestCaseFieldAttachments))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseFieldAttachments in the database
        List<TestCaseFieldAttachments> testCaseFieldAttachmentsList = testCaseFieldAttachmentsRepository.findAll();
        assertThat(testCaseFieldAttachmentsList).hasSize(databaseSizeBeforeUpdate);
        TestCaseFieldAttachments testTestCaseFieldAttachments = testCaseFieldAttachmentsList.get(testCaseFieldAttachmentsList.size() - 1);
        assertThat(testTestCaseFieldAttachments.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void putNonExistingTestCaseFieldAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldAttachmentsRepository.findAll().size();
        testCaseFieldAttachments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseFieldAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCaseFieldAttachments.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFieldAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseFieldAttachments in the database
        List<TestCaseFieldAttachments> testCaseFieldAttachmentsList = testCaseFieldAttachmentsRepository.findAll();
        assertThat(testCaseFieldAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestCaseFieldAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldAttachmentsRepository.findAll().size();
        testCaseFieldAttachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseFieldAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFieldAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseFieldAttachments in the database
        List<TestCaseFieldAttachments> testCaseFieldAttachmentsList = testCaseFieldAttachmentsRepository.findAll();
        assertThat(testCaseFieldAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestCaseFieldAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldAttachmentsRepository.findAll().size();
        testCaseFieldAttachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseFieldAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFieldAttachments))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCaseFieldAttachments in the database
        List<TestCaseFieldAttachments> testCaseFieldAttachmentsList = testCaseFieldAttachmentsRepository.findAll();
        assertThat(testCaseFieldAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestCaseFieldAttachmentsWithPatch() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentsRepository.saveAndFlush(testCaseFieldAttachments);

        int databaseSizeBeforeUpdate = testCaseFieldAttachmentsRepository.findAll().size();

        // Update the testCaseFieldAttachments using partial update
        TestCaseFieldAttachments partialUpdatedTestCaseFieldAttachments = new TestCaseFieldAttachments();
        partialUpdatedTestCaseFieldAttachments.setId(testCaseFieldAttachments.getId());

        partialUpdatedTestCaseFieldAttachments.url(UPDATED_URL);

        restTestCaseFieldAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCaseFieldAttachments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCaseFieldAttachments))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseFieldAttachments in the database
        List<TestCaseFieldAttachments> testCaseFieldAttachmentsList = testCaseFieldAttachmentsRepository.findAll();
        assertThat(testCaseFieldAttachmentsList).hasSize(databaseSizeBeforeUpdate);
        TestCaseFieldAttachments testTestCaseFieldAttachments = testCaseFieldAttachmentsList.get(testCaseFieldAttachmentsList.size() - 1);
        assertThat(testTestCaseFieldAttachments.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void fullUpdateTestCaseFieldAttachmentsWithPatch() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentsRepository.saveAndFlush(testCaseFieldAttachments);

        int databaseSizeBeforeUpdate = testCaseFieldAttachmentsRepository.findAll().size();

        // Update the testCaseFieldAttachments using partial update
        TestCaseFieldAttachments partialUpdatedTestCaseFieldAttachments = new TestCaseFieldAttachments();
        partialUpdatedTestCaseFieldAttachments.setId(testCaseFieldAttachments.getId());

        partialUpdatedTestCaseFieldAttachments.url(UPDATED_URL);

        restTestCaseFieldAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCaseFieldAttachments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCaseFieldAttachments))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseFieldAttachments in the database
        List<TestCaseFieldAttachments> testCaseFieldAttachmentsList = testCaseFieldAttachmentsRepository.findAll();
        assertThat(testCaseFieldAttachmentsList).hasSize(databaseSizeBeforeUpdate);
        TestCaseFieldAttachments testTestCaseFieldAttachments = testCaseFieldAttachmentsList.get(testCaseFieldAttachmentsList.size() - 1);
        assertThat(testTestCaseFieldAttachments.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void patchNonExistingTestCaseFieldAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldAttachmentsRepository.findAll().size();
        testCaseFieldAttachments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseFieldAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testCaseFieldAttachments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFieldAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseFieldAttachments in the database
        List<TestCaseFieldAttachments> testCaseFieldAttachmentsList = testCaseFieldAttachmentsRepository.findAll();
        assertThat(testCaseFieldAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestCaseFieldAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldAttachmentsRepository.findAll().size();
        testCaseFieldAttachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseFieldAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFieldAttachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseFieldAttachments in the database
        List<TestCaseFieldAttachments> testCaseFieldAttachmentsList = testCaseFieldAttachmentsRepository.findAll();
        assertThat(testCaseFieldAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestCaseFieldAttachments() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldAttachmentsRepository.findAll().size();
        testCaseFieldAttachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseFieldAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFieldAttachments))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCaseFieldAttachments in the database
        List<TestCaseFieldAttachments> testCaseFieldAttachmentsList = testCaseFieldAttachmentsRepository.findAll();
        assertThat(testCaseFieldAttachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestCaseFieldAttachments() throws Exception {
        // Initialize the database
        testCaseFieldAttachmentsRepository.saveAndFlush(testCaseFieldAttachments);

        int databaseSizeBeforeDelete = testCaseFieldAttachmentsRepository.findAll().size();

        // Delete the testCaseFieldAttachments
        restTestCaseFieldAttachmentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, testCaseFieldAttachments.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestCaseFieldAttachments> testCaseFieldAttachmentsList = testCaseFieldAttachmentsRepository.findAll();
        assertThat(testCaseFieldAttachmentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
