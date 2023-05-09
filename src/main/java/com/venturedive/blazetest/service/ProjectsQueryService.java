package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.Projects;
import com.venturedive.blazetest.repository.ProjectsRepository;
import com.venturedive.blazetest.service.criteria.ProjectsCriteria;
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
 * Service for executing complex queries for {@link Projects} entities in the database.
 * The main input is a {@link ProjectsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Projects} or a {@link Page} of {@link Projects} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProjectsQueryService extends QueryService<Projects> {

    private final Logger log = LoggerFactory.getLogger(ProjectsQueryService.class);

    private final ProjectsRepository projectsRepository;

    public ProjectsQueryService(ProjectsRepository projectsRepository) {
        this.projectsRepository = projectsRepository;
    }

    /**
     * Return a {@link List} of {@link Projects} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Projects> findByCriteria(ProjectsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Projects> specification = createSpecification(criteria);
        return projectsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Projects} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Projects> findByCriteria(ProjectsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Projects> specification = createSpecification(criteria);
        return projectsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProjectsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Projects> specification = createSpecification(criteria);
        return projectsRepository.count(specification);
    }

    /**
     * Function to convert {@link ProjectsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Projects> createSpecification(ProjectsCriteria criteria) {
        Specification<Projects> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Projects_.id));
            }
            if (criteria.getProjectName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProjectName(), Projects_.projectName));
            }
            if (criteria.getIsactive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsactive(), Projects_.isactive));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), Projects_.createdBy));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Projects_.createdAt));
            }
            if (criteria.getUpdatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedBy(), Projects_.updatedBy));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Projects_.updatedAt));
            }
            if (criteria.getDefaultTemplateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDefaultTemplateId(),
                            root -> root.join(Projects_.defaultTemplate, JoinType.LEFT).get(Templates_.id)
                        )
                    );
            }
            if (criteria.getCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCompanyId(), root -> root.join(Projects_.company, JoinType.LEFT).get(Companies_.id))
                    );
            }
            if (criteria.getMilestonesProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMilestonesProjectId(),
                            root -> root.join(Projects_.milestonesProjects, JoinType.LEFT).get(Milestones_.id)
                        )
                    );
            }
            if (criteria.getTestplansProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestplansProjectId(),
                            root -> root.join(Projects_.testplansProjects, JoinType.LEFT).get(TestPlans_.id)
                        )
                    );
            }
            if (criteria.getTestsuitesProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestsuitesProjectId(),
                            root -> root.join(Projects_.testsuitesProjects, JoinType.LEFT).get(TestSuites_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Projects_.users, JoinType.LEFT).get(Users_.id))
                    );
            }
        }
        return specification;
    }
}
