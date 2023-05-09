package com.venturedive.blazetest.web.rest;

import com.venturedive.blazetest.domain.TestSuites;
import com.venturedive.blazetest.repository.TestSuitesRepository;
import com.venturedive.blazetest.service.TestSuitesQueryService;
import com.venturedive.blazetest.service.TestSuitesService;
import com.venturedive.blazetest.service.criteria.TestSuitesCriteria;
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
 * REST controller for managing {@link com.venturedive.blazetest.domain.TestSuites}.
 */
@RestController
@RequestMapping("/api")
public class TestSuitesResource {

    private final Logger log = LoggerFactory.getLogger(TestSuitesResource.class);

    private static final String ENTITY_NAME = "testSuites";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestSuitesService testSuitesService;

    private final TestSuitesRepository testSuitesRepository;

    private final TestSuitesQueryService testSuitesQueryService;

    public TestSuitesResource(
        TestSuitesService testSuitesService,
        TestSuitesRepository testSuitesRepository,
        TestSuitesQueryService testSuitesQueryService
    ) {
        this.testSuitesService = testSuitesService;
        this.testSuitesRepository = testSuitesRepository;
        this.testSuitesQueryService = testSuitesQueryService;
    }

    /**
     * {@code POST  /test-suites} : Create a new testSuites.
     *
     * @param testSuites the testSuites to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testSuites, or with status {@code 400 (Bad Request)} if the testSuites has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-suites")
    public ResponseEntity<TestSuites> createTestSuites(@Valid @RequestBody TestSuites testSuites) throws URISyntaxException {
        log.debug("REST request to save TestSuites : {}", testSuites);
        if (testSuites.getId() != null) {
            throw new BadRequestAlertException("A new testSuites cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestSuites result = testSuitesService.save(testSuites);
        return ResponseEntity
            .created(new URI("/api/test-suites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-suites/:id} : Updates an existing testSuites.
     *
     * @param id the id of the testSuites to save.
     * @param testSuites the testSuites to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testSuites,
     * or with status {@code 400 (Bad Request)} if the testSuites is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testSuites couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-suites/{id}")
    public ResponseEntity<TestSuites> updateTestSuites(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestSuites testSuites
    ) throws URISyntaxException {
        log.debug("REST request to update TestSuites : {}, {}", id, testSuites);
        if (testSuites.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testSuites.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testSuitesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestSuites result = testSuitesService.update(testSuites);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testSuites.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-suites/:id} : Partial updates given fields of an existing testSuites, field will ignore if it is null
     *
     * @param id the id of the testSuites to save.
     * @param testSuites the testSuites to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testSuites,
     * or with status {@code 400 (Bad Request)} if the testSuites is not valid,
     * or with status {@code 404 (Not Found)} if the testSuites is not found,
     * or with status {@code 500 (Internal Server Error)} if the testSuites couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-suites/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestSuites> partialUpdateTestSuites(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestSuites testSuites
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestSuites partially : {}, {}", id, testSuites);
        if (testSuites.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testSuites.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testSuitesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestSuites> result = testSuitesService.partialUpdate(testSuites);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testSuites.getId().toString())
        );
    }

    /**
     * {@code GET  /test-suites} : get all the testSuites.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testSuites in body.
     */
    @GetMapping("/test-suites")
    public ResponseEntity<List<TestSuites>> getAllTestSuites(
        TestSuitesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestSuites by criteria: {}", criteria);
        Page<TestSuites> page = testSuitesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-suites/count} : count all the testSuites.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-suites/count")
    public ResponseEntity<Long> countTestSuites(TestSuitesCriteria criteria) {
        log.debug("REST request to count TestSuites by criteria: {}", criteria);
        return ResponseEntity.ok().body(testSuitesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-suites/:id} : get the "id" testSuites.
     *
     * @param id the id of the testSuites to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testSuites, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-suites/{id}")
    public ResponseEntity<TestSuites> getTestSuites(@PathVariable Long id) {
        log.debug("REST request to get TestSuites : {}", id);
        Optional<TestSuites> testSuites = testSuitesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testSuites);
    }

    /**
     * {@code DELETE  /test-suites/:id} : delete the "id" testSuites.
     *
     * @param id the id of the testSuites to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-suites/{id}")
    public ResponseEntity<Void> deleteTestSuites(@PathVariable Long id) {
        log.debug("REST request to delete TestSuites : {}", id);
        testSuitesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
