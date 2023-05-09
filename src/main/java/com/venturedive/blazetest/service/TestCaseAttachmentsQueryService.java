package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.TestCaseAttachments;
import com.venturedive.blazetest.repository.TestCaseAttachmentsRepository;
import com.venturedive.blazetest.service.criteria.TestCaseAttachmentsCriteria;
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
 * Service for executing complex queries for {@link TestCaseAttachments} entities in the database.
 * The main input is a {@link TestCaseAttachmentsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestCaseAttachments} or a {@link Page} of {@link TestCaseAttachments} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestCaseAttachmentsQueryService extends QueryService<TestCaseAttachments> {

    private final Logger log = LoggerFactory.getLogger(TestCaseAttachmentsQueryService.class);

    private final TestCaseAttachmentsRepository testCaseAttachmentsRepository;

    public TestCaseAttachmentsQueryService(TestCaseAttachmentsRepository testCaseAttachmentsRepository) {
        this.testCaseAttachmentsRepository = testCaseAttachmentsRepository;
    }

    /**
     * Return a {@link List} of {@link TestCaseAttachments} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestCaseAttachments> findByCriteria(TestCaseAttachmentsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestCaseAttachments> specification = createSpecification(criteria);
        return testCaseAttachmentsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestCaseAttachments} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestCaseAttachments> findByCriteria(TestCaseAttachmentsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestCaseAttachments> specification = createSpecification(criteria);
        return testCaseAttachmentsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestCaseAttachmentsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestCaseAttachments> specification = createSpecification(criteria);
        return testCaseAttachmentsRepository.count(specification);
    }

    /**
     * Function to convert {@link TestCaseAttachmentsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestCaseAttachments> createSpecification(TestCaseAttachmentsCriteria criteria) {
        Specification<TestCaseAttachments> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestCaseAttachments_.id));
            }
            if (criteria.getTestCaseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestCaseId(),
                            root -> root.join(TestCaseAttachments_.testCase, JoinType.LEFT).get(TestCases_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
