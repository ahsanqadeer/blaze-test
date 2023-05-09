package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.TestCaseFields;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestCaseFields entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestCaseFieldsRepository extends JpaRepository<TestCaseFields, Long>, JpaSpecificationExecutor<TestCaseFields> {}
