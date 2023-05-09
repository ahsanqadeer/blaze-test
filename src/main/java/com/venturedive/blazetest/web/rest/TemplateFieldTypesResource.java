package com.venturedive.blazetest.web.rest;

import com.venturedive.blazetest.domain.TemplateFieldTypes;
import com.venturedive.blazetest.repository.TemplateFieldTypesRepository;
import com.venturedive.blazetest.service.TemplateFieldTypesQueryService;
import com.venturedive.blazetest.service.TemplateFieldTypesService;
import com.venturedive.blazetest.service.criteria.TemplateFieldTypesCriteria;
import com.venturedive.blazetest.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.venturedive.blazetest.domain.TemplateFieldTypes}.
 */
@RestController
@RequestMapping("/api")
public class TemplateFieldTypesResource {

    private final Logger log = LoggerFactory.getLogger(TemplateFieldTypesResource.class);

    private static final String ENTITY_NAME = "templateFieldTypes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TemplateFieldTypesService templateFieldTypesService;

    private final TemplateFieldTypesRepository templateFieldTypesRepository;

    private final TemplateFieldTypesQueryService templateFieldTypesQueryService;

    public TemplateFieldTypesResource(
        TemplateFieldTypesService templateFieldTypesService,
        TemplateFieldTypesRepository templateFieldTypesRepository,
        TemplateFieldTypesQueryService templateFieldTypesQueryService
    ) {
        this.templateFieldTypesService = templateFieldTypesService;
        this.templateFieldTypesRepository = templateFieldTypesRepository;
        this.templateFieldTypesQueryService = templateFieldTypesQueryService;
    }

    /**
     * {@code POST  /template-field-types} : Create a new templateFieldTypes.
     *
     * @param templateFieldTypes the templateFieldTypes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new templateFieldTypes, or with status {@code 400 (Bad Request)} if the templateFieldTypes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/template-field-types")
    public ResponseEntity<TemplateFieldTypes> createTemplateFieldTypes(@Valid @RequestBody TemplateFieldTypes templateFieldTypes)
        throws URISyntaxException {
        log.debug("REST request to save TemplateFieldTypes : {}", templateFieldTypes);
        if (templateFieldTypes.getId() != null) {
            throw new BadRequestAlertException("A new templateFieldTypes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TemplateFieldTypes result = templateFieldTypesService.save(templateFieldTypes);
        return ResponseEntity
            .created(new URI("/api/template-field-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /template-field-types/:id} : Updates an existing templateFieldTypes.
     *
     * @param id the id of the templateFieldTypes to save.
     * @param templateFieldTypes the templateFieldTypes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated templateFieldTypes,
     * or with status {@code 400 (Bad Request)} if the templateFieldTypes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the templateFieldTypes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/template-field-types/{id}")
    public ResponseEntity<TemplateFieldTypes> updateTemplateFieldTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TemplateFieldTypes templateFieldTypes
    ) throws URISyntaxException {
        log.debug("REST request to update TemplateFieldTypes : {}, {}", id, templateFieldTypes);
        if (templateFieldTypes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, templateFieldTypes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!templateFieldTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TemplateFieldTypes result = templateFieldTypesService.update(templateFieldTypes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, templateFieldTypes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /template-field-types/:id} : Partial updates given fields of an existing templateFieldTypes, field will ignore if it is null
     *
     * @param id the id of the templateFieldTypes to save.
     * @param templateFieldTypes the templateFieldTypes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated templateFieldTypes,
     * or with status {@code 400 (Bad Request)} if the templateFieldTypes is not valid,
     * or with status {@code 404 (Not Found)} if the templateFieldTypes is not found,
     * or with status {@code 500 (Internal Server Error)} if the templateFieldTypes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/template-field-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TemplateFieldTypes> partialUpdateTemplateFieldTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TemplateFieldTypes templateFieldTypes
    ) throws URISyntaxException {
        log.debug("REST request to partial update TemplateFieldTypes partially : {}, {}", id, templateFieldTypes);
        if (templateFieldTypes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, templateFieldTypes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!templateFieldTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TemplateFieldTypes> result = templateFieldTypesService.partialUpdate(templateFieldTypes);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, templateFieldTypes.getId().toString())
        );
    }

    /**
     * {@code GET  /template-field-types} : get all the templateFieldTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of templateFieldTypes in body.
     */
    @GetMapping("/template-field-types")
    public ResponseEntity<List<TemplateFieldTypes>> getAllTemplateFieldTypes(
        TemplateFieldTypesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TemplateFieldTypes by criteria: {}", criteria);
        Page<TemplateFieldTypes> page = templateFieldTypesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /template-field-types/count} : count all the templateFieldTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/template-field-types/count")
    public ResponseEntity<Long> countTemplateFieldTypes(TemplateFieldTypesCriteria criteria) {
        log.debug("REST request to count TemplateFieldTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(templateFieldTypesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /template-field-types/:id} : get the "id" templateFieldTypes.
     *
     * @param id the id of the templateFieldTypes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the templateFieldTypes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/template-field-types/{id}")
    public ResponseEntity<TemplateFieldTypes> getTemplateFieldTypes(@PathVariable Long id) {
        log.debug("REST request to get TemplateFieldTypes : {}", id);
        Optional<TemplateFieldTypes> templateFieldTypes = templateFieldTypesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(templateFieldTypes);
    }

    /**
     * {@code DELETE  /template-field-types/:id} : delete the "id" templateFieldTypes.
     *
     * @param id the id of the templateFieldTypes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/template-field-types/{id}")
    public ResponseEntity<Void> deleteTemplateFieldTypes(@PathVariable Long id) {
        log.debug("REST request to delete TemplateFieldTypes : {}", id);
        templateFieldTypesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
