package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.TemplateFields;
import com.venturedive.blazetest.domain.TestCaseFieldAttachments;
import com.venturedive.blazetest.domain.TestCaseFields;
import com.venturedive.blazetest.domain.TestCases;
import com.venturedive.blazetest.domain.TestRunStepDetails;
import com.venturedive.blazetest.repository.TestCaseFieldsRepository;
import com.venturedive.blazetest.service.criteria.TestCaseFieldsCriteria;
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
 * Integration tests for the {@link TestCaseFieldsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestCaseFieldsResourceIT {

    private static final String DEFAULT_EXPECTED_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_EXPECTED_RESULT = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-case-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestCaseFieldsRepository testCaseFieldsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestCaseFieldsMockMvc;

    private TestCaseFields testCaseFields;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCaseFields createEntity(EntityManager em) {
        TestCaseFields testCaseFields = new TestCaseFields().expectedResult(DEFAULT_EXPECTED_RESULT).value(DEFAULT_VALUE);
        // Add required entity
        TemplateFields templateFields;
        if (TestUtil.findAll(em, TemplateFields.class).isEmpty()) {
            templateFields = TemplateFieldsResourceIT.createEntity(em);
            em.persist(templateFields);
            em.flush();
        } else {
            templateFields = TestUtil.findAll(em, TemplateFields.class).get(0);
        }
        testCaseFields.setTemplateField(templateFields);
        // Add required entity
        TestCases testCases;
        if (TestUtil.findAll(em, TestCases.class).isEmpty()) {
            testCases = TestCasesResourceIT.createEntity(em);
            em.persist(testCases);
            em.flush();
        } else {
            testCases = TestUtil.findAll(em, TestCases.class).get(0);
        }
        testCaseFields.setTestCase(testCases);
        return testCaseFields;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCaseFields createUpdatedEntity(EntityManager em) {
        TestCaseFields testCaseFields = new TestCaseFields().expectedResult(UPDATED_EXPECTED_RESULT).value(UPDATED_VALUE);
        // Add required entity
        TemplateFields templateFields;
        if (TestUtil.findAll(em, TemplateFields.class).isEmpty()) {
            templateFields = TemplateFieldsResourceIT.createUpdatedEntity(em);
            em.persist(templateFields);
            em.flush();
        } else {
            templateFields = TestUtil.findAll(em, TemplateFields.class).get(0);
        }
        testCaseFields.setTemplateField(templateFields);
        // Add required entity
        TestCases testCases;
        if (TestUtil.findAll(em, TestCases.class).isEmpty()) {
            testCases = TestCasesResourceIT.createUpdatedEntity(em);
            em.persist(testCases);
            em.flush();
        } else {
            testCases = TestUtil.findAll(em, TestCases.class).get(0);
        }
        testCaseFields.setTestCase(testCases);
        return testCaseFields;
    }

    @BeforeEach
    public void initTest() {
        testCaseFields = createEntity(em);
    }

    @Test
    @Transactional
    void createTestCaseFields() throws Exception {
        int databaseSizeBeforeCreate = testCaseFieldsRepository.findAll().size();
        // Create the TestCaseFields
        restTestCaseFieldsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseFields))
            )
            .andExpect(status().isCreated());

        // Validate the TestCaseFields in the database
        List<TestCaseFields> testCaseFieldsList = testCaseFieldsRepository.findAll();
        assertThat(testCaseFieldsList).hasSize(databaseSizeBeforeCreate + 1);
        TestCaseFields testTestCaseFields = testCaseFieldsList.get(testCaseFieldsList.size() - 1);
        assertThat(testTestCaseFields.getExpectedResult()).isEqualTo(DEFAULT_EXPECTED_RESULT);
        assertThat(testTestCaseFields.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createTestCaseFieldsWithExistingId() throws Exception {
        // Create the TestCaseFields with an existing ID
        testCaseFields.setId(1L);

        int databaseSizeBeforeCreate = testCaseFieldsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCaseFieldsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseFields))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseFields in the database
        List<TestCaseFields> testCaseFieldsList = testCaseFieldsRepository.findAll();
        assertThat(testCaseFieldsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestCaseFields() throws Exception {
        // Initialize the database
        testCaseFieldsRepository.saveAndFlush(testCaseFields);

        // Get all the testCaseFieldsList
        restTestCaseFieldsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCaseFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].expectedResult").value(hasItem(DEFAULT_EXPECTED_RESULT.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getTestCaseFields() throws Exception {
        // Initialize the database
        testCaseFieldsRepository.saveAndFlush(testCaseFields);

        // Get the testCaseFields
        restTestCaseFieldsMockMvc
            .perform(get(ENTITY_API_URL_ID, testCaseFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testCaseFields.getId().intValue()))
            .andExpect(jsonPath("$.expectedResult").value(DEFAULT_EXPECTED_RESULT.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getTestCaseFieldsByIdFiltering() throws Exception {
        // Initialize the database
        testCaseFieldsRepository.saveAndFlush(testCaseFields);

        Long id = testCaseFields.getId();

        defaultTestCaseFieldsShouldBeFound("id.equals=" + id);
        defaultTestCaseFieldsShouldNotBeFound("id.notEquals=" + id);

        defaultTestCaseFieldsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestCaseFieldsShouldNotBeFound("id.greaterThan=" + id);

        defaultTestCaseFieldsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestCaseFieldsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        testCaseFieldsRepository.saveAndFlush(testCaseFields);

        // Get all the testCaseFieldsList where value equals to DEFAULT_VALUE
        defaultTestCaseFieldsShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the testCaseFieldsList where value equals to UPDATED_VALUE
        defaultTestCaseFieldsShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        testCaseFieldsRepository.saveAndFlush(testCaseFields);

        // Get all the testCaseFieldsList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultTestCaseFieldsShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the testCaseFieldsList where value equals to UPDATED_VALUE
        defaultTestCaseFieldsShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        testCaseFieldsRepository.saveAndFlush(testCaseFields);

        // Get all the testCaseFieldsList where value is not null
        defaultTestCaseFieldsShouldBeFound("value.specified=true");

        // Get all the testCaseFieldsList where value is null
        defaultTestCaseFieldsShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByValueContainsSomething() throws Exception {
        // Initialize the database
        testCaseFieldsRepository.saveAndFlush(testCaseFields);

        // Get all the testCaseFieldsList where value contains DEFAULT_VALUE
        defaultTestCaseFieldsShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the testCaseFieldsList where value contains UPDATED_VALUE
        defaultTestCaseFieldsShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByValueNotContainsSomething() throws Exception {
        // Initialize the database
        testCaseFieldsRepository.saveAndFlush(testCaseFields);

        // Get all the testCaseFieldsList where value does not contain DEFAULT_VALUE
        defaultTestCaseFieldsShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the testCaseFieldsList where value does not contain UPDATED_VALUE
        defaultTestCaseFieldsShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByTemplateFieldIsEqualToSomething() throws Exception {
        TemplateFields templateField;
        if (TestUtil.findAll(em, TemplateFields.class).isEmpty()) {
            testCaseFieldsRepository.saveAndFlush(testCaseFields);
            templateField = TemplateFieldsResourceIT.createEntity(em);
        } else {
            templateField = TestUtil.findAll(em, TemplateFields.class).get(0);
        }
        em.persist(templateField);
        em.flush();
        testCaseFields.setTemplateField(templateField);
        testCaseFieldsRepository.saveAndFlush(testCaseFields);
        Long templateFieldId = templateField.getId();

        // Get all the testCaseFieldsList where templateField equals to templateFieldId
        defaultTestCaseFieldsShouldBeFound("templateFieldId.equals=" + templateFieldId);

        // Get all the testCaseFieldsList where templateField equals to (templateFieldId + 1)
        defaultTestCaseFieldsShouldNotBeFound("templateFieldId.equals=" + (templateFieldId + 1));
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByTestCaseIsEqualToSomething() throws Exception {
        TestCases testCase;
        if (TestUtil.findAll(em, TestCases.class).isEmpty()) {
            testCaseFieldsRepository.saveAndFlush(testCaseFields);
            testCase = TestCasesResourceIT.createEntity(em);
        } else {
            testCase = TestUtil.findAll(em, TestCases.class).get(0);
        }
        em.persist(testCase);
        em.flush();
        testCaseFields.setTestCase(testCase);
        testCaseFieldsRepository.saveAndFlush(testCaseFields);
        Long testCaseId = testCase.getId();

        // Get all the testCaseFieldsList where testCase equals to testCaseId
        defaultTestCaseFieldsShouldBeFound("testCaseId.equals=" + testCaseId);

        // Get all the testCaseFieldsList where testCase equals to (testCaseId + 1)
        defaultTestCaseFieldsShouldNotBeFound("testCaseId.equals=" + (testCaseId + 1));
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByTestcasefieldattachmentsTestcasefieldIsEqualToSomething() throws Exception {
        TestCaseFieldAttachments testcasefieldattachmentsTestcasefield;
        if (TestUtil.findAll(em, TestCaseFieldAttachments.class).isEmpty()) {
            testCaseFieldsRepository.saveAndFlush(testCaseFields);
            testcasefieldattachmentsTestcasefield = TestCaseFieldAttachmentsResourceIT.createEntity(em);
        } else {
            testcasefieldattachmentsTestcasefield = TestUtil.findAll(em, TestCaseFieldAttachments.class).get(0);
        }
        em.persist(testcasefieldattachmentsTestcasefield);
        em.flush();
        testCaseFields.addTestcasefieldattachmentsTestcasefield(testcasefieldattachmentsTestcasefield);
        testCaseFieldsRepository.saveAndFlush(testCaseFields);
        Long testcasefieldattachmentsTestcasefieldId = testcasefieldattachmentsTestcasefield.getId();

        // Get all the testCaseFieldsList where testcasefieldattachmentsTestcasefield equals to testcasefieldattachmentsTestcasefieldId
        defaultTestCaseFieldsShouldBeFound("testcasefieldattachmentsTestcasefieldId.equals=" + testcasefieldattachmentsTestcasefieldId);

        // Get all the testCaseFieldsList where testcasefieldattachmentsTestcasefield equals to (testcasefieldattachmentsTestcasefieldId + 1)
        defaultTestCaseFieldsShouldNotBeFound(
            "testcasefieldattachmentsTestcasefieldId.equals=" + (testcasefieldattachmentsTestcasefieldId + 1)
        );
    }

    @Test
    @Transactional
    void getAllTestCaseFieldsByTestrunstepdetailsStepdetailIsEqualToSomething() throws Exception {
        TestRunStepDetails testrunstepdetailsStepdetail;
        if (TestUtil.findAll(em, TestRunStepDetails.class).isEmpty()) {
            testCaseFieldsRepository.saveAndFlush(testCaseFields);
            testrunstepdetailsStepdetail = TestRunStepDetailsResourceIT.createEntity(em);
        } else {
            testrunstepdetailsStepdetail = TestUtil.findAll(em, TestRunStepDetails.class).get(0);
        }
        em.persist(testrunstepdetailsStepdetail);
        em.flush();
        testCaseFields.addTestrunstepdetailsStepdetail(testrunstepdetailsStepdetail);
        testCaseFieldsRepository.saveAndFlush(testCaseFields);
        Long testrunstepdetailsStepdetailId = testrunstepdetailsStepdetail.getId();

        // Get all the testCaseFieldsList where testrunstepdetailsStepdetail equals to testrunstepdetailsStepdetailId
        defaultTestCaseFieldsShouldBeFound("testrunstepdetailsStepdetailId.equals=" + testrunstepdetailsStepdetailId);

        // Get all the testCaseFieldsList where testrunstepdetailsStepdetail equals to (testrunstepdetailsStepdetailId + 1)
        defaultTestCaseFieldsShouldNotBeFound("testrunstepdetailsStepdetailId.equals=" + (testrunstepdetailsStepdetailId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestCaseFieldsShouldBeFound(String filter) throws Exception {
        restTestCaseFieldsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCaseFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].expectedResult").value(hasItem(DEFAULT_EXPECTED_RESULT.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));

        // Check, that the count call also returns 1
        restTestCaseFieldsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestCaseFieldsShouldNotBeFound(String filter) throws Exception {
        restTestCaseFieldsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestCaseFieldsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestCaseFields() throws Exception {
        // Get the testCaseFields
        restTestCaseFieldsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestCaseFields() throws Exception {
        // Initialize the database
        testCaseFieldsRepository.saveAndFlush(testCaseFields);

        int databaseSizeBeforeUpdate = testCaseFieldsRepository.findAll().size();

        // Update the testCaseFields
        TestCaseFields updatedTestCaseFields = testCaseFieldsRepository.findById(testCaseFields.getId()).get();
        // Disconnect from session so that the updates on updatedTestCaseFields are not directly saved in db
        em.detach(updatedTestCaseFields);
        updatedTestCaseFields.expectedResult(UPDATED_EXPECTED_RESULT).value(UPDATED_VALUE);

        restTestCaseFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestCaseFields.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestCaseFields))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseFields in the database
        List<TestCaseFields> testCaseFieldsList = testCaseFieldsRepository.findAll();
        assertThat(testCaseFieldsList).hasSize(databaseSizeBeforeUpdate);
        TestCaseFields testTestCaseFields = testCaseFieldsList.get(testCaseFieldsList.size() - 1);
        assertThat(testTestCaseFields.getExpectedResult()).isEqualTo(UPDATED_EXPECTED_RESULT);
        assertThat(testTestCaseFields.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingTestCaseFields() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldsRepository.findAll().size();
        testCaseFields.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCaseFields.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFields))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseFields in the database
        List<TestCaseFields> testCaseFieldsList = testCaseFieldsRepository.findAll();
        assertThat(testCaseFieldsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestCaseFields() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldsRepository.findAll().size();
        testCaseFields.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFields))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseFields in the database
        List<TestCaseFields> testCaseFieldsList = testCaseFieldsRepository.findAll();
        assertThat(testCaseFieldsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestCaseFields() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldsRepository.findAll().size();
        testCaseFields.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseFieldsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testCaseFields)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCaseFields in the database
        List<TestCaseFields> testCaseFieldsList = testCaseFieldsRepository.findAll();
        assertThat(testCaseFieldsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestCaseFieldsWithPatch() throws Exception {
        // Initialize the database
        testCaseFieldsRepository.saveAndFlush(testCaseFields);

        int databaseSizeBeforeUpdate = testCaseFieldsRepository.findAll().size();

        // Update the testCaseFields using partial update
        TestCaseFields partialUpdatedTestCaseFields = new TestCaseFields();
        partialUpdatedTestCaseFields.setId(testCaseFields.getId());

        restTestCaseFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCaseFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCaseFields))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseFields in the database
        List<TestCaseFields> testCaseFieldsList = testCaseFieldsRepository.findAll();
        assertThat(testCaseFieldsList).hasSize(databaseSizeBeforeUpdate);
        TestCaseFields testTestCaseFields = testCaseFieldsList.get(testCaseFieldsList.size() - 1);
        assertThat(testTestCaseFields.getExpectedResult()).isEqualTo(DEFAULT_EXPECTED_RESULT);
        assertThat(testTestCaseFields.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateTestCaseFieldsWithPatch() throws Exception {
        // Initialize the database
        testCaseFieldsRepository.saveAndFlush(testCaseFields);

        int databaseSizeBeforeUpdate = testCaseFieldsRepository.findAll().size();

        // Update the testCaseFields using partial update
        TestCaseFields partialUpdatedTestCaseFields = new TestCaseFields();
        partialUpdatedTestCaseFields.setId(testCaseFields.getId());

        partialUpdatedTestCaseFields.expectedResult(UPDATED_EXPECTED_RESULT).value(UPDATED_VALUE);

        restTestCaseFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCaseFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestCaseFields))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseFields in the database
        List<TestCaseFields> testCaseFieldsList = testCaseFieldsRepository.findAll();
        assertThat(testCaseFieldsList).hasSize(databaseSizeBeforeUpdate);
        TestCaseFields testTestCaseFields = testCaseFieldsList.get(testCaseFieldsList.size() - 1);
        assertThat(testTestCaseFields.getExpectedResult()).isEqualTo(UPDATED_EXPECTED_RESULT);
        assertThat(testTestCaseFields.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingTestCaseFields() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldsRepository.findAll().size();
        testCaseFields.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testCaseFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFields))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseFields in the database
        List<TestCaseFields> testCaseFieldsList = testCaseFieldsRepository.findAll();
        assertThat(testCaseFieldsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestCaseFields() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldsRepository.findAll().size();
        testCaseFields.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testCaseFields))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseFields in the database
        List<TestCaseFields> testCaseFieldsList = testCaseFieldsRepository.findAll();
        assertThat(testCaseFieldsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestCaseFields() throws Exception {
        int databaseSizeBeforeUpdate = testCaseFieldsRepository.findAll().size();
        testCaseFields.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testCaseFields))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCaseFields in the database
        List<TestCaseFields> testCaseFieldsList = testCaseFieldsRepository.findAll();
        assertThat(testCaseFieldsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestCaseFields() throws Exception {
        // Initialize the database
        testCaseFieldsRepository.saveAndFlush(testCaseFields);

        int databaseSizeBeforeDelete = testCaseFieldsRepository.findAll().size();

        // Delete the testCaseFields
        restTestCaseFieldsMockMvc
            .perform(delete(ENTITY_API_URL_ID, testCaseFields.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestCaseFields> testCaseFieldsList = testCaseFieldsRepository.findAll();
        assertThat(testCaseFieldsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
