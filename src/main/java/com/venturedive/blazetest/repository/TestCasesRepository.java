package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.TestCases;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestCases entity.
 *
 * When extending this class, extend TestCasesRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface TestCasesRepository
    extends TestCasesRepositoryWithBagRelationships, JpaRepository<TestCases, Long>, JpaSpecificationExecutor<TestCases> {
    default Optional<TestCases> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<TestCases> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<TestCases> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct testCases from TestCases testCases left join fetch testCases.priority",
        countQuery = "select count(distinct testCases) from TestCases testCases"
    )
    Page<TestCases> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct testCases from TestCases testCases left join fetch testCases.priority")
    List<TestCases> findAllWithToOneRelationships();

    @Query("select testCases from TestCases testCases left join fetch testCases.priority where testCases.id =:id")
    Optional<TestCases> findOneWithToOneRelationships(@Param("id") Long id);
}
