package com.venturedive.blazetest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blazetest.domain.Sections} entity. This class is used
 * in {@link com.venturedive.blazetest.web.rest.SectionsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sections?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SectionsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private InstantFilter createdAt;

    private IntegerFilter createdBy;

    private InstantFilter updatedAt;

    private IntegerFilter updatedBy;

    private LongFilter testSuiteId;

    private LongFilter parentSectionId;

    private LongFilter sectionsParentsectionId;

    private LongFilter testcasesSectionId;

    private Boolean distinct;

    public SectionsCriteria() {}

    public SectionsCriteria(SectionsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.updatedBy = other.updatedBy == null ? null : other.updatedBy.copy();
        this.testSuiteId = other.testSuiteId == null ? null : other.testSuiteId.copy();
        this.parentSectionId = other.parentSectionId == null ? null : other.parentSectionId.copy();
        this.sectionsParentsectionId = other.sectionsParentsectionId == null ? null : other.sectionsParentsectionId.copy();
        this.testcasesSectionId = other.testcasesSectionId == null ? null : other.testcasesSectionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SectionsCriteria copy() {
        return new SectionsCriteria(this);
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

    public LongFilter getTestSuiteId() {
        return testSuiteId;
    }

    public LongFilter testSuiteId() {
        if (testSuiteId == null) {
            testSuiteId = new LongFilter();
        }
        return testSuiteId;
    }

    public void setTestSuiteId(LongFilter testSuiteId) {
        this.testSuiteId = testSuiteId;
    }

    public LongFilter getParentSectionId() {
        return parentSectionId;
    }

    public LongFilter parentSectionId() {
        if (parentSectionId == null) {
            parentSectionId = new LongFilter();
        }
        return parentSectionId;
    }

    public void setParentSectionId(LongFilter parentSectionId) {
        this.parentSectionId = parentSectionId;
    }

    public LongFilter getSectionsParentsectionId() {
        return sectionsParentsectionId;
    }

    public LongFilter sectionsParentsectionId() {
        if (sectionsParentsectionId == null) {
            sectionsParentsectionId = new LongFilter();
        }
        return sectionsParentsectionId;
    }

    public void setSectionsParentsectionId(LongFilter sectionsParentsectionId) {
        this.sectionsParentsectionId = sectionsParentsectionId;
    }

    public LongFilter getTestcasesSectionId() {
        return testcasesSectionId;
    }

    public LongFilter testcasesSectionId() {
        if (testcasesSectionId == null) {
            testcasesSectionId = new LongFilter();
        }
        return testcasesSectionId;
    }

    public void setTestcasesSectionId(LongFilter testcasesSectionId) {
        this.testcasesSectionId = testcasesSectionId;
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
        final SectionsCriteria that = (SectionsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(updatedBy, that.updatedBy) &&
            Objects.equals(testSuiteId, that.testSuiteId) &&
            Objects.equals(parentSectionId, that.parentSectionId) &&
            Objects.equals(sectionsParentsectionId, that.sectionsParentsectionId) &&
            Objects.equals(testcasesSectionId, that.testcasesSectionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            createdAt,
            createdBy,
            updatedAt,
            updatedBy,
            testSuiteId,
            parentSectionId,
            sectionsParentsectionId,
            testcasesSectionId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SectionsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (updatedBy != null ? "updatedBy=" + updatedBy + ", " : "") +
            (testSuiteId != null ? "testSuiteId=" + testSuiteId + ", " : "") +
            (parentSectionId != null ? "parentSectionId=" + parentSectionId + ", " : "") +
            (sectionsParentsectionId != null ? "sectionsParentsectionId=" + sectionsParentsectionId + ", " : "") +
            (testcasesSectionId != null ? "testcasesSectionId=" + testcasesSectionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
