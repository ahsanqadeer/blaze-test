package com.venturedive.blazetest.web.rest;

import com.venturedive.blazetest.domain.TemplateFields;
import com.venturedive.blazetest.repository.TemplateFieldsRepository;
import com.venturedive.blazetest.service.TemplateFieldsQueryService;
import com.venturedive.blazetest.service.TemplateFieldsService;
import com.venturedive.blazetest.service.criteria.TemplateFieldsCriteria;
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
 * REST controller for managing {@link com.venturedive.blazetest.domain.TemplateFields}.
 */
@RestController
@RequestMapping("/api")
public class TemplateFieldsResource {

    private final Logger log = LoggerFactory.getLogger(TemplateFieldsResource.class);

    private static final String ENTITY_NAME = "templateFields";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TemplateFieldsService templateFieldsService;

    private final TemplateFieldsRepository templateFieldsRepository;

    private final TemplateFieldsQueryService templateFieldsQueryService;

    public TemplateFieldsResource(
        TemplateFieldsService templateFieldsService,
        TemplateFieldsRepository templateFieldsRepository,
        TemplateFieldsQueryService templateFieldsQueryService
    ) {
        this.templateFieldsService = templateFieldsService;
        this.templateFieldsRepository = templateFieldsRepository;
        this.templateFieldsQueryService = templateFieldsQueryService;
    }

    /**
     * {@code POST  /template-fields} : Create a new templateFields.
     *
     * @param templateFields the templateFields to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new templateFields, or with status {@code 400 (Bad Request)} if the templateFields has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/template-fields")
    public ResponseEntity<TemplateFields> createTemplateFields(@Valid @RequestBody TemplateFields templateFields)
        throws URISyntaxException {
        log.debug("REST request to save TemplateFields : {}", templateFields);
        if (templateFields.getId() != null) {
            throw new BadRequestAlertException("A new templateFields cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TemplateFields result = templateFieldsService.save(templateFields);
        return ResponseEntity
            .created(new URI("/api/template-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /template-fields/:id} : Updates an existing templateFields.
     *
     * @param id the id of the templateFields to save.
     * @param templateFields the templateFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated templateFields,
     * or with status {@code 400 (Bad Request)} if the templateFields is not valid,
     * or with status {@code 500 (Internal Server Error)} if the templateFields couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/template-fields/{id}")
    public ResponseEntity<TemplateFields> updateTemplateFields(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TemplateFields templateFields
    ) throws URISyntaxException {
        log.debug("REST request to update TemplateFields : {}, {}", id, templateFields);
        if (templateFields.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, templateFields.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!templateFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TemplateFields result = templateFieldsService.update(templateFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, templateFields.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /template-fields/:id} : Partial updates given fields of an existing templateFields, field will ignore if it is null
     *
     * @param id the id of the templateFields to save.
     * @param templateFields the templateFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated templateFields,
     * or with status {@code 400 (Bad Request)} if the templateFields is not valid,
     * or with status {@code 404 (Not Found)} if the templateFields is not found,
     * or with status {@code 500 (Internal Server Error)} if the templateFields couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/template-fields/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TemplateFields> partialUpdateTemplateFields(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TemplateFields templateFields
    ) throws URISyntaxException {
        log.debug("REST request to partial update TemplateFields partially : {}, {}", id, templateFields);
        if (templateFields.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, templateFields.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!templateFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TemplateFields> result = templateFieldsService.partialUpdate(templateFields);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, templateFields.getId().toString())
        );
    }

    /**
     * {@code GET  /template-fields} : get all the templateFields.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of templateFields in body.
     */
    @GetMapping("/template-fields")
    public ResponseEntity<List<TemplateFields>> getAllTemplateFields(
        TemplateFieldsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TemplateFields by criteria: {}", criteria);
        Page<TemplateFields> page = templateFieldsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /template-fields/count} : count all the templateFields.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/template-fields/count")
    public ResponseEntity<Long> countTemplateFields(TemplateFieldsCriteria criteria) {
        log.debug("REST request to count TemplateFields by criteria: {}", criteria);
        return ResponseEntity.ok().body(templateFieldsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /template-fields/:id} : get the "id" templateFields.
     *
     * @param id the id of the templateFields to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the templateFields, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/template-fields/{id}")
    public ResponseEntity<TemplateFields> getTemplateFields(@PathVariable Long id) {
        log.debug("REST request to get TemplateFields : {}", id);
        Optional<TemplateFields> templateFields = templateFieldsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(templateFields);
    }

    /**
     * {@code DELETE  /template-fields/:id} : delete the "id" templateFields.
     *
     * @param id the id of the templateFields to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/template-fields/{id}")
    public ResponseEntity<Void> deleteTemplateFields(@PathVariable Long id) {
        log.debug("REST request to delete TemplateFields : {}", id);
        templateFieldsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
