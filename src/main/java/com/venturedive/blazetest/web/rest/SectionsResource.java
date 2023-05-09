package com.venturedive.blazetest.web.rest;

import com.venturedive.blazetest.domain.Sections;
import com.venturedive.blazetest.repository.SectionsRepository;
import com.venturedive.blazetest.service.SectionsQueryService;
import com.venturedive.blazetest.service.SectionsService;
import com.venturedive.blazetest.service.criteria.SectionsCriteria;
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
 * REST controller for managing {@link com.venturedive.blazetest.domain.Sections}.
 */
@RestController
@RequestMapping("/api")
public class SectionsResource {

    private final Logger log = LoggerFactory.getLogger(SectionsResource.class);

    private static final String ENTITY_NAME = "sections";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SectionsService sectionsService;

    private final SectionsRepository sectionsRepository;

    private final SectionsQueryService sectionsQueryService;

    public SectionsResource(
        SectionsService sectionsService,
        SectionsRepository sectionsRepository,
        SectionsQueryService sectionsQueryService
    ) {
        this.sectionsService = sectionsService;
        this.sectionsRepository = sectionsRepository;
        this.sectionsQueryService = sectionsQueryService;
    }

    /**
     * {@code POST  /sections} : Create a new sections.
     *
     * @param sections the sections to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sections, or with status {@code 400 (Bad Request)} if the sections has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sections")
    public ResponseEntity<Sections> createSections(@Valid @RequestBody Sections sections) throws URISyntaxException {
        log.debug("REST request to save Sections : {}", sections);
        if (sections.getId() != null) {
            throw new BadRequestAlertException("A new sections cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sections result = sectionsService.save(sections);
        return ResponseEntity
            .created(new URI("/api/sections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sections/:id} : Updates an existing sections.
     *
     * @param id the id of the sections to save.
     * @param sections the sections to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sections,
     * or with status {@code 400 (Bad Request)} if the sections is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sections couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sections/{id}")
    public ResponseEntity<Sections> updateSections(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Sections sections
    ) throws URISyntaxException {
        log.debug("REST request to update Sections : {}, {}", id, sections);
        if (sections.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sections.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sectionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Sections result = sectionsService.update(sections);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sections.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sections/:id} : Partial updates given fields of an existing sections, field will ignore if it is null
     *
     * @param id the id of the sections to save.
     * @param sections the sections to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sections,
     * or with status {@code 400 (Bad Request)} if the sections is not valid,
     * or with status {@code 404 (Not Found)} if the sections is not found,
     * or with status {@code 500 (Internal Server Error)} if the sections couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sections/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Sections> partialUpdateSections(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Sections sections
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sections partially : {}, {}", id, sections);
        if (sections.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sections.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sectionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Sections> result = sectionsService.partialUpdate(sections);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sections.getId().toString())
        );
    }

    /**
     * {@code GET  /sections} : get all the sections.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sections in body.
     */
    @GetMapping("/sections")
    public ResponseEntity<List<Sections>> getAllSections(
        SectionsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Sections by criteria: {}", criteria);
        Page<Sections> page = sectionsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sections/count} : count all the sections.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sections/count")
    public ResponseEntity<Long> countSections(SectionsCriteria criteria) {
        log.debug("REST request to count Sections by criteria: {}", criteria);
        return ResponseEntity.ok().body(sectionsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sections/:id} : get the "id" sections.
     *
     * @param id the id of the sections to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sections, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sections/{id}")
    public ResponseEntity<Sections> getSections(@PathVariable Long id) {
        log.debug("REST request to get Sections : {}", id);
        Optional<Sections> sections = sectionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sections);
    }

    /**
     * {@code DELETE  /sections/:id} : delete the "id" sections.
     *
     * @param id the id of the sections to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sections/{id}")
    public ResponseEntity<Void> deleteSections(@PathVariable Long id) {
        log.debug("REST request to delete Sections : {}", id);
        sectionsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
