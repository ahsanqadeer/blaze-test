package com.venturedive.blazetest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blazetest.domain.TestRunStepDetails} entity. This class is used
 * in {@link com.venturedive.blazetest.web.rest.TestRunStepDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-run-step-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestRunStepDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter testRunDetailId;

    private LongFilter stepDetailId;

    private LongFilter statusId;

    private LongFilter testrunstepdetailattachmentsTestrunstepdetailId;

    private Boolean distinct;

    public TestRunStepDetailsCriteria() {}

    public TestRunStepDetailsCriteria(TestRunStepDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.testRunDetailId = other.testRunDetailId == null ? null : other.testRunDetailId.copy();
        this.stepDetailId = other.stepDetailId == null ? null : other.stepDetailId.copy();
        this.statusId = other.statusId == null ? null : other.statusId.copy();
        this.testrunstepdetailattachmentsTestrunstepdetailId =
            other.testrunstepdetailattachmentsTestrunstepdetailId == null
                ? null
                : other.testrunstepdetailattachmentsTestrunstepdetailId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestRunStepDetailsCriteria copy() {
        return new TestRunStepDetailsCriteria(this);
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

    public LongFilter getTestRunDetailId() {
        return testRunDetailId;
    }

    public LongFilter testRunDetailId() {
        if (testRunDetailId == null) {
            testRunDetailId = new LongFilter();
        }
        return testRunDetailId;
    }

    public void setTestRunDetailId(LongFilter testRunDetailId) {
        this.testRunDetailId = testRunDetailId;
    }

    public LongFilter getStepDetailId() {
        return stepDetailId;
    }

    public LongFilter stepDetailId() {
        if (stepDetailId == null) {
            stepDetailId = new LongFilter();
        }
        return stepDetailId;
    }

    public void setStepDetailId(LongFilter stepDetailId) {
        this.stepDetailId = stepDetailId;
    }

    public LongFilter getStatusId() {
        return statusId;
    }

    public LongFilter statusId() {
        if (statusId == null) {
            statusId = new LongFilter();
        }
        return statusId;
    }

    public void setStatusId(LongFilter statusId) {
        this.statusId = statusId;
    }

    public LongFilter getTestrunstepdetailattachmentsTestrunstepdetailId() {
        return testrunstepdetailattachmentsTestrunstepdetailId;
    }

    public LongFilter testrunstepdetailattachmentsTestrunstepdetailId() {
        if (testrunstepdetailattachmentsTestrunstepdetailId == null) {
            testrunstepdetailattachmentsTestrunstepdetailId = new LongFilter();
        }
        return testrunstepdetailattachmentsTestrunstepdetailId;
    }

    public void setTestrunstepdetailattachmentsTestrunstepdetailId(LongFilter testrunstepdetailattachmentsTestrunstepdetailId) {
        this.testrunstepdetailattachmentsTestrunstepdetailId = testrunstepdetailattachmentsTestrunstepdetailId;
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
        final TestRunStepDetailsCriteria that = (TestRunStepDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(testRunDetailId, that.testRunDetailId) &&
            Objects.equals(stepDetailId, that.stepDetailId) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(testrunstepdetailattachmentsTestrunstepdetailId, that.testrunstepdetailattachmentsTestrunstepdetailId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, testRunDetailId, stepDetailId, statusId, testrunstepdetailattachmentsTestrunstepdetailId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestRunStepDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (testRunDetailId != null ? "testRunDetailId=" + testRunDetailId + ", " : "") +
            (stepDetailId != null ? "stepDetailId=" + stepDetailId + ", " : "") +
            (statusId != null ? "statusId=" + statusId + ", " : "") +
            (testrunstepdetailattachmentsTestrunstepdetailId != null ? "testrunstepdetailattachmentsTestrunstepdetailId=" + testrunstepdetailattachmentsTestrunstepdetailId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
