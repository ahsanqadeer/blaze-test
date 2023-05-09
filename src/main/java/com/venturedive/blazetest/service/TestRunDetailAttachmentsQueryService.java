package com.venturedive.blazetest.service;

import com.venturedive.blazetest.domain.*; // for static metamodels
import com.venturedive.blazetest.domain.TestRunDetailAttachments;
import com.venturedive.blazetest.repository.TestRunDetailAttachmentsRepository;
import com.venturedive.blazetest.service.criteria.TestRunDetailAttachmentsCriteria;
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
 * Service for executing complex queries for {@link TestRunDetailAttachments} entities in the database.
 * The main input is a {@link TestRunDetailAttachmentsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestRunDetailAttachments} or a {@link Page} of {@link TestRunDetailAttachments} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestRunDetailAttachmentsQueryService extends QueryService<TestRunDetailAttachments> {

    private final Logger log = LoggerFactory.getLogger(TestRunDetailAttachmentsQueryService.class);

    private final TestRunDetailAttachmentsRepository testRunDetailAttachmentsRepository;

    public TestRunDetailAttachmentsQueryService(TestRunDetailAttachmentsRepository testRunDetailAttachmentsRepository) {
        this.testRunDetailAttachmentsRepository = testRunDetailAttachmentsRepository;
    }

    /**
     * Return a {@link List} of {@link TestRunDetailAttachments} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestRunDetailAttachments> findByCriteria(TestRunDetailAttachmentsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TestRunDetailAttachments> specification = createSpecification(criteria);
        return testRunDetailAttachmentsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TestRunDetailAttachments} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestRunDetailAttachments> findByCriteria(TestRunDetailAttachmentsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TestRunDetailAttachments> specification = createSpecification(criteria);
        return testRunDetailAttachmentsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestRunDetailAttachmentsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TestRunDetailAttachments> specification = createSpecification(criteria);
        return testRunDetailAttachmentsRepository.count(specification);
    }

    /**
     * Function to convert {@link TestRunDetailAttachmentsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TestRunDetailAttachments> createSpecification(TestRunDetailAttachmentsCriteria criteria) {
        Specification<TestRunDetailAttachments> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TestRunDetailAttachments_.id));
            }
            if (criteria.getTestRunDetailId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTestRunDetailId(),
                            root -> root.join(TestRunDetailAttachments_.testRunDetail, JoinType.LEFT).get(TestRunDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
