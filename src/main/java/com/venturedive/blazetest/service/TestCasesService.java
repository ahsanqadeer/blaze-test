package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.TestCases;
import com.venturedive.blazetest.repository.TestCasesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestCases}.
 */
@Service
@Transactional
public class TestCasesService {

    private final Logger log = LoggerFactory.getLogger(TestCasesService.class);

    private final TestCasesRepository testCasesRepository;

    public TestCasesService(TestCasesRepository testCasesRepository) {
        this.testCasesRepository = testCasesRepository;
    }

    /**
     * Save a testCases.
     *
     * @param testCases the entity to save.
     * @return the persisted entity.
     */
    public TestCases save(TestCases testCases) {
        log.debug("Request to save TestCases : {}", testCases);
        return testCasesRepository.save(testCases);
    }

    /**
     * Update a testCases.
     *
     * @param testCases the entity to save.
     * @return the persisted entity.
     */
    public TestCases update(TestCases testCases) {
        log.debug("Request to update TestCases : {}", testCases);
        return testCasesRepository.save(testCases);
    }

    /**
     * Partially update a testCases.
     *
     * @param testCases the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestCases> partialUpdate(TestCases testCases) {
        log.debug("Request to partially update TestCases : {}", testCases);

        return testCasesRepository
            .findById(testCases.getId())
            .map(existingTestCases -> {
                if (testCases.getTitle() != null) {
                    existingTestCases.setTitle(testCases.getTitle());
                }
                if (testCases.getEstimate() != null) {
                    existingTestCases.setEstimate(testCases.getEstimate());
                }
                if (testCases.getCreatedBy() != null) {
                    existingTestCases.setCreatedBy(testCases.getCreatedBy());
                }
                if (testCases.getCreatedAt() != null) {
                    existingTestCases.setCreatedAt(testCases.getCreatedAt());
                }
                if (testCases.getUpdatedBy() != null) {
                    existingTestCases.setUpdatedBy(testCases.getUpdatedBy());
                }
                if (testCases.getUpdatedAt() != null) {
                    existingTestCases.setUpdatedAt(testCases.getUpdatedAt());
                }
                if (testCases.getPrecondition() != null) {
                    existingTestCases.setPrecondition(testCases.getPrecondition());
                }
                if (testCases.getDescription() != null) {
                    existingTestCases.setDescription(testCases.getDescription());
                }
                if (testCases.getIsAutomated() != null) {
                    existingTestCases.setIsAutomated(testCases.getIsAutomated());
                }

                return existingTestCases;
            })
            .map(testCasesRepository::save);
    }

    /**
     * Get all the testCases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCases> findAll(Pageable pageable) {
        log.debug("Request to get all TestCases");
        return testCasesRepository.findAll(pageable);
    }

    /**
     * Get all the testCases with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TestCases> findAllWithEagerRelationships(Pageable pageable) {
        return testCasesRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one testCases by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestCases> findOne(Long id) {
        log.debug("Request to get TestCases : {}", id);
        return testCasesRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the testCases by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestCases : {}", id);
        testCasesRepository.deleteById(id);
    }
}
