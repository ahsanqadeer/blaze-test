package com.venturedive.blazetest.web.rest;

import com.venturedive.blazetest.domain.TestRunDetailAttachments;
import com.venturedive.blazetest.repository.TestRunDetailAttachmentsRepository;
import com.venturedive.blazetest.service.TestRunDetailAttachmentsQueryService;
import com.venturedive.blazetest.service.TestRunDetailAttachmentsService;
import com.venturedive.blazetest.service.criteria.TestRunDetailAttachmentsCriteria;
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
 * REST controller for managing {@link com.venturedive.blazetest.domain.TestRunDetailAttachments}.
 */
@RestController
@RequestMapping("/api")
public class TestRunDetailAttachmentsResource {

    private final Logger log = LoggerFactory.getLogger(TestRunDetailAttachmentsResource.class);

    private static final String ENTITY_NAME = "testRunDetailAttachments";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestRunDetailAttachmentsService testRunDetailAttachmentsService;

    private final TestRunDetailAttachmentsRepository testRunDetailAttachmentsRepository;

    private final TestRunDetailAttachmentsQueryService testRunDetailAttachmentsQueryService;

    public TestRunDetailAttachmentsResource(
        TestRunDetailAttachmentsService testRunDetailAttachmentsService,
        TestRunDetailAttachmentsRepository testRunDetailAttachmentsRepository,
        TestRunDetailAttachmentsQueryService testRunDetailAttachmentsQueryService
    ) {
        this.testRunDetailAttachmentsService = testRunDetailAttachmentsService;
        this.testRunDetailAttachmentsRepository = testRunDetailAttachmentsRepository;
        this.testRunDetailAttachmentsQueryService = testRunDetailAttachmentsQueryService;
    }

    /**
     * {@code POST  /test-run-detail-attachments} : Create a new testRunDetailAttachments.
     *
     * @param testRunDetailAttachments the testRunDetailAttachments to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testRunDetailAttachments, or with status {@code 400 (Bad Request)} if the testRunDetailAttachments has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-run-detail-attachments")
    public ResponseEntity<TestRunDetailAttachments> createTestRunDetailAttachments(
        @Valid @RequestBody TestRunDetailAttachments testRunDetailAttachments
    ) throws URISyntaxException {
        log.debug("REST request to save TestRunDetailAttachments : {}", testRunDetailAttachments);
        if (testRunDetailAttachments.getId() != null) {
            throw new BadRequestAlertException("A new testRunDetailAttachments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestRunDetailAttachments result = testRunDetailAttachmentsService.save(testRunDetailAttachments);
        return ResponseEntity
            .created(new URI("/api/test-run-detail-attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-run-detail-attachments/:id} : Updates an existing testRunDetailAttachments.
     *
     * @param id the id of the testRunDetailAttachments to save.
     * @param testRunDetailAttachments the testRunDetailAttachments to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testRunDetailAttachments,
     * or with status {@code 400 (Bad Request)} if the testRunDetailAttachments is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testRunDetailAttachments couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-run-detail-attachments/{id}")
    public ResponseEntity<TestRunDetailAttachments> updateTestRunDetailAttachments(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestRunDetailAttachments testRunDetailAttachments
    ) throws URISyntaxException {
        log.debug("REST request to update TestRunDetailAttachments : {}, {}", id, testRunDetailAttachments);
        if (testRunDetailAttachments.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testRunDetailAttachments.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testRunDetailAttachmentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestRunDetailAttachments result = testRunDetailAttachmentsService.update(testRunDetailAttachments);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testRunDetailAttachments.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-run-detail-attachments/:id} : Partial updates given fields of an existing testRunDetailAttachments, field will ignore if it is null
     *
     * @param id the id of the testRunDetailAttachments to save.
     * @param testRunDetailAttachments the testRunDetailAttachments to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testRunDetailAttachments,
     * or with status {@code 400 (Bad Request)} if the testRunDetailAttachments is not valid,
     * or with status {@code 404 (Not Found)} if the testRunDetailAttachments is not found,
     * or with status {@code 500 (Internal Server Error)} if the testRunDetailAttachments couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-run-detail-attachments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestRunDetailAttachments> partialUpdateTestRunDetailAttachments(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestRunDetailAttachments testRunDetailAttachments
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestRunDetailAttachments partially : {}, {}", id, testRunDetailAttachments);
        if (testRunDetailAttachments.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testRunDetailAttachments.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testRunDetailAttachmentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestRunDetailAttachments> result = testRunDetailAttachmentsService.partialUpdate(testRunDetailAttachments);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testRunDetailAttachments.getId().toString())
        );
    }

    /**
     * {@code GET  /test-run-detail-attachments} : get all the testRunDetailAttachments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testRunDetailAttachments in body.
     */
    @GetMapping("/test-run-detail-attachments")
    public ResponseEntity<List<TestRunDetailAttachments>> getAllTestRunDetailAttachments(
        TestRunDetailAttachmentsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestRunDetailAttachments by criteria: {}", criteria);
        Page<TestRunDetailAttachments> page = testRunDetailAttachmentsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-run-detail-attachments/count} : count all the testRunDetailAttachments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-run-detail-attachments/count")
    public ResponseEntity<Long> countTestRunDetailAttachments(TestRunDetailAttachmentsCriteria criteria) {
        log.debug("REST request to count TestRunDetailAttachments by criteria: {}", criteria);
        return ResponseEntity.ok().body(testRunDetailAttachmentsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-run-detail-attachments/:id} : get the "id" testRunDetailAttachments.
     *
     * @param id the id of the testRunDetailAttachments to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testRunDetailAttachments, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-run-detail-attachments/{id}")
    public ResponseEntity<TestRunDetailAttachments> getTestRunDetailAttachments(@PathVariable Long id) {
        log.debug("REST request to get TestRunDetailAttachments : {}", id);
        Optional<TestRunDetailAttachments> testRunDetailAttachments = testRunDetailAttachmentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testRunDetailAttachments);
    }

    /**
     * {@code DELETE  /test-run-detail-attachments/:id} : delete the "id" testRunDetailAttachments.
     *
     * @param id the id of the testRunDetailAttachments to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-run-detail-attachments/{id}")
    public ResponseEntity<Void> deleteTestRunDetailAttachments(@PathVariable Long id) {
        log.debug("REST request to delete TestRunDetailAttachments : {}", id);
        testRunDetailAttachmentsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
