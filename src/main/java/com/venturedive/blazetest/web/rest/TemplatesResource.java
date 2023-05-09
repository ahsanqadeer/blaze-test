package com.venturedive.blazetest.web.rest;

import com.venturedive.blazetest.domain.Templates;
import com.venturedive.blazetest.repository.TemplatesRepository;
import com.venturedive.blazetest.service.TemplatesQueryService;
import com.venturedive.blazetest.service.TemplatesService;
import com.venturedive.blazetest.service.criteria.TemplatesCriteria;
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
 * REST controller for managing {@link com.venturedive.blazetest.domain.Templates}.
 */
@RestController
@RequestMapping("/api")
public class TemplatesResource {

    private final Logger log = LoggerFactory.getLogger(TemplatesResource.class);

    private static final String ENTITY_NAME = "templates";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TemplatesService templatesService;

    private final TemplatesRepository templatesRepository;

    private final TemplatesQueryService templatesQueryService;

    public TemplatesResource(
        TemplatesService templatesService,
        TemplatesRepository templatesRepository,
        TemplatesQueryService templatesQueryService
    ) {
        this.templatesService = templatesService;
        this.templatesRepository = templatesRepository;
        this.templatesQueryService = templatesQueryService;
    }

    /**
     * {@code POST  /templates} : Create a new templates.
     *
     * @param templates the templates to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new templates, or with status {@code 400 (Bad Request)} if the templates has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/templates")
    public ResponseEntity<Templates> createTemplates(@Valid @RequestBody Templates templates) throws URISyntaxException {
        log.debug("REST request to save Templates : {}", templates);
        if (templates.getId() != null) {
            throw new BadRequestAlertException("A new templates cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Templates result = templatesService.save(templates);
        return ResponseEntity
            .created(new URI("/api/templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /templates/:id} : Updates an existing templates.
     *
     * @param id the id of the templates to save.
     * @param templates the templates to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated templates,
     * or with status {@code 400 (Bad Request)} if the templates is not valid,
     * or with status {@code 500 (Internal Server Error)} if the templates couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/templates/{id}")
    public ResponseEntity<Templates> updateTemplates(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Templates templates
    ) throws URISyntaxException {
        log.debug("REST request to update Templates : {}, {}", id, templates);
        if (templates.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, templates.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!templatesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Templates result = templatesService.update(templates);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, templates.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /templates/:id} : Partial updates given fields of an existing templates, field will ignore if it is null
     *
     * @param id the id of the templates to save.
     * @param templates the templates to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated templates,
     * or with status {@code 400 (Bad Request)} if the templates is not valid,
     * or with status {@code 404 (Not Found)} if the templates is not found,
     * or with status {@code 500 (Internal Server Error)} if the templates couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/templates/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Templates> partialUpdateTemplates(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Templates templates
    ) throws URISyntaxException {
        log.debug("REST request to partial update Templates partially : {}, {}", id, templates);
        if (templates.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, templates.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!templatesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Templates> result = templatesService.partialUpdate(templates);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, templates.getId().toString())
        );
    }

    /**
     * {@code GET  /templates} : get all the templates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of templates in body.
     */
    @GetMapping("/templates")
    public ResponseEntity<List<Templates>> getAllTemplates(
        TemplatesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Templates by criteria: {}", criteria);
        Page<Templates> page = templatesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /templates/count} : count all the templates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/templates/count")
    public ResponseEntity<Long> countTemplates(TemplatesCriteria criteria) {
        log.debug("REST request to count Templates by criteria: {}", criteria);
        return ResponseEntity.ok().body(templatesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /templates/:id} : get the "id" templates.
     *
     * @param id the id of the templates to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the templates, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/templates/{id}")
    public ResponseEntity<Templates> getTemplates(@PathVariable Long id) {
        log.debug("REST request to get Templates : {}", id);
        Optional<Templates> templates = templatesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(templates);
    }

    /**
     * {@code DELETE  /templates/:id} : delete the "id" templates.
     *
     * @param id the id of the templates to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/templates/{id}")
    public ResponseEntity<Void> deleteTemplates(@PathVariable Long id) {
        log.debug("REST request to delete Templates : {}", id);
        templatesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
