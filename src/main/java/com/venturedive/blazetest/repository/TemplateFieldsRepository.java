package com.venturedive.blazetest.repository;

import com.venturedive.blazetest.domain.TemplateFields;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TemplateFields entity.
 */
@Repository
public interface TemplateFieldsRepository extends JpaRepository<TemplateFields, Long>, JpaSpecificationExecutor<TemplateFields> {
    default Optional<TemplateFields> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TemplateFields> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TemplateFields> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct templateFields from TemplateFields templateFields left join fetch templateFields.templateFieldType",
        countQuery = "select count(distinct templateFields) from TemplateFields templateFields"
    )
    Page<TemplateFields> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct templateFields from TemplateFields templateFields left join fetch templateFields.templateFieldType")
    List<TemplateFields> findAllWithToOneRelationships();

    @Query(
        "select templateFields from TemplateFields templateFields left join fetch templateFields.templateFieldType where templateFields.id =:id"
    )
    Optional<TemplateFields> findOneWithToOneRelationships(@Param("id") Long id);
}
