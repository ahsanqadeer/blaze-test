package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.TestPlans;
import com.venturedive.blazetest.repository.TestPlansRepository;
import com.venturedive.blazetest.service.criteria.TestPlansCriteria;
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
 * Service for executing complex queries for {@link TestPlans} entities in the database.
 * The main input is a {@link TestPlansCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestPlans} or a {@link Page} of {@link TestPlans} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestPlansQueryService extends QueryService<TestPlans> {

    private final Logger log = LoggerFactory.getLogger(TestPlansQueryService.class);

    private final TestPlansRepository testPlansRepository;

    public TestPlansQueryService(TestPlansRepository testPlansRepository) {
        this.testPlansRepository = testPlansRepository;
    }

    /**
     * Return a {@link List} of {@link TestPlans} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestPlans> findByCriteria(TestPlansCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestPlans> specification = createSpecification(criteria);
        return testPlansRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestPlans} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestPlans> findByCriteria(TestPlansCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestPlans> specification = createSpecification(criteria);
        return testPlansRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestPlansCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestPlans> specification = createSpecification(criteria);
        return testPlansRepository.count(specification);
    }

    /**
     * Function to convert {@link TestPlansCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestPlans> createSpecification(TestPlansCriteria criteria) {
        Specification<TestPlans> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestPlans_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TestPlans_.name));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), TestPlans_.createdBy));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), TestPlans_.createdAt));
            }
            if (criteria.getProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProjectId(), root -> root.join(TestPlans_.project, JoinType.LEFT).get(Projects_.id))
                    );
            }
        }
        return specification;
    }
}
