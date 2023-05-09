package com.venturedive.blazetest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blazetest.domain.TestLevels} entity. This class is used
 * in {@link com.venturedive.blazetest.web.rest.TestLevelsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-levels?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestLevelsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter testrunsTestlevelId;

    private LongFilter testCaseId;

    private Boolean distinct;

    public TestLevelsCriteria() {}

    public TestLevelsCriteria(TestLevelsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.testrunsTestlevelId = other.testrunsTestlevelId == null ? null : other.testrunsTestlevelId.copy();
        this.testCaseId = other.testCaseId == null ? null : other.testCaseId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestLevelsCriteria copy() {
        return new TestLevelsCriteria(this);
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

    public LongFilter getTestrunsTestlevelId() {
        return testrunsTestlevelId;
    }

    public LongFilter testrunsTestlevelId() {
        if (testrunsTestlevelId == null) {
            testrunsTestlevelId = new LongFilter();
        }
        return testrunsTestlevelId;
    }

    public void setTestrunsTestlevelId(LongFilter testrunsTestlevelId) {
        this.testrunsTestlevelId = testrunsTestlevelId;
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
        final TestLevelsCriteria that = (TestLevelsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(testrunsTestlevelId, that.testrunsTestlevelId) &&
            Objects.equals(testCaseId, that.testCaseId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, testrunsTestlevelId, testCaseId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestLevelsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (testrunsTestlevelId != null ? "testrunsTestlevelId=" + testrunsTestlevelId + ", " : "") +
            (testCaseId != null ? "testCaseId=" + testCaseId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
