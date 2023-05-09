package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.TestPlans;
import com.venturedive.blazetest.repository.TestPlansRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestPlans}.
 */
@Service
@Transactional
public class TestPlansService {

    private final Logger log = LoggerFactory.getLogger(TestPlansService.class);

    private final TestPlansRepository testPlansRepository;

    public TestPlansService(TestPlansRepository testPlansRepository) {
        this.testPlansRepository = testPlansRepository;
    }

    /**
     * Save a testPlans.
     *
     * @param testPlans the entity to save.
     * @return the persisted entity.
     */
    public TestPlans save(TestPlans testPlans) {
        log.debug("Request to save TestPlans : {}", testPlans);
        return testPlansRepository.save(testPlans);
    }

    /**
     * Update a testPlans.
     *
     * @param testPlans the entity to save.
     * @return the persisted entity.
     */
    public TestPlans update(TestPlans testPlans) {
        log.debug("Request to update TestPlans : {}", testPlans);
        return testPlansRepository.save(testPlans);
    }

    /**
     * Partially update a testPlans.
     *
     * @param testPlans the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestPlans> partialUpdate(TestPlans testPlans) {
        log.debug("Request to partially update TestPlans : {}", testPlans);

        return testPlansRepository
            .findById(testPlans.getId())
            .map(existingTestPlans -> {
                if (testPlans.getName() != null) {
                    existingTestPlans.setName(testPlans.getName());
                }
                if (testPlans.getDescription() != null) {
                    existingTestPlans.setDescription(testPlans.getDescription());
                }
                if (testPlans.getCreatedBy() != null) {
                    existingTestPlans.setCreatedBy(testPlans.getCreatedBy());
                }
                if (testPlans.getCreatedAt() != null) {
                    existingTestPlans.setCreatedAt(testPlans.getCreatedAt());
                }

                return existingTestPlans;
            })
            .map(testPlansRepository::save);
    }

    /**
     * Get all the testPlans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestPlans> findAll(Pageable pageable) {
        log.debug("Request to get all TestPlans");
        return testPlansRepository.findAll(pageable);
    }

    /**
     * Get one testPlans by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestPlans> findOne(Long id) {
        log.debug("Request to get TestPlans : {}", id);
        return testPlansRepository.findById(id);
    }

    /**
     * Delete the testPlans by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestPlans : {}", id);
        testPlansRepository.deleteById(id);
    }
}
