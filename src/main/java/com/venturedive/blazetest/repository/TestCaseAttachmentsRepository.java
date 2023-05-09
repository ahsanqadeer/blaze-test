package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.TestCaseAttachments;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestCaseAttachments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestCaseAttachmentsRepository
    extends JpaRepository<TestCaseAttachments, Long>, JpaSpecificationExecutor<TestCaseAttachments> {}
