package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.TestPlans;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestPlans entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestPlansRepository extends JpaRepository<TestPlans, Long>, JpaSpecificationExecutor<TestPlans> {}
