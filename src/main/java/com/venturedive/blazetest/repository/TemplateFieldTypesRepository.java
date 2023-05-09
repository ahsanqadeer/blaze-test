package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.TemplateFieldTypes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TemplateFieldTypes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TemplateFieldTypesRepository
    extends JpaRepository<TemplateFieldTypes, Long>, JpaSpecificationExecutor<TemplateFieldTypes> {}
