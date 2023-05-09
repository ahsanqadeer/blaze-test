package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.Sections;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Sections entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SectionsRepository extends JpaRepository<Sections, Long>, JpaSpecificationExecutor<Sections> {}
