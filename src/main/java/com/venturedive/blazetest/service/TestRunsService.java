package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.TestRuns;
import com.venturedive.blazetest.repository.TestRunsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestRuns}.
 */
@Service
@Transactional
public class TestRunsService {

    private final Logger log = LoggerFactory.getLogger(TestRunsService.class);

    private final TestRunsRepository testRunsRepository;

    public TestRunsService(TestRunsRepository testRunsRepository) {
        this.testRunsRepository = testRunsRepository;
    }

    /**
     * Save a testRuns.
     *
     * @param testRuns the entity to save.
     * @return the persisted entity.
     */
    public TestRuns save(TestRuns testRuns) {
        log.debug("Request to save TestRuns : {}", testRuns);
        return testRunsRepository.save(testRuns);
    }

    /**
     * Update a testRuns.
     *
     * @param testRuns the entity to save.
     * @return the persisted entity.
     */
    public TestRuns update(TestRuns testRuns) {
        log.debug("Request to update TestRuns : {}", testRuns);
        return testRunsRepository.save(testRuns);
    }

    /**
     * Partially update a testRuns.
     *
     * @param testRuns the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestRuns> partialUpdate(TestRuns testRuns) {
        log.debug("Request to partially update TestRuns : {}", testRuns);

        return testRunsRepository
            .findById(testRuns.getId())
            .map(existingTestRuns -> {
                if (testRuns.getName() != null) {
                    existingTestRuns.setName(testRuns.getName());
                }
                if (testRuns.getDescription() != null) {
                    existingTestRuns.setDescription(testRuns.getDescription());
                }
                if (testRuns.getCreatedAt() != null) {
                    existingTestRuns.setCreatedAt(testRuns.getCreatedAt());
                }
                if (testRuns.getCreatedBy() != null) {
                    existingTestRuns.setCreatedBy(testRuns.getCreatedBy());
                }

                return existingTestRuns;
            })
            .map(testRunsRepository::save);
    }

    /**
     * Get all the testRuns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestRuns> findAll(Pageable pageable) {
        log.debug("Request to get all TestRuns");
        return testRunsRepository.findAll(pageable);
    }

    /**
     * Get one testRuns by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestRuns> findOne(Long id) {
        log.debug("Request to get TestRuns : {}", id);
        return testRunsRepository.findById(id);
    }

    /**
     * Delete the testRuns by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestRuns : {}", id);
        testRunsRepository.deleteById(id);
    }
}
