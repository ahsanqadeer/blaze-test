package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.TestCases;
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
public class TestCasesRepositoryWithBagRelationshipsImpl implements TestCasesRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TestCases> fetchBagRelationships(Optional<TestCases> testCases) {
        return testCases.map(this::fetchTestLevels);
    }

    @Override
    public Page<TestCases> fetchBagRelationships(Page<TestCases> testCases) {
        return new PageImpl<>(fetchBagRelationships(testCases.getContent()), testCases.getPageable(), testCases.getTotalElements());
    }

    @Override
    public List<TestCases> fetchBagRelationships(List<TestCases> testCases) {
        return Optional.of(testCases).map(this::fetchTestLevels).orElse(Collections.emptyList());
    }

    TestCases fetchTestLevels(TestCases result) {
        return entityManager
            .createQuery(
                "select testCases from TestCases testCases left join fetch testCases.testLevels where testCases is :testCases",
                TestCases.class
            )
            .setParameter("testCases", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<TestCases> fetchTestLevels(List<TestCases> testCases) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, testCases.size()).forEach(index -> order.put(testCases.get(index).getId(), index));
        List<TestCases> result = entityManager
            .createQuery(
                "select distinct testCases from TestCases testCases left join fetch testCases.testLevels where testCases in :testCases",
                TestCases.class
            )
            .setParameter("testCases", testCases)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
