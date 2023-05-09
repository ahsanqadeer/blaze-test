package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.TestCasePriorities;
import com.venturedive.blazetest.repository.TestCasePrioritiesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestCasePriorities}.
 */
@Service
@Transactional
public class TestCasePrioritiesService {

    private final Logger log = LoggerFactory.getLogger(TestCasePrioritiesService.class);

    private final TestCasePrioritiesRepository testCasePrioritiesRepository;

    public TestCasePrioritiesService(TestCasePrioritiesRepository testCasePrioritiesRepository) {
        this.testCasePrioritiesRepository = testCasePrioritiesRepository;
    }

    /**
     * Save a testCasePriorities.
     *
     * @param testCasePriorities the entity to save.
     * @return the persisted entity.
     */
    public TestCasePriorities save(TestCasePriorities testCasePriorities) {
        log.debug("Request to save TestCasePriorities : {}", testCasePriorities);
        return testCasePrioritiesRepository.save(testCasePriorities);
    }

    /**
     * Update a testCasePriorities.
     *
     * @param testCasePriorities the entity to save.
     * @return the persisted entity.
     */
    public TestCasePriorities update(TestCasePriorities testCasePriorities) {
        log.debug("Request to update TestCasePriorities : {}", testCasePriorities);
        return testCasePrioritiesRepository.save(testCasePriorities);
    }

    /**
     * Partially update a testCasePriorities.
     *
     * @param testCasePriorities the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestCasePriorities> partialUpdate(TestCasePriorities testCasePriorities) {
        log.debug("Request to partially update TestCasePriorities : {}", testCasePriorities);

        return testCasePrioritiesRepository
            .findById(testCasePriorities.getId())
            .map(existingTestCasePriorities -> {
                if (testCasePriorities.getName() != null) {
                    existingTestCasePriorities.setName(testCasePriorities.getName());
                }

                return existingTestCasePriorities;
            })
            .map(testCasePrioritiesRepository::save);
    }

    /**
     * Get all the testCasePriorities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCasePriorities> findAll(Pageable pageable) {
        log.debug("Request to get all TestCasePriorities");
        return testCasePrioritiesRepository.findAll(pageable);
    }

    /**
     * Get one testCasePriorities by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestCasePriorities> findOne(Long id) {
        log.debug("Request to get TestCasePriorities : {}", id);
        return testCasePrioritiesRepository.findById(id);
    }

    /**
     * Delete the testCasePriorities by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestCasePriorities : {}", id);
        testCasePrioritiesRepository.deleteById(id);
    }
}
