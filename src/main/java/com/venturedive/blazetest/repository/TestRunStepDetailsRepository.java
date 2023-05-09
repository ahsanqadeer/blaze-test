package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.TestRunStepDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestRunStepDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestRunStepDetailsRepository
    extends JpaRepository<TestRunStepDetails, Long>, JpaSpecificationExecutor<TestRunStepDetails> {}
