package com.venturedive.blazetest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blazetest.domain.TestRunDetailAttachments} entity. This class is used
 * in {@link com.venturedive.blazetest.web.rest.TestRunDetailAttachmentsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-run-detail-attachments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestRunDetailAttachmentsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter testRunDetailId;

    private Boolean distinct;

    public TestRunDetailAttachmentsCriteria() {}

    public TestRunDetailAttachmentsCriteria(TestRunDetailAttachmentsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.testRunDetailId = other.testRunDetailId == null ? null : other.testRunDetailId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestRunDetailAttachmentsCriteria copy() {
        return new TestRunDetailAttachmentsCriteria(this);
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
        final TestRunDetailAttachmentsCriteria that = (TestRunDetailAttachmentsCriteria) o;
        return (
            Objects.equals(id, that.id) && Objects.equals(testRunDetailId, that.testRunDetailId) && Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, testRunDetailId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestRunDetailAttachmentsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (testRunDetailId != null ? "testRunDetailId=" + testRunDetailId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
