package com.venturedive.blazetest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blazetest.domain.Milestones} entity. This class is used
 * in {@link com.venturedive.blazetest.web.rest.MilestonesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /milestones?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MilestonesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private BooleanFilter isCompleted;

    private LongFilter parentMilestoneId;

    private LongFilter projectId;

    private LongFilter milestonesParentmilestoneId;

    private LongFilter testcasesMilestoneId;

    private LongFilter testrunsMilestoneId;

    private Boolean distinct;

    public MilestonesCriteria() {}

    public MilestonesCriteria(MilestonesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.isCompleted = other.isCompleted == null ? null : other.isCompleted.copy();
        this.parentMilestoneId = other.parentMilestoneId == null ? null : other.parentMilestoneId.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
        this.milestonesParentmilestoneId = other.milestonesParentmilestoneId == null ? null : other.milestonesParentmilestoneId.copy();
        this.testcasesMilestoneId = other.testcasesMilestoneId == null ? null : other.testcasesMilestoneId.copy();
        this.testrunsMilestoneId = other.testrunsMilestoneId == null ? null : other.testrunsMilestoneId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MilestonesCriteria copy() {
        return new MilestonesCriteria(this);
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

    public InstantFilter getStartDate() {
        return startDate;
    }

    public InstantFilter startDate() {
        if (startDate == null) {
            startDate = new InstantFilter();
        }
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public InstantFilter endDate() {
        if (endDate == null) {
            endDate = new InstantFilter();
        }
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
    }

    public BooleanFilter getIsCompleted() {
        return isCompleted;
    }

    public BooleanFilter isCompleted() {
        if (isCompleted == null) {
            isCompleted = new BooleanFilter();
        }
        return isCompleted;
    }

    public void setIsCompleted(BooleanFilter isCompleted) {
        this.isCompleted = isCompleted;
    }

    public LongFilter getParentMilestoneId() {
        return parentMilestoneId;
    }

    public LongFilter parentMilestoneId() {
        if (parentMilestoneId == null) {
            parentMilestoneId = new LongFilter();
        }
        return parentMilestoneId;
    }

    public void setParentMilestoneId(LongFilter parentMilestoneId) {
        this.parentMilestoneId = parentMilestoneId;
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

    public LongFilter getMilestonesParentmilestoneId() {
        return milestonesParentmilestoneId;
    }

    public LongFilter milestonesParentmilestoneId() {
        if (milestonesParentmilestoneId == null) {
            milestonesParentmilestoneId = new LongFilter();
        }
        return milestonesParentmilestoneId;
    }

    public void setMilestonesParentmilestoneId(LongFilter milestonesParentmilestoneId) {
        this.milestonesParentmilestoneId = milestonesParentmilestoneId;
    }

    public LongFilter getTestcasesMilestoneId() {
        return testcasesMilestoneId;
    }

    public LongFilter testcasesMilestoneId() {
        if (testcasesMilestoneId == null) {
            testcasesMilestoneId = new LongFilter();
        }
        return testcasesMilestoneId;
    }

    public void setTestcasesMilestoneId(LongFilter testcasesMilestoneId) {
        this.testcasesMilestoneId = testcasesMilestoneId;
    }

    public LongFilter getTestrunsMilestoneId() {
        return testrunsMilestoneId;
    }

    public LongFilter testrunsMilestoneId() {
        if (testrunsMilestoneId == null) {
            testrunsMilestoneId = new LongFilter();
        }
        return testrunsMilestoneId;
    }

    public void setTestrunsMilestoneId(LongFilter testrunsMilestoneId) {
        this.testrunsMilestoneId = testrunsMilestoneId;
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
        final MilestonesCriteria that = (MilestonesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(isCompleted, that.isCompleted) &&
            Objects.equals(parentMilestoneId, that.parentMilestoneId) &&
            Objects.equals(projectId, that.projectId) &&
            Objects.equals(milestonesParentmilestoneId, that.milestonesParentmilestoneId) &&
            Objects.equals(testcasesMilestoneId, that.testcasesMilestoneId) &&
            Objects.equals(testrunsMilestoneId, that.testrunsMilestoneId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            startDate,
            endDate,
            isCompleted,
            parentMilestoneId,
            projectId,
            milestonesParentmilestoneId,
            testcasesMilestoneId,
            testrunsMilestoneId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MilestonesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (isCompleted != null ? "isCompleted=" + isCompleted + ", " : "") +
            (parentMilestoneId != null ? "parentMilestoneId=" + parentMilestoneId + ", " : "") +
            (projectId != null ? "projectId=" + projectId + ", " : "") +
            (milestonesParentmilestoneId != null ? "milestonesParentmilestoneId=" + milestonesParentmilestoneId + ", " : "") +
            (testcasesMilestoneId != null ? "testcasesMilestoneId=" + testcasesMilestoneId + ", " : "") +
            (testrunsMilestoneId != null ? "testrunsMilestoneId=" + testrunsMilestoneId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
