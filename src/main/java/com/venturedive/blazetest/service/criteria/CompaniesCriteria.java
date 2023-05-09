package com.venturedive.blazetest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blazetest.domain.Companies} entity. This class is used
 * in {@link com.venturedive.blazetest.web.rest.CompaniesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /companies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompaniesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter country;

    private StringFilter url;

    private StringFilter name;

    private IntegerFilter expectedNoOfUsers;

    private IntegerFilter createdBy;

    private InstantFilter createdAt;

    private IntegerFilter updatedBy;

    private InstantFilter updatedAt;

    private LongFilter projectsCompanyId;

    private LongFilter templatefieldsCompanyId;

    private LongFilter templatesCompanyId;

    private LongFilter usersCompanyId;

    private Boolean distinct;

    public CompaniesCriteria() {}

    public CompaniesCriteria(CompaniesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.expectedNoOfUsers = other.expectedNoOfUsers == null ? null : other.expectedNoOfUsers.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedBy = other.updatedBy == null ? null : other.updatedBy.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.projectsCompanyId = other.projectsCompanyId == null ? null : other.projectsCompanyId.copy();
        this.templatefieldsCompanyId = other.templatefieldsCompanyId == null ? null : other.templatefieldsCompanyId.copy();
        this.templatesCompanyId = other.templatesCompanyId == null ? null : other.templatesCompanyId.copy();
        this.usersCompanyId = other.usersCompanyId == null ? null : other.usersCompanyId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CompaniesCriteria copy() {
        return new CompaniesCriteria(this);
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

    public StringFilter getCountry() {
        return country;
    }

    public StringFilter country() {
        if (country == null) {
            country = new StringFilter();
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getUrl() {
        return url;
    }

    public StringFilter url() {
        if (url == null) {
            url = new StringFilter();
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
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

    public IntegerFilter getExpectedNoOfUsers() {
        return expectedNoOfUsers;
    }

    public IntegerFilter expectedNoOfUsers() {
        if (expectedNoOfUsers == null) {
            expectedNoOfUsers = new IntegerFilter();
        }
        return expectedNoOfUsers;
    }

    public void setExpectedNoOfUsers(IntegerFilter expectedNoOfUsers) {
        this.expectedNoOfUsers = expectedNoOfUsers;
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

    public LongFilter getProjectsCompanyId() {
        return projectsCompanyId;
    }

    public LongFilter projectsCompanyId() {
        if (projectsCompanyId == null) {
            projectsCompanyId = new LongFilter();
        }
        return projectsCompanyId;
    }

    public void setProjectsCompanyId(LongFilter projectsCompanyId) {
        this.projectsCompanyId = projectsCompanyId;
    }

    public LongFilter getTemplatefieldsCompanyId() {
        return templatefieldsCompanyId;
    }

    public LongFilter templatefieldsCompanyId() {
        if (templatefieldsCompanyId == null) {
            templatefieldsCompanyId = new LongFilter();
        }
        return templatefieldsCompanyId;
    }

    public void setTemplatefieldsCompanyId(LongFilter templatefieldsCompanyId) {
        this.templatefieldsCompanyId = templatefieldsCompanyId;
    }

    public LongFilter getTemplatesCompanyId() {
        return templatesCompanyId;
    }

    public LongFilter templatesCompanyId() {
        if (templatesCompanyId == null) {
            templatesCompanyId = new LongFilter();
        }
        return templatesCompanyId;
    }

    public void setTemplatesCompanyId(LongFilter templatesCompanyId) {
        this.templatesCompanyId = templatesCompanyId;
    }

    public LongFilter getUsersCompanyId() {
        return usersCompanyId;
    }

    public LongFilter usersCompanyId() {
        if (usersCompanyId == null) {
            usersCompanyId = new LongFilter();
        }
        return usersCompanyId;
    }

    public void setUsersCompanyId(LongFilter usersCompanyId) {
        this.usersCompanyId = usersCompanyId;
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
        final CompaniesCriteria that = (CompaniesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(country, that.country) &&
            Objects.equals(url, that.url) &&
            Objects.equals(name, that.name) &&
            Objects.equals(expectedNoOfUsers, that.expectedNoOfUsers) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedBy, that.updatedBy) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(projectsCompanyId, that.projectsCompanyId) &&
            Objects.equals(templatefieldsCompanyId, that.templatefieldsCompanyId) &&
            Objects.equals(templatesCompanyId, that.templatesCompanyId) &&
            Objects.equals(usersCompanyId, that.usersCompanyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            country,
            url,
            name,
            expectedNoOfUsers,
            createdBy,
            createdAt,
            updatedBy,
            updatedAt,
            projectsCompanyId,
            templatefieldsCompanyId,
            templatesCompanyId,
            usersCompanyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompaniesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (url != null ? "url=" + url + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (expectedNoOfUsers != null ? "expectedNoOfUsers=" + expectedNoOfUsers + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedBy != null ? "updatedBy=" + updatedBy + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (projectsCompanyId != null ? "projectsCompanyId=" + projectsCompanyId + ", " : "") +
            (templatefieldsCompanyId != null ? "templatefieldsCompanyId=" + templatefieldsCompanyId + ", " : "") +
            (templatesCompanyId != null ? "templatesCompanyId=" + templatesCompanyId + ", " : "") +
            (usersCompanyId != null ? "usersCompanyId=" + usersCompanyId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
