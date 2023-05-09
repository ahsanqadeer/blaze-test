package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.TestLevels;
import com.venturedive.blazetest.repository.TestLevelsRepository;
import com.venturedive.blazetest.service.criteria.TestLevelsCriteria;
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
 * Service for executing complex queries for {@link TestLevels} entities in the database.
 * The main input is a {@link TestLevelsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestLevels} or a {@link Page} of {@link TestLevels} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestLevelsQueryService extends QueryService<TestLevels> {

    private final Logger log = LoggerFactory.getLogger(TestLevelsQueryService.class);

    private final TestLevelsRepository testLevelsRepository;

    public TestLevelsQueryService(TestLevelsRepository testLevelsRepository) {
        this.testLevelsRepository = testLevelsRepository;
    }

    /**
     * Return a {@link List} of {@link TestLevels} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestLevels> findByCriteria(TestLevelsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestLevels> specification = createSpecification(criteria);
        return testLevelsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestLevels} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestLevels> findByCriteria(TestLevelsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestLevels> specification = createSpecification(criteria);
        return testLevelsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestLevelsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestLevels> specification = createSpecification(criteria);
        return testLevelsRepository.count(specification);
    }

    /**
     * Function to convert {@link TestLevelsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestLevels> createSpecification(TestLevelsCriteria criteria) {
        Specification<TestLevels> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestLevels_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TestLevels_.name));
            }
            if (criteria.getTestrunsTestlevelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestrunsTestlevelId(),
                            root -> root.join(TestLevels_.testrunsTestlevels, JoinType.LEFT).get(TestRuns_.id)
                        )
                    );
            }
            if (criteria.getTestCaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestCaseId(),
                            root -> root.join(TestLevels_.testCases, JoinType.LEFT).get(TestCases_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
