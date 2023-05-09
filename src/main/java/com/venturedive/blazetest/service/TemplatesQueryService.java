package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.Templates;
import com.venturedive.blazetest.repository.TemplatesRepository;
import com.venturedive.blazetest.service.criteria.TemplatesCriteria;
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
 * Service for executing complex queries for {@link Templates} entities in the database.
 * The main input is a {@link TemplatesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Templates} or a {@link Page} of {@link Templates} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TemplatesQueryService extends QueryService<Templates> {

    private final Logger log = LoggerFactory.getLogger(TemplatesQueryService.class);

    private final TemplatesRepository templatesRepository;

    public TemplatesQueryService(TemplatesRepository templatesRepository) {
        this.templatesRepository = templatesRepository;
    }

    /**
     * Return a {@link List} of {@link Templates} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Templates> findByCriteria(TemplatesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Templates> specification = createSpecification(criteria);
        return templatesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Templates} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Templates> findByCriteria(TemplatesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Templates> specification = createSpecification(criteria);
        return templatesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TemplatesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Templates> specification = createSpecification(criteria);
        return templatesRepository.count(specification);
    }

    /**
     * Function to convert {@link TemplatesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Templates> createSpecification(TemplatesCriteria criteria) {
        Specification<Templates> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Templates_.id));
            }
            if (criteria.getTemplateName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTemplateName(), Templates_.templateName));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Templates_.createdAt));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), Templates_.createdBy));
            }
            if (criteria.getCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCompanyId(), root -> root.join(Templates_.company, JoinType.LEFT).get(Companies_.id))
                    );
            }
            if (criteria.getTemplateFieldId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTemplateFieldId(),
                            root -> root.join(Templates_.templateFields, JoinType.LEFT).get(TemplateFields_.id)
                        )
                    );
            }
            if (criteria.getProjectsDefaulttemplateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProjectsDefaulttemplateId(),
                            root -> root.join(Templates_.projectsDefaulttemplates, JoinType.LEFT).get(Projects_.id)
                        )
                    );
            }
            if (criteria.getTestcasesTemplateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestcasesTemplateId(),
                            root -> root.join(Templates_.testcasesTemplates, JoinType.LEFT).get(TestCases_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
