package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.TestSuites;
import com.venturedive.blazetest.repository.TestSuitesRepository;
import com.venturedive.blazetest.service.criteria.TestSuitesCriteria;
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
 * Service for executing complex queries for {@link TestSuites} entities in the database.
 * The main input is a {@link TestSuitesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestSuites} or a {@link Page} of {@link TestSuites} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestSuitesQueryService extends QueryService<TestSuites> {

    private final Logger log = LoggerFactory.getLogger(TestSuitesQueryService.class);

    private final TestSuitesRepository testSuitesRepository;

    public TestSuitesQueryService(TestSuitesRepository testSuitesRepository) {
        this.testSuitesRepository = testSuitesRepository;
    }

    /**
     * Return a {@link List} of {@link TestSuites} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestSuites> findByCriteria(TestSuitesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestSuites> specification = createSpecification(criteria);
        return testSuitesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestSuites} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestSuites> findByCriteria(TestSuitesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestSuites> specification = createSpecification(criteria);
        return testSuitesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestSuitesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestSuites> specification = createSpecification(criteria);
        return testSuitesRepository.count(specification);
    }

    /**
     * Function to convert {@link TestSuitesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestSuites> createSpecification(TestSuitesCriteria criteria) {
        Specification<TestSuites> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestSuites_.id));
            }
            if (criteria.getTestSuiteName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTestSuiteName(), TestSuites_.testSuiteName));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), TestSuites_.createdBy));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), TestSuites_.createdAt));
            }
            if (criteria.getUpdatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedBy(), TestSuites_.updatedBy));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), TestSuites_.updatedAt));
            }
            if (criteria.getProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProjectId(), root -> root.join(TestSuites_.project, JoinType.LEFT).get(Projects_.id))
                    );
            }
            if (criteria.getSectionsTestsuiteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSectionsTestsuiteId(),
                            root -> root.join(TestSuites_.sectionsTestsuites, JoinType.LEFT).get(Sections_.id)
                        )
                    );
            }
            if (criteria.getTestcasesTestsuiteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestcasesTestsuiteId(),
                            root -> root.join(TestSuites_.testcasesTestsuites, JoinType.LEFT).get(TestCases_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
