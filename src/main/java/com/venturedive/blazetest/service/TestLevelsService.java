package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.TestLevels;
import com.venturedive.blazetest.repository.TestLevelsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestLevels}.
 */
@Service
@Transactional
public class TestLevelsService {

    private final Logger log = LoggerFactory.getLogger(TestLevelsService.class);

    private final TestLevelsRepository testLevelsRepository;

    public TestLevelsService(TestLevelsRepository testLevelsRepository) {
        this.testLevelsRepository = testLevelsRepository;
    }

    /**
     * Save a testLevels.
     *
     * @param testLevels the entity to save.
     * @return the persisted entity.
     */
    public TestLevels save(TestLevels testLevels) {
        log.debug("Request to save TestLevels : {}", testLevels);
        return testLevelsRepository.save(testLevels);
    }

    /**
     * Update a testLevels.
     *
     * @param testLevels the entity to save.
     * @return the persisted entity.
     */
    public TestLevels update(TestLevels testLevels) {
        log.debug("Request to update TestLevels : {}", testLevels);
        return testLevelsRepository.save(testLevels);
    }

    /**
     * Partially update a testLevels.
     *
     * @param testLevels the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestLevels> partialUpdate(TestLevels testLevels) {
        log.debug("Request to partially update TestLevels : {}", testLevels);

        return testLevelsRepository
            .findById(testLevels.getId())
            .map(existingTestLevels -> {
                if (testLevels.getName() != null) {
                    existingTestLevels.setName(testLevels.getName());
                }

                return existingTestLevels;
            })
            .map(testLevelsRepository::save);
    }

    /**
     * Get all the testLevels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestLevels> findAll(Pageable pageable) {
        log.debug("Request to get all TestLevels");
        return testLevelsRepository.findAll(pageable);
    }

    /**
     * Get one testLevels by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestLevels> findOne(Long id) {
        log.debug("Request to get TestLevels : {}", id);
        return testLevelsRepository.findById(id);
    }

    /**
     * Delete the testLevels by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestLevels : {}", id);
        testLevelsRepository.deleteById(id);
    }
}
