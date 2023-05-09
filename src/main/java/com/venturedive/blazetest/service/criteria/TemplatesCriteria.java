package com.venturedive.blazetest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blazetest.domain.Templates} entity. This class is used
 * in {@link com.venturedive.blazetest.web.rest.TemplatesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /templates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TemplatesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter templateName;

    private InstantFilter createdAt;

    private IntegerFilter createdBy;

    private LongFilter companyId;

    private LongFilter templateFieldId;

    private LongFilter projectsDefaulttemplateId;

    private LongFilter testcasesTemplateId;

    private Boolean distinct;

    public TemplatesCriteria() {}

    public TemplatesCriteria(TemplatesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.templateName = other.templateName == null ? null : other.templateName.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.templateFieldId = other.templateFieldId == null ? null : other.templateFieldId.copy();
        this.projectsDefaulttemplateId = other.projectsDefaulttemplateId == null ? null : other.projectsDefaulttemplateId.copy();
        this.testcasesTemplateId = other.testcasesTemplateId == null ? null : other.testcasesTemplateId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TemplatesCriteria copy() {
        return new TemplatesCriteria(this);
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

    public StringFilter getTemplateName() {
        return templateName;
    }

    public StringFilter templateName() {
        if (templateName == null) {
            templateName = new StringFilter();
        }
        return templateName;
    }

    public void setTemplateName(StringFilter templateName) {
        this.templateName = templateName;
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

    public LongFilter getCompanyId() {
        return companyId;
    }

    public LongFilter companyId() {
        if (companyId == null) {
            companyId = new LongFilter();
        }
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
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

    public LongFilter getProjectsDefaulttemplateId() {
        return projectsDefaulttemplateId;
    }

    public LongFilter projectsDefaulttemplateId() {
        if (projectsDefaulttemplateId == null) {
            projectsDefaulttemplateId = new LongFilter();
        }
        return projectsDefaulttemplateId;
    }

    public void setProjectsDefaulttemplateId(LongFilter projectsDefaulttemplateId) {
        this.projectsDefaulttemplateId = projectsDefaulttemplateId;
    }

    public LongFilter getTestcasesTemplateId() {
        return testcasesTemplateId;
    }

    public LongFilter testcasesTemplateId() {
        if (testcasesTemplateId == null) {
            testcasesTemplateId = new LongFilter();
        }
        return testcasesTemplateId;
    }

    public void setTestcasesTemplateId(LongFilter testcasesTemplateId) {
        this.testcasesTemplateId = testcasesTemplateId;
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
        final TemplatesCriteria that = (TemplatesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(templateName, that.templateName) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(templateFieldId, that.templateFieldId) &&
            Objects.equals(projectsDefaulttemplateId, that.projectsDefaulttemplateId) &&
            Objects.equals(testcasesTemplateId, that.testcasesTemplateId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            templateName,
            createdAt,
            createdBy,
            companyId,
            templateFieldId,
            projectsDefaulttemplateId,
            testcasesTemplateId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TemplatesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (templateName != null ? "templateName=" + templateName + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            (templateFieldId != null ? "templateFieldId=" + templateFieldId + ", " : "") +
            (projectsDefaulttemplateId != null ? "projectsDefaulttemplateId=" + projectsDefaulttemplateId + ", " : "") +
            (testcasesTemplateId != null ? "testcasesTemplateId=" + testcasesTemplateId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
