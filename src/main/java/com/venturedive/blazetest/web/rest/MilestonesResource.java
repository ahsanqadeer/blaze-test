package com.venturedive.blazetest.web.rest;

import com.venturedive.blazetest.domain.Milestones;
import com.venturedive.blazetest.repository.MilestonesRepository;
import com.venturedive.blazetest.service.MilestonesQueryService;
import com.venturedive.blazetest.service.MilestonesService;
import com.venturedive.blazetest.service.criteria.MilestonesCriteria;
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
 * REST controller for managing {@link com.venturedive.blazetest.domain.Milestones}.
 */
@RestController
@RequestMapping("/api")
public class MilestonesResource {

    private final Logger log = LoggerFactory.getLogger(MilestonesResource.class);

    private static final String ENTITY_NAME = "milestones";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MilestonesService milestonesService;

    private final MilestonesRepository milestonesRepository;

    private final MilestonesQueryService milestonesQueryService;

    public MilestonesResource(
        MilestonesService milestonesService,
        MilestonesRepository milestonesRepository,
        MilestonesQueryService milestonesQueryService
    ) {
        this.milestonesService = milestonesService;
        this.milestonesRepository = milestonesRepository;
        this.milestonesQueryService = milestonesQueryService;
    }

    /**
     * {@code POST  /milestones} : Create a new milestones.
     *
     * @param milestones the milestones to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new milestones, or with status {@code 400 (Bad Request)} if the milestones has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/milestones")
    public ResponseEntity<Milestones> createMilestones(@Valid @RequestBody Milestones milestones) throws URISyntaxException {
        log.debug("REST request to save Milestones : {}", milestones);
        if (milestones.getId() != null) {
            throw new BadRequestAlertException("A new milestones cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Milestones result = milestonesService.save(milestones);
        return ResponseEntity
            .created(new URI("/api/milestones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /milestones/:id} : Updates an existing milestones.
     *
     * @param id the id of the milestones to save.
     * @param milestones the milestones to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated milestones,
     * or with status {@code 400 (Bad Request)} if the milestones is not valid,
     * or with status {@code 500 (Internal Server Error)} if the milestones couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/milestones/{id}")
    public ResponseEntity<Milestones> updateMilestones(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Milestones milestones
    ) throws URISyntaxException {
        log.debug("REST request to update Milestones : {}, {}", id, milestones);
        if (milestones.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, milestones.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!milestonesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Milestones result = milestonesService.update(milestones);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, milestones.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /milestones/:id} : Partial updates given fields of an existing milestones, field will ignore if it is null
     *
     * @param id the id of the milestones to save.
     * @param milestones the milestones to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated milestones,
     * or with status {@code 400 (Bad Request)} if the milestones is not valid,
     * or with status {@code 404 (Not Found)} if the milestones is not found,
     * or with status {@code 500 (Internal Server Error)} if the milestones couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/milestones/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Milestones> partialUpdateMilestones(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Milestones milestones
    ) throws URISyntaxException {
        log.debug("REST request to partial update Milestones partially : {}, {}", id, milestones);
        if (milestones.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, milestones.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!milestonesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Milestones> result = milestonesService.partialUpdate(milestones);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, milestones.getId().toString())
        );
    }

    /**
     * {@code GET  /milestones} : get all the milestones.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of milestones in body.
     */
    @GetMapping("/milestones")
    public ResponseEntity<List<Milestones>> getAllMilestones(
        MilestonesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Milestones by criteria: {}", criteria);
        Page<Milestones> page = milestonesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /milestones/count} : count all the milestones.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/milestones/count")
    public ResponseEntity<Long> countMilestones(MilestonesCriteria criteria) {
        log.debug("REST request to count Milestones by criteria: {}", criteria);
        return ResponseEntity.ok().body(milestonesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /milestones/:id} : get the "id" milestones.
     *
     * @param id the id of the milestones to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the milestones, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/milestones/{id}")
    public ResponseEntity<Milestones> getMilestones(@PathVariable Long id) {
        log.debug("REST request to get Milestones : {}", id);
        Optional<Milestones> milestones = milestonesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(milestones);
    }

    /**
     * {@code DELETE  /milestones/:id} : delete the "id" milestones.
     *
     * @param id the id of the milestones to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/milestones/{id}")
    public ResponseEntity<Void> deleteMilestones(@PathVariable Long id) {
        log.debug("REST request to delete Milestones : {}", id);
        milestonesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
