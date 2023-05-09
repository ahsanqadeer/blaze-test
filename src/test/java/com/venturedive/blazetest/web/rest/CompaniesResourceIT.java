package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.Companies;
import com.venturedive.blazetest.domain.Projects;
import com.venturedive.blazetest.domain.TemplateFields;
import com.venturedive.blazetest.domain.Templates;
import com.venturedive.blazetest.domain.Users;
import com.venturedive.blazetest.repository.CompaniesRepository;
import com.venturedive.blazetest.service.criteria.CompaniesCriteria;
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

/**
 * Integration tests for the {@link CompaniesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompaniesResourceIT {

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_EXPECTED_NO_OF_USERS = 1;
    private static final Integer UPDATED_EXPECTED_NO_OF_USERS = 2;
    private static final Integer SMALLER_EXPECTED_NO_OF_USERS = 1 - 1;

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

    private static final String ENTITY_API_URL = "/api/companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompaniesRepository companiesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompaniesMockMvc;

    private Companies companies;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Companies createEntity(EntityManager em) {
        Companies companies = new Companies()
            .country(DEFAULT_COUNTRY)
            .url(DEFAULT_URL)
            .name(DEFAULT_NAME)
            .expectedNoOfUsers(DEFAULT_EXPECTED_NO_OF_USERS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT);
        return companies;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Companies createUpdatedEntity(EntityManager em) {
        Companies companies = new Companies()
            .country(UPDATED_COUNTRY)
            .url(UPDATED_URL)
            .name(UPDATED_NAME)
            .expectedNoOfUsers(UPDATED_EXPECTED_NO_OF_USERS)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);
        return companies;
    }

    @BeforeEach
    public void initTest() {
        companies = createEntity(em);
    }

    @Test
    @Transactional
    void createCompanies() throws Exception {
        int databaseSizeBeforeCreate = companiesRepository.findAll().size();
        // Create the Companies
        restCompaniesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companies)))
            .andExpect(status().isCreated());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeCreate + 1);
        Companies testCompanies = companiesList.get(companiesList.size() - 1);
        assertThat(testCompanies.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCompanies.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testCompanies.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompanies.getExpectedNoOfUsers()).isEqualTo(DEFAULT_EXPECTED_NO_OF_USERS);
        assertThat(testCompanies.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCompanies.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCompanies.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testCompanies.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createCompaniesWithExistingId() throws Exception {
        // Create the Companies with an existing ID
        companies.setId(1L);

        int databaseSizeBeforeCreate = companiesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompaniesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companies)))
            .andExpect(status().isBadRequest());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = companiesRepository.findAll().size();
        // set the field null
        companies.setCreatedBy(null);

        // Create the Companies, which fails.

        restCompaniesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companies)))
            .andExpect(status().isBadRequest());

        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = companiesRepository.findAll().size();
        // set the field null
        companies.setCreatedAt(null);

        // Create the Companies, which fails.

        restCompaniesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companies)))
            .andExpect(status().isBadRequest());

        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompanies() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList
        restCompaniesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companies.getId().intValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].expectedNoOfUsers").value(hasItem(DEFAULT_EXPECTED_NO_OF_USERS)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getCompanies() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get the companies
        restCompaniesMockMvc
            .perform(get(ENTITY_API_URL_ID, companies.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(companies.getId().intValue()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.expectedNoOfUsers").value(DEFAULT_EXPECTED_NO_OF_USERS))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getCompaniesByIdFiltering() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        Long id = companies.getId();

        defaultCompaniesShouldBeFound("id.equals=" + id);
        defaultCompaniesShouldNotBeFound("id.notEquals=" + id);

        defaultCompaniesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCompaniesShouldNotBeFound("id.greaterThan=" + id);

        defaultCompaniesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCompaniesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCompaniesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where country equals to DEFAULT_COUNTRY
        defaultCompaniesShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the companiesList where country equals to UPDATED_COUNTRY
        defaultCompaniesShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultCompaniesShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the companiesList where country equals to UPDATED_COUNTRY
        defaultCompaniesShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where country is not null
        defaultCompaniesShouldBeFound("country.specified=true");

        // Get all the companiesList where country is null
        defaultCompaniesShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByCountryContainsSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where country contains DEFAULT_COUNTRY
        defaultCompaniesShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the companiesList where country contains UPDATED_COUNTRY
        defaultCompaniesShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where country does not contain DEFAULT_COUNTRY
        defaultCompaniesShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the companiesList where country does not contain UPDATED_COUNTRY
        defaultCompaniesShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllCompaniesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where url equals to DEFAULT_URL
        defaultCompaniesShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the companiesList where url equals to UPDATED_URL
        defaultCompaniesShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllCompaniesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where url in DEFAULT_URL or UPDATED_URL
        defaultCompaniesShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the companiesList where url equals to UPDATED_URL
        defaultCompaniesShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllCompaniesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where url is not null
        defaultCompaniesShouldBeFound("url.specified=true");

        // Get all the companiesList where url is null
        defaultCompaniesShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByUrlContainsSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where url contains DEFAULT_URL
        defaultCompaniesShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the companiesList where url contains UPDATED_URL
        defaultCompaniesShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllCompaniesByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where url does not contain DEFAULT_URL
        defaultCompaniesShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the companiesList where url does not contain UPDATED_URL
        defaultCompaniesShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllCompaniesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where name equals to DEFAULT_NAME
        defaultCompaniesShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the companiesList where name equals to UPDATED_NAME
        defaultCompaniesShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCompaniesShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the companiesList where name equals to UPDATED_NAME
        defaultCompaniesShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where name is not null
        defaultCompaniesShouldBeFound("name.specified=true");

        // Get all the companiesList where name is null
        defaultCompaniesShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByNameContainsSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where name contains DEFAULT_NAME
        defaultCompaniesShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the companiesList where name contains UPDATED_NAME
        defaultCompaniesShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where name does not contain DEFAULT_NAME
        defaultCompaniesShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the companiesList where name does not contain UPDATED_NAME
        defaultCompaniesShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByExpectedNoOfUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where expectedNoOfUsers equals to DEFAULT_EXPECTED_NO_OF_USERS
        defaultCompaniesShouldBeFound("expectedNoOfUsers.equals=" + DEFAULT_EXPECTED_NO_OF_USERS);

        // Get all the companiesList where expectedNoOfUsers equals to UPDATED_EXPECTED_NO_OF_USERS
        defaultCompaniesShouldNotBeFound("expectedNoOfUsers.equals=" + UPDATED_EXPECTED_NO_OF_USERS);
    }

    @Test
    @Transactional
    void getAllCompaniesByExpectedNoOfUsersIsInShouldWork() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where expectedNoOfUsers in DEFAULT_EXPECTED_NO_OF_USERS or UPDATED_EXPECTED_NO_OF_USERS
        defaultCompaniesShouldBeFound("expectedNoOfUsers.in=" + DEFAULT_EXPECTED_NO_OF_USERS + "," + UPDATED_EXPECTED_NO_OF_USERS);

        // Get all the companiesList where expectedNoOfUsers equals to UPDATED_EXPECTED_NO_OF_USERS
        defaultCompaniesShouldNotBeFound("expectedNoOfUsers.in=" + UPDATED_EXPECTED_NO_OF_USERS);
    }

    @Test
    @Transactional
    void getAllCompaniesByExpectedNoOfUsersIsNullOrNotNull() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where expectedNoOfUsers is not null
        defaultCompaniesShouldBeFound("expectedNoOfUsers.specified=true");

        // Get all the companiesList where expectedNoOfUsers is null
        defaultCompaniesShouldNotBeFound("expectedNoOfUsers.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByExpectedNoOfUsersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where expectedNoOfUsers is greater than or equal to DEFAULT_EXPECTED_NO_OF_USERS
        defaultCompaniesShouldBeFound("expectedNoOfUsers.greaterThanOrEqual=" + DEFAULT_EXPECTED_NO_OF_USERS);

        // Get all the companiesList where expectedNoOfUsers is greater than or equal to UPDATED_EXPECTED_NO_OF_USERS
        defaultCompaniesShouldNotBeFound("expectedNoOfUsers.greaterThanOrEqual=" + UPDATED_EXPECTED_NO_OF_USERS);
    }

    @Test
    @Transactional
    void getAllCompaniesByExpectedNoOfUsersIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where expectedNoOfUsers is less than or equal to DEFAULT_EXPECTED_NO_OF_USERS
        defaultCompaniesShouldBeFound("expectedNoOfUsers.lessThanOrEqual=" + DEFAULT_EXPECTED_NO_OF_USERS);

        // Get all the companiesList where expectedNoOfUsers is less than or equal to SMALLER_EXPECTED_NO_OF_USERS
        defaultCompaniesShouldNotBeFound("expectedNoOfUsers.lessThanOrEqual=" + SMALLER_EXPECTED_NO_OF_USERS);
    }

    @Test
    @Transactional
    void getAllCompaniesByExpectedNoOfUsersIsLessThanSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where expectedNoOfUsers is less than DEFAULT_EXPECTED_NO_OF_USERS
        defaultCompaniesShouldNotBeFound("expectedNoOfUsers.lessThan=" + DEFAULT_EXPECTED_NO_OF_USERS);

        // Get all the companiesList where expectedNoOfUsers is less than UPDATED_EXPECTED_NO_OF_USERS
        defaultCompaniesShouldBeFound("expectedNoOfUsers.lessThan=" + UPDATED_EXPECTED_NO_OF_USERS);
    }

    @Test
    @Transactional
    void getAllCompaniesByExpectedNoOfUsersIsGreaterThanSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where expectedNoOfUsers is greater than DEFAULT_EXPECTED_NO_OF_USERS
        defaultCompaniesShouldNotBeFound("expectedNoOfUsers.greaterThan=" + DEFAULT_EXPECTED_NO_OF_USERS);

        // Get all the companiesList where expectedNoOfUsers is greater than SMALLER_EXPECTED_NO_OF_USERS
        defaultCompaniesShouldBeFound("expectedNoOfUsers.greaterThan=" + SMALLER_EXPECTED_NO_OF_USERS);
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where createdBy equals to DEFAULT_CREATED_BY
        defaultCompaniesShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the companiesList where createdBy equals to UPDATED_CREATED_BY
        defaultCompaniesShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultCompaniesShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the companiesList where createdBy equals to UPDATED_CREATED_BY
        defaultCompaniesShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where createdBy is not null
        defaultCompaniesShouldBeFound("createdBy.specified=true");

        // Get all the companiesList where createdBy is null
        defaultCompaniesShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultCompaniesShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the companiesList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultCompaniesShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultCompaniesShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the companiesList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultCompaniesShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where createdBy is less than DEFAULT_CREATED_BY
        defaultCompaniesShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the companiesList where createdBy is less than UPDATED_CREATED_BY
        defaultCompaniesShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where createdBy is greater than DEFAULT_CREATED_BY
        defaultCompaniesShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the companiesList where createdBy is greater than SMALLER_CREATED_BY
        defaultCompaniesShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where createdAt equals to DEFAULT_CREATED_AT
        defaultCompaniesShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the companiesList where createdAt equals to UPDATED_CREATED_AT
        defaultCompaniesShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultCompaniesShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the companiesList where createdAt equals to UPDATED_CREATED_AT
        defaultCompaniesShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where createdAt is not null
        defaultCompaniesShouldBeFound("createdAt.specified=true");

        // Get all the companiesList where createdAt is null
        defaultCompaniesShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where updatedBy equals to DEFAULT_UPDATED_BY
        defaultCompaniesShouldBeFound("updatedBy.equals=" + DEFAULT_UPDATED_BY);

        // Get all the companiesList where updatedBy equals to UPDATED_UPDATED_BY
        defaultCompaniesShouldNotBeFound("updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllCompaniesByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where updatedBy in DEFAULT_UPDATED_BY or UPDATED_UPDATED_BY
        defaultCompaniesShouldBeFound("updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY);

        // Get all the companiesList where updatedBy equals to UPDATED_UPDATED_BY
        defaultCompaniesShouldNotBeFound("updatedBy.in=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllCompaniesByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where updatedBy is not null
        defaultCompaniesShouldBeFound("updatedBy.specified=true");

        // Get all the companiesList where updatedBy is null
        defaultCompaniesShouldNotBeFound("updatedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByUpdatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where updatedBy is greater than or equal to DEFAULT_UPDATED_BY
        defaultCompaniesShouldBeFound("updatedBy.greaterThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the companiesList where updatedBy is greater than or equal to UPDATED_UPDATED_BY
        defaultCompaniesShouldNotBeFound("updatedBy.greaterThanOrEqual=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllCompaniesByUpdatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where updatedBy is less than or equal to DEFAULT_UPDATED_BY
        defaultCompaniesShouldBeFound("updatedBy.lessThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the companiesList where updatedBy is less than or equal to SMALLER_UPDATED_BY
        defaultCompaniesShouldNotBeFound("updatedBy.lessThanOrEqual=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllCompaniesByUpdatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where updatedBy is less than DEFAULT_UPDATED_BY
        defaultCompaniesShouldNotBeFound("updatedBy.lessThan=" + DEFAULT_UPDATED_BY);

        // Get all the companiesList where updatedBy is less than UPDATED_UPDATED_BY
        defaultCompaniesShouldBeFound("updatedBy.lessThan=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllCompaniesByUpdatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where updatedBy is greater than DEFAULT_UPDATED_BY
        defaultCompaniesShouldNotBeFound("updatedBy.greaterThan=" + DEFAULT_UPDATED_BY);

        // Get all the companiesList where updatedBy is greater than SMALLER_UPDATED_BY
        defaultCompaniesShouldBeFound("updatedBy.greaterThan=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllCompaniesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultCompaniesShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the companiesList where updatedAt equals to UPDATED_UPDATED_AT
        defaultCompaniesShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCompaniesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultCompaniesShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the companiesList where updatedAt equals to UPDATED_UPDATED_AT
        defaultCompaniesShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCompaniesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList where updatedAt is not null
        defaultCompaniesShouldBeFound("updatedAt.specified=true");

        // Get all the companiesList where updatedAt is null
        defaultCompaniesShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByProjectsCompanyIsEqualToSomething() throws Exception {
        Projects projectsCompany;
        if (TestUtil.findAll(em, Projects.class).isEmpty()) {
            companiesRepository.saveAndFlush(companies);
            projectsCompany = ProjectsResourceIT.createEntity(em);
        } else {
            projectsCompany = TestUtil.findAll(em, Projects.class).get(0);
        }
        em.persist(projectsCompany);
        em.flush();
        companies.addProjectsCompany(projectsCompany);
        companiesRepository.saveAndFlush(companies);
        Long projectsCompanyId = projectsCompany.getId();

        // Get all the companiesList where projectsCompany equals to projectsCompanyId
        defaultCompaniesShouldBeFound("projectsCompanyId.equals=" + projectsCompanyId);

        // Get all the companiesList where projectsCompany equals to (projectsCompanyId + 1)
        defaultCompaniesShouldNotBeFound("projectsCompanyId.equals=" + (projectsCompanyId + 1));
    }

    @Test
    @Transactional
    void getAllCompaniesByTemplatefieldsCompanyIsEqualToSomething() throws Exception {
        TemplateFields templatefieldsCompany;
        if (TestUtil.findAll(em, TemplateFields.class).isEmpty()) {
            companiesRepository.saveAndFlush(companies);
            templatefieldsCompany = TemplateFieldsResourceIT.createEntity(em);
        } else {
            templatefieldsCompany = TestUtil.findAll(em, TemplateFields.class).get(0);
        }
        em.persist(templatefieldsCompany);
        em.flush();
        companies.addTemplatefieldsCompany(templatefieldsCompany);
        companiesRepository.saveAndFlush(companies);
        Long templatefieldsCompanyId = templatefieldsCompany.getId();

        // Get all the companiesList where templatefieldsCompany equals to templatefieldsCompanyId
        defaultCompaniesShouldBeFound("templatefieldsCompanyId.equals=" + templatefieldsCompanyId);

        // Get all the companiesList where templatefieldsCompany equals to (templatefieldsCompanyId + 1)
        defaultCompaniesShouldNotBeFound("templatefieldsCompanyId.equals=" + (templatefieldsCompanyId + 1));
    }

    @Test
    @Transactional
    void getAllCompaniesByTemplatesCompanyIsEqualToSomething() throws Exception {
        Templates templatesCompany;
        if (TestUtil.findAll(em, Templates.class).isEmpty()) {
            companiesRepository.saveAndFlush(companies);
            templatesCompany = TemplatesResourceIT.createEntity(em);
        } else {
            templatesCompany = TestUtil.findAll(em, Templates.class).get(0);
        }
        em.persist(templatesCompany);
        em.flush();
        companies.addTemplatesCompany(templatesCompany);
        companiesRepository.saveAndFlush(companies);
        Long templatesCompanyId = templatesCompany.getId();

        // Get all the companiesList where templatesCompany equals to templatesCompanyId
        defaultCompaniesShouldBeFound("templatesCompanyId.equals=" + templatesCompanyId);

        // Get all the companiesList where templatesCompany equals to (templatesCompanyId + 1)
        defaultCompaniesShouldNotBeFound("templatesCompanyId.equals=" + (templatesCompanyId + 1));
    }

    @Test
    @Transactional
    void getAllCompaniesByUsersCompanyIsEqualToSomething() throws Exception {
        Users usersCompany;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            companiesRepository.saveAndFlush(companies);
            usersCompany = UsersResourceIT.createEntity(em);
        } else {
            usersCompany = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(usersCompany);
        em.flush();
        companies.addUsersCompany(usersCompany);
        companiesRepository.saveAndFlush(companies);
        Long usersCompanyId = usersCompany.getId();

        // Get all the companiesList where usersCompany equals to usersCompanyId
        defaultCompaniesShouldBeFound("usersCompanyId.equals=" + usersCompanyId);

        // Get all the companiesList where usersCompany equals to (usersCompanyId + 1)
        defaultCompaniesShouldNotBeFound("usersCompanyId.equals=" + (usersCompanyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompaniesShouldBeFound(String filter) throws Exception {
        restCompaniesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companies.getId().intValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].expectedNoOfUsers").value(hasItem(DEFAULT_EXPECTED_NO_OF_USERS)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restCompaniesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompaniesShouldNotBeFound(String filter) throws Exception {
        restCompaniesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompaniesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCompanies() throws Exception {
        // Get the companies
        restCompaniesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompanies() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        int databaseSizeBeforeUpdate = companiesRepository.findAll().size();

        // Update the companies
        Companies updatedCompanies = companiesRepository.findById(companies.getId()).get();
        // Disconnect from session so that the updates on updatedCompanies are not directly saved in db
        em.detach(updatedCompanies);
        updatedCompanies
            .country(UPDATED_COUNTRY)
            .url(UPDATED_URL)
            .name(UPDATED_NAME)
            .expectedNoOfUsers(UPDATED_EXPECTED_NO_OF_USERS)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restCompaniesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompanies.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompanies))
            )
            .andExpect(status().isOk());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeUpdate);
        Companies testCompanies = companiesList.get(companiesList.size() - 1);
        assertThat(testCompanies.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCompanies.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testCompanies.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompanies.getExpectedNoOfUsers()).isEqualTo(UPDATED_EXPECTED_NO_OF_USERS);
        assertThat(testCompanies.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCompanies.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCompanies.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testCompanies.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingCompanies() throws Exception {
        int databaseSizeBeforeUpdate = companiesRepository.findAll().size();
        companies.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompaniesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companies.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companies))
            )
            .andExpect(status().isBadRequest());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompanies() throws Exception {
        int databaseSizeBeforeUpdate = companiesRepository.findAll().size();
        companies.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompaniesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companies))
            )
            .andExpect(status().isBadRequest());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompanies() throws Exception {
        int databaseSizeBeforeUpdate = companiesRepository.findAll().size();
        companies.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompaniesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companies)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompaniesWithPatch() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        int databaseSizeBeforeUpdate = companiesRepository.findAll().size();

        // Update the companies using partial update
        Companies partialUpdatedCompanies = new Companies();
        partialUpdatedCompanies.setId(companies.getId());

        partialUpdatedCompanies.country(UPDATED_COUNTRY).createdAt(UPDATED_CREATED_AT).updatedBy(UPDATED_UPDATED_BY);

        restCompaniesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanies.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanies))
            )
            .andExpect(status().isOk());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeUpdate);
        Companies testCompanies = companiesList.get(companiesList.size() - 1);
        assertThat(testCompanies.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCompanies.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testCompanies.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompanies.getExpectedNoOfUsers()).isEqualTo(DEFAULT_EXPECTED_NO_OF_USERS);
        assertThat(testCompanies.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCompanies.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCompanies.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testCompanies.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateCompaniesWithPatch() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        int databaseSizeBeforeUpdate = companiesRepository.findAll().size();

        // Update the companies using partial update
        Companies partialUpdatedCompanies = new Companies();
        partialUpdatedCompanies.setId(companies.getId());

        partialUpdatedCompanies
            .country(UPDATED_COUNTRY)
            .url(UPDATED_URL)
            .name(UPDATED_NAME)
            .expectedNoOfUsers(UPDATED_EXPECTED_NO_OF_USERS)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restCompaniesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanies.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanies))
            )
            .andExpect(status().isOk());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeUpdate);
        Companies testCompanies = companiesList.get(companiesList.size() - 1);
        assertThat(testCompanies.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCompanies.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testCompanies.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompanies.getExpectedNoOfUsers()).isEqualTo(UPDATED_EXPECTED_NO_OF_USERS);
        assertThat(testCompanies.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCompanies.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCompanies.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testCompanies.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingCompanies() throws Exception {
        int databaseSizeBeforeUpdate = companiesRepository.findAll().size();
        companies.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompaniesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, companies.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companies))
            )
            .andExpect(status().isBadRequest());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompanies() throws Exception {
        int databaseSizeBeforeUpdate = companiesRepository.findAll().size();
        companies.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompaniesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companies))
            )
            .andExpect(status().isBadRequest());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompanies() throws Exception {
        int databaseSizeBeforeUpdate = companiesRepository.findAll().size();
        companies.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompaniesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(companies))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompanies() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        int databaseSizeBeforeDelete = companiesRepository.findAll().size();

        // Delete the companies
        restCompaniesMockMvc
            .perform(delete(ENTITY_API_URL_ID, companies.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
