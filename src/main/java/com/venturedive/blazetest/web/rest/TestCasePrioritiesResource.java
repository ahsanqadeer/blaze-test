package com.venturedive.blazetest.web.rest;

import com.venturedive.blazetest.domain.TestCasePriorities;
import com.venturedive.blazetest.repository.TestCasePrioritiesRepository;
import com.venturedive.blazetest.service.TestCasePrioritiesQueryService;
import com.venturedive.blazetest.service.TestCasePrioritiesService;
import com.venturedive.blazetest.service.criteria.TestCasePrioritiesCriteria;
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
 * REST controller for managing {@link com.venturedive.blazetest.domain.TestCasePriorities}.
 */
@RestController
@RequestMapping("/api")
public class TestCasePrioritiesResource {

    private final Logger log = LoggerFactory.getLogger(TestCasePrioritiesResource.class);

    private static final String ENTITY_NAME = "testCasePriorities";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestCasePrioritiesService testCasePrioritiesService;

    private final TestCasePrioritiesRepository testCasePrioritiesRepository;

    private final TestCasePrioritiesQueryService testCasePrioritiesQueryService;

    public TestCasePrioritiesResource(
        TestCasePrioritiesService testCasePrioritiesService,
        TestCasePrioritiesRepository testCasePrioritiesRepository,
        TestCasePrioritiesQueryService testCasePrioritiesQueryService
    ) {
        this.testCasePrioritiesService = testCasePrioritiesService;
        this.testCasePrioritiesRepository = testCasePrioritiesRepository;
        this.testCasePrioritiesQueryService = testCasePrioritiesQueryService;
    }

    /**
     * {@code POST  /test-case-priorities} : Create a new testCasePriorities.
     *
     * @param testCasePriorities the testCasePriorities to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testCasePriorities, or with status {@code 400 (Bad Request)} if the testCasePriorities has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-case-priorities")
    public ResponseEntity<TestCasePriorities> createTestCasePriorities(@Valid @RequestBody TestCasePriorities testCasePriorities)
        throws URISyntaxException {
        log.debug("REST request to save TestCasePriorities : {}", testCasePriorities);
        if (testCasePriorities.getId() != null) {
            throw new BadRequestAlertException("A new testCasePriorities cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestCasePriorities result = testCasePrioritiesService.save(testCasePriorities);
        return ResponseEntity
            .created(new URI("/api/test-case-priorities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-case-priorities/:id} : Updates an existing testCasePriorities.
     *
     * @param id the id of the testCasePriorities to save.
     * @param testCasePriorities the testCasePriorities to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCasePriorities,
     * or with status {@code 400 (Bad Request)} if the testCasePriorities is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testCasePriorities couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-case-priorities/{id}")
    public ResponseEntity<TestCasePriorities> updateTestCasePriorities(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestCasePriorities testCasePriorities
    ) throws URISyntaxException {
        log.debug("REST request to update TestCasePriorities : {}, {}", id, testCasePriorities);
        if (testCasePriorities.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCasePriorities.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCasePrioritiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestCasePriorities result = testCasePrioritiesService.update(testCasePriorities);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCasePriorities.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-case-priorities/:id} : Partial updates given fields of an existing testCasePriorities, field will ignore if it is null
     *
     * @param id the id of the testCasePriorities to save.
     * @param testCasePriorities the testCasePriorities to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCasePriorities,
     * or with status {@code 400 (Bad Request)} if the testCasePriorities is not valid,
     * or with status {@code 404 (Not Found)} if the testCasePriorities is not found,
     * or with status {@code 500 (Internal Server Error)} if the testCasePriorities couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-case-priorities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestCasePriorities> partialUpdateTestCasePriorities(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestCasePriorities testCasePriorities
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestCasePriorities partially : {}, {}", id, testCasePriorities);
        if (testCasePriorities.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCasePriorities.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCasePrioritiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestCasePriorities> result = testCasePrioritiesService.partialUpdate(testCasePriorities);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCasePriorities.getId().toString())
        );
    }

    /**
     * {@code GET  /test-case-priorities} : get all the testCasePriorities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testCasePriorities in body.
     */
    @GetMapping("/test-case-priorities")
    public ResponseEntity<List<TestCasePriorities>> getAllTestCasePriorities(
        TestCasePrioritiesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestCasePriorities by criteria: {}", criteria);
        Page<TestCasePriorities> page = testCasePrioritiesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-case-priorities/count} : count all the testCasePriorities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-case-priorities/count")
    public ResponseEntity<Long> countTestCasePriorities(TestCasePrioritiesCriteria criteria) {
        log.debug("REST request to count TestCasePriorities by criteria: {}", criteria);
        return ResponseEntity.ok().body(testCasePrioritiesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-case-priorities/:id} : get the "id" testCasePriorities.
     *
     * @param id the id of the testCasePriorities to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testCasePriorities, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-case-priorities/{id}")
    public ResponseEntity<TestCasePriorities> getTestCasePriorities(@PathVariable Long id) {
        log.debug("REST request to get TestCasePriorities : {}", id);
        Optional<TestCasePriorities> testCasePriorities = testCasePrioritiesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testCasePriorities);
    }

    /**
     * {@code DELETE  /test-case-priorities/:id} : delete the "id" testCasePriorities.
     *
     * @param id the id of the testCasePriorities to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-case-priorities/{id}")
    public ResponseEntity<Void> deleteTestCasePriorities(@PathVariable Long id) {
        log.debug("REST request to delete TestCasePriorities : {}", id);
        testCasePrioritiesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
