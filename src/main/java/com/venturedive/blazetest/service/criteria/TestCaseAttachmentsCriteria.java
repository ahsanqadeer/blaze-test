package com.venturedive.blazetest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blazetest.domain.TestCaseAttachments} entity. This class is used
 * in {@link com.venturedive.blazetest.web.rest.TestCaseAttachmentsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-case-attachments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCaseAttachmentsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter testCaseId;

    private Boolean distinct;

    public TestCaseAttachmentsCriteria() {}

    public TestCaseAttachmentsCriteria(TestCaseAttachmentsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.testCaseId = other.testCaseId == null ? null : other.testCaseId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestCaseAttachmentsCriteria copy() {
        return new TestCaseAttachmentsCriteria(this);
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

    public LongFilter getTestCaseId() {
        return testCaseId;
    }

    public LongFilter testCaseId() {
        if (testCaseId == null) {
            testCaseId = new LongFilter();
        }
        return testCaseId;
    }

    public void setTestCaseId(LongFilter testCaseId) {
        this.testCaseId = testCaseId;
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
        final TestCaseAttachmentsCriteria that = (TestCaseAttachmentsCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(testCaseId, that.testCaseId) && Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, testCaseId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCaseAttachmentsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (testCaseId != null ? "testCaseId=" + testCaseId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
