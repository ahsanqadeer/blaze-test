package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.TestStatuses;
import com.venturedive.blazetest.repository.TestStatusesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestStatuses}.
 */
@Service
@Transactional
public class TestStatusesService {

    private final Logger log = LoggerFactory.getLogger(TestStatusesService.class);

    private final TestStatusesRepository testStatusesRepository;

    public TestStatusesService(TestStatusesRepository testStatusesRepository) {
        this.testStatusesRepository = testStatusesRepository;
    }

    /**
     * Save a testStatuses.
     *
     * @param testStatuses the entity to save.
     * @return the persisted entity.
     */
    public TestStatuses save(TestStatuses testStatuses) {
        log.debug("Request to save TestStatuses : {}", testStatuses);
        return testStatusesRepository.save(testStatuses);
    }

    /**
     * Update a testStatuses.
     *
     * @param testStatuses the entity to save.
     * @return the persisted entity.
     */
    public TestStatuses update(TestStatuses testStatuses) {
        log.debug("Request to update TestStatuses : {}", testStatuses);
        return testStatusesRepository.save(testStatuses);
    }

    /**
     * Partially update a testStatuses.
     *
     * @param testStatuses the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestStatuses> partialUpdate(TestStatuses testStatuses) {
        log.debug("Request to partially update TestStatuses : {}", testStatuses);

        return testStatusesRepository
            .findById(testStatuses.getId())
            .map(existingTestStatuses -> {
                if (testStatuses.getStatusName() != null) {
                    existingTestStatuses.setStatusName(testStatuses.getStatusName());
                }

                return existingTestStatuses;
            })
            .map(testStatusesRepository::save);
    }

    /**
     * Get all the testStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestStatuses> findAll(Pageable pageable) {
        log.debug("Request to get all TestStatuses");
        return testStatusesRepository.findAll(pageable);
    }

    /**
     * Get one testStatuses by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestStatuses> findOne(Long id) {
        log.debug("Request to get TestStatuses : {}", id);
        return testStatusesRepository.findById(id);
    }

    /**
     * Delete the testStatuses by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestStatuses : {}", id);
        testStatusesRepository.deleteById(id);
    }
}
