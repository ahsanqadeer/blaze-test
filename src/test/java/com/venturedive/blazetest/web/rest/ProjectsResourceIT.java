package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.Companies;
import com.venturedive.blazetest.domain.Milestones;
import com.venturedive.blazetest.domain.Projects;
import com.venturedive.blazetest.domain.Templates;
import com.venturedive.blazetest.domain.TestPlans;
import com.venturedive.blazetest.domain.TestSuites;
import com.venturedive.blazetest.domain.Users;
import com.venturedive.blazetest.repository.ProjectsRepository;
import com.venturedive.blazetest.service.criteria.ProjectsCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ProjectsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProjectsResourceIT {

    private static final String DEFAULT_PROJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ISACTIVE = false;
    private static final Boolean UPDATED_ISACTIVE = true;

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;
    private static final Integer SMALLER_CREATED_BY = 1 - 1;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_UPDATED_BY = 1;
    private static final Integer UPDATED_UPDATED_BY = 2;
    private static final Integer SMALLER_UPDATED_BY = 1 - 1;

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/projects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjectsRepository projectsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectsMockMvc;

    private Projects projects;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projects createEntity(EntityManager em) {
        Projects projects = new Projects()
            .projectName(DEFAULT_PROJECT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .isactive(DEFAULT_ISACTIVE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT);
        return projects;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projects createUpdatedEntity(EntityManager em) {
        Projects projects = new Projects()
            .projectName(UPDATED_PROJECT_NAME)
            .description(UPDATED_DESCRIPTION)
            .isactive(UPDATED_ISACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);
        return projects;
    }

    @BeforeEach
    public void initTest() {
        projects = createEntity(em);
    }

    @Test
    @Transactional
    void createProjects() throws Exception {
        int databaseSizeBeforeCreate = projectsRepository.findAll().size();
        // Create the Projects
        restProjectsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projects)))
            .andExpect(status().isCreated());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeCreate + 1);
        Projects testProjects = projectsList.get(projectsList.size() - 1);
        assertThat(testProjects.getProjectName()).isEqualTo(DEFAULT_PROJECT_NAME);
        assertThat(testProjects.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProjects.getIsactive()).isEqualTo(DEFAULT_ISACTIVE);
        assertThat(testProjects.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProjects.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testProjects.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testProjects.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createProjectsWithExistingId() throws Exception {
        // Create the Projects with an existing ID
        projects.setId(1L);

        int databaseSizeBeforeCreate = projectsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projects)))
            .andExpect(status().isBadRequest());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProjectNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectsRepository.findAll().size();
        // set the field null
        projects.setProjectName(null);

        // Create the Projects, which fails.

        restProjectsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projects)))
            .andExpect(status().isBadRequest());

        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProjects() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList
        restProjectsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projects.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].isactive").value(hasItem(DEFAULT_ISACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getProjects() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get the projects
        restProjectsMockMvc
            .perform(get(ENTITY_API_URL_ID, projects.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(projects.getId().intValue()))
            .andExpect(jsonPath("$.projectName").value(DEFAULT_PROJECT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.isactive").value(DEFAULT_ISACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getProjectsByIdFiltering() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        Long id = projects.getId();

        defaultProjectsShouldBeFound("id.equals=" + id);
        defaultProjectsShouldNotBeFound("id.notEquals=" + id);

        defaultProjectsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProjectsShouldNotBeFound("id.greaterThan=" + id);

        defaultProjectsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProjectsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProjectsByProjectNameIsEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where projectName equals to DEFAULT_PROJECT_NAME
        defaultProjectsShouldBeFound("projectName.equals=" + DEFAULT_PROJECT_NAME);

        // Get all the projectsList where projectName equals to UPDATED_PROJECT_NAME
        defaultProjectsShouldNotBeFound("projectName.equals=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByProjectNameIsInShouldWork() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where projectName in DEFAULT_PROJECT_NAME or UPDATED_PROJECT_NAME
        defaultProjectsShouldBeFound("projectName.in=" + DEFAULT_PROJECT_NAME + "," + UPDATED_PROJECT_NAME);

        // Get all the projectsList where projectName equals to UPDATED_PROJECT_NAME
        defaultProjectsShouldNotBeFound("projectName.in=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByProjectNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where projectName is not null
        defaultProjectsShouldBeFound("projectName.specified=true");

        // Get all the projectsList where projectName is null
        defaultProjectsShouldNotBeFound("projectName.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByProjectNameContainsSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where projectName contains DEFAULT_PROJECT_NAME
        defaultProjectsShouldBeFound("projectName.contains=" + DEFAULT_PROJECT_NAME);

        // Get all the projectsList where projectName contains UPDATED_PROJECT_NAME
        defaultProjectsShouldNotBeFound("projectName.contains=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByProjectNameNotContainsSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where projectName does not contain DEFAULT_PROJECT_NAME
        defaultProjectsShouldNotBeFound("projectName.doesNotContain=" + DEFAULT_PROJECT_NAME);

        // Get all the projectsList where projectName does not contain UPDATED_PROJECT_NAME
        defaultProjectsShouldBeFound("projectName.doesNotContain=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByIsactiveIsEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where isactive equals to DEFAULT_ISACTIVE
        defaultProjectsShouldBeFound("isactive.equals=" + DEFAULT_ISACTIVE);

        // Get all the projectsList where isactive equals to UPDATED_ISACTIVE
        defaultProjectsShouldNotBeFound("isactive.equals=" + UPDATED_ISACTIVE);
    }

    @Test
    @Transactional
    void getAllProjectsByIsactiveIsInShouldWork() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where isactive in DEFAULT_ISACTIVE or UPDATED_ISACTIVE
        defaultProjectsShouldBeFound("isactive.in=" + DEFAULT_ISACTIVE + "," + UPDATED_ISACTIVE);

        // Get all the projectsList where isactive equals to UPDATED_ISACTIVE
        defaultProjectsShouldNotBeFound("isactive.in=" + UPDATED_ISACTIVE);
    }

    @Test
    @Transactional
    void getAllProjectsByIsactiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where isactive is not null
        defaultProjectsShouldBeFound("isactive.specified=true");

        // Get all the projectsList where isactive is null
        defaultProjectsShouldNotBeFound("isactive.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where createdBy equals to DEFAULT_CREATED_BY
        defaultProjectsShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the projectsList where createdBy equals to UPDATED_CREATED_BY
        defaultProjectsShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultProjectsShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the projectsList where createdBy equals to UPDATED_CREATED_BY
        defaultProjectsShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where createdBy is not null
        defaultProjectsShouldBeFound("createdBy.specified=true");

        // Get all the projectsList where createdBy is null
        defaultProjectsShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultProjectsShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the projectsList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultProjectsShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultProjectsShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the projectsList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultProjectsShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where createdBy is less than DEFAULT_CREATED_BY
        defaultProjectsShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the projectsList where createdBy is less than UPDATED_CREATED_BY
        defaultProjectsShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where createdBy is greater than DEFAULT_CREATED_BY
        defaultProjectsShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the projectsList where createdBy is greater than SMALLER_CREATED_BY
        defaultProjectsShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where createdAt equals to DEFAULT_CREATED_AT
        defaultProjectsShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the projectsList where createdAt equals to UPDATED_CREATED_AT
        defaultProjectsShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultProjectsShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the projectsList where createdAt equals to UPDATED_CREATED_AT
        defaultProjectsShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllProjectsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where createdAt is not null
        defaultProjectsShouldBeFound("createdAt.specified=true");

        // Get all the projectsList where createdAt is null
        defaultProjectsShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where updatedBy equals to DEFAULT_UPDATED_BY
        defaultProjectsShouldBeFound("updatedBy.equals=" + DEFAULT_UPDATED_BY);

        // Get all the projectsList where updatedBy equals to UPDATED_UPDATED_BY
        defaultProjectsShouldNotBeFound("updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where updatedBy in DEFAULT_UPDATED_BY or UPDATED_UPDATED_BY
        defaultProjectsShouldBeFound("updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY);

        // Get all the projectsList where updatedBy equals to UPDATED_UPDATED_BY
        defaultProjectsShouldNotBeFound("updatedBy.in=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where updatedBy is not null
        defaultProjectsShouldBeFound("updatedBy.specified=true");

        // Get all the projectsList where updatedBy is null
        defaultProjectsShouldNotBeFound("updatedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where updatedBy is greater than or equal to DEFAULT_UPDATED_BY
        defaultProjectsShouldBeFound("updatedBy.greaterThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the projectsList where updatedBy is greater than or equal to UPDATED_UPDATED_BY
        defaultProjectsShouldNotBeFound("updatedBy.greaterThanOrEqual=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where updatedBy is less than or equal to DEFAULT_UPDATED_BY
        defaultProjectsShouldBeFound("updatedBy.lessThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the projectsList where updatedBy is less than or equal to SMALLER_UPDATED_BY
        defaultProjectsShouldNotBeFound("updatedBy.lessThanOrEqual=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where updatedBy is less than DEFAULT_UPDATED_BY
        defaultProjectsShouldNotBeFound("updatedBy.lessThan=" + DEFAULT_UPDATED_BY);

        // Get all the projectsList where updatedBy is less than UPDATED_UPDATED_BY
        defaultProjectsShouldBeFound("updatedBy.lessThan=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where updatedBy is greater than DEFAULT_UPDATED_BY
        defaultProjectsShouldNotBeFound("updatedBy.greaterThan=" + DEFAULT_UPDATED_BY);

        // Get all the projectsList where updatedBy is greater than SMALLER_UPDATED_BY
        defaultProjectsShouldBeFound("updatedBy.greaterThan=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultProjectsShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the projectsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultProjectsShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultProjectsShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the projectsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultProjectsShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllProjectsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList where updatedAt is not null
        defaultProjectsShouldBeFound("updatedAt.specified=true");

        // Get all the projectsList where updatedAt is null
        defaultProjectsShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByDefaultTemplateIsEqualToSomething() throws Exception {
        Templates defaultTemplate;
        if (TestUtil.findAll(em, Templates.class).isEmpty()) {
            projectsRepository.saveAndFlush(projects);
            defaultTemplate = TemplatesResourceIT.createEntity(em);
        } else {
            defaultTemplate = TestUtil.findAll(em, Templates.class).get(0);
        }
        em.persist(defaultTemplate);
        em.flush();
        projects.setDefaultTemplate(defaultTemplate);
        projectsRepository.saveAndFlush(projects);
        Long defaultTemplateId = defaultTemplate.getId();

        // Get all the projectsList where defaultTemplate equals to defaultTemplateId
        defaultProjectsShouldBeFound("defaultTemplateId.equals=" + defaultTemplateId);

        // Get all the projectsList where defaultTemplate equals to (defaultTemplateId + 1)
        defaultProjectsShouldNotBeFound("defaultTemplateId.equals=" + (defaultTemplateId + 1));
    }

    @Test
    @Transactional
    void getAllProjectsByCompanyIsEqualToSomething() throws Exception {
        Companies company;
        if (TestUtil.findAll(em, Companies.class).isEmpty()) {
            projectsRepository.saveAndFlush(projects);
            company = CompaniesResourceIT.createEntity(em);
        } else {
            company = TestUtil.findAll(em, Companies.class).get(0);
        }
        em.persist(company);
        em.flush();
        projects.setCompany(company);
        projectsRepository.saveAndFlush(projects);
        Long companyId = company.getId();

        // Get all the projectsList where company equals to companyId
        defaultProjectsShouldBeFound("companyId.equals=" + companyId);

        // Get all the projectsList where company equals to (companyId + 1)
        defaultProjectsShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    @Test
    @Transactional
    void getAllProjectsByMilestonesProjectIsEqualToSomething() throws Exception {
        Milestones milestonesProject;
        if (TestUtil.findAll(em, Milestones.class).isEmpty()) {
            projectsRepository.saveAndFlush(projects);
            milestonesProject = MilestonesResourceIT.createEntity(em);
        } else {
            milestonesProject = TestUtil.findAll(em, Milestones.class).get(0);
        }
        em.persist(milestonesProject);
        em.flush();
        projects.addMilestonesProject(milestonesProject);
        projectsRepository.saveAndFlush(projects);
        Long milestonesProjectId = milestonesProject.getId();

        // Get all the projectsList where milestonesProject equals to milestonesProjectId
        defaultProjectsShouldBeFound("milestonesProjectId.equals=" + milestonesProjectId);

        // Get all the projectsList where milestonesProject equals to (milestonesProjectId + 1)
        defaultProjectsShouldNotBeFound("milestonesProjectId.equals=" + (milestonesProjectId + 1));
    }

    @Test
    @Transactional
    void getAllProjectsByTestplansProjectIsEqualToSomething() throws Exception {
        TestPlans testplansProject;
        if (TestUtil.findAll(em, TestPlans.class).isEmpty()) {
            projectsRepository.saveAndFlush(projects);
            testplansProject = TestPlansResourceIT.createEntity(em);
        } else {
            testplansProject = TestUtil.findAll(em, TestPlans.class).get(0);
        }
        em.persist(testplansProject);
        em.flush();
        projects.addTestplansProject(testplansProject);
        projectsRepository.saveAndFlush(projects);
        Long testplansProjectId = testplansProject.getId();

        // Get all the projectsList where testplansProject equals to testplansProjectId
        defaultProjectsShouldBeFound("testplansProjectId.equals=" + testplansProjectId);

        // Get all the projectsList where testplansProject equals to (testplansProjectId + 1)
        defaultProjectsShouldNotBeFound("testplansProjectId.equals=" + (testplansProjectId + 1));
    }

    @Test
    @Transactional
    void getAllProjectsByTestsuitesProjectIsEqualToSomething() throws Exception {
        TestSuites testsuitesProject;
        if (TestUtil.findAll(em, TestSuites.class).isEmpty()) {
            projectsRepository.saveAndFlush(projects);
            testsuitesProject = TestSuitesResourceIT.createEntity(em);
        } else {
            testsuitesProject = TestUtil.findAll(em, TestSuites.class).get(0);
        }
        em.persist(testsuitesProject);
        em.flush();
        projects.addTestsuitesProject(testsuitesProject);
        projectsRepository.saveAndFlush(projects);
        Long testsuitesProjectId = testsuitesProject.getId();

        // Get all the projectsList where testsuitesProject equals to testsuitesProjectId
        defaultProjectsShouldBeFound("testsuitesProjectId.equals=" + testsuitesProjectId);

        // Get all the projectsList where testsuitesProject equals to (testsuitesProjectId + 1)
        defaultProjectsShouldNotBeFound("testsuitesProjectId.equals=" + (testsuitesProjectId + 1));
    }

    @Test
    @Transactional
    void getAllProjectsByUserIsEqualToSomething() throws Exception {
        Users user;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            projectsRepository.saveAndFlush(projects);
            user = UsersResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(user);
        em.flush();
        projects.addUser(user);
        projectsRepository.saveAndFlush(projects);
        Long userId = user.getId();

        // Get all the projectsList where user equals to userId
        defaultProjectsShouldBeFound("userId.equals=" + userId);

        // Get all the projectsList where user equals to (userId + 1)
        defaultProjectsShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProjectsShouldBeFound(String filter) throws Exception {
        restProjectsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projects.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].isactive").value(hasItem(DEFAULT_ISACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restProjectsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProjectsShouldNotBeFound(String filter) throws Exception {
        restProjectsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProjectsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProjects() throws Exception {
        // Get the projects
        restProjectsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProjects() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();

        // Update the projects
        Projects updatedProjects = projectsRepository.findById(projects.getId()).get();
        // Disconnect from session so that the updates on updatedProjects are not directly saved in db
        em.detach(updatedProjects);
        updatedProjects
            .projectName(UPDATED_PROJECT_NAME)
            .description(UPDATED_DESCRIPTION)
            .isactive(UPDATED_ISACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restProjectsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProjects.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProjects))
            )
            .andExpect(status().isOk());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
        Projects testProjects = projectsList.get(projectsList.size() - 1);
        assertThat(testProjects.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testProjects.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProjects.getIsactive()).isEqualTo(UPDATED_ISACTIVE);
        assertThat(testProjects.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProjects.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProjects.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testProjects.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingProjects() throws Exception {
        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();
        projects.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projects.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projects))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProjects() throws Exception {
        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();
        projects.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projects))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProjects() throws Exception {
        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();
        projects.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projects)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjectsWithPatch() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();

        // Update the projects using partial update
        Projects partialUpdatedProjects = new Projects();
        partialUpdatedProjects.setId(projects.getId());

        partialUpdatedProjects.projectName(UPDATED_PROJECT_NAME).createdBy(UPDATED_CREATED_BY);

        restProjectsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjects.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjects))
            )
            .andExpect(status().isOk());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
        Projects testProjects = projectsList.get(projectsList.size() - 1);
        assertThat(testProjects.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testProjects.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProjects.getIsactive()).isEqualTo(DEFAULT_ISACTIVE);
        assertThat(testProjects.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProjects.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testProjects.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testProjects.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateProjectsWithPatch() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();

        // Update the projects using partial update
        Projects partialUpdatedProjects = new Projects();
        partialUpdatedProjects.setId(projects.getId());

        partialUpdatedProjects
            .projectName(UPDATED_PROJECT_NAME)
            .description(UPDATED_DESCRIPTION)
            .isactive(UPDATED_ISACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restProjectsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjects.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjects))
            )
            .andExpect(status().isOk());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
        Projects testProjects = projectsList.get(projectsList.size() - 1);
        assertThat(testProjects.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testProjects.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProjects.getIsactive()).isEqualTo(UPDATED_ISACTIVE);
        assertThat(testProjects.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProjects.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProjects.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testProjects.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingProjects() throws Exception {
        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();
        projects.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projects.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projects))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProjects() throws Exception {
        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();
        projects.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projects))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProjects() throws Exception {
        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();
        projects.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(projects)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProjects() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        int databaseSizeBeforeDelete = projectsRepository.findAll().size();

        // Delete the projects
        restProjectsMockMvc
            .perform(delete(ENTITY_API_URL_ID, projects.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
