package com.venturedive.blazetest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blazetest.domain.TestCases} entity. This class is used
 * in {@link com.venturedive.blazetest.web.rest.TestCasesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-cases?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCasesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter estimate;

    private IntegerFilter createdBy;

    private InstantFilter createdAt;

    private IntegerFilter updatedBy;

    private InstantFilter updatedAt;

    private StringFilter precondition;

    private BooleanFilter isAutomated;

    private LongFilter testSuiteId;

    private LongFilter sectionId;

    private LongFilter priorityId;

    private LongFilter templateId;

    private LongFilter milestoneId;

    private LongFilter testLevelId;

    private LongFilter testcaseattachmentsTestcaseId;

    private LongFilter testcasefieldsTestcaseId;

    private LongFilter testrundetailsTestcaseId;

    private Boolean distinct;

    public TestCasesCriteria() {}

    public TestCasesCriteria(TestCasesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.estimate = other.estimate == null ? null : other.estimate.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedBy = other.updatedBy == null ? null : other.updatedBy.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.precondition = other.precondition == null ? null : other.precondition.copy();
        this.isAutomated = other.isAutomated == null ? null : other.isAutomated.copy();
        this.testSuiteId = other.testSuiteId == null ? null : other.testSuiteId.copy();
        this.sectionId = other.sectionId == null ? null : other.sectionId.copy();
        this.priorityId = other.priorityId == null ? null : other.priorityId.copy();
        this.templateId = other.templateId == null ? null : other.templateId.copy();
        this.milestoneId = other.milestoneId == null ? null : other.milestoneId.copy();
        this.testLevelId = other.testLevelId == null ? null : other.testLevelId.copy();
        this.testcaseattachmentsTestcaseId =
            other.testcaseattachmentsTestcaseId == null ? null : other.testcaseattachmentsTestcaseId.copy();
        this.testcasefieldsTestcaseId = other.testcasefieldsTestcaseId == null ? null : other.testcasefieldsTestcaseId.copy();
        this.testrundetailsTestcaseId = other.testrundetailsTestcaseId == null ? null : other.testrundetailsTestcaseId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestCasesCriteria copy() {
        return new TestCasesCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getEstimate() {
        return estimate;
    }

    public StringFilter estimate() {
        if (estimate == null) {
            estimate = new StringFilter();
        }
        return estimate;
    }

    public void setEstimate(StringFilter estimate) {
        this.estimate = estimate;
    }

    public IntegerFilter getCreatedBy() {
        return createdBy;
    }

    public IntegerFilter createdBy() {
        if (createdBy == null) {
            createdBy = new IntegerFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(IntegerFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            createdAt = new InstantFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public IntegerFilter getUpdatedBy() {
        return updatedBy;
    }

    public IntegerFilter updatedBy() {
        if (updatedBy == null) {
            updatedBy = new IntegerFilter();
        }
        return updatedBy;
    }

    public void setUpdatedBy(IntegerFilter updatedBy) {
        this.updatedBy = updatedBy;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new InstantFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public StringFilter getPrecondition() {
        return precondition;
    }

    public StringFilter precondition() {
        if (precondition == null) {
            precondition = new StringFilter();
        }
        return precondition;
    }

    public void setPrecondition(StringFilter precondition) {
        this.precondition = precondition;
    }

    public BooleanFilter getIsAutomated() {
        return isAutomated;
    }

    public BooleanFilter isAutomated() {
        if (isAutomated == null) {
            isAutomated = new BooleanFilter();
        }
        return isAutomated;
    }

    public void setIsAutomated(BooleanFilter isAutomated) {
        this.isAutomated = isAutomated;
    }

    public LongFilter getTestSuiteId() {
        return testSuiteId;
    }

    public LongFilter testSuiteId() {
        if (testSuiteId == null) {
            testSuiteId = new LongFilter();
        }
        return testSuiteId;
    }

    public void setTestSuiteId(LongFilter testSuiteId) {
        this.testSuiteId = testSuiteId;
    }

    public LongFilter getSectionId() {
        return sectionId;
    }

    public LongFilter sectionId() {
        if (sectionId == null) {
            sectionId = new LongFilter();
        }
        return sectionId;
    }

    public void setSectionId(LongFilter sectionId) {
        this.sectionId = sectionId;
    }

    public LongFilter getPriorityId() {
        return priorityId;
    }

    public LongFilter priorityId() {
        if (priorityId == null) {
            priorityId = new LongFilter();
        }
        return priorityId;
    }

    public void setPriorityId(LongFilter priorityId) {
        this.priorityId = priorityId;
    }

    public LongFilter getTemplateId() {
        return templateId;
    }

    public LongFilter templateId() {
        if (templateId == null) {
            templateId = new LongFilter();
        }
        return templateId;
    }

    public void setTemplateId(LongFilter templateId) {
        this.templateId = templateId;
    }

    public LongFilter getMilestoneId() {
        return milestoneId;
    }

    public LongFilter milestoneId() {
        if (milestoneId == null) {
            milestoneId = new LongFilter();
        }
        return milestoneId;
    }

    public void setMilestoneId(LongFilter milestoneId) {
        this.milestoneId = milestoneId;
    }

    public LongFilter getTestLevelId() {
        return testLevelId;
    }

    public LongFilter testLevelId() {
        if (testLevelId == null) {
            testLevelId = new LongFilter();
        }
        return testLevelId;
    }

    public void setTestLevelId(LongFilter testLevelId) {
        this.testLevelId = testLevelId;
    }

    public LongFilter getTestcaseattachmentsTestcaseId() {
        return testcaseattachmentsTestcaseId;
    }

    public LongFilter testcaseattachmentsTestcaseId() {
        if (testcaseattachmentsTestcaseId == null) {
            testcaseattachmentsTestcaseId = new LongFilter();
        }
        return testcaseattachmentsTestcaseId;
    }

    public void setTestcaseattachmentsTestcaseId(LongFilter testcaseattachmentsTestcaseId) {
        this.testcaseattachmentsTestcaseId = testcaseattachmentsTestcaseId;
    }

    public LongFilter getTestcasefieldsTestcaseId() {
        return testcasefieldsTestcaseId;
    }

    public LongFilter testcasefieldsTestcaseId() {
        if (testcasefieldsTestcaseId == null) {
            testcasefieldsTestcaseId = new LongFilter();
        }
        return testcasefieldsTestcaseId;
    }

    public void setTestcasefieldsTestcaseId(LongFilter testcasefieldsTestcaseId) {
        this.testcasefieldsTestcaseId = testcasefieldsTestcaseId;
    }

    public LongFilter getTestrundetailsTestcaseId() {
        return testrundetailsTestcaseId;
    }

    public LongFilter testrundetailsTestcaseId() {
        if (testrundetailsTestcaseId == null) {
            testrundetailsTestcaseId = new LongFilter();
        }
        return testrundetailsTestcaseId;
    }

    public void setTestrundetailsTestcaseId(LongFilter testrundetailsTestcaseId) {
        this.testrundetailsTestcaseId = testrundetailsTestcaseId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TestCasesCriteria that = (TestCasesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(estimate, that.estimate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedBy, that.updatedBy) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(precondition, that.precondition) &&
            Objects.equals(isAutomated, that.isAutomated) &&
            Objects.equals(testSuiteId, that.testSuiteId) &&
            Objects.equals(sectionId, that.sectionId) &&
            Objects.equals(priorityId, that.priorityId) &&
            Objects.equals(templateId, that.templateId) &&
            Objects.equals(milestoneId, that.milestoneId) &&
            Objects.equals(testLevelId, that.testLevelId) &&
            Objects.equals(testcaseattachmentsTestcaseId, that.testcaseattachmentsTestcaseId) &&
            Objects.equals(testcasefieldsTestcaseId, that.testcasefieldsTestcaseId) &&
            Objects.equals(testrundetailsTestcaseId, that.testrundetailsTestcaseId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            estimate,
            createdBy,
            createdAt,
            updatedBy,
            updatedAt,
            precondition,
            isAutomated,
            testSuiteId,
            sectionId,
            priorityId,
            templateId,
            milestoneId,
            testLevelId,
            testcaseattachmentsTestcaseId,
            testcasefieldsTestcaseId,
            testrundetailsTestcaseId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCasesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (estimate != null ? "estimate=" + estimate + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedBy != null ? "updatedBy=" + updatedBy + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (precondition != null ? "precondition=" + precondition + ", " : "") +
            (isAutomated != null ? "isAutomated=" + isAutomated + ", " : "") +
            (testSuiteId != null ? "testSuiteId=" + testSuiteId + ", " : "") +
            (sectionId != null ? "sectionId=" + sectionId + ", " : "") +
            (priorityId != null ? "priorityId=" + priorityId + ", " : "") +
            (templateId != null ? "templateId=" + templateId + ", " : "") +
            (milestoneId != null ? "milestoneId=" + milestoneId + ", " : "") +
            (testLevelId != null ? "testLevelId=" + testLevelId + ", " : "") +
            (testcaseattachmentsTestcaseId != null ? "testcaseattachmentsTestcaseId=" + testcaseattachmentsTestcaseId + ", " : "") +
            (testcasefieldsTestcaseId != null ? "testcasefieldsTestcaseId=" + testcasefieldsTestcaseId + ", " : "") +
            (testrundetailsTestcaseId != null ? "testrundetailsTestcaseId=" + testrundetailsTestcaseId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
