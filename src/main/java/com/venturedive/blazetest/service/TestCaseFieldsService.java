package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.TestCaseFields;
import com.venturedive.blazetest.repository.TestCaseFieldsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TestCaseFields}.
 */
@Service
@Transactional
public class TestCaseFieldsService {

    private final Logger log = LoggerFactory.getLogger(TestCaseFieldsService.class);

    private final TestCaseFieldsRepository testCaseFieldsRepository;

    public TestCaseFieldsService(TestCaseFieldsRepository testCaseFieldsRepository) {
        this.testCaseFieldsRepository = testCaseFieldsRepository;
    }

    /**
     * Save a testCaseFields.
     *
     * @param testCaseFields the entity to save.
     * @return the persisted entity.
     */
    public TestCaseFields save(TestCaseFields testCaseFields) {
        log.debug("Request to save TestCaseFields : {}", testCaseFields);
        return testCaseFieldsRepository.save(testCaseFields);
    }

    /**
     * Update a testCaseFields.
     *
     * @param testCaseFields the entity to save.
     * @return the persisted entity.
     */
    public TestCaseFields update(TestCaseFields testCaseFields) {
        log.debug("Request to update TestCaseFields : {}", testCaseFields);
        return testCaseFieldsRepository.save(testCaseFields);
    }

    /**
     * Partially update a testCaseFields.
     *
     * @param testCaseFields the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestCaseFields> partialUpdate(TestCaseFields testCaseFields) {
        log.debug("Request to partially update TestCaseFields : {}", testCaseFields);

        return testCaseFieldsRepository
            .findById(testCaseFields.getId())
            .map(existingTestCaseFields -> {
                if (testCaseFields.getExpectedResult() != null) {
                    existingTestCaseFields.setExpectedResult(testCaseFields.getExpectedResult());
                }
                if (testCaseFields.getValue() != null) {
                    existingTestCaseFields.setValue(testCaseFields.getValue());
                }

                return existingTestCaseFields;
            })
            .map(testCaseFieldsRepository::save);
    }

    /**
     * Get all the testCaseFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCaseFields> findAll(Pageable pageable) {
        log.debug("Request to get all TestCaseFields");
        return testCaseFieldsRepository.findAll(pageable);
    }

    /**
     * Get one testCaseFields by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestCaseFields> findOne(Long id) {
        log.debug("Request to get TestCaseFields : {}", id);
        return testCaseFieldsRepository.findById(id);
    }

    /**
     * Delete the testCaseFields by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TestCaseFields : {}", id);
        testCaseFieldsRepository.deleteById(id);
    }
}
