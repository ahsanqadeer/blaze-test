package com.venturedive.blazetest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blazetest.domain.TestCaseFieldAttachments} entity. This class is used
 * in {@link com.venturedive.blazetest.web.rest.TestCaseFieldAttachmentsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-case-field-attachments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCaseFieldAttachmentsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter testCaseFieldId;

    private Boolean distinct;

    public TestCaseFieldAttachmentsCriteria() {}

    public TestCaseFieldAttachmentsCriteria(TestCaseFieldAttachmentsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.testCaseFieldId = other.testCaseFieldId == null ? null : other.testCaseFieldId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestCaseFieldAttachmentsCriteria copy() {
        return new TestCaseFieldAttachmentsCriteria(this);
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

    public LongFilter getTestCaseFieldId() {
        return testCaseFieldId;
    }

    public LongFilter testCaseFieldId() {
        if (testCaseFieldId == null) {
            testCaseFieldId = new LongFilter();
        }
        return testCaseFieldId;
    }

    public void setTestCaseFieldId(LongFilter testCaseFieldId) {
        this.testCaseFieldId = testCaseFieldId;
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
        final TestCaseFieldAttachmentsCriteria that = (TestCaseFieldAttachmentsCriteria) o;
        return (
            Objects.equals(id, that.id) && Objects.equals(testCaseFieldId, that.testCaseFieldId) && Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, testCaseFieldId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCaseFieldAttachmentsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (testCaseFieldId != null ? "testCaseFieldId=" + testCaseFieldId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
