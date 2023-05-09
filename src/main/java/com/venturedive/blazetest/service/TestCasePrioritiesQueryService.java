package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.TestCasePriorities;
import com.venturedive.blazetest.repository.TestCasePrioritiesRepository;
import com.venturedive.blazetest.service.criteria.TestCasePrioritiesCriteria;
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
 * Service for executing complex queries for {@link TestCasePriorities} entities in the database.
 * The main input is a {@link TestCasePrioritiesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestCasePriorities} or a {@link Page} of {@link TestCasePriorities} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestCasePrioritiesQueryService extends QueryService<TestCasePriorities> {

    private final Logger log = LoggerFactory.getLogger(TestCasePrioritiesQueryService.class);

    private final TestCasePrioritiesRepository testCasePrioritiesRepository;

    public TestCasePrioritiesQueryService(TestCasePrioritiesRepository testCasePrioritiesRepository) {
        this.testCasePrioritiesRepository = testCasePrioritiesRepository;
    }

    /**
     * Return a {@link List} of {@link TestCasePriorities} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestCasePriorities> findByCriteria(TestCasePrioritiesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestCasePriorities> specification = createSpecification(criteria);
        return testCasePrioritiesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestCasePriorities} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCasePriorities> findByCriteria(TestCasePrioritiesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestCasePriorities> specification = createSpecification(criteria);
        return testCasePrioritiesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestCasePrioritiesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestCasePriorities> specification = createSpecification(criteria);
        return testCasePrioritiesRepository.count(specification);
    }

    /**
     * Function to convert {@link TestCasePrioritiesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestCasePriorities> createSpecification(TestCasePrioritiesCriteria criteria) {
        Specification<TestCasePriorities> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestCasePriorities_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TestCasePriorities_.name));
            }
            if (criteria.getTestcasesPriorityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestcasesPriorityId(),
                            root -> root.join(TestCasePriorities_.testcasesPriorities, JoinType.LEFT).get(TestCases_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
