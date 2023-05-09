package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.TestCaseFieldAttachments;
import com.venturedive.blazetest.repository.TestCaseFieldAttachmentsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestCaseFieldAttachments}.
 */
@Service
@Transactional
public class TestCaseFieldAttachmentsService {

    private final Logger log = LoggerFactory.getLogger(TestCaseFieldAttachmentsService.class);

    private final TestCaseFieldAttachmentsRepository testCaseFieldAttachmentsRepository;

    public TestCaseFieldAttachmentsService(TestCaseFieldAttachmentsRepository testCaseFieldAttachmentsRepository) {
        this.testCaseFieldAttachmentsRepository = testCaseFieldAttachmentsRepository;
    }

    /**
     * Save a testCaseFieldAttachments.
     *
     * @param testCaseFieldAttachments the entity to save.
     * @return the persisted entity.
     */
    public TestCaseFieldAttachments save(TestCaseFieldAttachments testCaseFieldAttachments) {
        log.debug("Request to save TestCaseFieldAttachments : {}", testCaseFieldAttachments);
        return testCaseFieldAttachmentsRepository.save(testCaseFieldAttachments);
    }

    /**
     * Update a testCaseFieldAttachments.
     *
     * @param testCaseFieldAttachments the entity to save.
     * @return the persisted entity.
     */
    public TestCaseFieldAttachments update(TestCaseFieldAttachments testCaseFieldAttachments) {
        log.debug("Request to update TestCaseFieldAttachments : {}", testCaseFieldAttachments);
        return testCaseFieldAttachmentsRepository.save(testCaseFieldAttachments);
    }

    /**
     * Partially update a testCaseFieldAttachments.
     *
     * @param testCaseFieldAttachments the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestCaseFieldAttachments> partialUpdate(TestCaseFieldAttachments testCaseFieldAttachments) {
        log.debug("Request to partially update TestCaseFieldAttachments : {}", testCaseFieldAttachments);

        return testCaseFieldAttachmentsRepository
            .findById(testCaseFieldAttachments.getId())
            .map(existingTestCaseFieldAttachments -> {
                if (testCaseFieldAttachments.getUrl() != null) {
                    existingTestCaseFieldAttachments.setUrl(testCaseFieldAttachments.getUrl());
                }

                return existingTestCaseFieldAttachments;
            })
            .map(testCaseFieldAttachmentsRepository::save);
    }

    /**
     * Get all the testCaseFieldAttachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCaseFieldAttachments> findAll(Pageable pageable) {
        log.debug("Request to get all TestCaseFieldAttachments");
        return testCaseFieldAttachmentsRepository.findAll(pageable);
    }

    /**
     * Get one testCaseFieldAttachments by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestCaseFieldAttachments> findOne(Long id) {
        log.debug("Request to get TestCaseFieldAttachments : {}", id);
        return testCaseFieldAttachmentsRepository.findById(id);
    }

    /**
     * Delete the testCaseFieldAttachments by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestCaseFieldAttachments : {}", id);
        testCaseFieldAttachmentsRepository.deleteById(id);
    }
}
