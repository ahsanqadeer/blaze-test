package com.venturedive.blazetest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blazetest.domain.TestCaseFields} entity. This class is used
 * in {@link com.venturedive.blazetest.web.rest.TestCaseFieldsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-case-fields?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCaseFieldsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter value;

    private LongFilter templateFieldId;

    private LongFilter testCaseId;

    private LongFilter testcasefieldattachmentsTestcasefieldId;

    private LongFilter testrunstepdetailsStepdetailId;

    private Boolean distinct;

    public TestCaseFieldsCriteria() {}

    public TestCaseFieldsCriteria(TestCaseFieldsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.templateFieldId = other.templateFieldId == null ? null : other.templateFieldId.copy();
        this.testCaseId = other.testCaseId == null ? null : other.testCaseId.copy();
        this.testcasefieldattachmentsTestcasefieldId =
            other.testcasefieldattachmentsTestcasefieldId == null ? null : other.testcasefieldattachmentsTestcasefieldId.copy();
        this.testrunstepdetailsStepdetailId =
            other.testrunstepdetailsStepdetailId == null ? null : other.testrunstepdetailsStepdetailId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestCaseFieldsCriteria copy() {
        return new TestCaseFieldsCriteria(this);
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

    public StringFilter getValue() {
        return value;
    }

    public StringFilter value() {
        if (value == null) {
            value = new StringFilter();
        }
        return value;
    }

    public void setValue(StringFilter value) {
        this.value = value;
    }

    public LongFilter getTemplateFieldId() {
        return templateFieldId;
    }

    public LongFilter templateFieldId() {
        if (templateFieldId == null) {
            templateFieldId = new LongFilter();
        }
        return templateFieldId;
    }

    public void setTemplateFieldId(LongFilter templateFieldId) {
        this.templateFieldId = templateFieldId;
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

    public LongFilter getTestcasefieldattachmentsTestcasefieldId() {
        return testcasefieldattachmentsTestcasefieldId;
    }

    public LongFilter testcasefieldattachmentsTestcasefieldId() {
        if (testcasefieldattachmentsTestcasefieldId == null) {
            testcasefieldattachmentsTestcasefieldId = new LongFilter();
        }
        return testcasefieldattachmentsTestcasefieldId;
    }

    public void setTestcasefieldattachmentsTestcasefieldId(LongFilter testcasefieldattachmentsTestcasefieldId) {
        this.testcasefieldattachmentsTestcasefieldId = testcasefieldattachmentsTestcasefieldId;
    }

    public LongFilter getTestrunstepdetailsStepdetailId() {
        return testrunstepdetailsStepdetailId;
    }

    public LongFilter testrunstepdetailsStepdetailId() {
        if (testrunstepdetailsStepdetailId == null) {
            testrunstepdetailsStepdetailId = new LongFilter();
        }
        return testrunstepdetailsStepdetailId;
    }

    public void setTestrunstepdetailsStepdetailId(LongFilter testrunstepdetailsStepdetailId) {
        this.testrunstepdetailsStepdetailId = testrunstepdetailsStepdetailId;
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
        final TestCaseFieldsCriteria that = (TestCaseFieldsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(value, that.value) &&
            Objects.equals(templateFieldId, that.templateFieldId) &&
            Objects.equals(testCaseId, that.testCaseId) &&
            Objects.equals(testcasefieldattachmentsTestcasefieldId, that.testcasefieldattachmentsTestcasefieldId) &&
            Objects.equals(testrunstepdetailsStepdetailId, that.testrunstepdetailsStepdetailId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            value,
            templateFieldId,
            testCaseId,
            testcasefieldattachmentsTestcasefieldId,
            testrunstepdetailsStepdetailId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCaseFieldsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (value != null ? "value=" + value + ", " : "") +
            (templateFieldId != null ? "templateFieldId=" + templateFieldId + ", " : "") +
            (testCaseId != null ? "testCaseId=" + testCaseId + ", " : "") +
            (testcasefieldattachmentsTestcasefieldId != null ? "testcasefieldattachmentsTestcasefieldId=" + testcasefieldattachmentsTestcasefieldId + ", " : "") +
            (testrunstepdetailsStepdetailId != null ? "testrunstepdetailsStepdetailId=" + testrunstepdetailsStepdetailId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
