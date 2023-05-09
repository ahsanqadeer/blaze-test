package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.TemplateFieldTypes;
import com.venturedive.blazetest.repository.TemplateFieldTypesRepository;
import com.venturedive.blazetest.service.criteria.TemplateFieldTypesCriteria;
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
 * Service for executing complex queries for {@link TemplateFieldTypes} entities in the database.
 * The main input is a {@link TemplateFieldTypesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TemplateFieldTypes} or a {@link Page} of {@link TemplateFieldTypes} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TemplateFieldTypesQueryService extends QueryService<TemplateFieldTypes> {

    private final Logger log = LoggerFactory.getLogger(TemplateFieldTypesQueryService.class);

    private final TemplateFieldTypesRepository templateFieldTypesRepository;

    public TemplateFieldTypesQueryService(TemplateFieldTypesRepository templateFieldTypesRepository) {
        this.templateFieldTypesRepository = templateFieldTypesRepository;
    }

    /**
     * Return a {@link List} of {@link TemplateFieldTypes} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TemplateFieldTypes> findByCriteria(TemplateFieldTypesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TemplateFieldTypes> specification = createSpecification(criteria);
        return templateFieldTypesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TemplateFieldTypes} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TemplateFieldTypes> findByCriteria(TemplateFieldTypesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TemplateFieldTypes> specification = createSpecification(criteria);
        return templateFieldTypesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TemplateFieldTypesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TemplateFieldTypes> specification = createSpecification(criteria);
        return templateFieldTypesRepository.count(specification);
    }

    /**
     * Function to convert {@link TemplateFieldTypesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TemplateFieldTypes> createSpecification(TemplateFieldTypesCriteria criteria) {
        Specification<TemplateFieldTypes> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TemplateFieldTypes_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), TemplateFieldTypes_.type));
            }
            if (criteria.getIsList() != null) {
                specification = specification.and(buildSpecification(criteria.getIsList(), TemplateFieldTypes_.isList));
            }
            if (criteria.getAttachments() != null) {
                specification = specification.and(buildSpecification(criteria.getAttachments(), TemplateFieldTypes_.attachments));
            }
            if (criteria.getTemplatefieldsTemplatefieldtypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTemplatefieldsTemplatefieldtypeId(),
                            root -> root.join(TemplateFieldTypes_.templatefieldsTemplatefieldtypes, JoinType.LEFT).get(TemplateFields_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
