package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.TestRunDetailAttachments;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestRunDetailAttachments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestRunDetailAttachmentsRepository
    extends JpaRepository<TestRunDetailAttachments, Long>, JpaSpecificationExecutor<TestRunDetailAttachments> {}
