package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.Sections;
import com.venturedive.blazetest.domain.Sections;
import com.venturedive.blazetest.domain.TestCases;
import com.venturedive.blazetest.domain.TestSuites;
import com.venturedive.blazetest.repository.SectionsRepository;
import com.venturedive.blazetest.service.criteria.SectionsCriteria;
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
 * Integration tests for the {@link SectionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SectionsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;
    private static final Integer SMALLER_CREATED_BY = 1 - 1;

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_UPDATED_BY = 1;
    private static final Integer UPDATED_UPDATED_BY = 2;
    private static final Integer SMALLER_UPDATED_BY = 1 - 1;

    private static final String ENTITY_API_URL = "/api/sections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SectionsRepository sectionsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSectionsMockMvc;

    private Sections sections;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sections createEntity(EntityManager em) {
        Sections sections = new Sections()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY);
        return sections;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sections createUpdatedEntity(EntityManager em) {
        Sections sections = new Sections()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);
        return sections;
    }

    @BeforeEach
    public void initTest() {
        sections = createEntity(em);
    }

    @Test
    @Transactional
    void createSections() throws Exception {
        int databaseSizeBeforeCreate = sectionsRepository.findAll().size();
        // Create the Sections
        restSectionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sections)))
            .andExpect(status().isCreated());

        // Validate the Sections in the database
        List<Sections> sectionsList = sectionsRepository.findAll();
        assertThat(sectionsList).hasSize(databaseSizeBeforeCreate + 1);
        Sections testSections = sectionsList.get(sectionsList.size() - 1);
        assertThat(testSections.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSections.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSections.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSections.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSections.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testSections.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createSectionsWithExistingId() throws Exception {
        // Create the Sections with an existing ID
        sections.setId(1L);

        int databaseSizeBeforeCreate = sectionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSectionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sections)))
            .andExpect(status().isBadRequest());

        // Validate the Sections in the database
        List<Sections> sectionsList = sectionsRepository.findAll();
        assertThat(sectionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSections() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList
        restSectionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sections.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getSections() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get the sections
        restSectionsMockMvc
            .perform(get(ENTITY_API_URL_ID, sections.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sections.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getSectionsByIdFiltering() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        Long id = sections.getId();

        defaultSectionsShouldBeFound("id.equals=" + id);
        defaultSectionsShouldNotBeFound("id.notEquals=" + id);

        defaultSectionsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSectionsShouldNotBeFound("id.greaterThan=" + id);

        defaultSectionsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSectionsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSectionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where name equals to DEFAULT_NAME
        defaultSectionsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the sectionsList where name equals to UPDATED_NAME
        defaultSectionsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSectionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSectionsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the sectionsList where name equals to UPDATED_NAME
        defaultSectionsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSectionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where name is not null
        defaultSectionsShouldBeFound("name.specified=true");

        // Get all the sectionsList where name is null
        defaultSectionsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSectionsByNameContainsSomething() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where name contains DEFAULT_NAME
        defaultSectionsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the sectionsList where name contains UPDATED_NAME
        defaultSectionsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSectionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where name does not contain DEFAULT_NAME
        defaultSectionsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the sectionsList where name does not contain UPDATED_NAME
        defaultSectionsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where createdAt equals to DEFAULT_CREATED_AT
        defaultSectionsShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the sectionsList where createdAt equals to UPDATED_CREATED_AT
        defaultSectionsShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultSectionsShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the sectionsList where createdAt equals to UPDATED_CREATED_AT
        defaultSectionsShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where createdAt is not null
        defaultSectionsShouldBeFound("createdAt.specified=true");

        // Get all the sectionsList where createdAt is null
        defaultSectionsShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where createdBy equals to DEFAULT_CREATED_BY
        defaultSectionsShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the sectionsList where createdBy equals to UPDATED_CREATED_BY
        defaultSectionsShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultSectionsShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the sectionsList where createdBy equals to UPDATED_CREATED_BY
        defaultSectionsShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where createdBy is not null
        defaultSectionsShouldBeFound("createdBy.specified=true");

        // Get all the sectionsList where createdBy is null
        defaultSectionsShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultSectionsShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the sectionsList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultSectionsShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultSectionsShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the sectionsList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultSectionsShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where createdBy is less than DEFAULT_CREATED_BY
        defaultSectionsShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the sectionsList where createdBy is less than UPDATED_CREATED_BY
        defaultSectionsShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where createdBy is greater than DEFAULT_CREATED_BY
        defaultSectionsShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the sectionsList where createdBy is greater than SMALLER_CREATED_BY
        defaultSectionsShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultSectionsShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the sectionsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSectionsShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultSectionsShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the sectionsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSectionsShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where updatedAt is not null
        defaultSectionsShouldBeFound("updatedAt.specified=true");

        // Get all the sectionsList where updatedAt is null
        defaultSectionsShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where updatedBy equals to DEFAULT_UPDATED_BY
        defaultSectionsShouldBeFound("updatedBy.equals=" + DEFAULT_UPDATED_BY);

        // Get all the sectionsList where updatedBy equals to UPDATED_UPDATED_BY
        defaultSectionsShouldNotBeFound("updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where updatedBy in DEFAULT_UPDATED_BY or UPDATED_UPDATED_BY
        defaultSectionsShouldBeFound("updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY);

        // Get all the sectionsList where updatedBy equals to UPDATED_UPDATED_BY
        defaultSectionsShouldNotBeFound("updatedBy.in=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where updatedBy is not null
        defaultSectionsShouldBeFound("updatedBy.specified=true");

        // Get all the sectionsList where updatedBy is null
        defaultSectionsShouldNotBeFound("updatedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where updatedBy is greater than or equal to DEFAULT_UPDATED_BY
        defaultSectionsShouldBeFound("updatedBy.greaterThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the sectionsList where updatedBy is greater than or equal to UPDATED_UPDATED_BY
        defaultSectionsShouldNotBeFound("updatedBy.greaterThanOrEqual=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where updatedBy is less than or equal to DEFAULT_UPDATED_BY
        defaultSectionsShouldBeFound("updatedBy.lessThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the sectionsList where updatedBy is less than or equal to SMALLER_UPDATED_BY
        defaultSectionsShouldNotBeFound("updatedBy.lessThanOrEqual=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where updatedBy is less than DEFAULT_UPDATED_BY
        defaultSectionsShouldNotBeFound("updatedBy.lessThan=" + DEFAULT_UPDATED_BY);

        // Get all the sectionsList where updatedBy is less than UPDATED_UPDATED_BY
        defaultSectionsShouldBeFound("updatedBy.lessThan=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByUpdatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        // Get all the sectionsList where updatedBy is greater than DEFAULT_UPDATED_BY
        defaultSectionsShouldNotBeFound("updatedBy.greaterThan=" + DEFAULT_UPDATED_BY);

        // Get all the sectionsList where updatedBy is greater than SMALLER_UPDATED_BY
        defaultSectionsShouldBeFound("updatedBy.greaterThan=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllSectionsByTestSuiteIsEqualToSomething() throws Exception {
        TestSuites testSuite;
        if (TestUtil.findAll(em, TestSuites.class).isEmpty()) {
            sectionsRepository.saveAndFlush(sections);
            testSuite = TestSuitesResourceIT.createEntity(em);
        } else {
            testSuite = TestUtil.findAll(em, TestSuites.class).get(0);
        }
        em.persist(testSuite);
        em.flush();
        sections.setTestSuite(testSuite);
        sectionsRepository.saveAndFlush(sections);
        Long testSuiteId = testSuite.getId();

        // Get all the sectionsList where testSuite equals to testSuiteId
        defaultSectionsShouldBeFound("testSuiteId.equals=" + testSuiteId);

        // Get all the sectionsList where testSuite equals to (testSuiteId + 1)
        defaultSectionsShouldNotBeFound("testSuiteId.equals=" + (testSuiteId + 1));
    }

    @Test
    @Transactional
    void getAllSectionsByParentSectionIsEqualToSomething() throws Exception {
        Sections parentSection;
        if (TestUtil.findAll(em, Sections.class).isEmpty()) {
            sectionsRepository.saveAndFlush(sections);
            parentSection = SectionsResourceIT.createEntity(em);
        } else {
            parentSection = TestUtil.findAll(em, Sections.class).get(0);
        }
        em.persist(parentSection);
        em.flush();
        sections.setParentSection(parentSection);
        sectionsRepository.saveAndFlush(sections);
        Long parentSectionId = parentSection.getId();

        // Get all the sectionsList where parentSection equals to parentSectionId
        defaultSectionsShouldBeFound("parentSectionId.equals=" + parentSectionId);

        // Get all the sectionsList where parentSection equals to (parentSectionId + 1)
        defaultSectionsShouldNotBeFound("parentSectionId.equals=" + (parentSectionId + 1));
    }

    @Test
    @Transactional
    void getAllSectionsBySectionsParentsectionIsEqualToSomething() throws Exception {
        Sections sectionsParentsection;
        if (TestUtil.findAll(em, Sections.class).isEmpty()) {
            sectionsRepository.saveAndFlush(sections);
            sectionsParentsection = SectionsResourceIT.createEntity(em);
        } else {
            sectionsParentsection = TestUtil.findAll(em, Sections.class).get(0);
        }
        em.persist(sectionsParentsection);
        em.flush();
        sections.addSectionsParentsection(sectionsParentsection);
        sectionsRepository.saveAndFlush(sections);
        Long sectionsParentsectionId = sectionsParentsection.getId();

        // Get all the sectionsList where sectionsParentsection equals to sectionsParentsectionId
        defaultSectionsShouldBeFound("sectionsParentsectionId.equals=" + sectionsParentsectionId);

        // Get all the sectionsList where sectionsParentsection equals to (sectionsParentsectionId + 1)
        defaultSectionsShouldNotBeFound("sectionsParentsectionId.equals=" + (sectionsParentsectionId + 1));
    }

    @Test
    @Transactional
    void getAllSectionsByTestcasesSectionIsEqualToSomething() throws Exception {
        TestCases testcasesSection;
        if (TestUtil.findAll(em, TestCases.class).isEmpty()) {
            sectionsRepository.saveAndFlush(sections);
            testcasesSection = TestCasesResourceIT.createEntity(em);
        } else {
            testcasesSection = TestUtil.findAll(em, TestCases.class).get(0);
        }
        em.persist(testcasesSection);
        em.flush();
        sections.addTestcasesSection(testcasesSection);
        sectionsRepository.saveAndFlush(sections);
        Long testcasesSectionId = testcasesSection.getId();

        // Get all the sectionsList where testcasesSection equals to testcasesSectionId
        defaultSectionsShouldBeFound("testcasesSectionId.equals=" + testcasesSectionId);

        // Get all the sectionsList where testcasesSection equals to (testcasesSectionId + 1)
        defaultSectionsShouldNotBeFound("testcasesSectionId.equals=" + (testcasesSectionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSectionsShouldBeFound(String filter) throws Exception {
        restSectionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sections.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));

        // Check, that the count call also returns 1
        restSectionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSectionsShouldNotBeFound(String filter) throws Exception {
        restSectionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSectionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSections() throws Exception {
        // Get the sections
        restSectionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSections() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        int databaseSizeBeforeUpdate = sectionsRepository.findAll().size();

        // Update the sections
        Sections updatedSections = sectionsRepository.findById(sections.getId()).get();
        // Disconnect from session so that the updates on updatedSections are not directly saved in db
        em.detach(updatedSections);
        updatedSections
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restSectionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSections.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSections))
            )
            .andExpect(status().isOk());

        // Validate the Sections in the database
        List<Sections> sectionsList = sectionsRepository.findAll();
        assertThat(sectionsList).hasSize(databaseSizeBeforeUpdate);
        Sections testSections = sectionsList.get(sectionsList.size() - 1);
        assertThat(testSections.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSections.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSections.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSections.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSections.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSections.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingSections() throws Exception {
        int databaseSizeBeforeUpdate = sectionsRepository.findAll().size();
        sections.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSectionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sections.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sections))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sections in the database
        List<Sections> sectionsList = sectionsRepository.findAll();
        assertThat(sectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSections() throws Exception {
        int databaseSizeBeforeUpdate = sectionsRepository.findAll().size();
        sections.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sections))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sections in the database
        List<Sections> sectionsList = sectionsRepository.findAll();
        assertThat(sectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSections() throws Exception {
        int databaseSizeBeforeUpdate = sectionsRepository.findAll().size();
        sections.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sections)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sections in the database
        List<Sections> sectionsList = sectionsRepository.findAll();
        assertThat(sectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSectionsWithPatch() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        int databaseSizeBeforeUpdate = sectionsRepository.findAll().size();

        // Update the sections using partial update
        Sections partialUpdatedSections = new Sections();
        partialUpdatedSections.setId(sections.getId());

        partialUpdatedSections.updatedAt(UPDATED_UPDATED_AT).updatedBy(UPDATED_UPDATED_BY);

        restSectionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSections.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSections))
            )
            .andExpect(status().isOk());

        // Validate the Sections in the database
        List<Sections> sectionsList = sectionsRepository.findAll();
        assertThat(sectionsList).hasSize(databaseSizeBeforeUpdate);
        Sections testSections = sectionsList.get(sectionsList.size() - 1);
        assertThat(testSections.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSections.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSections.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSections.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSections.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSections.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateSectionsWithPatch() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        int databaseSizeBeforeUpdate = sectionsRepository.findAll().size();

        // Update the sections using partial update
        Sections partialUpdatedSections = new Sections();
        partialUpdatedSections.setId(sections.getId());

        partialUpdatedSections
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restSectionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSections.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSections))
            )
            .andExpect(status().isOk());

        // Validate the Sections in the database
        List<Sections> sectionsList = sectionsRepository.findAll();
        assertThat(sectionsList).hasSize(databaseSizeBeforeUpdate);
        Sections testSections = sectionsList.get(sectionsList.size() - 1);
        assertThat(testSections.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSections.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSections.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSections.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSections.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSections.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingSections() throws Exception {
        int databaseSizeBeforeUpdate = sectionsRepository.findAll().size();
        sections.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSectionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sections.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sections))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sections in the database
        List<Sections> sectionsList = sectionsRepository.findAll();
        assertThat(sectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSections() throws Exception {
        int databaseSizeBeforeUpdate = sectionsRepository.findAll().size();
        sections.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sections))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sections in the database
        List<Sections> sectionsList = sectionsRepository.findAll();
        assertThat(sectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSections() throws Exception {
        int databaseSizeBeforeUpdate = sectionsRepository.findAll().size();
        sections.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sections)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sections in the database
        List<Sections> sectionsList = sectionsRepository.findAll();
        assertThat(sectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSections() throws Exception {
        // Initialize the database
        sectionsRepository.saveAndFlush(sections);

        int databaseSizeBeforeDelete = sectionsRepository.findAll().size();

        // Delete the sections
        restSectionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, sections.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sections> sectionsList = sectionsRepository.findAll();
        assertThat(sectionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
