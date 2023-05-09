package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.Companies;
import com.venturedive.blazetest.domain.Projects;
import com.venturedive.blazetest.domain.TemplateFields;
import com.venturedive.blazetest.domain.Templates;
import com.venturedive.blazetest.domain.TestCases;
import com.venturedive.blazetest.repository.TemplatesRepository;
import com.venturedive.blazetest.service.TemplatesService;
import com.venturedive.blazetest.service.criteria.TemplatesCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link TemplatesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TemplatesResourceIT {

    private static final String DEFAULT_TEMPLATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;
    private static final Integer SMALLER_CREATED_BY = 1 - 1;

    private static final String ENTITY_API_URL = "/api/templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TemplatesRepository templatesRepository;

    @Mock
    private TemplatesRepository templatesRepositoryMock;

    @Mock
    private TemplatesService templatesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTemplatesMockMvc;

    private Templates templates;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Templates createEntity(EntityManager em) {
        Templates templates = new Templates()
            .templateName(DEFAULT_TEMPLATE_NAME)
            .createdAt(DEFAULT_CREATED_AT)
            .createdBy(DEFAULT_CREATED_BY);
        // Add required entity
        Companies companies;
        if (TestUtil.findAll(em, Companies.class).isEmpty()) {
            companies = CompaniesResourceIT.createEntity(em);
            em.persist(companies);
            em.flush();
        } else {
            companies = TestUtil.findAll(em, Companies.class).get(0);
        }
        templates.setCompany(companies);
        return templates;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Templates createUpdatedEntity(EntityManager em) {
        Templates templates = new Templates()
            .templateName(UPDATED_TEMPLATE_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY);
        // Add required entity
        Companies companies;
        if (TestUtil.findAll(em, Companies.class).isEmpty()) {
            companies = CompaniesResourceIT.createUpdatedEntity(em);
            em.persist(companies);
            em.flush();
        } else {
            companies = TestUtil.findAll(em, Companies.class).get(0);
        }
        templates.setCompany(companies);
        return templates;
    }

    @BeforeEach
    public void initTest() {
        templates = createEntity(em);
    }

    @Test
    @Transactional
    void createTemplates() throws Exception {
        int databaseSizeBeforeCreate = templatesRepository.findAll().size();
        // Create the Templates
        restTemplatesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templates)))
            .andExpect(status().isCreated());

        // Validate the Templates in the database
        List<Templates> templatesList = templatesRepository.findAll();
        assertThat(templatesList).hasSize(databaseSizeBeforeCreate + 1);
        Templates testTemplates = templatesList.get(templatesList.size() - 1);
        assertThat(testTemplates.getTemplateName()).isEqualTo(DEFAULT_TEMPLATE_NAME);
        assertThat(testTemplates.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTemplates.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void createTemplatesWithExistingId() throws Exception {
        // Create the Templates with an existing ID
        templates.setId(1L);

        int databaseSizeBeforeCreate = templatesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTemplatesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templates)))
            .andExpect(status().isBadRequest());

        // Validate the Templates in the database
        List<Templates> templatesList = templatesRepository.findAll();
        assertThat(templatesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTemplates() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get all the templatesList
        restTemplatesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(templates.getId().intValue())))
            .andExpect(jsonPath("$.[*].templateName").value(hasItem(DEFAULT_TEMPLATE_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTemplatesWithEagerRelationshipsIsEnabled() throws Exception {
        when(templatesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTemplatesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(templatesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTemplatesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(templatesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTemplatesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(templatesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTemplates() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get the templates
        restTemplatesMockMvc
            .perform(get(ENTITY_API_URL_ID, templates.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(templates.getId().intValue()))
            .andExpect(jsonPath("$.templateName").value(DEFAULT_TEMPLATE_NAME))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getTemplatesByIdFiltering() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        Long id = templates.getId();

        defaultTemplatesShouldBeFound("id.equals=" + id);
        defaultTemplatesShouldNotBeFound("id.notEquals=" + id);

        defaultTemplatesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTemplatesShouldNotBeFound("id.greaterThan=" + id);

        defaultTemplatesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTemplatesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTemplatesByTemplateNameIsEqualToSomething() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get all the templatesList where templateName equals to DEFAULT_TEMPLATE_NAME
        defaultTemplatesShouldBeFound("templateName.equals=" + DEFAULT_TEMPLATE_NAME);

        // Get all the templatesList where templateName equals to UPDATED_TEMPLATE_NAME
        defaultTemplatesShouldNotBeFound("templateName.equals=" + UPDATED_TEMPLATE_NAME);
    }

    @Test
    @Transactional
    void getAllTemplatesByTemplateNameIsInShouldWork() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get all the templatesList where templateName in DEFAULT_TEMPLATE_NAME or UPDATED_TEMPLATE_NAME
        defaultTemplatesShouldBeFound("templateName.in=" + DEFAULT_TEMPLATE_NAME + "," + UPDATED_TEMPLATE_NAME);

        // Get all the templatesList where templateName equals to UPDATED_TEMPLATE_NAME
        defaultTemplatesShouldNotBeFound("templateName.in=" + UPDATED_TEMPLATE_NAME);
    }

    @Test
    @Transactional
    void getAllTemplatesByTemplateNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get all the templatesList where templateName is not null
        defaultTemplatesShouldBeFound("templateName.specified=true");

        // Get all the templatesList where templateName is null
        defaultTemplatesShouldNotBeFound("templateName.specified=false");
    }

    @Test
    @Transactional
    void getAllTemplatesByTemplateNameContainsSomething() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get all the templatesList where templateName contains DEFAULT_TEMPLATE_NAME
        defaultTemplatesShouldBeFound("templateName.contains=" + DEFAULT_TEMPLATE_NAME);

        // Get all the templatesList where templateName contains UPDATED_TEMPLATE_NAME
        defaultTemplatesShouldNotBeFound("templateName.contains=" + UPDATED_TEMPLATE_NAME);
    }

    @Test
    @Transactional
    void getAllTemplatesByTemplateNameNotContainsSomething() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get all the templatesList where templateName does not contain DEFAULT_TEMPLATE_NAME
        defaultTemplatesShouldNotBeFound("templateName.doesNotContain=" + DEFAULT_TEMPLATE_NAME);

        // Get all the templatesList where templateName does not contain UPDATED_TEMPLATE_NAME
        defaultTemplatesShouldBeFound("templateName.doesNotContain=" + UPDATED_TEMPLATE_NAME);
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get all the templatesList where createdAt equals to DEFAULT_CREATED_AT
        defaultTemplatesShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the templatesList where createdAt equals to UPDATED_CREATED_AT
        defaultTemplatesShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get all the templatesList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultTemplatesShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the templatesList where createdAt equals to UPDATED_CREATED_AT
        defaultTemplatesShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get all the templatesList where createdAt is not null
        defaultTemplatesShouldBeFound("createdAt.specified=true");

        // Get all the templatesList where createdAt is null
        defaultTemplatesShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get all the templatesList where createdBy equals to DEFAULT_CREATED_BY
        defaultTemplatesShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the templatesList where createdBy equals to UPDATED_CREATED_BY
        defaultTemplatesShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get all the templatesList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultTemplatesShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the templatesList where createdBy equals to UPDATED_CREATED_BY
        defaultTemplatesShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get all the templatesList where createdBy is not null
        defaultTemplatesShouldBeFound("createdBy.specified=true");

        // Get all the templatesList where createdBy is null
        defaultTemplatesShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get all the templatesList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultTemplatesShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the templatesList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultTemplatesShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get all the templatesList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultTemplatesShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the templatesList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultTemplatesShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get all the templatesList where createdBy is less than DEFAULT_CREATED_BY
        defaultTemplatesShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the templatesList where createdBy is less than UPDATED_CREATED_BY
        defaultTemplatesShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTemplatesByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        // Get all the templatesList where createdBy is greater than DEFAULT_CREATED_BY
        defaultTemplatesShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the templatesList where createdBy is greater than SMALLER_CREATED_BY
        defaultTemplatesShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTemplatesByCompanyIsEqualToSomething() throws Exception {
        Companies company;
        if (TestUtil.findAll(em, Companies.class).isEmpty()) {
            templatesRepository.saveAndFlush(templates);
            company = CompaniesResourceIT.createEntity(em);
        } else {
            company = TestUtil.findAll(em, Companies.class).get(0);
        }
        em.persist(company);
        em.flush();
        templates.setCompany(company);
        templatesRepository.saveAndFlush(templates);
        Long companyId = company.getId();

        // Get all the templatesList where company equals to companyId
        defaultTemplatesShouldBeFound("companyId.equals=" + companyId);

        // Get all the templatesList where company equals to (companyId + 1)
        defaultTemplatesShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    @Test
    @Transactional
    void getAllTemplatesByTemplateFieldIsEqualToSomething() throws Exception {
        TemplateFields templateField;
        if (TestUtil.findAll(em, TemplateFields.class).isEmpty()) {
            templatesRepository.saveAndFlush(templates);
            templateField = TemplateFieldsResourceIT.createEntity(em);
        } else {
            templateField = TestUtil.findAll(em, TemplateFields.class).get(0);
        }
        em.persist(templateField);
        em.flush();
        templates.addTemplateField(templateField);
        templatesRepository.saveAndFlush(templates);
        Long templateFieldId = templateField.getId();

        // Get all the templatesList where templateField equals to templateFieldId
        defaultTemplatesShouldBeFound("templateFieldId.equals=" + templateFieldId);

        // Get all the templatesList where templateField equals to (templateFieldId + 1)
        defaultTemplatesShouldNotBeFound("templateFieldId.equals=" + (templateFieldId + 1));
    }

    @Test
    @Transactional
    void getAllTemplatesByProjectsDefaulttemplateIsEqualToSomething() throws Exception {
        Projects projectsDefaulttemplate;
        if (TestUtil.findAll(em, Projects.class).isEmpty()) {
            templatesRepository.saveAndFlush(templates);
            projectsDefaulttemplate = ProjectsResourceIT.createEntity(em);
        } else {
            projectsDefaulttemplate = TestUtil.findAll(em, Projects.class).get(0);
        }
        em.persist(projectsDefaulttemplate);
        em.flush();
        templates.addProjectsDefaulttemplate(projectsDefaulttemplate);
        templatesRepository.saveAndFlush(templates);
        Long projectsDefaulttemplateId = projectsDefaulttemplate.getId();

        // Get all the templatesList where projectsDefaulttemplate equals to projectsDefaulttemplateId
        defaultTemplatesShouldBeFound("projectsDefaulttemplateId.equals=" + projectsDefaulttemplateId);

        // Get all the templatesList where projectsDefaulttemplate equals to (projectsDefaulttemplateId + 1)
        defaultTemplatesShouldNotBeFound("projectsDefaulttemplateId.equals=" + (projectsDefaulttemplateId + 1));
    }

    @Test
    @Transactional
    void getAllTemplatesByTestcasesTemplateIsEqualToSomething() throws Exception {
        TestCases testcasesTemplate;
        if (TestUtil.findAll(em, TestCases.class).isEmpty()) {
            templatesRepository.saveAndFlush(templates);
            testcasesTemplate = TestCasesResourceIT.createEntity(em);
        } else {
            testcasesTemplate = TestUtil.findAll(em, TestCases.class).get(0);
        }
        em.persist(testcasesTemplate);
        em.flush();
        templates.addTestcasesTemplate(testcasesTemplate);
        templatesRepository.saveAndFlush(templates);
        Long testcasesTemplateId = testcasesTemplate.getId();

        // Get all the templatesList where testcasesTemplate equals to testcasesTemplateId
        defaultTemplatesShouldBeFound("testcasesTemplateId.equals=" + testcasesTemplateId);

        // Get all the templatesList where testcasesTemplate equals to (testcasesTemplateId + 1)
        defaultTemplatesShouldNotBeFound("testcasesTemplateId.equals=" + (testcasesTemplateId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTemplatesShouldBeFound(String filter) throws Exception {
        restTemplatesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(templates.getId().intValue())))
            .andExpect(jsonPath("$.[*].templateName").value(hasItem(DEFAULT_TEMPLATE_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restTemplatesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTemplatesShouldNotBeFound(String filter) throws Exception {
        restTemplatesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTemplatesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTemplates() throws Exception {
        // Get the templates
        restTemplatesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTemplates() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        int databaseSizeBeforeUpdate = templatesRepository.findAll().size();

        // Update the templates
        Templates updatedTemplates = templatesRepository.findById(templates.getId()).get();
        // Disconnect from session so that the updates on updatedTemplates are not directly saved in db
        em.detach(updatedTemplates);
        updatedTemplates.templateName(UPDATED_TEMPLATE_NAME).createdAt(UPDATED_CREATED_AT).createdBy(UPDATED_CREATED_BY);

        restTemplatesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTemplates.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTemplates))
            )
            .andExpect(status().isOk());

        // Validate the Templates in the database
        List<Templates> templatesList = templatesRepository.findAll();
        assertThat(templatesList).hasSize(databaseSizeBeforeUpdate);
        Templates testTemplates = templatesList.get(templatesList.size() - 1);
        assertThat(testTemplates.getTemplateName()).isEqualTo(UPDATED_TEMPLATE_NAME);
        assertThat(testTemplates.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTemplates.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingTemplates() throws Exception {
        int databaseSizeBeforeUpdate = templatesRepository.findAll().size();
        templates.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemplatesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, templates.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(templates))
            )
            .andExpect(status().isBadRequest());

        // Validate the Templates in the database
        List<Templates> templatesList = templatesRepository.findAll();
        assertThat(templatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTemplates() throws Exception {
        int databaseSizeBeforeUpdate = templatesRepository.findAll().size();
        templates.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplatesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(templates))
            )
            .andExpect(status().isBadRequest());

        // Validate the Templates in the database
        List<Templates> templatesList = templatesRepository.findAll();
        assertThat(templatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTemplates() throws Exception {
        int databaseSizeBeforeUpdate = templatesRepository.findAll().size();
        templates.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplatesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templates)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Templates in the database
        List<Templates> templatesList = templatesRepository.findAll();
        assertThat(templatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTemplatesWithPatch() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        int databaseSizeBeforeUpdate = templatesRepository.findAll().size();

        // Update the templates using partial update
        Templates partialUpdatedTemplates = new Templates();
        partialUpdatedTemplates.setId(templates.getId());

        partialUpdatedTemplates.createdBy(UPDATED_CREATED_BY);

        restTemplatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemplates.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemplates))
            )
            .andExpect(status().isOk());

        // Validate the Templates in the database
        List<Templates> templatesList = templatesRepository.findAll();
        assertThat(templatesList).hasSize(databaseSizeBeforeUpdate);
        Templates testTemplates = templatesList.get(templatesList.size() - 1);
        assertThat(testTemplates.getTemplateName()).isEqualTo(DEFAULT_TEMPLATE_NAME);
        assertThat(testTemplates.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTemplates.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateTemplatesWithPatch() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        int databaseSizeBeforeUpdate = templatesRepository.findAll().size();

        // Update the templates using partial update
        Templates partialUpdatedTemplates = new Templates();
        partialUpdatedTemplates.setId(templates.getId());

        partialUpdatedTemplates.templateName(UPDATED_TEMPLATE_NAME).createdAt(UPDATED_CREATED_AT).createdBy(UPDATED_CREATED_BY);

        restTemplatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemplates.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemplates))
            )
            .andExpect(status().isOk());

        // Validate the Templates in the database
        List<Templates> templatesList = templatesRepository.findAll();
        assertThat(templatesList).hasSize(databaseSizeBeforeUpdate);
        Templates testTemplates = templatesList.get(templatesList.size() - 1);
        assertThat(testTemplates.getTemplateName()).isEqualTo(UPDATED_TEMPLATE_NAME);
        assertThat(testTemplates.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTemplates.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingTemplates() throws Exception {
        int databaseSizeBeforeUpdate = templatesRepository.findAll().size();
        templates.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemplatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, templates.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(templates))
            )
            .andExpect(status().isBadRequest());

        // Validate the Templates in the database
        List<Templates> templatesList = templatesRepository.findAll();
        assertThat(templatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTemplates() throws Exception {
        int databaseSizeBeforeUpdate = templatesRepository.findAll().size();
        templates.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(templates))
            )
            .andExpect(status().isBadRequest());

        // Validate the Templates in the database
        List<Templates> templatesList = templatesRepository.findAll();
        assertThat(templatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTemplates() throws Exception {
        int databaseSizeBeforeUpdate = templatesRepository.findAll().size();
        templates.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplatesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(templates))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Templates in the database
        List<Templates> templatesList = templatesRepository.findAll();
        assertThat(templatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTemplates() throws Exception {
        // Initialize the database
        templatesRepository.saveAndFlush(templates);

        int databaseSizeBeforeDelete = templatesRepository.findAll().size();

        // Delete the templates
        restTemplatesMockMvc
            .perform(delete(ENTITY_API_URL_ID, templates.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Templates> templatesList = templatesRepository.findAll();
        assertThat(templatesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
