package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.Companies;
import com.venturedive.blazetest.repository.CompaniesRepository;
import com.venturedive.blazetest.service.criteria.CompaniesCriteria;
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
 * Service for executing complex queries for {@link Companies} entities in the database.
 * The main input is a {@link CompaniesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Companies} or a {@link Page} of {@link Companies} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompaniesQueryService extends QueryService<Companies> {

    private final Logger log = LoggerFactory.getLogger(CompaniesQueryService.class);

    private final CompaniesRepository companiesRepository;

    public CompaniesQueryService(CompaniesRepository companiesRepository) {
        this.companiesRepository = companiesRepository;
    }

    /**
     * Return a {@link List} of {@link Companies} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Companies> findByCriteria(CompaniesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Companies> specification = createSpecification(criteria);
        return companiesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Companies} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Companies> findByCriteria(CompaniesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Companies> specification = createSpecification(criteria);
        return companiesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompaniesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Companies> specification = createSpecification(criteria);
        return companiesRepository.count(specification);
    }

    /**
     * Function to convert {@link CompaniesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Companies> createSpecification(CompaniesCriteria criteria) {
        Specification<Companies> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Companies_.id));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), Companies_.country));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Companies_.url));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Companies_.name));
            }
            if (criteria.getExpectedNoOfUsers() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpectedNoOfUsers(), Companies_.expectedNoOfUsers));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedBy(), Companies_.createdBy));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Companies_.createdAt));
            }
            if (criteria.getUpdatedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedBy(), Companies_.updatedBy));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Companies_.updatedAt));
            }
            if (criteria.getProjectsCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProjectsCompanyId(),
                            root -> root.join(Companies_.projectsCompanies, JoinType.LEFT).get(Projects_.id)
                        )
                    );
            }
            if (criteria.getTemplatefieldsCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTemplatefieldsCompanyId(),
                            root -> root.join(Companies_.templatefieldsCompanies, JoinType.LEFT).get(TemplateFields_.id)
                        )
                    );
            }
            if (criteria.getTemplatesCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTemplatesCompanyId(),
                            root -> root.join(Companies_.templatesCompanies, JoinType.LEFT).get(Templates_.id)
                        )
                    );
            }
            if (criteria.getUsersCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUsersCompanyId(),
                            root -> root.join(Companies_.usersCompanies, JoinType.LEFT).get(Users_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
