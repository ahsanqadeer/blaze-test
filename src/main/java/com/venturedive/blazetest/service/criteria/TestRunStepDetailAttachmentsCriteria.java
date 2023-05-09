package com.venturedive.blazetest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blazetest.domain.TestRunStepDetailAttachments} entity. This class is used
 * in {@link com.venturedive.blazetest.web.rest.TestRunStepDetailAttachmentsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-run-step-detail-attachments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestRunStepDetailAttachmentsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter testRunStepDetailId;

    private Boolean distinct;

    public TestRunStepDetailAttachmentsCriteria() {}

    public TestRunStepDetailAttachmentsCriteria(TestRunStepDetailAttachmentsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.testRunStepDetailId = other.testRunStepDetailId == null ? null : other.testRunStepDetailId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestRunStepDetailAttachmentsCriteria copy() {
        return new TestRunStepDetailAttachmentsCriteria(this);
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

    public LongFilter getTestRunStepDetailId() {
        return testRunStepDetailId;
    }

    public LongFilter testRunStepDetailId() {
        if (testRunStepDetailId == null) {
            testRunStepDetailId = new LongFilter();
        }
        return testRunStepDetailId;
    }

    public void setTestRunStepDetailId(LongFilter testRunStepDetailId) {
        this.testRunStepDetailId = testRunStepDetailId;
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
        final TestRunStepDetailAttachmentsCriteria that = (TestRunStepDetailAttachmentsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(testRunStepDetailId, that.testRunStepDetailId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, testRunStepDetailId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestRunStepDetailAttachmentsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (testRunStepDetailId != null ? "testRunStepDetailId=" + testRunStepDetailId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
