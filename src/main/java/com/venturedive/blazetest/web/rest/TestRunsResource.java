package com.venturedive.blazetest.web.rest;

import com.venturedive.blazetest.domain.TestRuns;
import com.venturedive.blazetest.repository.TestRunsRepository;
import com.venturedive.blazetest.service.TestRunsQueryService;
import com.venturedive.blazetest.service.TestRunsService;
import com.venturedive.blazetest.service.criteria.TestRunsCriteria;
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
 * REST controller for managing {@link com.venturedive.blazetest.domain.TestRuns}.
 */
@RestController
@RequestMapping("/api")
public class TestRunsResource {

    private final Logger log = LoggerFactory.getLogger(TestRunsResource.class);

    private static final String ENTITY_NAME = "testRuns";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestRunsService testRunsService;

    private final TestRunsRepository testRunsRepository;

    private final TestRunsQueryService testRunsQueryService;

    public TestRunsResource(
        TestRunsService testRunsService,
        TestRunsRepository testRunsRepository,
        TestRunsQueryService testRunsQueryService
    ) {
        this.testRunsService = testRunsService;
        this.testRunsRepository = testRunsRepository;
        this.testRunsQueryService = testRunsQueryService;
    }

    /**
     * {@code POST  /test-runs} : Create a new testRuns.
     *
     * @param testRuns the testRuns to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testRuns, or with status {@code 400 (Bad Request)} if the testRuns has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-runs")
    public ResponseEntity<TestRuns> createTestRuns(@Valid @RequestBody TestRuns testRuns) throws URISyntaxException {
        log.debug("REST request to save TestRuns : {}", testRuns);
        if (testRuns.getId() != null) {
            throw new BadRequestAlertException("A new testRuns cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestRuns result = testRunsService.save(testRuns);
        return ResponseEntity
            .created(new URI("/api/test-runs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-runs/:id} : Updates an existing testRuns.
     *
     * @param id the id of the testRuns to save.
     * @param testRuns the testRuns to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testRuns,
     * or with status {@code 400 (Bad Request)} if the testRuns is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testRuns couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-runs/{id}")
    public ResponseEntity<TestRuns> updateTestRuns(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestRuns testRuns
    ) throws URISyntaxException {
        log.debug("REST request to update TestRuns : {}, {}", id, testRuns);
        if (testRuns.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testRuns.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testRunsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestRuns result = testRunsService.update(testRuns);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testRuns.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-runs/:id} : Partial updates given fields of an existing testRuns, field will ignore if it is null
     *
     * @param id the id of the testRuns to save.
     * @param testRuns the testRuns to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testRuns,
     * or with status {@code 400 (Bad Request)} if the testRuns is not valid,
     * or with status {@code 404 (Not Found)} if the testRuns is not found,
     * or with status {@code 500 (Internal Server Error)} if the testRuns couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-runs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestRuns> partialUpdateTestRuns(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestRuns testRuns
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestRuns partially : {}, {}", id, testRuns);
        if (testRuns.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testRuns.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testRunsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestRuns> result = testRunsService.partialUpdate(testRuns);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testRuns.getId().toString())
        );
    }

    /**
     * {@code GET  /test-runs} : get all the testRuns.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testRuns in body.
     */
    @GetMapping("/test-runs")
    public ResponseEntity<List<TestRuns>> getAllTestRuns(
        TestRunsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestRuns by criteria: {}", criteria);
        Page<TestRuns> page = testRunsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-runs/count} : count all the testRuns.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-runs/count")
    public ResponseEntity<Long> countTestRuns(TestRunsCriteria criteria) {
        log.debug("REST request to count TestRuns by criteria: {}", criteria);
        return ResponseEntity.ok().body(testRunsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-runs/:id} : get the "id" testRuns.
     *
     * @param id the id of the testRuns to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testRuns, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-runs/{id}")
    public ResponseEntity<TestRuns> getTestRuns(@PathVariable Long id) {
        log.debug("REST request to get TestRuns : {}", id);
        Optional<TestRuns> testRuns = testRunsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testRuns);
    }

    /**
     * {@code DELETE  /test-runs/:id} : delete the "id" testRuns.
     *
     * @param id the id of the testRuns to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-runs/{id}")
    public ResponseEntity<Void> deleteTestRuns(@PathVariable Long id) {
        log.debug("REST request to delete TestRuns : {}", id);
        testRunsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
