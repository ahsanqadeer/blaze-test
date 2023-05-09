package com.venturedive.blazetest.web.rest;

import com.venturedive.blazetest.domain.TestCases;
import com.venturedive.blazetest.repository.TestCasesRepository;
import com.venturedive.blazetest.service.TestCasesQueryService;
import com.venturedive.blazetest.service.TestCasesService;
import com.venturedive.blazetest.service.criteria.TestCasesCriteria;
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
 * REST controller for managing {@link com.venturedive.blazetest.domain.TestCases}.
 */
@RestController
@RequestMapping("/api")
public class TestCasesResource {

    private final Logger log = LoggerFactory.getLogger(TestCasesResource.class);

    private static final String ENTITY_NAME = "testCases";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestCasesService testCasesService;

    private final TestCasesRepository testCasesRepository;

    private final TestCasesQueryService testCasesQueryService;

    public TestCasesResource(
        TestCasesService testCasesService,
        TestCasesRepository testCasesRepository,
        TestCasesQueryService testCasesQueryService
    ) {
        this.testCasesService = testCasesService;
        this.testCasesRepository = testCasesRepository;
        this.testCasesQueryService = testCasesQueryService;
    }

    /**
     * {@code POST  /test-cases} : Create a new testCases.
     *
     * @param testCases the testCases to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testCases, or with status {@code 400 (Bad Request)} if the testCases has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-cases")
    public ResponseEntity<TestCases> createTestCases(@Valid @RequestBody TestCases testCases) throws URISyntaxException {
        log.debug("REST request to save TestCases : {}", testCases);
        if (testCases.getId() != null) {
            throw new BadRequestAlertException("A new testCases cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestCases result = testCasesService.save(testCases);
        return ResponseEntity
            .created(new URI("/api/test-cases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-cases/:id} : Updates an existing testCases.
     *
     * @param id the id of the testCases to save.
     * @param testCases the testCases to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCases,
     * or with status {@code 400 (Bad Request)} if the testCases is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testCases couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-cases/{id}")
    public ResponseEntity<TestCases> updateTestCases(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestCases testCases
    ) throws URISyntaxException {
        log.debug("REST request to update TestCases : {}, {}", id, testCases);
        if (testCases.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCases.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCasesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestCases result = testCasesService.update(testCases);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCases.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-cases/:id} : Partial updates given fields of an existing testCases, field will ignore if it is null
     *
     * @param id the id of the testCases to save.
     * @param testCases the testCases to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCases,
     * or with status {@code 400 (Bad Request)} if the testCases is not valid,
     * or with status {@code 404 (Not Found)} if the testCases is not found,
     * or with status {@code 500 (Internal Server Error)} if the testCases couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-cases/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestCases> partialUpdateTestCases(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestCases testCases
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestCases partially : {}, {}", id, testCases);
        if (testCases.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCases.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCasesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestCases> result = testCasesService.partialUpdate(testCases);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCases.getId().toString())
        );
    }

    /**
     * {@code GET  /test-cases} : get all the testCases.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testCases in body.
     */
    @GetMapping("/test-cases")
    public ResponseEntity<List<TestCases>> getAllTestCases(
        TestCasesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestCases by criteria: {}", criteria);
        Page<TestCases> page = testCasesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-cases/count} : count all the testCases.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-cases/count")
    public ResponseEntity<Long> countTestCases(TestCasesCriteria criteria) {
        log.debug("REST request to count TestCases by criteria: {}", criteria);
        return ResponseEntity.ok().body(testCasesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-cases/:id} : get the "id" testCases.
     *
     * @param id the id of the testCases to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testCases, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-cases/{id}")
    public ResponseEntity<TestCases> getTestCases(@PathVariable Long id) {
        log.debug("REST request to get TestCases : {}", id);
        Optional<TestCases> testCases = testCasesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testCases);
    }

    /**
     * {@code DELETE  /test-cases/:id} : delete the "id" testCases.
     *
     * @param id the id of the testCases to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-cases/{id}")
    public ResponseEntity<Void> deleteTestCases(@PathVariable Long id) {
        log.debug("REST request to delete TestCases : {}", id);
        testCasesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
