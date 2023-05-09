package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.Permissions;
import com.venturedive.blazetest.repository.PermissionsRepository;
import com.venturedive.blazetest.service.criteria.PermissionsCriteria;
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
 * Service for executing complex queries for {@link Permissions} entities in the database.
 * The main input is a {@link PermissionsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Permissions} or a {@link Page} of {@link Permissions} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PermissionsQueryService extends QueryService<Permissions> {

    private final Logger log = LoggerFactory.getLogger(PermissionsQueryService.class);

    private final PermissionsRepository permissionsRepository;

    public PermissionsQueryService(PermissionsRepository permissionsRepository) {
        this.permissionsRepository = permissionsRepository;
    }

    /**
     * Return a {@link List} of {@link Permissions} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Permissions> findByCriteria(PermissionsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Permissions> specification = createSpecification(criteria);
        return permissionsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Permissions} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Permissions> findByCriteria(PermissionsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Permissions> specification = createSpecification(criteria);
        return permissionsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PermissionsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Permissions> specification = createSpecification(criteria);
        return permissionsRepository.count(specification);
    }

    /**
     * Function to convert {@link PermissionsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Permissions> createSpecification(PermissionsCriteria criteria) {
        Specification<Permissions> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Permissions_.id));
            }
            if (criteria.getPermissionName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPermissionName(), Permissions_.permissionName));
            }
            if (criteria.getRoleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRoleId(), root -> root.join(Permissions_.roles, JoinType.LEFT).get(Roles_.id))
                    );
            }
        }
        return specification;
    }
}
