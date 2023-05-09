package com.venturedive.blazetest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blazetest.domain.TestRuns} entity. This class is used
 * in {@link com.venturedive.blazetest.web.rest.TestRunsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-runs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestRunsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private InstantFilter createdAt;

    private IntegerFilter createdBy;

    private LongFilter testLevelId;

    private LongFilter mileStoneId;

    private LongFilter testrundetailsTestrunId;

    private Boolean distinct;

    public TestRunsCriteria() {}

    public TestRunsCriteria(TestRunsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.testLevelId = other.testLevelId == null ? null : other.testLevelId.copy();
        this.mileStoneId = other.mileStoneId == null ? null : other.mileStoneId.copy();
        this.testrundetailsTestrunId = other.testrundetailsTestrunId == null ? null : other.testrundetailsTestrunId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestRunsCriteria copy() {
        return new TestRunsCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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

    public LongFilter getMileStoneId() {
        return mileStoneId;
    }

    public LongFilter mileStoneId() {
        if (mileStoneId == null) {
            mileStoneId = new LongFilter();
        }
        return mileStoneId;
    }

    public void setMileStoneId(LongFilter mileStoneId) {
        this.mileStoneId = mileStoneId;
    }

    public LongFilter getTestrundetailsTestrunId() {
        return testrundetailsTestrunId;
    }

    public LongFilter testrundetailsTestrunId() {
        if (testrundetailsTestrunId == null) {
            testrundetailsTestrunId = new LongFilter();
        }
        return testrundetailsTestrunId;
    }

    public void setTestrundetailsTestrunId(LongFilter testrundetailsTestrunId) {
        this.testrundetailsTestrunId = testrundetailsTestrunId;
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
        final TestRunsCriteria that = (TestRunsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(testLevelId, that.testLevelId) &&
            Objects.equals(mileStoneId, that.mileStoneId) &&
            Objects.equals(testrundetailsTestrunId, that.testrundetailsTestrunId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, createdAt, createdBy, testLevelId, mileStoneId, testrundetailsTestrunId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestRunsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (testLevelId != null ? "testLevelId=" + testLevelId + ", " : "") +
            (mileStoneId != null ? "mileStoneId=" + mileStoneId + ", " : "") +
            (testrundetailsTestrunId != null ? "testrundetailsTestrunId=" + testrundetailsTestrunId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
