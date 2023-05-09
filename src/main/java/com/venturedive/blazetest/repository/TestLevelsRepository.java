package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.TestLevels;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestLevels entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestLevelsRepository extends JpaRepository<TestLevels, Long>, JpaSpecificationExecutor<TestLevels> {}
