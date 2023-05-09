package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.TestCaseAttachments;
import com.venturedive.blazetest.repository.TestCaseAttachmentsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestCaseAttachments}.
 */
@Service
@Transactional
public class TestCaseAttachmentsService {

    private final Logger log = LoggerFactory.getLogger(TestCaseAttachmentsService.class);

    private final TestCaseAttachmentsRepository testCaseAttachmentsRepository;

    public TestCaseAttachmentsService(TestCaseAttachmentsRepository testCaseAttachmentsRepository) {
        this.testCaseAttachmentsRepository = testCaseAttachmentsRepository;
    }

    /**
     * Save a testCaseAttachments.
     *
     * @param testCaseAttachments the entity to save.
     * @return the persisted entity.
     */
    public TestCaseAttachments save(TestCaseAttachments testCaseAttachments) {
        log.debug("Request to save TestCaseAttachments : {}", testCaseAttachments);
        return testCaseAttachmentsRepository.save(testCaseAttachments);
    }

    /**
     * Update a testCaseAttachments.
     *
     * @param testCaseAttachments the entity to save.
     * @return the persisted entity.
     */
    public TestCaseAttachments update(TestCaseAttachments testCaseAttachments) {
        log.debug("Request to update TestCaseAttachments : {}", testCaseAttachments);
        return testCaseAttachmentsRepository.save(testCaseAttachments);
    }

    /**
     * Partially update a testCaseAttachments.
     *
     * @param testCaseAttachments the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestCaseAttachments> partialUpdate(TestCaseAttachments testCaseAttachments) {
        log.debug("Request to partially update TestCaseAttachments : {}", testCaseAttachments);

        return testCaseAttachmentsRepository
            .findById(testCaseAttachments.getId())
            .map(existingTestCaseAttachments -> {
                if (testCaseAttachments.getUrl() != null) {
                    existingTestCaseAttachments.setUrl(testCaseAttachments.getUrl());
                }

                return existingTestCaseAttachments;
            })
            .map(testCaseAttachmentsRepository::save);
    }

    /**
     * Get all the testCaseAttachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCaseAttachments> findAll(Pageable pageable) {
        log.debug("Request to get all TestCaseAttachments");
        return testCaseAttachmentsRepository.findAll(pageable);
    }

    /**
     * Get one testCaseAttachments by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestCaseAttachments> findOne(Long id) {
        log.debug("Request to get TestCaseAttachments : {}", id);
        return testCaseAttachmentsRepository.findById(id);
    }

    /**
     * Delete the testCaseAttachments by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestCaseAttachments : {}", id);
        testCaseAttachmentsRepository.deleteById(id);
    }
}
