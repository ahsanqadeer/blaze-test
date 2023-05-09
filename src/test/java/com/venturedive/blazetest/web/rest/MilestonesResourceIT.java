package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.Milestones;
import com.venturedive.blazetest.domain.Milestones;
import com.venturedive.blazetest.domain.Projects;
import com.venturedive.blazetest.domain.TestCases;
import com.venturedive.blazetest.domain.TestRuns;
import com.venturedive.blazetest.repository.MilestonesRepository;
import com.venturedive.blazetest.service.criteria.MilestonesCriteria;
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
 * Integration tests for the {@link MilestonesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MilestonesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_COMPLETED = false;
    private static final Boolean UPDATED_IS_COMPLETED = true;

    private static final String ENTITY_API_URL = "/api/milestones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MilestonesRepository milestonesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMilestonesMockMvc;

    private Milestones milestones;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Milestones createEntity(EntityManager em) {
        Milestones milestones = new Milestones()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .reference(DEFAULT_REFERENCE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .isCompleted(DEFAULT_IS_COMPLETED);
        return milestones;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Milestones createUpdatedEntity(EntityManager em) {
        Milestones milestones = new Milestones()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .reference(UPDATED_REFERENCE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isCompleted(UPDATED_IS_COMPLETED);
        return milestones;
    }

    @BeforeEach
    public void initTest() {
        milestones = createEntity(em);
    }

    @Test
    @Transactional
    void createMilestones() throws Exception {
        int databaseSizeBeforeCreate = milestonesRepository.findAll().size();
        // Create the Milestones
        restMilestonesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(milestones)))
            .andExpect(status().isCreated());

        // Validate the Milestones in the database
        List<Milestones> milestonesList = milestonesRepository.findAll();
        assertThat(milestonesList).hasSize(databaseSizeBeforeCreate + 1);
        Milestones testMilestones = milestonesList.get(milestonesList.size() - 1);
        assertThat(testMilestones.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMilestones.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMilestones.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testMilestones.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testMilestones.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testMilestones.getIsCompleted()).isEqualTo(DEFAULT_IS_COMPLETED);
    }

    @Test
    @Transactional
    void createMilestonesWithExistingId() throws Exception {
        // Create the Milestones with an existing ID
        milestones.setId(1L);

        int databaseSizeBeforeCreate = milestonesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMilestonesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(milestones)))
            .andExpect(status().isBadRequest());

        // Validate the Milestones in the database
        List<Milestones> milestonesList = milestonesRepository.findAll();
        assertThat(milestonesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMilestones() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        // Get all the milestonesList
        restMilestonesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(milestones.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isCompleted").value(hasItem(DEFAULT_IS_COMPLETED.booleanValue())));
    }

    @Test
    @Transactional
    void getMilestones() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        // Get the milestones
        restMilestonesMockMvc
            .perform(get(ENTITY_API_URL_ID, milestones.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(milestones.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.isCompleted").value(DEFAULT_IS_COMPLETED.booleanValue()));
    }

    @Test
    @Transactional
    void getMilestonesByIdFiltering() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        Long id = milestones.getId();

        defaultMilestonesShouldBeFound("id.equals=" + id);
        defaultMilestonesShouldNotBeFound("id.notEquals=" + id);

        defaultMilestonesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMilestonesShouldNotBeFound("id.greaterThan=" + id);

        defaultMilestonesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMilestonesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMilestonesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        // Get all the milestonesList where name equals to DEFAULT_NAME
        defaultMilestonesShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the milestonesList where name equals to UPDATED_NAME
        defaultMilestonesShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMilestonesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        // Get all the milestonesList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMilestonesShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the milestonesList where name equals to UPDATED_NAME
        defaultMilestonesShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMilestonesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        // Get all the milestonesList where name is not null
        defaultMilestonesShouldBeFound("name.specified=true");

        // Get all the milestonesList where name is null
        defaultMilestonesShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMilestonesByNameContainsSomething() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        // Get all the milestonesList where name contains DEFAULT_NAME
        defaultMilestonesShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the milestonesList where name contains UPDATED_NAME
        defaultMilestonesShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMilestonesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        // Get all the milestonesList where name does not contain DEFAULT_NAME
        defaultMilestonesShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the milestonesList where name does not contain UPDATED_NAME
        defaultMilestonesShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMilestonesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        // Get all the milestonesList where startDate equals to DEFAULT_START_DATE
        defaultMilestonesShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the milestonesList where startDate equals to UPDATED_START_DATE
        defaultMilestonesShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllMilestonesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        // Get all the milestonesList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultMilestonesShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the milestonesList where startDate equals to UPDATED_START_DATE
        defaultMilestonesShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllMilestonesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        // Get all the milestonesList where startDate is not null
        defaultMilestonesShouldBeFound("startDate.specified=true");

        // Get all the milestonesList where startDate is null
        defaultMilestonesShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMilestonesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        // Get all the milestonesList where endDate equals to DEFAULT_END_DATE
        defaultMilestonesShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the milestonesList where endDate equals to UPDATED_END_DATE
        defaultMilestonesShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllMilestonesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        // Get all the milestonesList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultMilestonesShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the milestonesList where endDate equals to UPDATED_END_DATE
        defaultMilestonesShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllMilestonesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        // Get all the milestonesList where endDate is not null
        defaultMilestonesShouldBeFound("endDate.specified=true");

        // Get all the milestonesList where endDate is null
        defaultMilestonesShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMilestonesByIsCompletedIsEqualToSomething() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        // Get all the milestonesList where isCompleted equals to DEFAULT_IS_COMPLETED
        defaultMilestonesShouldBeFound("isCompleted.equals=" + DEFAULT_IS_COMPLETED);

        // Get all the milestonesList where isCompleted equals to UPDATED_IS_COMPLETED
        defaultMilestonesShouldNotBeFound("isCompleted.equals=" + UPDATED_IS_COMPLETED);
    }

    @Test
    @Transactional
    void getAllMilestonesByIsCompletedIsInShouldWork() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        // Get all the milestonesList where isCompleted in DEFAULT_IS_COMPLETED or UPDATED_IS_COMPLETED
        defaultMilestonesShouldBeFound("isCompleted.in=" + DEFAULT_IS_COMPLETED + "," + UPDATED_IS_COMPLETED);

        // Get all the milestonesList where isCompleted equals to UPDATED_IS_COMPLETED
        defaultMilestonesShouldNotBeFound("isCompleted.in=" + UPDATED_IS_COMPLETED);
    }

    @Test
    @Transactional
    void getAllMilestonesByIsCompletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        // Get all the milestonesList where isCompleted is not null
        defaultMilestonesShouldBeFound("isCompleted.specified=true");

        // Get all the milestonesList where isCompleted is null
        defaultMilestonesShouldNotBeFound("isCompleted.specified=false");
    }

    @Test
    @Transactional
    void getAllMilestonesByParentMilestoneIsEqualToSomething() throws Exception {
        Milestones parentMilestone;
        if (TestUtil.findAll(em, Milestones.class).isEmpty()) {
            milestonesRepository.saveAndFlush(milestones);
            parentMilestone = MilestonesResourceIT.createEntity(em);
        } else {
            parentMilestone = TestUtil.findAll(em, Milestones.class).get(0);
        }
        em.persist(parentMilestone);
        em.flush();
        milestones.setParentMilestone(parentMilestone);
        milestonesRepository.saveAndFlush(milestones);
        Long parentMilestoneId = parentMilestone.getId();

        // Get all the milestonesList where parentMilestone equals to parentMilestoneId
        defaultMilestonesShouldBeFound("parentMilestoneId.equals=" + parentMilestoneId);

        // Get all the milestonesList where parentMilestone equals to (parentMilestoneId + 1)
        defaultMilestonesShouldNotBeFound("parentMilestoneId.equals=" + (parentMilestoneId + 1));
    }

    @Test
    @Transactional
    void getAllMilestonesByProjectIsEqualToSomething() throws Exception {
        Projects project;
        if (TestUtil.findAll(em, Projects.class).isEmpty()) {
            milestonesRepository.saveAndFlush(milestones);
            project = ProjectsResourceIT.createEntity(em);
        } else {
            project = TestUtil.findAll(em, Projects.class).get(0);
        }
        em.persist(project);
        em.flush();
        milestones.setProject(project);
        milestonesRepository.saveAndFlush(milestones);
        Long projectId = project.getId();

        // Get all the milestonesList where project equals to projectId
        defaultMilestonesShouldBeFound("projectId.equals=" + projectId);

        // Get all the milestonesList where project equals to (projectId + 1)
        defaultMilestonesShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    @Test
    @Transactional
    void getAllMilestonesByMilestonesParentmilestoneIsEqualToSomething() throws Exception {
        Milestones milestonesParentmilestone;
        if (TestUtil.findAll(em, Milestones.class).isEmpty()) {
            milestonesRepository.saveAndFlush(milestones);
            milestonesParentmilestone = MilestonesResourceIT.createEntity(em);
        } else {
            milestonesParentmilestone = TestUtil.findAll(em, Milestones.class).get(0);
        }
        em.persist(milestonesParentmilestone);
        em.flush();
        milestones.addMilestonesParentmilestone(milestonesParentmilestone);
        milestonesRepository.saveAndFlush(milestones);
        Long milestonesParentmilestoneId = milestonesParentmilestone.getId();

        // Get all the milestonesList where milestonesParentmilestone equals to milestonesParentmilestoneId
        defaultMilestonesShouldBeFound("milestonesParentmilestoneId.equals=" + milestonesParentmilestoneId);

        // Get all the milestonesList where milestonesParentmilestone equals to (milestonesParentmilestoneId + 1)
        defaultMilestonesShouldNotBeFound("milestonesParentmilestoneId.equals=" + (milestonesParentmilestoneId + 1));
    }

    @Test
    @Transactional
    void getAllMilestonesByTestcasesMilestoneIsEqualToSomething() throws Exception {
        TestCases testcasesMilestone;
        if (TestUtil.findAll(em, TestCases.class).isEmpty()) {
            milestonesRepository.saveAndFlush(milestones);
            testcasesMilestone = TestCasesResourceIT.createEntity(em);
        } else {
            testcasesMilestone = TestUtil.findAll(em, TestCases.class).get(0);
        }
        em.persist(testcasesMilestone);
        em.flush();
        milestones.addTestcasesMilestone(testcasesMilestone);
        milestonesRepository.saveAndFlush(milestones);
        Long testcasesMilestoneId = testcasesMilestone.getId();

        // Get all the milestonesList where testcasesMilestone equals to testcasesMilestoneId
        defaultMilestonesShouldBeFound("testcasesMilestoneId.equals=" + testcasesMilestoneId);

        // Get all the milestonesList where testcasesMilestone equals to (testcasesMilestoneId + 1)
        defaultMilestonesShouldNotBeFound("testcasesMilestoneId.equals=" + (testcasesMilestoneId + 1));
    }

    @Test
    @Transactional
    void getAllMilestonesByTestrunsMilestoneIsEqualToSomething() throws Exception {
        TestRuns testrunsMilestone;
        if (TestUtil.findAll(em, TestRuns.class).isEmpty()) {
            milestonesRepository.saveAndFlush(milestones);
            testrunsMilestone = TestRunsResourceIT.createEntity(em);
        } else {
            testrunsMilestone = TestUtil.findAll(em, TestRuns.class).get(0);
        }
        em.persist(testrunsMilestone);
        em.flush();
        milestones.addTestrunsMilestone(testrunsMilestone);
        milestonesRepository.saveAndFlush(milestones);
        Long testrunsMilestoneId = testrunsMilestone.getId();

        // Get all the milestonesList where testrunsMilestone equals to testrunsMilestoneId
        defaultMilestonesShouldBeFound("testrunsMilestoneId.equals=" + testrunsMilestoneId);

        // Get all the milestonesList where testrunsMilestone equals to (testrunsMilestoneId + 1)
        defaultMilestonesShouldNotBeFound("testrunsMilestoneId.equals=" + (testrunsMilestoneId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMilestonesShouldBeFound(String filter) throws Exception {
        restMilestonesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(milestones.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isCompleted").value(hasItem(DEFAULT_IS_COMPLETED.booleanValue())));

        // Check, that the count call also returns 1
        restMilestonesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMilestonesShouldNotBeFound(String filter) throws Exception {
        restMilestonesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMilestonesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMilestones() throws Exception {
        // Get the milestones
        restMilestonesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMilestones() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        int databaseSizeBeforeUpdate = milestonesRepository.findAll().size();

        // Update the milestones
        Milestones updatedMilestones = milestonesRepository.findById(milestones.getId()).get();
        // Disconnect from session so that the updates on updatedMilestones are not directly saved in db
        em.detach(updatedMilestones);
        updatedMilestones
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .reference(UPDATED_REFERENCE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isCompleted(UPDATED_IS_COMPLETED);

        restMilestonesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMilestones.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMilestones))
            )
            .andExpect(status().isOk());

        // Validate the Milestones in the database
        List<Milestones> milestonesList = milestonesRepository.findAll();
        assertThat(milestonesList).hasSize(databaseSizeBeforeUpdate);
        Milestones testMilestones = milestonesList.get(milestonesList.size() - 1);
        assertThat(testMilestones.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMilestones.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMilestones.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMilestones.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMilestones.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testMilestones.getIsCompleted()).isEqualTo(UPDATED_IS_COMPLETED);
    }

    @Test
    @Transactional
    void putNonExistingMilestones() throws Exception {
        int databaseSizeBeforeUpdate = milestonesRepository.findAll().size();
        milestones.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMilestonesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, milestones.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(milestones))
            )
            .andExpect(status().isBadRequest());

        // Validate the Milestones in the database
        List<Milestones> milestonesList = milestonesRepository.findAll();
        assertThat(milestonesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMilestones() throws Exception {
        int databaseSizeBeforeUpdate = milestonesRepository.findAll().size();
        milestones.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMilestonesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(milestones))
            )
            .andExpect(status().isBadRequest());

        // Validate the Milestones in the database
        List<Milestones> milestonesList = milestonesRepository.findAll();
        assertThat(milestonesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMilestones() throws Exception {
        int databaseSizeBeforeUpdate = milestonesRepository.findAll().size();
        milestones.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMilestonesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(milestones)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Milestones in the database
        List<Milestones> milestonesList = milestonesRepository.findAll();
        assertThat(milestonesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMilestonesWithPatch() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        int databaseSizeBeforeUpdate = milestonesRepository.findAll().size();

        // Update the milestones using partial update
        Milestones partialUpdatedMilestones = new Milestones();
        partialUpdatedMilestones.setId(milestones.getId());

        partialUpdatedMilestones
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .reference(UPDATED_REFERENCE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restMilestonesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMilestones.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMilestones))
            )
            .andExpect(status().isOk());

        // Validate the Milestones in the database
        List<Milestones> milestonesList = milestonesRepository.findAll();
        assertThat(milestonesList).hasSize(databaseSizeBeforeUpdate);
        Milestones testMilestones = milestonesList.get(milestonesList.size() - 1);
        assertThat(testMilestones.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMilestones.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMilestones.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMilestones.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMilestones.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testMilestones.getIsCompleted()).isEqualTo(DEFAULT_IS_COMPLETED);
    }

    @Test
    @Transactional
    void fullUpdateMilestonesWithPatch() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        int databaseSizeBeforeUpdate = milestonesRepository.findAll().size();

        // Update the milestones using partial update
        Milestones partialUpdatedMilestones = new Milestones();
        partialUpdatedMilestones.setId(milestones.getId());

        partialUpdatedMilestones
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .reference(UPDATED_REFERENCE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isCompleted(UPDATED_IS_COMPLETED);

        restMilestonesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMilestones.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMilestones))
            )
            .andExpect(status().isOk());

        // Validate the Milestones in the database
        List<Milestones> milestonesList = milestonesRepository.findAll();
        assertThat(milestonesList).hasSize(databaseSizeBeforeUpdate);
        Milestones testMilestones = milestonesList.get(milestonesList.size() - 1);
        assertThat(testMilestones.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMilestones.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMilestones.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMilestones.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMilestones.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testMilestones.getIsCompleted()).isEqualTo(UPDATED_IS_COMPLETED);
    }

    @Test
    @Transactional
    void patchNonExistingMilestones() throws Exception {
        int databaseSizeBeforeUpdate = milestonesRepository.findAll().size();
        milestones.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMilestonesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, milestones.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(milestones))
            )
            .andExpect(status().isBadRequest());

        // Validate the Milestones in the database
        List<Milestones> milestonesList = milestonesRepository.findAll();
        assertThat(milestonesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMilestones() throws Exception {
        int databaseSizeBeforeUpdate = milestonesRepository.findAll().size();
        milestones.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMilestonesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(milestones))
            )
            .andExpect(status().isBadRequest());

        // Validate the Milestones in the database
        List<Milestones> milestonesList = milestonesRepository.findAll();
        assertThat(milestonesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMilestones() throws Exception {
        int databaseSizeBeforeUpdate = milestonesRepository.findAll().size();
        milestones.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMilestonesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(milestones))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Milestones in the database
        List<Milestones> milestonesList = milestonesRepository.findAll();
        assertThat(milestonesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMilestones() throws Exception {
        // Initialize the database
        milestonesRepository.saveAndFlush(milestones);

        int databaseSizeBeforeDelete = milestonesRepository.findAll().size();

        // Delete the milestones
        restMilestonesMockMvc
            .perform(delete(ENTITY_API_URL_ID, milestones.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Milestones> milestonesList = milestonesRepository.findAll();
        assertThat(milestonesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
