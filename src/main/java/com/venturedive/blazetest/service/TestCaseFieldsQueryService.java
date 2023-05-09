package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.TestCaseFields;
import com.venturedive.blazetest.repository.TestCaseFieldsRepository;
import com.venturedive.blazetest.service.criteria.TestCaseFieldsCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TestCaseFields} entities in the database.
 * The main input is a {@link TestCaseFieldsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestCaseFields} or a {@link Page} of {@link TestCaseFields} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestCaseFieldsQueryService extends QueryService<TestCaseFields> {

    private final Logger log = LoggerFactory.getLogger(TestCaseFieldsQueryService.class);

    private final TestCaseFieldsRepository testCaseFieldsRepository;

    public TestCaseFieldsQueryService(TestCaseFieldsRepository testCaseFieldsRepository) {
        this.testCaseFieldsRepository = testCaseFieldsRepository;
    }

    /**
     * Return a {@link List} of {@link TestCaseFields} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestCaseFields> findByCriteria(TestCaseFieldsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestCaseFields> specification = createSpecification(criteria);
        return testCaseFieldsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestCaseFields} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCaseFields> findByCriteria(TestCaseFieldsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestCaseFields> specification = createSpecification(criteria);
        return testCaseFieldsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestCaseFieldsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestCaseFields> specification = createSpecification(criteria);
        return testCaseFieldsRepository.count(specification);
    }

    /**
     * Function to convert {@link TestCaseFieldsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestCaseFields> createSpecification(TestCaseFieldsCriteria criteria) {
        Specification<TestCaseFields> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestCaseFields_.id));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), TestCaseFields_.value));
            }
            if (criteria.getTemplateFieldId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTemplateFieldId(),
                            root -> root.join(TestCaseFields_.templateField, JoinType.LEFT).get(TemplateFields_.id)
                        )
                    );
            }
            if (criteria.getTestCaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestCaseId(),
                            root -> root.join(TestCaseFields_.testCase, JoinType.LEFT).get(TestCases_.id)
                        )
                    );
            }
            if (criteria.getTestcasefieldattachmentsTestcasefieldId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestcasefieldattachmentsTestcasefieldId(),
                            root ->
                                root
                                    .join(TestCaseFields_.testcasefieldattachmentsTestcasefields, JoinType.LEFT)
                                    .get(TestCaseFieldAttachments_.id)
                        )
                    );
            }
            if (criteria.getTestrunstepdetailsStepdetailId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestrunstepdetailsStepdetailId(),
                            root -> root.join(TestCaseFields_.testrunstepdetailsStepdetails, JoinType.LEFT).get(TestRunStepDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
