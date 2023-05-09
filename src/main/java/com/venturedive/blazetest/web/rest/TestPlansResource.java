package com.venturedive.blazetest.web.rest;

import com.venturedive.blazetest.domain.TestPlans;
import com.venturedive.blazetest.repository.TestPlansRepository;
import com.venturedive.blazetest.service.TestPlansQueryService;
import com.venturedive.blazetest.service.TestPlansService;
import com.venturedive.blazetest.service.criteria.TestPlansCriteria;
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
 * REST controller for managing {@link com.venturedive.blazetest.domain.TestPlans}.
 */
@RestController
@RequestMapping("/api")
public class TestPlansResource {

    private final Logger log = LoggerFactory.getLogger(TestPlansResource.class);

    private static final String ENTITY_NAME = "testPlans";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestPlansService testPlansService;

    private final TestPlansRepository testPlansRepository;

    private final TestPlansQueryService testPlansQueryService;

    public TestPlansResource(
        TestPlansService testPlansService,
        TestPlansRepository testPlansRepository,
        TestPlansQueryService testPlansQueryService
    ) {
        this.testPlansService = testPlansService;
        this.testPlansRepository = testPlansRepository;
        this.testPlansQueryService = testPlansQueryService;
    }

    /**
     * {@code POST  /test-plans} : Create a new testPlans.
     *
     * @param testPlans the testPlans to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testPlans, or with status {@code 400 (Bad Request)} if the testPlans has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-plans")
    public ResponseEntity<TestPlans> createTestPlans(@Valid @RequestBody TestPlans testPlans) throws URISyntaxException {
        log.debug("REST request to save TestPlans : {}", testPlans);
        if (testPlans.getId() != null) {
            throw new BadRequestAlertException("A new testPlans cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestPlans result = testPlansService.save(testPlans);
        return ResponseEntity
            .created(new URI("/api/test-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-plans/:id} : Updates an existing testPlans.
     *
     * @param id the id of the testPlans to save.
     * @param testPlans the testPlans to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testPlans,
     * or with status {@code 400 (Bad Request)} if the testPlans is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testPlans couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-plans/{id}")
    public ResponseEntity<TestPlans> updateTestPlans(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestPlans testPlans
    ) throws URISyntaxException {
        log.debug("REST request to update TestPlans : {}, {}", id, testPlans);
        if (testPlans.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testPlans.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testPlansRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestPlans result = testPlansService.update(testPlans);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testPlans.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-plans/:id} : Partial updates given fields of an existing testPlans, field will ignore if it is null
     *
     * @param id the id of the testPlans to save.
     * @param testPlans the testPlans to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testPlans,
     * or with status {@code 400 (Bad Request)} if the testPlans is not valid,
     * or with status {@code 404 (Not Found)} if the testPlans is not found,
     * or with status {@code 500 (Internal Server Error)} if the testPlans couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-plans/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestPlans> partialUpdateTestPlans(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestPlans testPlans
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestPlans partially : {}, {}", id, testPlans);
        if (testPlans.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testPlans.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testPlansRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestPlans> result = testPlansService.partialUpdate(testPlans);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testPlans.getId().toString())
        );
    }

    /**
     * {@code GET  /test-plans} : get all the testPlans.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testPlans in body.
     */
    @GetMapping("/test-plans")
    public ResponseEntity<List<TestPlans>> getAllTestPlans(
        TestPlansCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestPlans by criteria: {}", criteria);
        Page<TestPlans> page = testPlansQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-plans/count} : count all the testPlans.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-plans/count")
    public ResponseEntity<Long> countTestPlans(TestPlansCriteria criteria) {
        log.debug("REST request to count TestPlans by criteria: {}", criteria);
        return ResponseEntity.ok().body(testPlansQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-plans/:id} : get the "id" testPlans.
     *
     * @param id the id of the testPlans to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testPlans, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-plans/{id}")
    public ResponseEntity<TestPlans> getTestPlans(@PathVariable Long id) {
        log.debug("REST request to get TestPlans : {}", id);
        Optional<TestPlans> testPlans = testPlansService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testPlans);
    }

    /**
     * {@code DELETE  /test-plans/:id} : delete the "id" testPlans.
     *
     * @param id the id of the testPlans to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-plans/{id}")
    public ResponseEntity<Void> deleteTestPlans(@PathVariable Long id) {
        log.debug("REST request to delete TestPlans : {}", id);
        testPlansService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
