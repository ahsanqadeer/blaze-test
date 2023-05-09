package com.venturedive.blazetest.web.rest;

import com.venturedive.blazetest.domain.TestStatuses;
import com.venturedive.blazetest.repository.TestStatusesRepository;
import com.venturedive.blazetest.service.TestStatusesQueryService;
import com.venturedive.blazetest.service.TestStatusesService;
import com.venturedive.blazetest.service.criteria.TestStatusesCriteria;
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
 * REST controller for managing {@link com.venturedive.blazetest.domain.TestStatuses}.
 */
@RestController
@RequestMapping("/api")
public class TestStatusesResource {

    private final Logger log = LoggerFactory.getLogger(TestStatusesResource.class);

    private static final String ENTITY_NAME = "testStatuses";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestStatusesService testStatusesService;

    private final TestStatusesRepository testStatusesRepository;

    private final TestStatusesQueryService testStatusesQueryService;

    public TestStatusesResource(
        TestStatusesService testStatusesService,
        TestStatusesRepository testStatusesRepository,
        TestStatusesQueryService testStatusesQueryService
    ) {
        this.testStatusesService = testStatusesService;
        this.testStatusesRepository = testStatusesRepository;
        this.testStatusesQueryService = testStatusesQueryService;
    }

    /**
     * {@code POST  /test-statuses} : Create a new testStatuses.
     *
     * @param testStatuses the testStatuses to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testStatuses, or with status {@code 400 (Bad Request)} if the testStatuses has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-statuses")
    public ResponseEntity<TestStatuses> createTestStatuses(@Valid @RequestBody TestStatuses testStatuses) throws URISyntaxException {
        log.debug("REST request to save TestStatuses : {}", testStatuses);
        if (testStatuses.getId() != null) {
            throw new BadRequestAlertException("A new testStatuses cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestStatuses result = testStatusesService.save(testStatuses);
        return ResponseEntity
            .created(new URI("/api/test-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-statuses/:id} : Updates an existing testStatuses.
     *
     * @param id the id of the testStatuses to save.
     * @param testStatuses the testStatuses to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testStatuses,
     * or with status {@code 400 (Bad Request)} if the testStatuses is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testStatuses couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-statuses/{id}")
    public ResponseEntity<TestStatuses> updateTestStatuses(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestStatuses testStatuses
    ) throws URISyntaxException {
        log.debug("REST request to update TestStatuses : {}, {}", id, testStatuses);
        if (testStatuses.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testStatuses.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testStatusesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestStatuses result = testStatusesService.update(testStatuses);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testStatuses.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-statuses/:id} : Partial updates given fields of an existing testStatuses, field will ignore if it is null
     *
     * @param id the id of the testStatuses to save.
     * @param testStatuses the testStatuses to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testStatuses,
     * or with status {@code 400 (Bad Request)} if the testStatuses is not valid,
     * or with status {@code 404 (Not Found)} if the testStatuses is not found,
     * or with status {@code 500 (Internal Server Error)} if the testStatuses couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestStatuses> partialUpdateTestStatuses(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestStatuses testStatuses
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestStatuses partially : {}, {}", id, testStatuses);
        if (testStatuses.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testStatuses.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testStatusesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestStatuses> result = testStatusesService.partialUpdate(testStatuses);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testStatuses.getId().toString())
        );
    }

    /**
     * {@code GET  /test-statuses} : get all the testStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testStatuses in body.
     */
    @GetMapping("/test-statuses")
    public ResponseEntity<List<TestStatuses>> getAllTestStatuses(
        TestStatusesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestStatuses by criteria: {}", criteria);
        Page<TestStatuses> page = testStatusesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-statuses/count} : count all the testStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-statuses/count")
    public ResponseEntity<Long> countTestStatuses(TestStatusesCriteria criteria) {
        log.debug("REST request to count TestStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(testStatusesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-statuses/:id} : get the "id" testStatuses.
     *
     * @param id the id of the testStatuses to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testStatuses, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-statuses/{id}")
    public ResponseEntity<TestStatuses> getTestStatuses(@PathVariable Long id) {
        log.debug("REST request to get TestStatuses : {}", id);
        Optional<TestStatuses> testStatuses = testStatusesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testStatuses);
    }

    /**
     * {@code DELETE  /test-statuses/:id} : delete the "id" testStatuses.
     *
     * @param id the id of the testStatuses to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-statuses/{id}")
    public ResponseEntity<Void> deleteTestStatuses(@PathVariable Long id) {
        log.debug("REST request to delete TestStatuses : {}", id);
        testStatusesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
