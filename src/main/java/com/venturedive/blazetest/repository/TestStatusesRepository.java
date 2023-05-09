package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.TestStatuses;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestStatuses entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestStatusesRepository extends JpaRepository<TestStatuses, Long>, JpaSpecificationExecutor<TestStatuses> {}
