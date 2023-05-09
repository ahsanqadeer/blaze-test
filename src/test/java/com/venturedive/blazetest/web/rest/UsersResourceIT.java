package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.Companies;
import com.venturedive.blazetest.domain.Projects;
import com.venturedive.blazetest.domain.Roles;
import com.venturedive.blazetest.domain.Users;
import com.venturedive.blazetest.repository.UsersRepository;
import com.venturedive.blazetest.service.UsersService;
import com.venturedive.blazetest.service.criteria.UsersCriteria;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link UsersResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UsersResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_ACTIVE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_ACTIVE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

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

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final Boolean DEFAULT_EMAIL_VERIFIED = false;
    private static final Boolean UPDATED_EMAIL_VERIFIED = true;

    private static final String DEFAULT_PROVIDER = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_VERIFICATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_VERIFICATION_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_FORGOT_PASSWORD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_FORGOT_PASSWORD_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsersRepository usersRepository;

    @Mock
    private UsersRepository usersRepositoryMock;

    @Mock
    private UsersService usersServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsersMockMvc;

    private Users users;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Users createEntity(EntityManager em) {
        Users users = new Users()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .password(DEFAULT_PASSWORD)
            .lastActive(DEFAULT_LAST_ACTIVE)
            .status(DEFAULT_STATUS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT)
            .email(DEFAULT_EMAIL)
            .isDeleted(DEFAULT_IS_DELETED)
            .emailVerified(DEFAULT_EMAIL_VERIFIED)
            .provider(DEFAULT_PROVIDER)
            .emailVerificationToken(DEFAULT_EMAIL_VERIFICATION_TOKEN)
            .forgotPasswordToken(DEFAULT_FORGOT_PASSWORD_TOKEN);
        return users;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Users createUpdatedEntity(EntityManager em) {
        Users users = new Users()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .password(UPDATED_PASSWORD)
            .lastActive(UPDATED_LAST_ACTIVE)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .email(UPDATED_EMAIL)
            .isDeleted(UPDATED_IS_DELETED)
            .emailVerified(UPDATED_EMAIL_VERIFIED)
            .provider(UPDATED_PROVIDER)
            .emailVerificationToken(UPDATED_EMAIL_VERIFICATION_TOKEN)
            .forgotPasswordToken(UPDATED_FORGOT_PASSWORD_TOKEN);
        return users;
    }

    @BeforeEach
    public void initTest() {
        users = createEntity(em);
    }

    @Test
    @Transactional
    void createUsers() throws Exception {
        int databaseSizeBeforeCreate = usersRepository.findAll().size();
        // Create the Users
        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(users)))
            .andExpect(status().isCreated());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeCreate + 1);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUsers.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUsers.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUsers.getLastActive()).isEqualTo(DEFAULT_LAST_ACTIVE);
        assertThat(testUsers.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUsers.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUsers.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUsers.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testUsers.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testUsers.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUsers.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testUsers.getEmailVerified()).isEqualTo(DEFAULT_EMAIL_VERIFIED);
        assertThat(testUsers.getProvider()).isEqualTo(DEFAULT_PROVIDER);
        assertThat(testUsers.getEmailVerificationToken()).isEqualTo(DEFAULT_EMAIL_VERIFICATION_TOKEN);
        assertThat(testUsers.getForgotPasswordToken()).isEqualTo(DEFAULT_FORGOT_PASSWORD_TOKEN);
    }

    @Test
    @Transactional
    void createUsersWithExistingId() throws Exception {
        // Create the Users with an existing ID
        users.setId(1L);

        int databaseSizeBeforeCreate = usersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(users)))
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setIsDeleted(null);

        // Create the Users, which fails.

        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(users)))
            .andExpect(status().isBadRequest());

        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailVerifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setEmailVerified(null);

        // Create the Users, which fails.

        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(users)))
            .andExpect(status().isBadRequest());

        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProviderIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setProvider(null);

        // Create the Users, which fails.

        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(users)))
            .andExpect(status().isBadRequest());

        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(users.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].lastActive").value(hasItem(DEFAULT_LAST_ACTIVE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].emailVerified").value(hasItem(DEFAULT_EMAIL_VERIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER)))
            .andExpect(jsonPath("$.[*].emailVerificationToken").value(hasItem(DEFAULT_EMAIL_VERIFICATION_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].forgotPasswordToken").value(hasItem(DEFAULT_FORGOT_PASSWORD_TOKEN.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(usersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUsersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(usersServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(usersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUsersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(usersRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get the users
        restUsersMockMvc
            .perform(get(ENTITY_API_URL_ID, users.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(users.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.lastActive").value(DEFAULT_LAST_ACTIVE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.emailVerified").value(DEFAULT_EMAIL_VERIFIED.booleanValue()))
            .andExpect(jsonPath("$.provider").value(DEFAULT_PROVIDER))
            .andExpect(jsonPath("$.emailVerificationToken").value(DEFAULT_EMAIL_VERIFICATION_TOKEN.toString()))
            .andExpect(jsonPath("$.forgotPasswordToken").value(DEFAULT_FORGOT_PASSWORD_TOKEN.toString()));
    }

    @Test
    @Transactional
    void getUsersByIdFiltering() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        Long id = users.getId();

        defaultUsersShouldBeFound("id.equals=" + id);
        defaultUsersShouldNotBeFound("id.notEquals=" + id);

        defaultUsersShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUsersShouldNotBeFound("id.greaterThan=" + id);

        defaultUsersShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUsersShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where firstName equals to DEFAULT_FIRST_NAME
        defaultUsersShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the usersList where firstName equals to UPDATED_FIRST_NAME
        defaultUsersShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultUsersShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the usersList where firstName equals to UPDATED_FIRST_NAME
        defaultUsersShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where firstName is not null
        defaultUsersShouldBeFound("firstName.specified=true");

        // Get all the usersList where firstName is null
        defaultUsersShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where firstName contains DEFAULT_FIRST_NAME
        defaultUsersShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the usersList where firstName contains UPDATED_FIRST_NAME
        defaultUsersShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where firstName does not contain DEFAULT_FIRST_NAME
        defaultUsersShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the usersList where firstName does not contain UPDATED_FIRST_NAME
        defaultUsersShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastName equals to DEFAULT_LAST_NAME
        defaultUsersShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the usersList where lastName equals to UPDATED_LAST_NAME
        defaultUsersShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultUsersShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the usersList where lastName equals to UPDATED_LAST_NAME
        defaultUsersShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastName is not null
        defaultUsersShouldBeFound("lastName.specified=true");

        // Get all the usersList where lastName is null
        defaultUsersShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastName contains DEFAULT_LAST_NAME
        defaultUsersShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the usersList where lastName contains UPDATED_LAST_NAME
        defaultUsersShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastName does not contain DEFAULT_LAST_NAME
        defaultUsersShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the usersList where lastName does not contain UPDATED_LAST_NAME
        defaultUsersShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password equals to DEFAULT_PASSWORD
        defaultUsersShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the usersList where password equals to UPDATED_PASSWORD
        defaultUsersShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultUsersShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the usersList where password equals to UPDATED_PASSWORD
        defaultUsersShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password is not null
        defaultUsersShouldBeFound("password.specified=true");

        // Get all the usersList where password is null
        defaultUsersShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByPasswordContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password contains DEFAULT_PASSWORD
        defaultUsersShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the usersList where password contains UPDATED_PASSWORD
        defaultUsersShouldNotBeFound("password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password does not contain DEFAULT_PASSWORD
        defaultUsersShouldNotBeFound("password.doesNotContain=" + DEFAULT_PASSWORD);

        // Get all the usersList where password does not contain UPDATED_PASSWORD
        defaultUsersShouldBeFound("password.doesNotContain=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByLastActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastActive equals to DEFAULT_LAST_ACTIVE
        defaultUsersShouldBeFound("lastActive.equals=" + DEFAULT_LAST_ACTIVE);

        // Get all the usersList where lastActive equals to UPDATED_LAST_ACTIVE
        defaultUsersShouldNotBeFound("lastActive.equals=" + UPDATED_LAST_ACTIVE);
    }

    @Test
    @Transactional
    void getAllUsersByLastActiveIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastActive in DEFAULT_LAST_ACTIVE or UPDATED_LAST_ACTIVE
        defaultUsersShouldBeFound("lastActive.in=" + DEFAULT_LAST_ACTIVE + "," + UPDATED_LAST_ACTIVE);

        // Get all the usersList where lastActive equals to UPDATED_LAST_ACTIVE
        defaultUsersShouldNotBeFound("lastActive.in=" + UPDATED_LAST_ACTIVE);
    }

    @Test
    @Transactional
    void getAllUsersByLastActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where lastActive is not null
        defaultUsersShouldBeFound("lastActive.specified=true");

        // Get all the usersList where lastActive is null
        defaultUsersShouldNotBeFound("lastActive.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where status equals to DEFAULT_STATUS
        defaultUsersShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the usersList where status equals to UPDATED_STATUS
        defaultUsersShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUsersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultUsersShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the usersList where status equals to UPDATED_STATUS
        defaultUsersShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUsersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where status is not null
        defaultUsersShouldBeFound("status.specified=true");

        // Get all the usersList where status is null
        defaultUsersShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByStatusContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where status contains DEFAULT_STATUS
        defaultUsersShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the usersList where status contains UPDATED_STATUS
        defaultUsersShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUsersByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where status does not contain DEFAULT_STATUS
        defaultUsersShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the usersList where status does not contain UPDATED_STATUS
        defaultUsersShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where createdBy equals to DEFAULT_CREATED_BY
        defaultUsersShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the usersList where createdBy equals to UPDATED_CREATED_BY
        defaultUsersShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultUsersShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the usersList where createdBy equals to UPDATED_CREATED_BY
        defaultUsersShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where createdBy is not null
        defaultUsersShouldBeFound("createdBy.specified=true");

        // Get all the usersList where createdBy is null
        defaultUsersShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByCreatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where createdBy is greater than or equal to DEFAULT_CREATED_BY
        defaultUsersShouldBeFound("createdBy.greaterThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the usersList where createdBy is greater than or equal to UPDATED_CREATED_BY
        defaultUsersShouldNotBeFound("createdBy.greaterThanOrEqual=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where createdBy is less than or equal to DEFAULT_CREATED_BY
        defaultUsersShouldBeFound("createdBy.lessThanOrEqual=" + DEFAULT_CREATED_BY);

        // Get all the usersList where createdBy is less than or equal to SMALLER_CREATED_BY
        defaultUsersShouldNotBeFound("createdBy.lessThanOrEqual=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where createdBy is less than DEFAULT_CREATED_BY
        defaultUsersShouldNotBeFound("createdBy.lessThan=" + DEFAULT_CREATED_BY);

        // Get all the usersList where createdBy is less than UPDATED_CREATED_BY
        defaultUsersShouldBeFound("createdBy.lessThan=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where createdBy is greater than DEFAULT_CREATED_BY
        defaultUsersShouldNotBeFound("createdBy.greaterThan=" + DEFAULT_CREATED_BY);

        // Get all the usersList where createdBy is greater than SMALLER_CREATED_BY
        defaultUsersShouldBeFound("createdBy.greaterThan=" + SMALLER_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where createdAt equals to DEFAULT_CREATED_AT
        defaultUsersShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the usersList where createdAt equals to UPDATED_CREATED_AT
        defaultUsersShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultUsersShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the usersList where createdAt equals to UPDATED_CREATED_AT
        defaultUsersShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where createdAt is not null
        defaultUsersShouldBeFound("createdAt.specified=true");

        // Get all the usersList where createdAt is null
        defaultUsersShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where updatedBy equals to DEFAULT_UPDATED_BY
        defaultUsersShouldBeFound("updatedBy.equals=" + DEFAULT_UPDATED_BY);

        // Get all the usersList where updatedBy equals to UPDATED_UPDATED_BY
        defaultUsersShouldNotBeFound("updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where updatedBy in DEFAULT_UPDATED_BY or UPDATED_UPDATED_BY
        defaultUsersShouldBeFound("updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY);

        // Get all the usersList where updatedBy equals to UPDATED_UPDATED_BY
        defaultUsersShouldNotBeFound("updatedBy.in=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where updatedBy is not null
        defaultUsersShouldBeFound("updatedBy.specified=true");

        // Get all the usersList where updatedBy is null
        defaultUsersShouldNotBeFound("updatedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByUpdatedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where updatedBy is greater than or equal to DEFAULT_UPDATED_BY
        defaultUsersShouldBeFound("updatedBy.greaterThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the usersList where updatedBy is greater than or equal to UPDATED_UPDATED_BY
        defaultUsersShouldNotBeFound("updatedBy.greaterThanOrEqual=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByUpdatedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where updatedBy is less than or equal to DEFAULT_UPDATED_BY
        defaultUsersShouldBeFound("updatedBy.lessThanOrEqual=" + DEFAULT_UPDATED_BY);

        // Get all the usersList where updatedBy is less than or equal to SMALLER_UPDATED_BY
        defaultUsersShouldNotBeFound("updatedBy.lessThanOrEqual=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByUpdatedByIsLessThanSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where updatedBy is less than DEFAULT_UPDATED_BY
        defaultUsersShouldNotBeFound("updatedBy.lessThan=" + DEFAULT_UPDATED_BY);

        // Get all the usersList where updatedBy is less than UPDATED_UPDATED_BY
        defaultUsersShouldBeFound("updatedBy.lessThan=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByUpdatedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where updatedBy is greater than DEFAULT_UPDATED_BY
        defaultUsersShouldNotBeFound("updatedBy.greaterThan=" + DEFAULT_UPDATED_BY);

        // Get all the usersList where updatedBy is greater than SMALLER_UPDATED_BY
        defaultUsersShouldBeFound("updatedBy.greaterThan=" + SMALLER_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllUsersByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultUsersShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the usersList where updatedAt equals to UPDATED_UPDATED_AT
        defaultUsersShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUsersByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultUsersShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the usersList where updatedAt equals to UPDATED_UPDATED_AT
        defaultUsersShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUsersByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where updatedAt is not null
        defaultUsersShouldBeFound("updatedAt.specified=true");

        // Get all the usersList where updatedAt is null
        defaultUsersShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where email equals to DEFAULT_EMAIL
        defaultUsersShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the usersList where email equals to UPDATED_EMAIL
        defaultUsersShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultUsersShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the usersList where email equals to UPDATED_EMAIL
        defaultUsersShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where email is not null
        defaultUsersShouldBeFound("email.specified=true");

        // Get all the usersList where email is null
        defaultUsersShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where email contains DEFAULT_EMAIL
        defaultUsersShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the usersList where email contains UPDATED_EMAIL
        defaultUsersShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where email does not contain DEFAULT_EMAIL
        defaultUsersShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the usersList where email does not contain UPDATED_EMAIL
        defaultUsersShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where isDeleted equals to DEFAULT_IS_DELETED
        defaultUsersShouldBeFound("isDeleted.equals=" + DEFAULT_IS_DELETED);

        // Get all the usersList where isDeleted equals to UPDATED_IS_DELETED
        defaultUsersShouldNotBeFound("isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllUsersByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where isDeleted in DEFAULT_IS_DELETED or UPDATED_IS_DELETED
        defaultUsersShouldBeFound("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED);

        // Get all the usersList where isDeleted equals to UPDATED_IS_DELETED
        defaultUsersShouldNotBeFound("isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllUsersByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where isDeleted is not null
        defaultUsersShouldBeFound("isDeleted.specified=true");

        // Get all the usersList where isDeleted is null
        defaultUsersShouldNotBeFound("isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByEmailVerifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where emailVerified equals to DEFAULT_EMAIL_VERIFIED
        defaultUsersShouldBeFound("emailVerified.equals=" + DEFAULT_EMAIL_VERIFIED);

        // Get all the usersList where emailVerified equals to UPDATED_EMAIL_VERIFIED
        defaultUsersShouldNotBeFound("emailVerified.equals=" + UPDATED_EMAIL_VERIFIED);
    }

    @Test
    @Transactional
    void getAllUsersByEmailVerifiedIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where emailVerified in DEFAULT_EMAIL_VERIFIED or UPDATED_EMAIL_VERIFIED
        defaultUsersShouldBeFound("emailVerified.in=" + DEFAULT_EMAIL_VERIFIED + "," + UPDATED_EMAIL_VERIFIED);

        // Get all the usersList where emailVerified equals to UPDATED_EMAIL_VERIFIED
        defaultUsersShouldNotBeFound("emailVerified.in=" + UPDATED_EMAIL_VERIFIED);
    }

    @Test
    @Transactional
    void getAllUsersByEmailVerifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where emailVerified is not null
        defaultUsersShouldBeFound("emailVerified.specified=true");

        // Get all the usersList where emailVerified is null
        defaultUsersShouldNotBeFound("emailVerified.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where provider equals to DEFAULT_PROVIDER
        defaultUsersShouldBeFound("provider.equals=" + DEFAULT_PROVIDER);

        // Get all the usersList where provider equals to UPDATED_PROVIDER
        defaultUsersShouldNotBeFound("provider.equals=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllUsersByProviderIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where provider in DEFAULT_PROVIDER or UPDATED_PROVIDER
        defaultUsersShouldBeFound("provider.in=" + DEFAULT_PROVIDER + "," + UPDATED_PROVIDER);

        // Get all the usersList where provider equals to UPDATED_PROVIDER
        defaultUsersShouldNotBeFound("provider.in=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllUsersByProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where provider is not null
        defaultUsersShouldBeFound("provider.specified=true");

        // Get all the usersList where provider is null
        defaultUsersShouldNotBeFound("provider.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByProviderContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where provider contains DEFAULT_PROVIDER
        defaultUsersShouldBeFound("provider.contains=" + DEFAULT_PROVIDER);

        // Get all the usersList where provider contains UPDATED_PROVIDER
        defaultUsersShouldNotBeFound("provider.contains=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllUsersByProviderNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where provider does not contain DEFAULT_PROVIDER
        defaultUsersShouldNotBeFound("provider.doesNotContain=" + DEFAULT_PROVIDER);

        // Get all the usersList where provider does not contain UPDATED_PROVIDER
        defaultUsersShouldBeFound("provider.doesNotContain=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllUsersByCompanyIsEqualToSomething() throws Exception {
        Companies company;
        if (TestUtil.findAll(em, Companies.class).isEmpty()) {
            usersRepository.saveAndFlush(users);
            company = CompaniesResourceIT.createEntity(em);
        } else {
            company = TestUtil.findAll(em, Companies.class).get(0);
        }
        em.persist(company);
        em.flush();
        users.setCompany(company);
        usersRepository.saveAndFlush(users);
        Long companyId = company.getId();

        // Get all the usersList where company equals to companyId
        defaultUsersShouldBeFound("companyId.equals=" + companyId);

        // Get all the usersList where company equals to (companyId + 1)
        defaultUsersShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    @Test
    @Transactional
    void getAllUsersByProjectIsEqualToSomething() throws Exception {
        Projects project;
        if (TestUtil.findAll(em, Projects.class).isEmpty()) {
            usersRepository.saveAndFlush(users);
            project = ProjectsResourceIT.createEntity(em);
        } else {
            project = TestUtil.findAll(em, Projects.class).get(0);
        }
        em.persist(project);
        em.flush();
        users.addProject(project);
        usersRepository.saveAndFlush(users);
        Long projectId = project.getId();

        // Get all the usersList where project equals to projectId
        defaultUsersShouldBeFound("projectId.equals=" + projectId);

        // Get all the usersList where project equals to (projectId + 1)
        defaultUsersShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    @Test
    @Transactional
    void getAllUsersByRoleIsEqualToSomething() throws Exception {
        Roles role;
        if (TestUtil.findAll(em, Roles.class).isEmpty()) {
            usersRepository.saveAndFlush(users);
            role = RolesResourceIT.createEntity(em);
        } else {
            role = TestUtil.findAll(em, Roles.class).get(0);
        }
        em.persist(role);
        em.flush();
        users.addRole(role);
        usersRepository.saveAndFlush(users);
        Long roleId = role.getId();

        // Get all the usersList where role equals to roleId
        defaultUsersShouldBeFound("roleId.equals=" + roleId);

        // Get all the usersList where role equals to (roleId + 1)
        defaultUsersShouldNotBeFound("roleId.equals=" + (roleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsersShouldBeFound(String filter) throws Exception {
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(users.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].lastActive").value(hasItem(DEFAULT_LAST_ACTIVE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].emailVerified").value(hasItem(DEFAULT_EMAIL_VERIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER)))
            .andExpect(jsonPath("$.[*].emailVerificationToken").value(hasItem(DEFAULT_EMAIL_VERIFICATION_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].forgotPasswordToken").value(hasItem(DEFAULT_FORGOT_PASSWORD_TOKEN.toString())));

        // Check, that the count call also returns 1
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsersShouldNotBeFound(String filter) throws Exception {
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsers() throws Exception {
        // Get the users
        restUsersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeUpdate = usersRepository.findAll().size();

        // Update the users
        Users updatedUsers = usersRepository.findById(users.getId()).get();
        // Disconnect from session so that the updates on updatedUsers are not directly saved in db
        em.detach(updatedUsers);
        updatedUsers
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .password(UPDATED_PASSWORD)
            .lastActive(UPDATED_LAST_ACTIVE)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .email(UPDATED_EMAIL)
            .isDeleted(UPDATED_IS_DELETED)
            .emailVerified(UPDATED_EMAIL_VERIFIED)
            .provider(UPDATED_PROVIDER)
            .emailVerificationToken(UPDATED_EMAIL_VERIFICATION_TOKEN)
            .forgotPasswordToken(UPDATED_FORGOT_PASSWORD_TOKEN);

        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUsers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUsers))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUsers.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUsers.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUsers.getLastActive()).isEqualTo(UPDATED_LAST_ACTIVE);
        assertThat(testUsers.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUsers.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUsers.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUsers.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testUsers.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testUsers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsers.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testUsers.getEmailVerified()).isEqualTo(UPDATED_EMAIL_VERIFIED);
        assertThat(testUsers.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testUsers.getEmailVerificationToken()).isEqualTo(UPDATED_EMAIL_VERIFICATION_TOKEN);
        assertThat(testUsers.getForgotPasswordToken()).isEqualTo(UPDATED_FORGOT_PASSWORD_TOKEN);
    }

    @Test
    @Transactional
    void putNonExistingUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, users.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(users))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(users))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(users)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsersWithPatch() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeUpdate = usersRepository.findAll().size();

        // Update the users using partial update
        Users partialUpdatedUsers = new Users();
        partialUpdatedUsers.setId(users.getId());

        partialUpdatedUsers
            .lastName(UPDATED_LAST_NAME)
            .lastActive(UPDATED_LAST_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .email(UPDATED_EMAIL)
            .emailVerificationToken(UPDATED_EMAIL_VERIFICATION_TOKEN)
            .forgotPasswordToken(UPDATED_FORGOT_PASSWORD_TOKEN);

        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsers))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUsers.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUsers.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUsers.getLastActive()).isEqualTo(UPDATED_LAST_ACTIVE);
        assertThat(testUsers.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUsers.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUsers.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUsers.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testUsers.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testUsers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsers.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testUsers.getEmailVerified()).isEqualTo(DEFAULT_EMAIL_VERIFIED);
        assertThat(testUsers.getProvider()).isEqualTo(DEFAULT_PROVIDER);
        assertThat(testUsers.getEmailVerificationToken()).isEqualTo(UPDATED_EMAIL_VERIFICATION_TOKEN);
        assertThat(testUsers.getForgotPasswordToken()).isEqualTo(UPDATED_FORGOT_PASSWORD_TOKEN);
    }

    @Test
    @Transactional
    void fullUpdateUsersWithPatch() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeUpdate = usersRepository.findAll().size();

        // Update the users using partial update
        Users partialUpdatedUsers = new Users();
        partialUpdatedUsers.setId(users.getId());

        partialUpdatedUsers
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .password(UPDATED_PASSWORD)
            .lastActive(UPDATED_LAST_ACTIVE)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT)
            .email(UPDATED_EMAIL)
            .isDeleted(UPDATED_IS_DELETED)
            .emailVerified(UPDATED_EMAIL_VERIFIED)
            .provider(UPDATED_PROVIDER)
            .emailVerificationToken(UPDATED_EMAIL_VERIFICATION_TOKEN)
            .forgotPasswordToken(UPDATED_FORGOT_PASSWORD_TOKEN);

        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsers))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUsers.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUsers.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUsers.getLastActive()).isEqualTo(UPDATED_LAST_ACTIVE);
        assertThat(testUsers.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUsers.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUsers.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUsers.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testUsers.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testUsers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsers.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testUsers.getEmailVerified()).isEqualTo(UPDATED_EMAIL_VERIFIED);
        assertThat(testUsers.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testUsers.getEmailVerificationToken()).isEqualTo(UPDATED_EMAIL_VERIFICATION_TOKEN);
        assertThat(testUsers.getForgotPasswordToken()).isEqualTo(UPDATED_FORGOT_PASSWORD_TOKEN);
    }

    @Test
    @Transactional
    void patchNonExistingUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, users.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(users))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(users))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(users)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeDelete = usersRepository.findAll().size();

        // Delete the users
        restUsersMockMvc
            .perform(delete(ENTITY_API_URL_ID, users.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
