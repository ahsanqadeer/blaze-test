package com.venturedive.blazetest.web.rest;

import com.venturedive.blazetest.domain.TestCaseFields;
import com.venturedive.blazetest.repository.TestCaseFieldsRepository;
import com.venturedive.blazetest.service.TestCaseFieldsQueryService;
import com.venturedive.blazetest.service.TestCaseFieldsService;
import com.venturedive.blazetest.service.criteria.TestCaseFieldsCriteria;
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
 * REST controller for managing {@link com.venturedive.blazetest.domain.TestCaseFields}.
 */
@RestController
@RequestMapping("/api")
public class TestCaseFieldsResource {

    private final Logger log = LoggerFactory.getLogger(TestCaseFieldsResource.class);

    private static final String ENTITY_NAME = "testCaseFields";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestCaseFieldsService testCaseFieldsService;

    private final TestCaseFieldsRepository testCaseFieldsRepository;

    private final TestCaseFieldsQueryService testCaseFieldsQueryService;

    public TestCaseFieldsResource(
        TestCaseFieldsService testCaseFieldsService,
        TestCaseFieldsRepository testCaseFieldsRepository,
        TestCaseFieldsQueryService testCaseFieldsQueryService
    ) {
        this.testCaseFieldsService = testCaseFieldsService;
        this.testCaseFieldsRepository = testCaseFieldsRepository;
        this.testCaseFieldsQueryService = testCaseFieldsQueryService;
    }

    /**
     * {@code POST  /test-case-fields} : Create a new testCaseFields.
     *
     * @param testCaseFields the testCaseFields to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testCaseFields, or with status {@code 400 (Bad Request)} if the testCaseFields has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-case-fields")
    public ResponseEntity<TestCaseFields> createTestCaseFields(@Valid @RequestBody TestCaseFields testCaseFields)
        throws URISyntaxException {
        log.debug("REST request to save TestCaseFields : {}", testCaseFields);
        if (testCaseFields.getId() != null) {
            throw new BadRequestAlertException("A new testCaseFields cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestCaseFields result = testCaseFieldsService.save(testCaseFields);
        return ResponseEntity
            .created(new URI("/api/test-case-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-case-fields/:id} : Updates an existing testCaseFields.
     *
     * @param id the id of the testCaseFields to save.
     * @param testCaseFields the testCaseFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCaseFields,
     * or with status {@code 400 (Bad Request)} if the testCaseFields is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testCaseFields couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-case-fields/{id}")
    public ResponseEntity<TestCaseFields> updateTestCaseFields(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestCaseFields testCaseFields
    ) throws URISyntaxException {
        log.debug("REST request to update TestCaseFields : {}, {}", id, testCaseFields);
        if (testCaseFields.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCaseFields.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCaseFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestCaseFields result = testCaseFieldsService.update(testCaseFields);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCaseFields.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-case-fields/:id} : Partial updates given fields of an existing testCaseFields, field will ignore if it is null
     *
     * @param id the id of the testCaseFields to save.
     * @param testCaseFields the testCaseFields to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCaseFields,
     * or with status {@code 400 (Bad Request)} if the testCaseFields is not valid,
     * or with status {@code 404 (Not Found)} if the testCaseFields is not found,
     * or with status {@code 500 (Internal Server Error)} if the testCaseFields couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-case-fields/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestCaseFields> partialUpdateTestCaseFields(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestCaseFields testCaseFields
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestCaseFields partially : {}, {}", id, testCaseFields);
        if (testCaseFields.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCaseFields.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCaseFieldsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestCaseFields> result = testCaseFieldsService.partialUpdate(testCaseFields);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCaseFields.getId().toString())
        );
    }

    /**
     * {@code GET  /test-case-fields} : get all the testCaseFields.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testCaseFields in body.
     */
    @GetMapping("/test-case-fields")
    public ResponseEntity<List<TestCaseFields>> getAllTestCaseFields(
        TestCaseFieldsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestCaseFields by criteria: {}", criteria);
        Page<TestCaseFields> page = testCaseFieldsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-case-fields/count} : count all the testCaseFields.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-case-fields/count")
    public ResponseEntity<Long> countTestCaseFields(TestCaseFieldsCriteria criteria) {
        log.debug("REST request to count TestCaseFields by criteria: {}", criteria);
        return ResponseEntity.ok().body(testCaseFieldsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-case-fields/:id} : get the "id" testCaseFields.
     *
     * @param id the id of the testCaseFields to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testCaseFields, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-case-fields/{id}")
    public ResponseEntity<TestCaseFields> getTestCaseFields(@PathVariable Long id) {
        log.debug("REST request to get TestCaseFields : {}", id);
        Optional<TestCaseFields> testCaseFields = testCaseFieldsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testCaseFields);
    }

    /**
     * {@code DELETE  /test-case-fields/:id} : delete the "id" testCaseFields.
     *
     * @param id the id of the testCaseFields to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-case-fields/{id}")
    public ResponseEntity<Void> deleteTestCaseFields(@PathVariable Long id) {
        log.debug("REST request to delete TestCaseFields : {}", id);
        testCaseFieldsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
