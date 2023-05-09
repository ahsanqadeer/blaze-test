package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.TestRuns;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestRuns entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestRunsRepository extends JpaRepository<TestRuns, Long>, JpaSpecificationExecutor<TestRuns> {}
