package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.Permissions;
import com.venturedive.blazetest.domain.Roles;
import com.venturedive.blazetest.repository.PermissionsRepository;
import com.venturedive.blazetest.service.criteria.PermissionsCriteria;
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
 * Integration tests for the {@link PermissionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PermissionsResourceIT {

    private static final String DEFAULT_PERMISSION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PERMISSION_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/permissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PermissionsRepository permissionsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPermissionsMockMvc;

    private Permissions permissions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Permissions createEntity(EntityManager em) {
        Permissions permissions = new Permissions().permissionName(DEFAULT_PERMISSION_NAME);
        return permissions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Permissions createUpdatedEntity(EntityManager em) {
        Permissions permissions = new Permissions().permissionName(UPDATED_PERMISSION_NAME);
        return permissions;
    }

    @BeforeEach
    public void initTest() {
        permissions = createEntity(em);
    }

    @Test
    @Transactional
    void createPermissions() throws Exception {
        int databaseSizeBeforeCreate = permissionsRepository.findAll().size();
        // Create the Permissions
        restPermissionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissions)))
            .andExpect(status().isCreated());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeCreate + 1);
        Permissions testPermissions = permissionsList.get(permissionsList.size() - 1);
        assertThat(testPermissions.getPermissionName()).isEqualTo(DEFAULT_PERMISSION_NAME);
    }

    @Test
    @Transactional
    void createPermissionsWithExistingId() throws Exception {
        // Create the Permissions with an existing ID
        permissions.setId(1L);

        int databaseSizeBeforeCreate = permissionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPermissionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissions)))
            .andExpect(status().isBadRequest());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPermissionNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = permissionsRepository.findAll().size();
        // set the field null
        permissions.setPermissionName(null);

        // Create the Permissions, which fails.

        restPermissionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissions)))
            .andExpect(status().isBadRequest());

        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPermissions() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList
        restPermissionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permissions.getId().intValue())))
            .andExpect(jsonPath("$.[*].permissionName").value(hasItem(DEFAULT_PERMISSION_NAME)));
    }

    @Test
    @Transactional
    void getPermissions() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get the permissions
        restPermissionsMockMvc
            .perform(get(ENTITY_API_URL_ID, permissions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(permissions.getId().intValue()))
            .andExpect(jsonPath("$.permissionName").value(DEFAULT_PERMISSION_NAME));
    }

    @Test
    @Transactional
    void getPermissionsByIdFiltering() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        Long id = permissions.getId();

        defaultPermissionsShouldBeFound("id.equals=" + id);
        defaultPermissionsShouldNotBeFound("id.notEquals=" + id);

        defaultPermissionsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPermissionsShouldNotBeFound("id.greaterThan=" + id);

        defaultPermissionsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPermissionsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPermissionsByPermissionNameIsEqualToSomething() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where permissionName equals to DEFAULT_PERMISSION_NAME
        defaultPermissionsShouldBeFound("permissionName.equals=" + DEFAULT_PERMISSION_NAME);

        // Get all the permissionsList where permissionName equals to UPDATED_PERMISSION_NAME
        defaultPermissionsShouldNotBeFound("permissionName.equals=" + UPDATED_PERMISSION_NAME);
    }

    @Test
    @Transactional
    void getAllPermissionsByPermissionNameIsInShouldWork() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where permissionName in DEFAULT_PERMISSION_NAME or UPDATED_PERMISSION_NAME
        defaultPermissionsShouldBeFound("permissionName.in=" + DEFAULT_PERMISSION_NAME + "," + UPDATED_PERMISSION_NAME);

        // Get all the permissionsList where permissionName equals to UPDATED_PERMISSION_NAME
        defaultPermissionsShouldNotBeFound("permissionName.in=" + UPDATED_PERMISSION_NAME);
    }

    @Test
    @Transactional
    void getAllPermissionsByPermissionNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where permissionName is not null
        defaultPermissionsShouldBeFound("permissionName.specified=true");

        // Get all the permissionsList where permissionName is null
        defaultPermissionsShouldNotBeFound("permissionName.specified=false");
    }

    @Test
    @Transactional
    void getAllPermissionsByPermissionNameContainsSomething() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where permissionName contains DEFAULT_PERMISSION_NAME
        defaultPermissionsShouldBeFound("permissionName.contains=" + DEFAULT_PERMISSION_NAME);

        // Get all the permissionsList where permissionName contains UPDATED_PERMISSION_NAME
        defaultPermissionsShouldNotBeFound("permissionName.contains=" + UPDATED_PERMISSION_NAME);
    }

    @Test
    @Transactional
    void getAllPermissionsByPermissionNameNotContainsSomething() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionsList where permissionName does not contain DEFAULT_PERMISSION_NAME
        defaultPermissionsShouldNotBeFound("permissionName.doesNotContain=" + DEFAULT_PERMISSION_NAME);

        // Get all the permissionsList where permissionName does not contain UPDATED_PERMISSION_NAME
        defaultPermissionsShouldBeFound("permissionName.doesNotContain=" + UPDATED_PERMISSION_NAME);
    }

    @Test
    @Transactional
    void getAllPermissionsByRoleIsEqualToSomething() throws Exception {
        Roles role;
        if (TestUtil.findAll(em, Roles.class).isEmpty()) {
            permissionsRepository.saveAndFlush(permissions);
            role = RolesResourceIT.createEntity(em);
        } else {
            role = TestUtil.findAll(em, Roles.class).get(0);
        }
        em.persist(role);
        em.flush();
        permissions.addRole(role);
        permissionsRepository.saveAndFlush(permissions);
        Long roleId = role.getId();

        // Get all the permissionsList where role equals to roleId
        defaultPermissionsShouldBeFound("roleId.equals=" + roleId);

        // Get all the permissionsList where role equals to (roleId + 1)
        defaultPermissionsShouldNotBeFound("roleId.equals=" + (roleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPermissionsShouldBeFound(String filter) throws Exception {
        restPermissionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permissions.getId().intValue())))
            .andExpect(jsonPath("$.[*].permissionName").value(hasItem(DEFAULT_PERMISSION_NAME)));

        // Check, that the count call also returns 1
        restPermissionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPermissionsShouldNotBeFound(String filter) throws Exception {
        restPermissionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPermissionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPermissions() throws Exception {
        // Get the permissions
        restPermissionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPermissions() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();

        // Update the permissions
        Permissions updatedPermissions = permissionsRepository.findById(permissions.getId()).get();
        // Disconnect from session so that the updates on updatedPermissions are not directly saved in db
        em.detach(updatedPermissions);
        updatedPermissions.permissionName(UPDATED_PERMISSION_NAME);

        restPermissionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPermissions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPermissions))
            )
            .andExpect(status().isOk());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
        Permissions testPermissions = permissionsList.get(permissionsList.size() - 1);
        assertThat(testPermissions.getPermissionName()).isEqualTo(UPDATED_PERMISSION_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPermissions() throws Exception {
        int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();
        permissions.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPermissions() throws Exception {
        int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();
        permissions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPermissions() throws Exception {
        int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();
        permissions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissions)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePermissionsWithPatch() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();

        // Update the permissions using partial update
        Permissions partialUpdatedPermissions = new Permissions();
        partialUpdatedPermissions.setId(permissions.getId());

        partialUpdatedPermissions.permissionName(UPDATED_PERMISSION_NAME);

        restPermissionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermissions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPermissions))
            )
            .andExpect(status().isOk());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
        Permissions testPermissions = permissionsList.get(permissionsList.size() - 1);
        assertThat(testPermissions.getPermissionName()).isEqualTo(UPDATED_PERMISSION_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePermissionsWithPatch() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();

        // Update the permissions using partial update
        Permissions partialUpdatedPermissions = new Permissions();
        partialUpdatedPermissions.setId(permissions.getId());

        partialUpdatedPermissions.permissionName(UPDATED_PERMISSION_NAME);

        restPermissionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermissions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPermissions))
            )
            .andExpect(status().isOk());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
        Permissions testPermissions = permissionsList.get(permissionsList.size() - 1);
        assertThat(testPermissions.getPermissionName()).isEqualTo(UPDATED_PERMISSION_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPermissions() throws Exception {
        int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();
        permissions.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, permissions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permissions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPermissions() throws Exception {
        int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();
        permissions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permissions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPermissions() throws Exception {
        int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();
        permissions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(permissions))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Permissions in the database
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePermissions() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        int databaseSizeBeforeDelete = permissionsRepository.findAll().size();

        // Delete the permissions
        restPermissionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, permissions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Permissions> permissionsList = permissionsRepository.findAll();
        assertThat(permissionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
