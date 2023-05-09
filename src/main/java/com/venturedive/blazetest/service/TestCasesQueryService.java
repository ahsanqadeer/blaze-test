package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.TestCases;
import com.venturedive.blazetest.repository.TestCasesRepository;
import com.venturedive.blazetest.service.criteria.TestCasesCriteria;
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
 * Service for executing complex queries for {@link TestCases} entities in the database.
 * The main input is a {@link TestCasesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestCases} or a {@link Page} of {@link TestCases} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestCasesQueryService extends QueryService<TestCases> {

    private final Logger log = LoggerFactory.getLogger(TestCasesQueryService.class);

    private final TestCasesRepository testCasesRepository;

    public TestCasesQueryService(TestCasesRepository testCasesRepository) {
        this.testCasesRepository = testCasesRepository;
    }

    /**
     * Return a {@link List} of {@link TestCases} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestCases> findByCriteria(TestCasesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestCases> specification = createSpecification(criteria);
        return testCasesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestCases} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCases> findByCriteria(TestCasesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestCases> specification = createSpecification(criteria);
        return testCasesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestCasesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestCases> specification = createSpecification(criteria);
        return testCasesRepository.count(specification);
    }

    /**
     * Function to convert {@link TestCasesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestCases> createSpecification(TestCasesCriteria criteria) {
        Specification<TestCases> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestCases_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), TestCases_.title));
            }
            if (criteria.getEstimate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEstimate(), TestCases_.estimate));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), TestCases_.createdBy));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), TestCases_.createdAt));
            }
            if (criteria.getUpdatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedBy(), TestCases_.updatedBy));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), TestCases_.updatedAt));
            }
            if (criteria.getPrecondition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrecondition(), TestCases_.precondition));
            }
            if (criteria.getIsAutomated() != null) {
                specification = specification.and(buildSpecification(criteria.getIsAutomated(), TestCases_.isAutomated));
            }
            if (criteria.getTestSuiteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestSuiteId(),
                            root -> root.join(TestCases_.testSuite, JoinType.LEFT).get(TestSuites_.id)
                        )
                    );
            }
            if (criteria.getSectionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSectionId(), root -> root.join(TestCases_.section, JoinType.LEFT).get(Sections_.id))
                    );
            }
            if (criteria.getPriorityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPriorityId(),
                            root -> root.join(TestCases_.priority, JoinType.LEFT).get(TestCasePriorities_.id)
                        )
                    );
            }
            if (criteria.getTemplateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTemplateId(),
                            root -> root.join(TestCases_.template, JoinType.LEFT).get(Templates_.id)
                        )
                    );
            }
            if (criteria.getMilestoneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMilestoneId(),
                            root -> root.join(TestCases_.milestone, JoinType.LEFT).get(Milestones_.id)
                        )
                    );
            }
            if (criteria.getTestLevelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestLevelId(),
                            root -> root.join(TestCases_.testLevels, JoinType.LEFT).get(TestLevels_.id)
                        )
                    );
            }
            if (criteria.getTestcaseattachmentsTestcaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestcaseattachmentsTestcaseId(),
                            root -> root.join(TestCases_.testcaseattachmentsTestcases, JoinType.LEFT).get(TestCaseAttachments_.id)
                        )
                    );
            }
            if (criteria.getTestcasefieldsTestcaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestcasefieldsTestcaseId(),
                            root -> root.join(TestCases_.testcasefieldsTestcases, JoinType.LEFT).get(TestCaseFields_.id)
                        )
                    );
            }
            if (criteria.getTestrundetailsTestcaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestrundetailsTestcaseId(),
                            root -> root.join(TestCases_.testrundetailsTestcases, JoinType.LEFT).get(TestRunDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
