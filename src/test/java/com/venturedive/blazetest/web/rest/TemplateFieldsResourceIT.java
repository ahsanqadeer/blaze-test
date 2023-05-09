package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.Companies;
import com.venturedive.blazetest.domain.TemplateFieldTypes;
import com.venturedive.blazetest.domain.TemplateFields;
import com.venturedive.blazetest.domain.Templates;
import com.venturedive.blazetest.domain.TestCaseFields;
import com.venturedive.blazetest.repository.TemplateFieldsRepository;
import com.venturedive.blazetest.service.TemplateFieldsService;
import com.venturedive.blazetest.service.criteria.TemplateFieldsCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TemplateFieldsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TemplateFieldsResourceIT {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/template-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TemplateFieldsRepository templateFieldsRepository;

    @Mock
    private TemplateFieldsRepository templateFieldsRepositoryMock;

    @Mock
    private TemplateFieldsService templateFieldsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTemplateFieldsMockMvc;

    private TemplateFields templateFields;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TemplateFields createEntity(EntityManager em) {
        TemplateFields templateFields = new TemplateFields().fieldName(DEFAULT_FIELD_NAME);
        // Add required entity
        Companies companies;
        if (TestUtil.findAll(em, Companies.class).isEmpty()) {
            companies = CompaniesResourceIT.createEntity(em);
            em.persist(companies);
            em.flush();
        } else {
            companies = TestUtil.findAll(em, Companies.class).get(0);
        }
        templateFields.setCompany(companies);
        // Add required entity
        TemplateFieldTypes templateFieldTypes;
        if (TestUtil.findAll(em, TemplateFieldTypes.class).isEmpty()) {
            templateFieldTypes = TemplateFieldTypesResourceIT.createEntity(em);
            em.persist(templateFieldTypes);
            em.flush();
        } else {
            templateFieldTypes = TestUtil.findAll(em, TemplateFieldTypes.class).get(0);
        }
        templateFields.setTemplateFieldType(templateFieldTypes);
        return templateFields;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TemplateFields createUpdatedEntity(EntityManager em) {
        TemplateFields templateFields = new TemplateFields().fieldName(UPDATED_FIELD_NAME);
        // Add required entity
        Companies companies;
        if (TestUtil.findAll(em, Companies.class).isEmpty()) {
            companies = CompaniesResourceIT.createUpdatedEntity(em);
            em.persist(companies);
            em.flush();
        } else {
            companies = TestUtil.findAll(em, Companies.class).get(0);
        }
        templateFields.setCompany(companies);
        // Add required entity
        TemplateFieldTypes templateFieldTypes;
        if (TestUtil.findAll(em, TemplateFieldTypes.class).isEmpty()) {
            templateFieldTypes = TemplateFieldTypesResourceIT.createUpdatedEntity(em);
            em.persist(templateFieldTypes);
            em.flush();
        } else {
            templateFieldTypes = TestUtil.findAll(em, TemplateFieldTypes.class).get(0);
        }
        templateFields.setTemplateFieldType(templateFieldTypes);
        return templateFields;
    }

    @BeforeEach
    public void initTest() {
        templateFields = createEntity(em);
    }

    @Test
    @Transactional
    void createTemplateFields() throws Exception {
        int databaseSizeBeforeCreate = templateFieldsRepository.findAll().size();
        // Create the TemplateFields
        restTemplateFieldsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateFields))
            )
            .andExpect(status().isCreated());

        // Validate the TemplateFields in the database
        List<TemplateFields> templateFieldsList = templateFieldsRepository.findAll();
        assertThat(templateFieldsList).hasSize(databaseSizeBeforeCreate + 1);
        TemplateFields testTemplateFields = templateFieldsList.get(templateFieldsList.size() - 1);
        assertThat(testTemplateFields.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
    }

    @Test
    @Transactional
    void createTemplateFieldsWithExistingId() throws Exception {
        // Create the TemplateFields with an existing ID
        templateFields.setId(1L);

        int databaseSizeBeforeCreate = templateFieldsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTemplateFieldsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateFields))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateFields in the database
        List<TemplateFields> templateFieldsList = templateFieldsRepository.findAll();
        assertThat(templateFieldsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTemplateFields() throws Exception {
        // Initialize the database
        templateFieldsRepository.saveAndFlush(templateFields);

        // Get all the templateFieldsList
        restTemplateFieldsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(templateFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTemplateFieldsWithEagerRelationshipsIsEnabled() throws Exception {
        when(templateFieldsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTemplateFieldsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(templateFieldsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTemplateFieldsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(templateFieldsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTemplateFieldsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(templateFieldsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTemplateFields() throws Exception {
        // Initialize the database
        templateFieldsRepository.saveAndFlush(templateFields);

        // Get the templateFields
        restTemplateFieldsMockMvc
            .perform(get(ENTITY_API_URL_ID, templateFields.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(templateFields.getId().intValue()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME));
    }

    @Test
    @Transactional
    void getTemplateFieldsByIdFiltering() throws Exception {
        // Initialize the database
        templateFieldsRepository.saveAndFlush(templateFields);

        Long id = templateFields.getId();

        defaultTemplateFieldsShouldBeFound("id.equals=" + id);
        defaultTemplateFieldsShouldNotBeFound("id.notEquals=" + id);

        defaultTemplateFieldsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTemplateFieldsShouldNotBeFound("id.greaterThan=" + id);

        defaultTemplateFieldsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTemplateFieldsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTemplateFieldsByFieldNameIsEqualToSomething() throws Exception {
        // Initialize the database
        templateFieldsRepository.saveAndFlush(templateFields);

        // Get all the templateFieldsList where fieldName equals to DEFAULT_FIELD_NAME
        defaultTemplateFieldsShouldBeFound("fieldName.equals=" + DEFAULT_FIELD_NAME);

        // Get all the templateFieldsList where fieldName equals to UPDATED_FIELD_NAME
        defaultTemplateFieldsShouldNotBeFound("fieldName.equals=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllTemplateFieldsByFieldNameIsInShouldWork() throws Exception {
        // Initialize the database
        templateFieldsRepository.saveAndFlush(templateFields);

        // Get all the templateFieldsList where fieldName in DEFAULT_FIELD_NAME or UPDATED_FIELD_NAME
        defaultTemplateFieldsShouldBeFound("fieldName.in=" + DEFAULT_FIELD_NAME + "," + UPDATED_FIELD_NAME);

        // Get all the templateFieldsList where fieldName equals to UPDATED_FIELD_NAME
        defaultTemplateFieldsShouldNotBeFound("fieldName.in=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllTemplateFieldsByFieldNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        templateFieldsRepository.saveAndFlush(templateFields);

        // Get all the templateFieldsList where fieldName is not null
        defaultTemplateFieldsShouldBeFound("fieldName.specified=true");

        // Get all the templateFieldsList where fieldName is null
        defaultTemplateFieldsShouldNotBeFound("fieldName.specified=false");
    }

    @Test
    @Transactional
    void getAllTemplateFieldsByFieldNameContainsSomething() throws Exception {
        // Initialize the database
        templateFieldsRepository.saveAndFlush(templateFields);

        // Get all the templateFieldsList where fieldName contains DEFAULT_FIELD_NAME
        defaultTemplateFieldsShouldBeFound("fieldName.contains=" + DEFAULT_FIELD_NAME);

        // Get all the templateFieldsList where fieldName contains UPDATED_FIELD_NAME
        defaultTemplateFieldsShouldNotBeFound("fieldName.contains=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllTemplateFieldsByFieldNameNotContainsSomething() throws Exception {
        // Initialize the database
        templateFieldsRepository.saveAndFlush(templateFields);

        // Get all the templateFieldsList where fieldName does not contain DEFAULT_FIELD_NAME
        defaultTemplateFieldsShouldNotBeFound("fieldName.doesNotContain=" + DEFAULT_FIELD_NAME);

        // Get all the templateFieldsList where fieldName does not contain UPDATED_FIELD_NAME
        defaultTemplateFieldsShouldBeFound("fieldName.doesNotContain=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllTemplateFieldsByCompanyIsEqualToSomething() throws Exception {
        Companies company;
        if (TestUtil.findAll(em, Companies.class).isEmpty()) {
            templateFieldsRepository.saveAndFlush(templateFields);
            company = CompaniesResourceIT.createEntity(em);
        } else {
            company = TestUtil.findAll(em, Companies.class).get(0);
        }
        em.persist(company);
        em.flush();
        templateFields.setCompany(company);
        templateFieldsRepository.saveAndFlush(templateFields);
        Long companyId = company.getId();

        // Get all the templateFieldsList where company equals to companyId
        defaultTemplateFieldsShouldBeFound("companyId.equals=" + companyId);

        // Get all the templateFieldsList where company equals to (companyId + 1)
        defaultTemplateFieldsShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    @Test
    @Transactional
    void getAllTemplateFieldsByTemplateFieldTypeIsEqualToSomething() throws Exception {
        TemplateFieldTypes templateFieldType;
        if (TestUtil.findAll(em, TemplateFieldTypes.class).isEmpty()) {
            templateFieldsRepository.saveAndFlush(templateFields);
            templateFieldType = TemplateFieldTypesResourceIT.createEntity(em);
        } else {
            templateFieldType = TestUtil.findAll(em, TemplateFieldTypes.class).get(0);
        }
        em.persist(templateFieldType);
        em.flush();
        templateFields.setTemplateFieldType(templateFieldType);
        templateFieldsRepository.saveAndFlush(templateFields);
        Long templateFieldTypeId = templateFieldType.getId();

        // Get all the templateFieldsList where templateFieldType equals to templateFieldTypeId
        defaultTemplateFieldsShouldBeFound("templateFieldTypeId.equals=" + templateFieldTypeId);

        // Get all the templateFieldsList where templateFieldType equals to (templateFieldTypeId + 1)
        defaultTemplateFieldsShouldNotBeFound("templateFieldTypeId.equals=" + (templateFieldTypeId + 1));
    }

    @Test
    @Transactional
    void getAllTemplateFieldsByTestcasefieldsTemplatefieldIsEqualToSomething() throws Exception {
        TestCaseFields testcasefieldsTemplatefield;
        if (TestUtil.findAll(em, TestCaseFields.class).isEmpty()) {
            templateFieldsRepository.saveAndFlush(templateFields);
            testcasefieldsTemplatefield = TestCaseFieldsResourceIT.createEntity(em);
        } else {
            testcasefieldsTemplatefield = TestUtil.findAll(em, TestCaseFields.class).get(0);
        }
        em.persist(testcasefieldsTemplatefield);
        em.flush();
        templateFields.addTestcasefieldsTemplatefield(testcasefieldsTemplatefield);
        templateFieldsRepository.saveAndFlush(templateFields);
        Long testcasefieldsTemplatefieldId = testcasefieldsTemplatefield.getId();

        // Get all the templateFieldsList where testcasefieldsTemplatefield equals to testcasefieldsTemplatefieldId
        defaultTemplateFieldsShouldBeFound("testcasefieldsTemplatefieldId.equals=" + testcasefieldsTemplatefieldId);

        // Get all the templateFieldsList where testcasefieldsTemplatefield equals to (testcasefieldsTemplatefieldId + 1)
        defaultTemplateFieldsShouldNotBeFound("testcasefieldsTemplatefieldId.equals=" + (testcasefieldsTemplatefieldId + 1));
    }

    @Test
    @Transactional
    void getAllTemplateFieldsByTemplateIsEqualToSomething() throws Exception {
        Templates template;
        if (TestUtil.findAll(em, Templates.class).isEmpty()) {
            templateFieldsRepository.saveAndFlush(templateFields);
            template = TemplatesResourceIT.createEntity(em);
        } else {
            template = TestUtil.findAll(em, Templates.class).get(0);
        }
        em.persist(template);
        em.flush();
        templateFields.addTemplate(template);
        templateFieldsRepository.saveAndFlush(templateFields);
        Long templateId = template.getId();

        // Get all the templateFieldsList where template equals to templateId
        defaultTemplateFieldsShouldBeFound("templateId.equals=" + templateId);

        // Get all the templateFieldsList where template equals to (templateId + 1)
        defaultTemplateFieldsShouldNotBeFound("templateId.equals=" + (templateId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTemplateFieldsShouldBeFound(String filter) throws Exception {
        restTemplateFieldsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(templateFields.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)));

        // Check, that the count call also returns 1
        restTemplateFieldsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTemplateFieldsShouldNotBeFound(String filter) throws Exception {
        restTemplateFieldsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTemplateFieldsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTemplateFields() throws Exception {
        // Get the templateFields
        restTemplateFieldsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTemplateFields() throws Exception {
        // Initialize the database
        templateFieldsRepository.saveAndFlush(templateFields);

        int databaseSizeBeforeUpdate = templateFieldsRepository.findAll().size();

        // Update the templateFields
        TemplateFields updatedTemplateFields = templateFieldsRepository.findById(templateFields.getId()).get();
        // Disconnect from session so that the updates on updatedTemplateFields are not directly saved in db
        em.detach(updatedTemplateFields);
        updatedTemplateFields.fieldName(UPDATED_FIELD_NAME);

        restTemplateFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTemplateFields.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTemplateFields))
            )
            .andExpect(status().isOk());

        // Validate the TemplateFields in the database
        List<TemplateFields> templateFieldsList = templateFieldsRepository.findAll();
        assertThat(templateFieldsList).hasSize(databaseSizeBeforeUpdate);
        TemplateFields testTemplateFields = templateFieldsList.get(templateFieldsList.size() - 1);
        assertThat(testTemplateFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTemplateFields() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldsRepository.findAll().size();
        templateFields.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemplateFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, templateFields.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(templateFields))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateFields in the database
        List<TemplateFields> templateFieldsList = templateFieldsRepository.findAll();
        assertThat(templateFieldsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTemplateFields() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldsRepository.findAll().size();
        templateFields.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateFieldsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(templateFields))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateFields in the database
        List<TemplateFields> templateFieldsList = templateFieldsRepository.findAll();
        assertThat(templateFieldsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTemplateFields() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldsRepository.findAll().size();
        templateFields.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateFieldsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateFields)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TemplateFields in the database
        List<TemplateFields> templateFieldsList = templateFieldsRepository.findAll();
        assertThat(templateFieldsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTemplateFieldsWithPatch() throws Exception {
        // Initialize the database
        templateFieldsRepository.saveAndFlush(templateFields);

        int databaseSizeBeforeUpdate = templateFieldsRepository.findAll().size();

        // Update the templateFields using partial update
        TemplateFields partialUpdatedTemplateFields = new TemplateFields();
        partialUpdatedTemplateFields.setId(templateFields.getId());

        partialUpdatedTemplateFields.fieldName(UPDATED_FIELD_NAME);

        restTemplateFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemplateFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemplateFields))
            )
            .andExpect(status().isOk());

        // Validate the TemplateFields in the database
        List<TemplateFields> templateFieldsList = templateFieldsRepository.findAll();
        assertThat(templateFieldsList).hasSize(databaseSizeBeforeUpdate);
        TemplateFields testTemplateFields = templateFieldsList.get(templateFieldsList.size() - 1);
        assertThat(testTemplateFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTemplateFieldsWithPatch() throws Exception {
        // Initialize the database
        templateFieldsRepository.saveAndFlush(templateFields);

        int databaseSizeBeforeUpdate = templateFieldsRepository.findAll().size();

        // Update the templateFields using partial update
        TemplateFields partialUpdatedTemplateFields = new TemplateFields();
        partialUpdatedTemplateFields.setId(templateFields.getId());

        partialUpdatedTemplateFields.fieldName(UPDATED_FIELD_NAME);

        restTemplateFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemplateFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemplateFields))
            )
            .andExpect(status().isOk());

        // Validate the TemplateFields in the database
        List<TemplateFields> templateFieldsList = templateFieldsRepository.findAll();
        assertThat(templateFieldsList).hasSize(databaseSizeBeforeUpdate);
        TemplateFields testTemplateFields = templateFieldsList.get(templateFieldsList.size() - 1);
        assertThat(testTemplateFields.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTemplateFields() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldsRepository.findAll().size();
        templateFields.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemplateFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, templateFields.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(templateFields))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateFields in the database
        List<TemplateFields> templateFieldsList = templateFieldsRepository.findAll();
        assertThat(templateFieldsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTemplateFields() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldsRepository.findAll().size();
        templateFields.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(templateFields))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateFields in the database
        List<TemplateFields> templateFieldsList = templateFieldsRepository.findAll();
        assertThat(templateFieldsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTemplateFields() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldsRepository.findAll().size();
        templateFields.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateFieldsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(templateFields))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TemplateFields in the database
        List<TemplateFields> templateFieldsList = templateFieldsRepository.findAll();
        assertThat(templateFieldsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTemplateFields() throws Exception {
        // Initialize the database
        templateFieldsRepository.saveAndFlush(templateFields);

        int databaseSizeBeforeDelete = templateFieldsRepository.findAll().size();

        // Delete the templateFields
        restTemplateFieldsMockMvc
            .perform(delete(ENTITY_API_URL_ID, templateFields.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TemplateFields> templateFieldsList = templateFieldsRepository.findAll();
        assertThat(templateFieldsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
