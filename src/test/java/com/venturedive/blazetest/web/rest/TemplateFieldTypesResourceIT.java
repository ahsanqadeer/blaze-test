package com.venturedive.blazetest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venturedive.blazetest.IntegrationTest;
import com.venturedive.blazetest.domain.TemplateFieldTypes;
import com.venturedive.blazetest.domain.TemplateFields;
import com.venturedive.blazetest.repository.TemplateFieldTypesRepository;
import com.venturedive.blazetest.service.criteria.TemplateFieldTypesCriteria;
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
 * Integration tests for the {@link TemplateFieldTypesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TemplateFieldTypesResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_LIST = false;
    private static final Boolean UPDATED_IS_LIST = true;

    private static final Boolean DEFAULT_ATTACHMENTS = false;
    private static final Boolean UPDATED_ATTACHMENTS = true;

    private static final String ENTITY_API_URL = "/api/template-field-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TemplateFieldTypesRepository templateFieldTypesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTemplateFieldTypesMockMvc;

    private TemplateFieldTypes templateFieldTypes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TemplateFieldTypes createEntity(EntityManager em) {
        TemplateFieldTypes templateFieldTypes = new TemplateFieldTypes()
            .type(DEFAULT_TYPE)
            .isList(DEFAULT_IS_LIST)
            .attachments(DEFAULT_ATTACHMENTS);
        return templateFieldTypes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TemplateFieldTypes createUpdatedEntity(EntityManager em) {
        TemplateFieldTypes templateFieldTypes = new TemplateFieldTypes()
            .type(UPDATED_TYPE)
            .isList(UPDATED_IS_LIST)
            .attachments(UPDATED_ATTACHMENTS);
        return templateFieldTypes;
    }

    @BeforeEach
    public void initTest() {
        templateFieldTypes = createEntity(em);
    }

    @Test
    @Transactional
    void createTemplateFieldTypes() throws Exception {
        int databaseSizeBeforeCreate = templateFieldTypesRepository.findAll().size();
        // Create the TemplateFieldTypes
        restTemplateFieldTypesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateFieldTypes))
            )
            .andExpect(status().isCreated());

        // Validate the TemplateFieldTypes in the database
        List<TemplateFieldTypes> templateFieldTypesList = templateFieldTypesRepository.findAll();
        assertThat(templateFieldTypesList).hasSize(databaseSizeBeforeCreate + 1);
        TemplateFieldTypes testTemplateFieldTypes = templateFieldTypesList.get(templateFieldTypesList.size() - 1);
        assertThat(testTemplateFieldTypes.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTemplateFieldTypes.getIsList()).isEqualTo(DEFAULT_IS_LIST);
        assertThat(testTemplateFieldTypes.getAttachments()).isEqualTo(DEFAULT_ATTACHMENTS);
    }

    @Test
    @Transactional
    void createTemplateFieldTypesWithExistingId() throws Exception {
        // Create the TemplateFieldTypes with an existing ID
        templateFieldTypes.setId(1L);

        int databaseSizeBeforeCreate = templateFieldTypesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTemplateFieldTypesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateFieldTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateFieldTypes in the database
        List<TemplateFieldTypes> templateFieldTypesList = templateFieldTypesRepository.findAll();
        assertThat(templateFieldTypesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = templateFieldTypesRepository.findAll().size();
        // set the field null
        templateFieldTypes.setType(null);

        // Create the TemplateFieldTypes, which fails.

        restTemplateFieldTypesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateFieldTypes))
            )
            .andExpect(status().isBadRequest());

        List<TemplateFieldTypes> templateFieldTypesList = templateFieldTypesRepository.findAll();
        assertThat(templateFieldTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsListIsRequired() throws Exception {
        int databaseSizeBeforeTest = templateFieldTypesRepository.findAll().size();
        // set the field null
        templateFieldTypes.setIsList(null);

        // Create the TemplateFieldTypes, which fails.

        restTemplateFieldTypesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateFieldTypes))
            )
            .andExpect(status().isBadRequest());

        List<TemplateFieldTypes> templateFieldTypesList = templateFieldTypesRepository.findAll();
        assertThat(templateFieldTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAttachmentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = templateFieldTypesRepository.findAll().size();
        // set the field null
        templateFieldTypes.setAttachments(null);

        // Create the TemplateFieldTypes, which fails.

        restTemplateFieldTypesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateFieldTypes))
            )
            .andExpect(status().isBadRequest());

        List<TemplateFieldTypes> templateFieldTypesList = templateFieldTypesRepository.findAll();
        assertThat(templateFieldTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypes() throws Exception {
        // Initialize the database
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);

        // Get all the templateFieldTypesList
        restTemplateFieldTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(templateFieldTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].isList").value(hasItem(DEFAULT_IS_LIST.booleanValue())))
            .andExpect(jsonPath("$.[*].attachments").value(hasItem(DEFAULT_ATTACHMENTS.booleanValue())));
    }

    @Test
    @Transactional
    void getTemplateFieldTypes() throws Exception {
        // Initialize the database
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);

        // Get the templateFieldTypes
        restTemplateFieldTypesMockMvc
            .perform(get(ENTITY_API_URL_ID, templateFieldTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(templateFieldTypes.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.isList").value(DEFAULT_IS_LIST.booleanValue()))
            .andExpect(jsonPath("$.attachments").value(DEFAULT_ATTACHMENTS.booleanValue()));
    }

    @Test
    @Transactional
    void getTemplateFieldTypesByIdFiltering() throws Exception {
        // Initialize the database
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);

        Long id = templateFieldTypes.getId();

        defaultTemplateFieldTypesShouldBeFound("id.equals=" + id);
        defaultTemplateFieldTypesShouldNotBeFound("id.notEquals=" + id);

        defaultTemplateFieldTypesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTemplateFieldTypesShouldNotBeFound("id.greaterThan=" + id);

        defaultTemplateFieldTypesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTemplateFieldTypesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);

        // Get all the templateFieldTypesList where type equals to DEFAULT_TYPE
        defaultTemplateFieldTypesShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the templateFieldTypesList where type equals to UPDATED_TYPE
        defaultTemplateFieldTypesShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);

        // Get all the templateFieldTypesList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultTemplateFieldTypesShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the templateFieldTypesList where type equals to UPDATED_TYPE
        defaultTemplateFieldTypesShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);

        // Get all the templateFieldTypesList where type is not null
        defaultTemplateFieldTypesShouldBeFound("type.specified=true");

        // Get all the templateFieldTypesList where type is null
        defaultTemplateFieldTypesShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByTypeContainsSomething() throws Exception {
        // Initialize the database
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);

        // Get all the templateFieldTypesList where type contains DEFAULT_TYPE
        defaultTemplateFieldTypesShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the templateFieldTypesList where type contains UPDATED_TYPE
        defaultTemplateFieldTypesShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);

        // Get all the templateFieldTypesList where type does not contain DEFAULT_TYPE
        defaultTemplateFieldTypesShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the templateFieldTypesList where type does not contain UPDATED_TYPE
        defaultTemplateFieldTypesShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByIsListIsEqualToSomething() throws Exception {
        // Initialize the database
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);

        // Get all the templateFieldTypesList where isList equals to DEFAULT_IS_LIST
        defaultTemplateFieldTypesShouldBeFound("isList.equals=" + DEFAULT_IS_LIST);

        // Get all the templateFieldTypesList where isList equals to UPDATED_IS_LIST
        defaultTemplateFieldTypesShouldNotBeFound("isList.equals=" + UPDATED_IS_LIST);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByIsListIsInShouldWork() throws Exception {
        // Initialize the database
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);

        // Get all the templateFieldTypesList where isList in DEFAULT_IS_LIST or UPDATED_IS_LIST
        defaultTemplateFieldTypesShouldBeFound("isList.in=" + DEFAULT_IS_LIST + "," + UPDATED_IS_LIST);

        // Get all the templateFieldTypesList where isList equals to UPDATED_IS_LIST
        defaultTemplateFieldTypesShouldNotBeFound("isList.in=" + UPDATED_IS_LIST);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByIsListIsNullOrNotNull() throws Exception {
        // Initialize the database
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);

        // Get all the templateFieldTypesList where isList is not null
        defaultTemplateFieldTypesShouldBeFound("isList.specified=true");

        // Get all the templateFieldTypesList where isList is null
        defaultTemplateFieldTypesShouldNotBeFound("isList.specified=false");
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByAttachmentsIsEqualToSomething() throws Exception {
        // Initialize the database
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);

        // Get all the templateFieldTypesList where attachments equals to DEFAULT_ATTACHMENTS
        defaultTemplateFieldTypesShouldBeFound("attachments.equals=" + DEFAULT_ATTACHMENTS);

        // Get all the templateFieldTypesList where attachments equals to UPDATED_ATTACHMENTS
        defaultTemplateFieldTypesShouldNotBeFound("attachments.equals=" + UPDATED_ATTACHMENTS);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByAttachmentsIsInShouldWork() throws Exception {
        // Initialize the database
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);

        // Get all the templateFieldTypesList where attachments in DEFAULT_ATTACHMENTS or UPDATED_ATTACHMENTS
        defaultTemplateFieldTypesShouldBeFound("attachments.in=" + DEFAULT_ATTACHMENTS + "," + UPDATED_ATTACHMENTS);

        // Get all the templateFieldTypesList where attachments equals to UPDATED_ATTACHMENTS
        defaultTemplateFieldTypesShouldNotBeFound("attachments.in=" + UPDATED_ATTACHMENTS);
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByAttachmentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);

        // Get all the templateFieldTypesList where attachments is not null
        defaultTemplateFieldTypesShouldBeFound("attachments.specified=true");

        // Get all the templateFieldTypesList where attachments is null
        defaultTemplateFieldTypesShouldNotBeFound("attachments.specified=false");
    }

    @Test
    @Transactional
    void getAllTemplateFieldTypesByTemplatefieldsTemplatefieldtypeIsEqualToSomething() throws Exception {
        TemplateFields templatefieldsTemplatefieldtype;
        if (TestUtil.findAll(em, TemplateFields.class).isEmpty()) {
            templateFieldTypesRepository.saveAndFlush(templateFieldTypes);
            templatefieldsTemplatefieldtype = TemplateFieldsResourceIT.createEntity(em);
        } else {
            templatefieldsTemplatefieldtype = TestUtil.findAll(em, TemplateFields.class).get(0);
        }
        em.persist(templatefieldsTemplatefieldtype);
        em.flush();
        templateFieldTypes.addTemplatefieldsTemplatefieldtype(templatefieldsTemplatefieldtype);
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);
        Long templatefieldsTemplatefieldtypeId = templatefieldsTemplatefieldtype.getId();

        // Get all the templateFieldTypesList where templatefieldsTemplatefieldtype equals to templatefieldsTemplatefieldtypeId
        defaultTemplateFieldTypesShouldBeFound("templatefieldsTemplatefieldtypeId.equals=" + templatefieldsTemplatefieldtypeId);

        // Get all the templateFieldTypesList where templatefieldsTemplatefieldtype equals to (templatefieldsTemplatefieldtypeId + 1)
        defaultTemplateFieldTypesShouldNotBeFound("templatefieldsTemplatefieldtypeId.equals=" + (templatefieldsTemplatefieldtypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTemplateFieldTypesShouldBeFound(String filter) throws Exception {
        restTemplateFieldTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(templateFieldTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].isList").value(hasItem(DEFAULT_IS_LIST.booleanValue())))
            .andExpect(jsonPath("$.[*].attachments").value(hasItem(DEFAULT_ATTACHMENTS.booleanValue())));

        // Check, that the count call also returns 1
        restTemplateFieldTypesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTemplateFieldTypesShouldNotBeFound(String filter) throws Exception {
        restTemplateFieldTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTemplateFieldTypesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTemplateFieldTypes() throws Exception {
        // Get the templateFieldTypes
        restTemplateFieldTypesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTemplateFieldTypes() throws Exception {
        // Initialize the database
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);

        int databaseSizeBeforeUpdate = templateFieldTypesRepository.findAll().size();

        // Update the templateFieldTypes
        TemplateFieldTypes updatedTemplateFieldTypes = templateFieldTypesRepository.findById(templateFieldTypes.getId()).get();
        // Disconnect from session so that the updates on updatedTemplateFieldTypes are not directly saved in db
        em.detach(updatedTemplateFieldTypes);
        updatedTemplateFieldTypes.type(UPDATED_TYPE).isList(UPDATED_IS_LIST).attachments(UPDATED_ATTACHMENTS);

        restTemplateFieldTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTemplateFieldTypes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTemplateFieldTypes))
            )
            .andExpect(status().isOk());

        // Validate the TemplateFieldTypes in the database
        List<TemplateFieldTypes> templateFieldTypesList = templateFieldTypesRepository.findAll();
        assertThat(templateFieldTypesList).hasSize(databaseSizeBeforeUpdate);
        TemplateFieldTypes testTemplateFieldTypes = templateFieldTypesList.get(templateFieldTypesList.size() - 1);
        assertThat(testTemplateFieldTypes.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTemplateFieldTypes.getIsList()).isEqualTo(UPDATED_IS_LIST);
        assertThat(testTemplateFieldTypes.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
    }

    @Test
    @Transactional
    void putNonExistingTemplateFieldTypes() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldTypesRepository.findAll().size();
        templateFieldTypes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemplateFieldTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, templateFieldTypes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(templateFieldTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateFieldTypes in the database
        List<TemplateFieldTypes> templateFieldTypesList = templateFieldTypesRepository.findAll();
        assertThat(templateFieldTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTemplateFieldTypes() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldTypesRepository.findAll().size();
        templateFieldTypes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateFieldTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(templateFieldTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateFieldTypes in the database
        List<TemplateFieldTypes> templateFieldTypesList = templateFieldTypesRepository.findAll();
        assertThat(templateFieldTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTemplateFieldTypes() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldTypesRepository.findAll().size();
        templateFieldTypes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateFieldTypesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateFieldTypes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TemplateFieldTypes in the database
        List<TemplateFieldTypes> templateFieldTypesList = templateFieldTypesRepository.findAll();
        assertThat(templateFieldTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTemplateFieldTypesWithPatch() throws Exception {
        // Initialize the database
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);

        int databaseSizeBeforeUpdate = templateFieldTypesRepository.findAll().size();

        // Update the templateFieldTypes using partial update
        TemplateFieldTypes partialUpdatedTemplateFieldTypes = new TemplateFieldTypes();
        partialUpdatedTemplateFieldTypes.setId(templateFieldTypes.getId());

        partialUpdatedTemplateFieldTypes.type(UPDATED_TYPE).isList(UPDATED_IS_LIST);

        restTemplateFieldTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemplateFieldTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemplateFieldTypes))
            )
            .andExpect(status().isOk());

        // Validate the TemplateFieldTypes in the database
        List<TemplateFieldTypes> templateFieldTypesList = templateFieldTypesRepository.findAll();
        assertThat(templateFieldTypesList).hasSize(databaseSizeBeforeUpdate);
        TemplateFieldTypes testTemplateFieldTypes = templateFieldTypesList.get(templateFieldTypesList.size() - 1);
        assertThat(testTemplateFieldTypes.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTemplateFieldTypes.getIsList()).isEqualTo(UPDATED_IS_LIST);
        assertThat(testTemplateFieldTypes.getAttachments()).isEqualTo(DEFAULT_ATTACHMENTS);
    }

    @Test
    @Transactional
    void fullUpdateTemplateFieldTypesWithPatch() throws Exception {
        // Initialize the database
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);

        int databaseSizeBeforeUpdate = templateFieldTypesRepository.findAll().size();

        // Update the templateFieldTypes using partial update
        TemplateFieldTypes partialUpdatedTemplateFieldTypes = new TemplateFieldTypes();
        partialUpdatedTemplateFieldTypes.setId(templateFieldTypes.getId());

        partialUpdatedTemplateFieldTypes.type(UPDATED_TYPE).isList(UPDATED_IS_LIST).attachments(UPDATED_ATTACHMENTS);

        restTemplateFieldTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemplateFieldTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemplateFieldTypes))
            )
            .andExpect(status().isOk());

        // Validate the TemplateFieldTypes in the database
        List<TemplateFieldTypes> templateFieldTypesList = templateFieldTypesRepository.findAll();
        assertThat(templateFieldTypesList).hasSize(databaseSizeBeforeUpdate);
        TemplateFieldTypes testTemplateFieldTypes = templateFieldTypesList.get(templateFieldTypesList.size() - 1);
        assertThat(testTemplateFieldTypes.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTemplateFieldTypes.getIsList()).isEqualTo(UPDATED_IS_LIST);
        assertThat(testTemplateFieldTypes.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
    }

    @Test
    @Transactional
    void patchNonExistingTemplateFieldTypes() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldTypesRepository.findAll().size();
        templateFieldTypes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemplateFieldTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, templateFieldTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(templateFieldTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateFieldTypes in the database
        List<TemplateFieldTypes> templateFieldTypesList = templateFieldTypesRepository.findAll();
        assertThat(templateFieldTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTemplateFieldTypes() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldTypesRepository.findAll().size();
        templateFieldTypes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateFieldTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(templateFieldTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateFieldTypes in the database
        List<TemplateFieldTypes> templateFieldTypesList = templateFieldTypesRepository.findAll();
        assertThat(templateFieldTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTemplateFieldTypes() throws Exception {
        int databaseSizeBeforeUpdate = templateFieldTypesRepository.findAll().size();
        templateFieldTypes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateFieldTypesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(templateFieldTypes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TemplateFieldTypes in the database
        List<TemplateFieldTypes> templateFieldTypesList = templateFieldTypesRepository.findAll();
        assertThat(templateFieldTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTemplateFieldTypes() throws Exception {
        // Initialize the database
        templateFieldTypesRepository.saveAndFlush(templateFieldTypes);

        int databaseSizeBeforeDelete = templateFieldTypesRepository.findAll().size();

        // Delete the templateFieldTypes
        restTemplateFieldTypesMockMvc
            .perform(delete(ENTITY_API_URL_ID, templateFieldTypes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TemplateFieldTypes> templateFieldTypesList = templateFieldTypesRepository.findAll();
        assertThat(templateFieldTypesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
