package com.venturedive.blazetest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.venturedive.blazetest.domain.TemplateFieldTypes} entity. This class is used
 * in {@link com.venturedive.blazetest.web.rest.TemplateFieldTypesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /template-field-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TemplateFieldTypesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter type;

    private BooleanFilter isList;

    private BooleanFilter attachments;

    private LongFilter templatefieldsTemplatefieldtypeId;

    private Boolean distinct;

    public TemplateFieldTypesCriteria() {}

    public TemplateFieldTypesCriteria(TemplateFieldTypesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.isList = other.isList == null ? null : other.isList.copy();
        this.attachments = other.attachments == null ? null : other.attachments.copy();
        this.templatefieldsTemplatefieldtypeId =
            other.templatefieldsTemplatefieldtypeId == null ? null : other.templatefieldsTemplatefieldtypeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TemplateFieldTypesCriteria copy() {
        return new TemplateFieldTypesCriteria(this);
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

    public StringFilter getType() {
        return type;
    }

    public StringFilter type() {
        if (type == null) {
            type = new StringFilter();
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public BooleanFilter getIsList() {
        return isList;
    }

    public BooleanFilter isList() {
        if (isList == null) {
            isList = new BooleanFilter();
        }
        return isList;
    }

    public void setIsList(BooleanFilter isList) {
        this.isList = isList;
    }

    public BooleanFilter getAttachments() {
        return attachments;
    }

    public BooleanFilter attachments() {
        if (attachments == null) {
            attachments = new BooleanFilter();
        }
        return attachments;
    }

    public void setAttachments(BooleanFilter attachments) {
        this.attachments = attachments;
    }

    public LongFilter getTemplatefieldsTemplatefieldtypeId() {
        return templatefieldsTemplatefieldtypeId;
    }

    public LongFilter templatefieldsTemplatefieldtypeId() {
        if (templatefieldsTemplatefieldtypeId == null) {
            templatefieldsTemplatefieldtypeId = new LongFilter();
        }
        return templatefieldsTemplatefieldtypeId;
    }

    public void setTemplatefieldsTemplatefieldtypeId(LongFilter templatefieldsTemplatefieldtypeId) {
        this.templatefieldsTemplatefieldtypeId = templatefieldsTemplatefieldtypeId;
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
        final TemplateFieldTypesCriteria that = (TemplateFieldTypesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(isList, that.isList) &&
            Objects.equals(attachments, that.attachments) &&
            Objects.equals(templatefieldsTemplatefieldtypeId, that.templatefieldsTemplatefieldtypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, isList, attachments, templatefieldsTemplatefieldtypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TemplateFieldTypesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (isList != null ? "isList=" + isList + ", " : "") +
            (attachments != null ? "attachments=" + attachments + ", " : "") +
            (templatefieldsTemplatefieldtypeId != null ? "templatefieldsTemplatefieldtypeId=" + templatefieldsTemplatefieldtypeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
