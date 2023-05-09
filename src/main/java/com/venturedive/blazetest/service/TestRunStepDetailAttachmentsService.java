package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.TestRunStepDetailAttachments;
import com.venturedive.blazetest.repository.TestRunStepDetailAttachmentsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestRunStepDetailAttachments}.
 */
@Service
@Transactional
public class TestRunStepDetailAttachmentsService {

    private final Logger log = LoggerFactory.getLogger(TestRunStepDetailAttachmentsService.class);

    private final TestRunStepDetailAttachmentsRepository testRunStepDetailAttachmentsRepository;

    public TestRunStepDetailAttachmentsService(TestRunStepDetailAttachmentsRepository testRunStepDetailAttachmentsRepository) {
        this.testRunStepDetailAttachmentsRepository = testRunStepDetailAttachmentsRepository;
    }

    /**
     * Save a testRunStepDetailAttachments.
     *
     * @param testRunStepDetailAttachments the entity to save.
     * @return the persisted entity.
     */
    public TestRunStepDetailAttachments save(TestRunStepDetailAttachments testRunStepDetailAttachments) {
        log.debug("Request to save TestRunStepDetailAttachments : {}", testRunStepDetailAttachments);
        return testRunStepDetailAttachmentsRepository.save(testRunStepDetailAttachments);
    }

    /**
     * Update a testRunStepDetailAttachments.
     *
     * @param testRunStepDetailAttachments the entity to save.
     * @return the persisted entity.
     */
    public TestRunStepDetailAttachments update(TestRunStepDetailAttachments testRunStepDetailAttachments) {
        log.debug("Request to update TestRunStepDetailAttachments : {}", testRunStepDetailAttachments);
        return testRunStepDetailAttachmentsRepository.save(testRunStepDetailAttachments);
    }

    /**
     * Partially update a testRunStepDetailAttachments.
     *
     * @param testRunStepDetailAttachments the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestRunStepDetailAttachments> partialUpdate(TestRunStepDetailAttachments testRunStepDetailAttachments) {
        log.debug("Request to partially update TestRunStepDetailAttachments : {}", testRunStepDetailAttachments);

        return testRunStepDetailAttachmentsRepository
            .findById(testRunStepDetailAttachments.getId())
            .map(existingTestRunStepDetailAttachments -> {
                if (testRunStepDetailAttachments.getUrl() != null) {
                    existingTestRunStepDetailAttachments.setUrl(testRunStepDetailAttachments.getUrl());
                }

                return existingTestRunStepDetailAttachments;
            })
            .map(testRunStepDetailAttachmentsRepository::save);
    }

    /**
     * Get all the testRunStepDetailAttachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestRunStepDetailAttachments> findAll(Pageable pageable) {
        log.debug("Request to get all TestRunStepDetailAttachments");
        return testRunStepDetailAttachmentsRepository.findAll(pageable);
    }

    /**
     * Get one testRunStepDetailAttachments by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestRunStepDetailAttachments> findOne(Long id) {
        log.debug("Request to get TestRunStepDetailAttachments : {}", id);
        return testRunStepDetailAttachmentsRepository.findById(id);
    }

    /**
     * Delete the testRunStepDetailAttachments by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestRunStepDetailAttachments : {}", id);
        testRunStepDetailAttachmentsRepository.deleteById(id);
    }
}
