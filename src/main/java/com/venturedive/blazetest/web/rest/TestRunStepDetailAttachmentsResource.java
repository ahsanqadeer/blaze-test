package com.venturedive.blazetest.web.rest;

import com.venturedive.blazetest.domain.TestRunStepDetailAttachments;
import com.venturedive.blazetest.repository.TestRunStepDetailAttachmentsRepository;
import com.venturedive.blazetest.service.TestRunStepDetailAttachmentsQueryService;
import com.venturedive.blazetest.service.TestRunStepDetailAttachmentsService;
import com.venturedive.blazetest.service.criteria.TestRunStepDetailAttachmentsCriteria;
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
 * REST controller for managing {@link com.venturedive.blazetest.domain.TestRunStepDetailAttachments}.
 */
@RestController
@RequestMapping("/api")
public class TestRunStepDetailAttachmentsResource {

    private final Logger log = LoggerFactory.getLogger(TestRunStepDetailAttachmentsResource.class);

    private static final String ENTITY_NAME = "testRunStepDetailAttachments";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestRunStepDetailAttachmentsService testRunStepDetailAttachmentsService;

    private final TestRunStepDetailAttachmentsRepository testRunStepDetailAttachmentsRepository;

    private final TestRunStepDetailAttachmentsQueryService testRunStepDetailAttachmentsQueryService;

    public TestRunStepDetailAttachmentsResource(
        TestRunStepDetailAttachmentsService testRunStepDetailAttachmentsService,
        TestRunStepDetailAttachmentsRepository testRunStepDetailAttachmentsRepository,
        TestRunStepDetailAttachmentsQueryService testRunStepDetailAttachmentsQueryService
    ) {
        this.testRunStepDetailAttachmentsService = testRunStepDetailAttachmentsService;
        this.testRunStepDetailAttachmentsRepository = testRunStepDetailAttachmentsRepository;
        this.testRunStepDetailAttachmentsQueryService = testRunStepDetailAttachmentsQueryService;
    }

    /**
     * {@code POST  /test-run-step-detail-attachments} : Create a new testRunStepDetailAttachments.
     *
     * @param testRunStepDetailAttachments the testRunStepDetailAttachments to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testRunStepDetailAttachments, or with status {@code 400 (Bad Request)} if the testRunStepDetailAttachments has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-run-step-detail-attachments")
    public ResponseEntity<TestRunStepDetailAttachments> createTestRunStepDetailAttachments(
        @Valid @RequestBody TestRunStepDetailAttachments testRunStepDetailAttachments
    ) throws URISyntaxException {
        log.debug("REST request to save TestRunStepDetailAttachments : {}", testRunStepDetailAttachments);
        if (testRunStepDetailAttachments.getId() != null) {
            throw new BadRequestAlertException("A new testRunStepDetailAttachments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestRunStepDetailAttachments result = testRunStepDetailAttachmentsService.save(testRunStepDetailAttachments);
        return ResponseEntity
            .created(new URI("/api/test-run-step-detail-attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-run-step-detail-attachments/:id} : Updates an existing testRunStepDetailAttachments.
     *
     * @param id the id of the testRunStepDetailAttachments to save.
     * @param testRunStepDetailAttachments the testRunStepDetailAttachments to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testRunStepDetailAttachments,
     * or with status {@code 400 (Bad Request)} if the testRunStepDetailAttachments is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testRunStepDetailAttachments couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-run-step-detail-attachments/{id}")
    public ResponseEntity<TestRunStepDetailAttachments> updateTestRunStepDetailAttachments(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestRunStepDetailAttachments testRunStepDetailAttachments
    ) throws URISyntaxException {
        log.debug("REST request to update TestRunStepDetailAttachments : {}, {}", id, testRunStepDetailAttachments);
        if (testRunStepDetailAttachments.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testRunStepDetailAttachments.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testRunStepDetailAttachmentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestRunStepDetailAttachments result = testRunStepDetailAttachmentsService.update(testRunStepDetailAttachments);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testRunStepDetailAttachments.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /test-run-step-detail-attachments/:id} : Partial updates given fields of an existing testRunStepDetailAttachments, field will ignore if it is null
     *
     * @param id the id of the testRunStepDetailAttachments to save.
     * @param testRunStepDetailAttachments the testRunStepDetailAttachments to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testRunStepDetailAttachments,
     * or with status {@code 400 (Bad Request)} if the testRunStepDetailAttachments is not valid,
     * or with status {@code 404 (Not Found)} if the testRunStepDetailAttachments is not found,
     * or with status {@code 500 (Internal Server Error)} if the testRunStepDetailAttachments couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-run-step-detail-attachments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestRunStepDetailAttachments> partialUpdateTestRunStepDetailAttachments(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestRunStepDetailAttachments testRunStepDetailAttachments
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestRunStepDetailAttachments partially : {}, {}", id, testRunStepDetailAttachments);
        if (testRunStepDetailAttachments.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testRunStepDetailAttachments.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testRunStepDetailAttachmentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestRunStepDetailAttachments> result = testRunStepDetailAttachmentsService.partialUpdate(testRunStepDetailAttachments);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testRunStepDetailAttachments.getId().toString())
        );
    }

    /**
     * {@code GET  /test-run-step-detail-attachments} : get all the testRunStepDetailAttachments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testRunStepDetailAttachments in body.
     */
    @GetMapping("/test-run-step-detail-attachments")
    public ResponseEntity<List<TestRunStepDetailAttachments>> getAllTestRunStepDetailAttachments(
        TestRunStepDetailAttachmentsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestRunStepDetailAttachments by criteria: {}", criteria);
        Page<TestRunStepDetailAttachments> page = testRunStepDetailAttachmentsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-run-step-detail-attachments/count} : count all the testRunStepDetailAttachments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-run-step-detail-attachments/count")
    public ResponseEntity<Long> countTestRunStepDetailAttachments(TestRunStepDetailAttachmentsCriteria criteria) {
        log.debug("REST request to count TestRunStepDetailAttachments by criteria: {}", criteria);
        return ResponseEntity.ok().body(testRunStepDetailAttachmentsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-run-step-detail-attachments/:id} : get the "id" testRunStepDetailAttachments.
     *
     * @param id the id of the testRunStepDetailAttachments to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testRunStepDetailAttachments, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-run-step-detail-attachments/{id}")
    public ResponseEntity<TestRunStepDetailAttachments> getTestRunStepDetailAttachments(@PathVariable Long id) {
        log.debug("REST request to get TestRunStepDetailAttachments : {}", id);
        Optional<TestRunStepDetailAttachments> testRunStepDetailAttachments = testRunStepDetailAttachmentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testRunStepDetailAttachments);
    }

    /**
     * {@code DELETE  /test-run-step-detail-attachments/:id} : delete the "id" testRunStepDetailAttachments.
     *
     * @param id the id of the testRunStepDetailAttachments to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-run-step-detail-attachments/{id}")
    public ResponseEntity<Void> deleteTestRunStepDetailAttachments(@PathVariable Long id) {
        log.debug("REST request to delete TestRunStepDetailAttachments : {}", id);
        testRunStepDetailAttachmentsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
