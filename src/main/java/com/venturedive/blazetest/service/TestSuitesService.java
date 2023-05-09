package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.TestSuites;
import com.venturedive.blazetest.repository.TestSuitesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestSuites}.
 */
@Service
@Transactional
public class TestSuitesService {

    private final Logger log = LoggerFactory.getLogger(TestSuitesService.class);

    private final TestSuitesRepository testSuitesRepository;

    public TestSuitesService(TestSuitesRepository testSuitesRepository) {
        this.testSuitesRepository = testSuitesRepository;
    }

    /**
     * Save a testSuites.
     *
     * @param testSuites the entity to save.
     * @return the persisted entity.
     */
    public TestSuites save(TestSuites testSuites) {
        log.debug("Request to save TestSuites : {}", testSuites);
        return testSuitesRepository.save(testSuites);
    }

    /**
     * Update a testSuites.
     *
     * @param testSuites the entity to save.
     * @return the persisted entity.
     */
    public TestSuites update(TestSuites testSuites) {
        log.debug("Request to update TestSuites : {}", testSuites);
        return testSuitesRepository.save(testSuites);
    }

    /**
     * Partially update a testSuites.
     *
     * @param testSuites the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestSuites> partialUpdate(TestSuites testSuites) {
        log.debug("Request to partially update TestSuites : {}", testSuites);

        return testSuitesRepository
            .findById(testSuites.getId())
            .map(existingTestSuites -> {
                if (testSuites.getTestSuiteName() != null) {
                    existingTestSuites.setTestSuiteName(testSuites.getTestSuiteName());
                }
                if (testSuites.getDescription() != null) {
                    existingTestSuites.setDescription(testSuites.getDescription());
                }
                if (testSuites.getCreatedBy() != null) {
                    existingTestSuites.setCreatedBy(testSuites.getCreatedBy());
                }
                if (testSuites.getCreatedAt() != null) {
                    existingTestSuites.setCreatedAt(testSuites.getCreatedAt());
                }
                if (testSuites.getUpdatedBy() != null) {
                    existingTestSuites.setUpdatedBy(testSuites.getUpdatedBy());
                }
                if (testSuites.getUpdatedAt() != null) {
                    existingTestSuites.setUpdatedAt(testSuites.getUpdatedAt());
                }

                return existingTestSuites;
            })
            .map(testSuitesRepository::save);
    }

    /**
     * Get all the testSuites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestSuites> findAll(Pageable pageable) {
        log.debug("Request to get all TestSuites");
        return testSuitesRepository.findAll(pageable);
    }

    /**
     * Get one testSuites by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestSuites> findOne(Long id) {
        log.debug("Request to get TestSuites : {}", id);
        return testSuitesRepository.findById(id);
    }

    /**
     * Delete the testSuites by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestSuites : {}", id);
        testSuitesRepository.deleteById(id);
    }
}
