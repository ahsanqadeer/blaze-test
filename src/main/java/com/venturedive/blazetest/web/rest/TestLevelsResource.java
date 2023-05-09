package com.venturedive.blazetest.web.rest;

import com.venturedive.blazetest.domain.TestLevels;
import com.venturedive.blazetest.repository.TestLevelsRepository;
import com.venturedive.blazetest.service.TestLevelsQueryService;
import com.venturedive.blazetest.service.TestLevelsService;
import com.venturedive.blazetest.service.criteria.TestLevelsCriteria;
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
 * REST controller for managing {@link com.venturedive.blazetest.domain.TestLevels}.
 */
@RestController
@RequestMapping("/api")
public class TestLevelsResource {

    private final Logger log = LoggerFactory.getLogger(TestLevelsResource.class);

    private static final String ENTITY_NAME = "testLevels";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestLevelsService testLevelsService;

    private final TestLevelsRepository testLevelsRepository;

    private final TestLevelsQueryService testLevelsQueryService;

    public TestLevelsResource(
        TestLevelsService testLevelsService,
        TestLevelsRepository testLevelsRepository,
        TestLevelsQueryService testLevelsQueryService
    ) {
        this.testLevelsService = testLevelsService;
        this.testLevelsRepository = testLevelsRepository;
        this.testLevelsQueryService = testLevelsQueryService;
    }

    /**
     * {@code POST  /test-levels} : Create a new testLevels.
     *
     * @param testLevels the testLevels to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testLevels, or with status {@code 400 (Bad Request)} if the testLevels has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-levels")
    public ResponseEntity<TestLevels> createTestLevels(@Valid @RequestBody TestLevels testLevels) throws URISyntaxException {
        log.debug("REST request to save TestLevels : {}", testLevels);
        if (testLevels.getId() != null) {
            throw new BadRequestAlertException("A new testLevels cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestLevels result = testLevelsService.save(testLevels);
        return ResponseEntity
            .created(new URI("/api/test-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-levels/:id} : Updates an existing testLevels.
     *
     * @param id the id of the testLevels to save.
     * @param testLevels the testLevels to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testLevels,
     * or with status {@code 400 (Bad Request)} if the testLevels is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testLevels couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-levels/{id}")
    public ResponseEntity<TestLevels> updateTestLevels(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestLevels testLevels
    ) throws URISyntaxException {
        log.debug("REST request to update TestLevels : {}, {}", id, testLevels);
        if (testLevels.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testLevels.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testLevelsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestLevels result = testLevelsService.update(testLevels);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testLevels.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-levels/:id} : Partial updates given fields of an existing testLevels, field will ignore if it is null
     *
     * @param id the id of the testLevels to save.
     * @param testLevels the testLevels to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testLevels,
     * or with status {@code 400 (Bad Request)} if the testLevels is not valid,
     * or with status {@code 404 (Not Found)} if the testLevels is not found,
     * or with status {@code 500 (Internal Server Error)} if the testLevels couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-levels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestLevels> partialUpdateTestLevels(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestLevels testLevels
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestLevels partially : {}, {}", id, testLevels);
        if (testLevels.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testLevels.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testLevelsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestLevels> result = testLevelsService.partialUpdate(testLevels);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testLevels.getId().toString())
        );
    }

    /**
     * {@code GET  /test-levels} : get all the testLevels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testLevels in body.
     */
    @GetMapping("/test-levels")
    public ResponseEntity<List<TestLevels>> getAllTestLevels(
        TestLevelsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestLevels by criteria: {}", criteria);
        Page<TestLevels> page = testLevelsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-levels/count} : count all the testLevels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-levels/count")
    public ResponseEntity<Long> countTestLevels(TestLevelsCriteria criteria) {
        log.debug("REST request to count TestLevels by criteria: {}", criteria);
        return ResponseEntity.ok().body(testLevelsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-levels/:id} : get the "id" testLevels.
     *
     * @param id the id of the testLevels to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testLevels, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-levels/{id}")
    public ResponseEntity<TestLevels> getTestLevels(@PathVariable Long id) {
        log.debug("REST request to get TestLevels : {}", id);
        Optional<TestLevels> testLevels = testLevelsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testLevels);
    }

    /**
     * {@code DELETE  /test-levels/:id} : delete the "id" testLevels.
     *
     * @param id the id of the testLevels to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-levels/{id}")
    public ResponseEntity<Void> deleteTestLevels(@PathVariable Long id) {
        log.debug("REST request to delete TestLevels : {}", id);
        testLevelsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
