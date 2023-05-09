package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.TestCaseFieldAttachments;
import com.venturedive.blazetest.repository.TestCaseFieldAttachmentsRepository;
import com.venturedive.blazetest.service.criteria.TestCaseFieldAttachmentsCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TestCaseFieldAttachments} entities in the database.
 * The main input is a {@link TestCaseFieldAttachmentsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestCaseFieldAttachments} or a {@link Page} of {@link TestCaseFieldAttachments} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestCaseFieldAttachmentsQueryService extends QueryService<TestCaseFieldAttachments> {

    private final Logger log = LoggerFactory.getLogger(TestCaseFieldAttachmentsQueryService.class);

    private final TestCaseFieldAttachmentsRepository testCaseFieldAttachmentsRepository;

    public TestCaseFieldAttachmentsQueryService(TestCaseFieldAttachmentsRepository testCaseFieldAttachmentsRepository) {
        this.testCaseFieldAttachmentsRepository = testCaseFieldAttachmentsRepository;
    }

    /**
     * Return a {@link List} of {@link TestCaseFieldAttachments} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestCaseFieldAttachments> findByCriteria(TestCaseFieldAttachmentsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestCaseFieldAttachments> specification = createSpecification(criteria);
        return testCaseFieldAttachmentsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestCaseFieldAttachments} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCaseFieldAttachments> findByCriteria(TestCaseFieldAttachmentsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestCaseFieldAttachments> specification = createSpecification(criteria);
        return testCaseFieldAttachmentsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestCaseFieldAttachmentsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestCaseFieldAttachments> specification = createSpecification(criteria);
        return testCaseFieldAttachmentsRepository.count(specification);
    }

    /**
     * Function to convert {@link TestCaseFieldAttachmentsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestCaseFieldAttachments> createSpecification(TestCaseFieldAttachmentsCriteria criteria) {
        Specification<TestCaseFieldAttachments> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestCaseFieldAttachments_.id));
            }
            if (criteria.getTestCaseFieldId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestCaseFieldId(),
                            root -> root.join(TestCaseFieldAttachments_.testCaseField, JoinType.LEFT).get(TestCaseFields_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
