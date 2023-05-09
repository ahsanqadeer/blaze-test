package com.venturedive.blazetest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blazetest.domain.Permissions} entity. This class is used
 * in {@link com.venturedive.blazetest.web.rest.PermissionsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /permissions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PermissionsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter permissionName;

    private LongFilter roleId;

    private Boolean distinct;

    public PermissionsCriteria() {}

    public PermissionsCriteria(PermissionsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.permissionName = other.permissionName == null ? null : other.permissionName.copy();
        this.roleId = other.roleId == null ? null : other.roleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PermissionsCriteria copy() {
        return new PermissionsCriteria(this);
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

    public StringFilter getPermissionName() {
        return permissionName;
    }

    public StringFilter permissionName() {
        if (permissionName == null) {
            permissionName = new StringFilter();
        }
        return permissionName;
    }

    public void setPermissionName(StringFilter permissionName) {
        this.permissionName = permissionName;
    }

    public LongFilter getRoleId() {
        return roleId;
    }

    public LongFilter roleId() {
        if (roleId == null) {
            roleId = new LongFilter();
        }
        return roleId;
    }

    public void setRoleId(LongFilter roleId) {
        this.roleId = roleId;
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
        final PermissionsCriteria that = (PermissionsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(permissionName, that.permissionName) &&
            Objects.equals(roleId, that.roleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, permissionName, roleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermissionsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (permissionName != null ? "permissionName=" + permissionName + ", " : "") +
            (roleId != null ? "roleId=" + roleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
