package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.Milestones;
import com.venturedive.blazetest.repository.MilestonesRepository;
import com.venturedive.blazetest.service.criteria.MilestonesCriteria;
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
 * Service for executing complex queries for {@link Milestones} entities in the database.
 * The main input is a {@link MilestonesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Milestones} or a {@link Page} of {@link Milestones} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MilestonesQueryService extends QueryService<Milestones> {

    private final Logger log = LoggerFactory.getLogger(MilestonesQueryService.class);

    private final MilestonesRepository milestonesRepository;

    public MilestonesQueryService(MilestonesRepository milestonesRepository) {
        this.milestonesRepository = milestonesRepository;
    }

    /**
     * Return a {@link List} of {@link Milestones} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Milestones> findByCriteria(MilestonesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Milestones> specification = createSpecification(criteria);
        return milestonesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Milestones} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Milestones> findByCriteria(MilestonesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Milestones> specification = createSpecification(criteria);
        return milestonesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MilestonesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Milestones> specification = createSpecification(criteria);
        return milestonesRepository.count(specification);
    }

    /**
     * Function to convert {@link MilestonesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Milestones> createSpecification(MilestonesCriteria criteria) {
        Specification<Milestones> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Milestones_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Milestones_.name));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Milestones_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Milestones_.endDate));
            }
            if (criteria.getIsCompleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsCompleted(), Milestones_.isCompleted));
            }
            if (criteria.getParentMilestoneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParentMilestoneId(),
                            root -> root.join(Milestones_.parentMilestone, JoinType.LEFT).get(Milestones_.id)
                        )
                    );
            }
            if (criteria.getProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProjectId(), root -> root.join(Milestones_.project, JoinType.LEFT).get(Projects_.id))
                    );
            }
            if (criteria.getMilestonesParentmilestoneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMilestonesParentmilestoneId(),
                            root -> root.join(Milestones_.milestonesParentmilestones, JoinType.LEFT).get(Milestones_.id)
                        )
                    );
            }
            if (criteria.getTestcasesMilestoneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestcasesMilestoneId(),
                            root -> root.join(Milestones_.testcasesMilestones, JoinType.LEFT).get(TestCases_.id)
                        )
                    );
            }
            if (criteria.getTestrunsMilestoneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestrunsMilestoneId(),
                            root -> root.join(Milestones_.testrunsMilestones, JoinType.LEFT).get(TestRuns_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
