package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.TestRuns;
import com.venturedive.blazetest.repository.TestRunsRepository;
import com.venturedive.blazetest.service.criteria.TestRunsCriteria;
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
 * Service for executing complex queries for {@link TestRuns} entities in the database.
 * The main input is a {@link TestRunsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestRuns} or a {@link Page} of {@link TestRuns} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestRunsQueryService extends QueryService<TestRuns> {

    private final Logger log = LoggerFactory.getLogger(TestRunsQueryService.class);

    private final TestRunsRepository testRunsRepository;

    public TestRunsQueryService(TestRunsRepository testRunsRepository) {
        this.testRunsRepository = testRunsRepository;
    }

    /**
     * Return a {@link List} of {@link TestRuns} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestRuns> findByCriteria(TestRunsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestRuns> specification = createSpecification(criteria);
        return testRunsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestRuns} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestRuns> findByCriteria(TestRunsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestRuns> specification = createSpecification(criteria);
        return testRunsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestRunsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestRuns> specification = createSpecification(criteria);
        return testRunsRepository.count(specification);
    }

    /**
     * Function to convert {@link TestRunsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestRuns> createSpecification(TestRunsCriteria criteria) {
        Specification<TestRuns> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestRuns_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TestRuns_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), TestRuns_.description));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), TestRuns_.createdAt));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), TestRuns_.createdBy));
            }
            if (criteria.getTestLevelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestLevelId(),
                            root -> root.join(TestRuns_.testLevel, JoinType.LEFT).get(TestLevels_.id)
                        )
                    );
            }
            if (criteria.getMileStoneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMileStoneId(),
                            root -> root.join(TestRuns_.mileStone, JoinType.LEFT).get(Milestones_.id)
                        )
                    );
            }
            if (criteria.getTestrundetailsTestrunId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestrundetailsTestrunId(),
                            root -> root.join(TestRuns_.testrundetailsTestruns, JoinType.LEFT).get(TestRunDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
