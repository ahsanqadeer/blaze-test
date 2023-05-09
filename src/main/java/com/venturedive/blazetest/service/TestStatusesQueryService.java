package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.TestStatuses;
import com.venturedive.blazetest.repository.TestStatusesRepository;
import com.venturedive.blazetest.service.criteria.TestStatusesCriteria;
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
 * Service for executing complex queries for {@link TestStatuses} entities in the database.
 * The main input is a {@link TestStatusesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestStatuses} or a {@link Page} of {@link TestStatuses} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestStatusesQueryService extends QueryService<TestStatuses> {

    private final Logger log = LoggerFactory.getLogger(TestStatusesQueryService.class);

    private final TestStatusesRepository testStatusesRepository;

    public TestStatusesQueryService(TestStatusesRepository testStatusesRepository) {
        this.testStatusesRepository = testStatusesRepository;
    }

    /**
     * Return a {@link List} of {@link TestStatuses} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestStatuses> findByCriteria(TestStatusesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestStatuses> specification = createSpecification(criteria);
        return testStatusesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestStatuses} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestStatuses> findByCriteria(TestStatusesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestStatuses> specification = createSpecification(criteria);
        return testStatusesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestStatusesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestStatuses> specification = createSpecification(criteria);
        return testStatusesRepository.count(specification);
    }

    /**
     * Function to convert {@link TestStatusesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestStatuses> createSpecification(TestStatusesCriteria criteria) {
        Specification<TestStatuses> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestStatuses_.id));
            }
            if (criteria.getStatusName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatusName(), TestStatuses_.statusName));
            }
            if (criteria.getTestrundetailsStatusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestrundetailsStatusId(),
                            root -> root.join(TestStatuses_.testrundetailsStatuses, JoinType.LEFT).get(TestRunDetails_.id)
                        )
                    );
            }
            if (criteria.getTestrunstepdetailsStatusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestrunstepdetailsStatusId(),
                            root -> root.join(TestStatuses_.testrunstepdetailsStatuses, JoinType.LEFT).get(TestRunStepDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
