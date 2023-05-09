package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.TestCasePriorities;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestCasePriorities entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestCasePrioritiesRepository
    extends JpaRepository<TestCasePriorities, Long>, JpaSpecificationExecutor<TestCasePriorities> {}
