package com.venturedive.blazetest.web.rest;

import com.venturedive.blazetest.domain.TestCaseAttachments;
import com.venturedive.blazetest.repository.TestCaseAttachmentsRepository;
import com.venturedive.blazetest.service.TestCaseAttachmentsQueryService;
import com.venturedive.blazetest.service.TestCaseAttachmentsService;
import com.venturedive.blazetest.service.criteria.TestCaseAttachmentsCriteria;
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
 * REST controller for managing {@link com.venturedive.blazetest.domain.TestCaseAttachments}.
 */
@RestController
@RequestMapping("/api")
public class TestCaseAttachmentsResource {

    private final Logger log = LoggerFactory.getLogger(TestCaseAttachmentsResource.class);

    private static final String ENTITY_NAME = "testCaseAttachments";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestCaseAttachmentsService testCaseAttachmentsService;

    private final TestCaseAttachmentsRepository testCaseAttachmentsRepository;

    private final TestCaseAttachmentsQueryService testCaseAttachmentsQueryService;

    public TestCaseAttachmentsResource(
        TestCaseAttachmentsService testCaseAttachmentsService,
        TestCaseAttachmentsRepository testCaseAttachmentsRepository,
        TestCaseAttachmentsQueryService testCaseAttachmentsQueryService
    ) {
        this.testCaseAttachmentsService = testCaseAttachmentsService;
        this.testCaseAttachmentsRepository = testCaseAttachmentsRepository;
        this.testCaseAttachmentsQueryService = testCaseAttachmentsQueryService;
    }

    /**
     * {@code POST  /test-case-attachments} : Create a new testCaseAttachments.
     *
     * @param testCaseAttachments the testCaseAttachments to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testCaseAttachments, or with status {@code 400 (Bad Request)} if the testCaseAttachments has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-case-attachments")
    public ResponseEntity<TestCaseAttachments> createTestCaseAttachments(@Valid @RequestBody TestCaseAttachments testCaseAttachments)
        throws URISyntaxException {
        log.debug("REST request to save TestCaseAttachments : {}", testCaseAttachments);
        if (testCaseAttachments.getId() != null) {
            throw new BadRequestAlertException("A new testCaseAttachments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestCaseAttachments result = testCaseAttachmentsService.save(testCaseAttachments);
        return ResponseEntity
            .created(new URI("/api/test-case-attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-case-attachments/:id} : Updates an existing testCaseAttachments.
     *
     * @param id the id of the testCaseAttachments to save.
     * @param testCaseAttachments the testCaseAttachments to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCaseAttachments,
     * or with status {@code 400 (Bad Request)} if the testCaseAttachments is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testCaseAttachments couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-case-attachments/{id}")
    public ResponseEntity<TestCaseAttachments> updateTestCaseAttachments(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestCaseAttachments testCaseAttachments
    ) throws URISyntaxException {
        log.debug("REST request to update TestCaseAttachments : {}, {}", id, testCaseAttachments);
        if (testCaseAttachments.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCaseAttachments.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCaseAttachmentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestCaseAttachments result = testCaseAttachmentsService.update(testCaseAttachments);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCaseAttachments.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-case-attachments/:id} : Partial updates given fields of an existing testCaseAttachments, field will ignore if it is null
     *
     * @param id the id of the testCaseAttachments to save.
     * @param testCaseAttachments the testCaseAttachments to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCaseAttachments,
     * or with status {@code 400 (Bad Request)} if the testCaseAttachments is not valid,
     * or with status {@code 404 (Not Found)} if the testCaseAttachments is not found,
     * or with status {@code 500 (Internal Server Error)} if the testCaseAttachments couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-case-attachments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestCaseAttachments> partialUpdateTestCaseAttachments(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestCaseAttachments testCaseAttachments
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestCaseAttachments partially : {}, {}", id, testCaseAttachments);
        if (testCaseAttachments.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCaseAttachments.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCaseAttachmentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestCaseAttachments> result = testCaseAttachmentsService.partialUpdate(testCaseAttachments);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testCaseAttachments.getId().toString())
        );
    }

    /**
     * {@code GET  /test-case-attachments} : get all the testCaseAttachments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testCaseAttachments in body.
     */
    @GetMapping("/test-case-attachments")
    public ResponseEntity<List<TestCaseAttachments>> getAllTestCaseAttachments(
        TestCaseAttachmentsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TestCaseAttachments by criteria: {}", criteria);
        Page<TestCaseAttachments> page = testCaseAttachmentsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-case-attachments/count} : count all the testCaseAttachments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/test-case-attachments/count")
    public ResponseEntity<Long> countTestCaseAttachments(TestCaseAttachmentsCriteria criteria) {
        log.debug("REST request to count TestCaseAttachments by criteria: {}", criteria);
        return ResponseEntity.ok().body(testCaseAttachmentsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /test-case-attachments/:id} : get the "id" testCaseAttachments.
     *
     * @param id the id of the testCaseAttachments to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testCaseAttachments, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-case-attachments/{id}")
    public ResponseEntity<TestCaseAttachments> getTestCaseAttachments(@PathVariable Long id) {
        log.debug("REST request to get TestCaseAttachments : {}", id);
        Optional<TestCaseAttachments> testCaseAttachments = testCaseAttachmentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testCaseAttachments);
    }

    /**
     * {@code DELETE  /test-case-attachments/:id} : delete the "id" testCaseAttachments.
     *
     * @param id the id of the testCaseAttachments to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-case-attachments/{id}")
    public ResponseEntity<Void> deleteTestCaseAttachments(@PathVariable Long id) {
        log.debug("REST request to delete TestCaseAttachments : {}", id);
        testCaseAttachmentsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
