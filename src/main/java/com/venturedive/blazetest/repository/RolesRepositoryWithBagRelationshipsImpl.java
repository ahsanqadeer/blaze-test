package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.Roles;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class RolesRepositoryWithBagRelationshipsImpl implements RolesRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Roles> fetchBagRelationships(Optional<Roles> roles) {
        return roles.map(this::fetchPermissions).map(this::fetchUsers);
    }

    @Override
    public Page<Roles> fetchBagRelationships(Page<Roles> roles) {
        return new PageImpl<>(fetchBagRelationships(roles.getContent()), roles.getPageable(), roles.getTotalElements());
    }

    @Override
    public List<Roles> fetchBagRelationships(List<Roles> roles) {
        return Optional.of(roles).map(this::fetchPermissions).map(this::fetchUsers).orElse(Collections.emptyList());
    }

    Roles fetchPermissions(Roles result) {
        return entityManager
            .createQuery("select roles from Roles roles left join fetch roles.permissions where roles is :roles", Roles.class)
            .setParameter("roles", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Roles> fetchPermissions(List<Roles> roles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, roles.size()).forEach(index -> order.put(roles.get(index).getId(), index));
        List<Roles> result = entityManager
            .createQuery("select distinct roles from Roles roles left join fetch roles.permissions where roles in :roles", Roles.class)
            .setParameter("roles", roles)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Roles fetchUsers(Roles result) {
        return entityManager
            .createQuery("select roles from Roles roles left join fetch roles.users where roles is :roles", Roles.class)
            .setParameter("roles", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Roles> fetchUsers(List<Roles> roles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, roles.size()).forEach(index -> order.put(roles.get(index).getId(), index));
        List<Roles> result = entityManager
            .createQuery("select distinct roles from Roles roles left join fetch roles.users where roles in :roles", Roles.class)
            .setParameter("roles", roles)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
