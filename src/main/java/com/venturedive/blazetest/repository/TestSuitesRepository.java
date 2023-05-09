package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.TestSuites;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestSuites entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestSuitesRepository extends JpaRepository<TestSuites, Long>, JpaSpecificationExecutor<TestSuites> {}
