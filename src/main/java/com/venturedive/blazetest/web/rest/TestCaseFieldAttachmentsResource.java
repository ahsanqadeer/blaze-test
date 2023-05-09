package com.venturedive.blazetest.web.rest;

import com.venturedive.blazetest.domain.TestCaseFieldAttachments;
import com.venturedive.blazetest.repository.TestCaseFieldAttachmentsRepository;
import com.venturedive.blazetest.service.TestCaseFieldAttachmentsQueryService;
import com.venturedive.blazetest.service.TestCaseFieldAttachmentsService;
import com.venturedive.blazetest.service.criteria.TestCaseFieldAttachmentsCriteria;
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
 * REST controller for managing {@link com.venturedive.blazetest.domain.TestCaseFieldAttachments}.
 */
@RestController
@RequestMapping("/api")
public class TestCaseFieldAttachmentsResource {

    private final Logger log = LoggerFactory.getLogger(TestCaseFieldAttachmentsResource.class);

    private static final String ENTITY_NAME = "testCaseFieldAttachments";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestCaseFieldAttachmentsService testCaseFieldAttachmentsService;

    private final TestCaseFieldAttachmentsRepository testCaseFieldAttachmentsRepository;

    private final TestCaseFieldAttachmentsQueryService testCaseFieldAttachmentsQueryService;

    public TestCaseFieldAttachmentsResource(
        TestCaseFieldAttachmentsService testCaseFieldAttachmentsService,
        TestCaseFieldAttachmentsRepository testCaseFieldAttachmentsRepository,
        TestCaseFieldAttachmentsQueryService testCaseFieldAttachmentsQueryService
    ) {
        this.testCaseFieldAttachmentsService = testCaseFieldAttachmentsService;
        this.testCaseFieldAttachmentsRepository = testCaseFieldAttachmentsRepository;
        this.testCaseFieldAttachmentsQueryService = testCaseFieldAttachmentsQueryService;
    }

    /**
     * {@code POST  /test-case-field-attachments} : Create a new testCaseFieldAttachments.
     *
     * @param testCaseFieldAttachments the testCaseFieldAttachments to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testCaseFieldAttachments, or with status {@code 400 (Bad Request)} if the testCaseFieldAttachments has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-case-field-attachments")
    public ResponseEntity<TestCaseFieldAttachments> createTestCaseFieldAttachments(
        @Valid @RequestBody TestCaseFieldAttachments testCaseFieldAttachments
    ) throws URISyntaxException {
        log.debug("REST request to save TestCaseFieldAttachments : {}", testCaseFieldAttachments);
        if (testCaseFieldAttachments.getId() != null) {
            throw new BadRequestAlertException("A new testCaseFieldAttachments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestCaseFieldAttachments result = testCaseFieldAttachmentsService.save(testCaseFieldAttachments);
        return ResponseEntity
            .created(new URI("/api/test-case-field-attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-case-field-attachments/:id} : Updates an existing testCaseFieldAttachments.
     *
     * @param id the id of the testCaseFieldAttachments to save.
     * @param testCaseFieldAttachments the testCaseFieldAttachments to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCaseFieldAttachments,
     * or with status {@code 400 (Bad Request)} if the testCaseFieldAttachments is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testCaseFieldAttachments couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-case-field-attachments/{id}")
    public ResponseEntity<TestCaseFieldAttachments> updateTestCaseFieldAttachments(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestCaseFieldAttachments testCaseFieldAttachments
    ) throws URISyntaxException {
        log.debug("REST request to update TestCaseFieldAttachments : {}, {}", id, testCaseFieldAttachments);
        if (testCaseFieldAttachments.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCaseFieldAttachments.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCaseFieldAttachmentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestCaseFieldAttachments result = testCaseFieldAttachmentsService.update(testCaseFieldAttachments);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCaseFieldAttachments.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-case-field-attachments/:id} : Partial updates given fields of an existing testCaseFieldAttachments, field will ignore if it is null
     *
     * @param id the id of the testCaseFieldAttachments to save.
     * @param testCaseFieldAttachments the testCaseFieldAttachments to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCaseFieldAttachments,
     * or with status {@code 400 (Bad Request)} if the testCaseFieldAttachments is not valid,
     * or with status {@code 404 (Not Found)} if the testCaseFieldAttachments is not found,
     * or with status {@code 500 (Internal Server Error)} if the testCaseFieldAttachments couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-case-field-attachments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestCaseFieldAttachments> partialUpdateTestCaseFieldAttachments(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestCaseFieldAttachments testCaseFieldAttachments
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestCaseFieldAttachments partially : {}, {}", id, testCaseFieldAttachments);
        if (testCaseFieldAttachments.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCaseFieldAttachments.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCaseFieldAttachmentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestCaseFieldAttachments> result = testCaseFieldAttachmentsService.partialUpdate(testCaseFieldAttachments);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCaseFieldAttachments.getId().toString())
        );
    }

    /**
     * {@code GET  /test-case-field-attachments} : get all the testCaseFieldAttachments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testCaseFieldAttachments in body.
     */
    @GetMapping("/test-case-field-attachments")
    public ResponseEntity<List<TestCaseFieldAttachments>> getAllTestCaseFieldAttachments(
        TestCaseFieldAttachmentsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestCaseFieldAttachments by criteria: {}", criteria);
        Page<TestCaseFieldAttachments> page = testCaseFieldAttachmentsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-case-field-attachments/count} : count all the testCaseFieldAttachments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-case-field-attachments/count")
    public ResponseEntity<Long> countTestCaseFieldAttachments(TestCaseFieldAttachmentsCriteria criteria) {
        log.debug("REST request to count TestCaseFieldAttachments by criteria: {}", criteria);
        return ResponseEntity.ok().body(testCaseFieldAttachmentsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-case-field-attachments/:id} : get the "id" testCaseFieldAttachments.
     *
     * @param id the id of the testCaseFieldAttachments to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testCaseFieldAttachments, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-case-field-attachments/{id}")
    public ResponseEntity<TestCaseFieldAttachments> getTestCaseFieldAttachments(@PathVariable Long id) {
        log.debug("REST request to get TestCaseFieldAttachments : {}", id);
        Optional<TestCaseFieldAttachments> testCaseFieldAttachments = testCaseFieldAttachmentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testCaseFieldAttachments);
    }

    /**
     * {@code DELETE  /test-case-field-attachments/:id} : delete the "id" testCaseFieldAttachments.
     *
     * @param id the id of the testCaseFieldAttachments to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-case-field-attachments/{id}")
    public ResponseEntity<Void> deleteTestCaseFieldAttachments(@PathVariable Long id) {
        log.debug("REST request to delete TestCaseFieldAttachments : {}", id);
        testCaseFieldAttachmentsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
