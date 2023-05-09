package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.TestRunDetailAttachments;
import com.venturedive.blazetest.repository.TestRunDetailAttachmentsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestRunDetailAttachments}.
 */
@Service
@Transactional
public class TestRunDetailAttachmentsService {

    private final Logger log = LoggerFactory.getLogger(TestRunDetailAttachmentsService.class);

    private final TestRunDetailAttachmentsRepository testRunDetailAttachmentsRepository;

    public TestRunDetailAttachmentsService(TestRunDetailAttachmentsRepository testRunDetailAttachmentsRepository) {
        this.testRunDetailAttachmentsRepository = testRunDetailAttachmentsRepository;
    }

    /**
     * Save a testRunDetailAttachments.
     *
     * @param testRunDetailAttachments the entity to save.
     * @return the persisted entity.
     */
    public TestRunDetailAttachments save(TestRunDetailAttachments testRunDetailAttachments) {
        log.debug("Request to save TestRunDetailAttachments : {}", testRunDetailAttachments);
        return testRunDetailAttachmentsRepository.save(testRunDetailAttachments);
    }

    /**
     * Update a testRunDetailAttachments.
     *
     * @param testRunDetailAttachments the entity to save.
     * @return the persisted entity.
     */
    public TestRunDetailAttachments update(TestRunDetailAttachments testRunDetailAttachments) {
        log.debug("Request to update TestRunDetailAttachments : {}", testRunDetailAttachments);
        return testRunDetailAttachmentsRepository.save(testRunDetailAttachments);
    }

    /**
     * Partially update a testRunDetailAttachments.
     *
     * @param testRunDetailAttachments the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestRunDetailAttachments> partialUpdate(TestRunDetailAttachments testRunDetailAttachments) {
        log.debug("Request to partially update TestRunDetailAttachments : {}", testRunDetailAttachments);

        return testRunDetailAttachmentsRepository
            .findById(testRunDetailAttachments.getId())
            .map(existingTestRunDetailAttachments -> {
                if (testRunDetailAttachments.getUrl() != null) {
                    existingTestRunDetailAttachments.setUrl(testRunDetailAttachments.getUrl());
                }

                return existingTestRunDetailAttachments;
            })
            .map(testRunDetailAttachmentsRepository::save);
    }

    /**
     * Get all the testRunDetailAttachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestRunDetailAttachments> findAll(Pageable pageable) {
        log.debug("Request to get all TestRunDetailAttachments");
        return testRunDetailAttachmentsRepository.findAll(pageable);
    }

    /**
     * Get one testRunDetailAttachments by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestRunDetailAttachments> findOne(Long id) {
        log.debug("Request to get TestRunDetailAttachments : {}", id);
        return testRunDetailAttachmentsRepository.findById(id);
    }

    /**
     * Delete the testRunDetailAttachments by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestRunDetailAttachments : {}", id);
        testRunDetailAttachmentsRepository.deleteById(id);
    }
}
