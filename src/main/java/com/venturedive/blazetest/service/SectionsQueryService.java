package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.Sections;
import com.venturedive.blazetest.repository.SectionsRepository;
import com.venturedive.blazetest.service.criteria.SectionsCriteria;
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
 * Service for executing complex queries for {@link Sections} entities in the database.
 * The main input is a {@link SectionsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Sections} or a {@link Page} of {@link Sections} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SectionsQueryService extends QueryService<Sections> {

    private final Logger log = LoggerFactory.getLogger(SectionsQueryService.class);

    private final SectionsRepository sectionsRepository;

    public SectionsQueryService(SectionsRepository sectionsRepository) {
        this.sectionsRepository = sectionsRepository;
    }

    /**
     * Return a {@link List} of {@link Sections} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Sections> findByCriteria(SectionsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sections> specification = createSpecification(criteria);
        return sectionsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Sections} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Sections> findByCriteria(SectionsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sections> specification = createSpecification(criteria);
        return sectionsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SectionsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sections> specification = createSpecification(criteria);
        return sectionsRepository.count(specification);
    }

    /**
     * Function to convert {@link SectionsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Sections> createSpecification(SectionsCriteria criteria) {
        Specification<Sections> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Sections_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Sections_.name));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Sections_.createdAt));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), Sections_.createdBy));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Sections_.updatedAt));
            }
            if (criteria.getUpdatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedBy(), Sections_.updatedBy));
            }
            if (criteria.getTestSuiteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestSuiteId(),
                            root -> root.join(Sections_.testSuite, JoinType.LEFT).get(TestSuites_.id)
                        )
                    );
            }
            if (criteria.getParentSectionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParentSectionId(),
                            root -> root.join(Sections_.parentSection, JoinType.LEFT).get(Sections_.id)
                        )
                    );
            }
            if (criteria.getSectionsParentsectionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSectionsParentsectionId(),
                            root -> root.join(Sections_.sectionsParentsections, JoinType.LEFT).get(Sections_.id)
                        )
                    );
            }
            if (criteria.getTestcasesSectionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestcasesSectionId(),
                            root -> root.join(Sections_.testcasesSections, JoinType.LEFT).get(TestCases_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
