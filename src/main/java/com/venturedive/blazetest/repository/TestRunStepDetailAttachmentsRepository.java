package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.TestRunStepDetailAttachments;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestRunStepDetailAttachments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestRunStepDetailAttachmentsRepository
    extends JpaRepository<TestRunStepDetailAttachments, Long>, JpaSpecificationExecutor<TestRunStepDetailAttachments> {}
