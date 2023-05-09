package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.TestCaseFieldAttachments;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestCaseFieldAttachments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestCaseFieldAttachmentsRepository
    extends JpaRepository<TestCaseFieldAttachments, Long>, JpaSpecificationExecutor<TestCaseFieldAttachments> {}
