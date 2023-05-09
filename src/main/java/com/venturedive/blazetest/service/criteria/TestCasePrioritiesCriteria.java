package com.venturedive.blazetest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blazetest.domain.TestCasePriorities} entity. This class is used
 * in {@link com.venturedive.blazetest.web.rest.TestCasePrioritiesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-case-priorities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCasePrioritiesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter testcasesPriorityId;

    private Boolean distinct;

    public TestCasePrioritiesCriteria() {}

    public TestCasePrioritiesCriteria(TestCasePrioritiesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.testcasesPriorityId = other.testcasesPriorityId == null ? null : other.testcasesPriorityId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TestCasePrioritiesCriteria copy() {
        return new TestCasePrioritiesCriteria(this);
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

    public LongFilter getTestcasesPriorityId() {
        return testcasesPriorityId;
    }

    public LongFilter testcasesPriorityId() {
        if (testcasesPriorityId == null) {
            testcasesPriorityId = new LongFilter();
        }
        return testcasesPriorityId;
    }

    public void setTestcasesPriorityId(LongFilter testcasesPriorityId) {
        this.testcasesPriorityId = testcasesPriorityId;
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
        final TestCasePrioritiesCriteria that = (TestCasePrioritiesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(testcasesPriorityId, that.testcasesPriorityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, testcasesPriorityId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCasePrioritiesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (testcasesPriorityId != null ? "testcasesPriorityId=" + testcasesPriorityId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
