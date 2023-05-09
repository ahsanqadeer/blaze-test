package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.TemplateFields;
import com.venturedive.blazetest.repository.TemplateFieldsRepository;
import com.venturedive.blazetest.service.criteria.TemplateFieldsCriteria;
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
 * Service for executing complex queries for {@link TemplateFields} entities in the database.
 * The main input is a {@link TemplateFieldsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TemplateFields} or a {@link Page} of {@link TemplateFields} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TemplateFieldsQueryService extends QueryService<TemplateFields> {

    private final Logger log = LoggerFactory.getLogger(TemplateFieldsQueryService.class);

    private final TemplateFieldsRepository templateFieldsRepository;

    public TemplateFieldsQueryService(TemplateFieldsRepository templateFieldsRepository) {
        this.templateFieldsRepository = templateFieldsRepository;
    }

    /**
     * Return a {@link List} of {@link TemplateFields} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TemplateFields> findByCriteria(TemplateFieldsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TemplateFields> specification = createSpecification(criteria);
        return templateFieldsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TemplateFields} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TemplateFields> findByCriteria(TemplateFieldsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TemplateFields> specification = createSpecification(criteria);
        return templateFieldsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TemplateFieldsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TemplateFields> specification = createSpecification(criteria);
        return templateFieldsRepository.count(specification);
    }

    /**
     * Function to convert {@link TemplateFieldsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TemplateFields> createSpecification(TemplateFieldsCriteria criteria) {
        Specification<TemplateFields> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TemplateFields_.id));
            }
            if (criteria.getFieldName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFieldName(), TemplateFields_.fieldName));
            }
            if (criteria.getCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompanyId(),
                            root -> root.join(TemplateFields_.company, JoinType.LEFT).get(Companies_.id)
                        )
                    );
            }
            if (criteria.getTemplateFieldTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTemplateFieldTypeId(),
                            root -> root.join(TemplateFields_.templateFieldType, JoinType.LEFT).get(TemplateFieldTypes_.id)
                        )
                    );
            }
            if (criteria.getTestcasefieldsTemplatefieldId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestcasefieldsTemplatefieldId(),
                            root -> root.join(TemplateFields_.testcasefieldsTemplatefields, JoinType.LEFT).get(TestCaseFields_.id)
                        )
                    );
            }
            if (criteria.getTemplateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTemplateId(),
                            root -> root.join(TemplateFields_.templates, JoinType.LEFT).get(Templates_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
