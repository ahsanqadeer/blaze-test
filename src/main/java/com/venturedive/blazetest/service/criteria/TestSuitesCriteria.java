package com.venturedive.blazetest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blazetest.domain.TestSuites} entity. This class is used
 * in {@link com.venturedive.blazetest.web.rest.TestSuitesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-suites?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestSuitesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter testSuiteName;

    private IntegerFilter createdBy;

    private InstantFilter createdAt;

    private IntegerFilter updatedBy;

    private InstantFilter updatedAt;

    private LongFilter projectId;

    private LongFilter sectionsTestsuiteId;

    private LongFilter testcasesTestsuiteId;

    private Boolean distinct;

    public TestSuitesCriteria() {}

    public TestSuitesCriteria(TestSuitesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.testSuiteName = other.testSuiteName == null ? null : other.testSuiteName.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedBy = other.updatedBy == null ? null : other.updatedBy.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
        this.sectionsTestsuiteId = other.sectionsTestsuiteId == null ? null : other.sectionsTestsuiteId.copy();
        this.testcasesTestsuiteId = other.testcasesTestsuiteId == null ? null : other.testcasesTestsuiteId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestSuitesCriteria copy() {
        return new TestSuitesCriteria(this);
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

    public StringFilter getTestSuiteName() {
        return testSuiteName;
    }

    public StringFilter testSuiteName() {
        if (testSuiteName == null) {
            testSuiteName = new StringFilter();
        }
        return testSuiteName;
    }

    public void setTestSuiteName(StringFilter testSuiteName) {
        this.testSuiteName = testSuiteName;
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

    public LongFilter getProjectId() {
        return projectId;
    }

    public LongFilter projectId() {
        if (projectId == null) {
            projectId = new LongFilter();
        }
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }

    public LongFilter getSectionsTestsuiteId() {
        return sectionsTestsuiteId;
    }

    public LongFilter sectionsTestsuiteId() {
        if (sectionsTestsuiteId == null) {
            sectionsTestsuiteId = new LongFilter();
        }
        return sectionsTestsuiteId;
    }

    public void setSectionsTestsuiteId(LongFilter sectionsTestsuiteId) {
        this.sectionsTestsuiteId = sectionsTestsuiteId;
    }

    public LongFilter getTestcasesTestsuiteId() {
        return testcasesTestsuiteId;
    }

    public LongFilter testcasesTestsuiteId() {
        if (testcasesTestsuiteId == null) {
            testcasesTestsuiteId = new LongFilter();
        }
        return testcasesTestsuiteId;
    }

    public void setTestcasesTestsuiteId(LongFilter testcasesTestsuiteId) {
        this.testcasesTestsuiteId = testcasesTestsuiteId;
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
        final TestSuitesCriteria that = (TestSuitesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(testSuiteName, that.testSuiteName) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedBy, that.updatedBy) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(projectId, that.projectId) &&
            Objects.equals(sectionsTestsuiteId, that.sectionsTestsuiteId) &&
            Objects.equals(testcasesTestsuiteId, that.testcasesTestsuiteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            testSuiteName,
            createdBy,
            createdAt,
            updatedBy,
            updatedAt,
            projectId,
            sectionsTestsuiteId,
            testcasesTestsuiteId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestSuitesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (testSuiteName != null ? "testSuiteName=" + testSuiteName + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedBy != null ? "updatedBy=" + updatedBy + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (projectId != null ? "projectId=" + projectId + ", " : "") +
            (sectionsTestsuiteId != null ? "sectionsTestsuiteId=" + sectionsTestsuiteId + ", " : "") +
            (testcasesTestsuiteId != null ? "testcasesTestsuiteId=" + testcasesTestsuiteId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
