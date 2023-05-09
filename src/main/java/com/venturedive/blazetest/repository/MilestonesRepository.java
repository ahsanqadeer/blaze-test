package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.Milestones;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Milestones entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MilestonesRepository extends JpaRepository<Milestones, Long>, JpaSpecificationExecutor<Milestones> {}
