package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TemplateFieldTypes.
 */
@Entity
@Table(name = "template_field_types")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TemplateFieldTypes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "type", length = 255, nullable = false, unique = true)
    private String type;

    @NotNull
    @Column(name = "is_list", nullable = false)
    private Boolean isList;

    @NotNull
    @Column(name = "attachments", nullable = false)
    private Boolean attachments;

    @OneToMany(mappedBy = "templateFieldType")
    @JsonIgnoreProperties(value = { "company", "templateFieldType", "testcasefieldsTemplatefields", "templates" }, allowSetters = true)
    private Set<TemplateFields> templatefieldsTemplatefieldtypes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TemplateFieldTypes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public TemplateFieldTypes type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsList() {
        return this.isList;
    }

    public TemplateFieldTypes isList(Boolean isList) {
        this.setIsList(isList);
        return this;
    }

    public void setIsList(Boolean isList) {
        this.isList = isList;
    }

    public Boolean getAttachments() {
        return this.attachments;
    }

    public TemplateFieldTypes attachments(Boolean attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public void setAttachments(Boolean attachments) {
        this.attachments = attachments;
    }

    public Set<TemplateFields> getTemplatefieldsTemplatefieldtypes() {
        return this.templatefieldsTemplatefieldtypes;
    }

    public void setTemplatefieldsTemplatefieldtypes(Set<TemplateFields> templateFields) {
        if (this.templatefieldsTemplatefieldtypes != null) {
            this.templatefieldsTemplatefieldtypes.forEach(i -> i.setTemplateFieldType(null));
        }
        if (templateFields != null) {
            templateFields.forEach(i -> i.setTemplateFieldType(this));
        }
        this.templatefieldsTemplatefieldtypes = templateFields;
    }

    public TemplateFieldTypes templatefieldsTemplatefieldtypes(Set<TemplateFields> templateFields) {
        this.setTemplatefieldsTemplatefieldtypes(templateFields);
        return this;
    }

    public TemplateFieldTypes addTemplatefieldsTemplatefieldtype(TemplateFields templateFields) {
        this.templatefieldsTemplatefieldtypes.add(templateFields);
        templateFields.setTemplateFieldType(this);
        return this;
    }

    public TemplateFieldTypes removeTemplatefieldsTemplatefieldtype(TemplateFields templateFields) {
        this.templatefieldsTemplatefieldtypes.remove(templateFields);
        templateFields.setTemplateFieldType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TemplateFieldTypes)) {
            return false;
        }
        return id != null && id.equals(((TemplateFieldTypes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TemplateFieldTypes{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", isList='" + getIsList() + "'" +
            ", attachments='" + getAttachments() + "'" +
            "}";
    }
}
