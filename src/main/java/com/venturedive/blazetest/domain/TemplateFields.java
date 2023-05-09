package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TemplateFields.
 */
@Entity
@Table(name = "template_fields")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TemplateFields implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "field_name", length = 255)
    private String fieldName;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "projectsCompanies", "templatefieldsCompanies", "templatesCompanies", "usersCompanies" },
        allowSetters = true
    )
    private Companies company;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "templatefieldsTemplatefieldtypes" }, allowSetters = true)
    private TemplateFieldTypes templateFieldType;

    @OneToMany(mappedBy = "templateField")
    @JsonIgnoreProperties(
        value = { "templateField", "testCase", "testcasefieldattachmentsTestcasefields", "testrunstepdetailsStepdetails" },
        allowSetters = true
    )
    private Set<TestCaseFields> testcasefieldsTemplatefields = new HashSet<>();

    @ManyToMany(mappedBy = "templateFields")
    @JsonIgnoreProperties(value = { "company", "templateFields", "projectsDefaulttemplates", "testcasesTemplates" }, allowSetters = true)
    private Set<Templates> templates = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TemplateFields id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public TemplateFields fieldName(String fieldName) {
        this.setFieldName(fieldName);
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Companies getCompany() {
        return this.company;
    }

    public void setCompany(Companies companies) {
        this.company = companies;
    }

    public TemplateFields company(Companies companies) {
        this.setCompany(companies);
        return this;
    }

    public TemplateFieldTypes getTemplateFieldType() {
        return this.templateFieldType;
    }

    public void setTemplateFieldType(TemplateFieldTypes templateFieldTypes) {
        this.templateFieldType = templateFieldTypes;
    }

    public TemplateFields templateFieldType(TemplateFieldTypes templateFieldTypes) {
        this.setTemplateFieldType(templateFieldTypes);
        return this;
    }

    public Set<TestCaseFields> getTestcasefieldsTemplatefields() {
        return this.testcasefieldsTemplatefields;
    }

    public void setTestcasefieldsTemplatefields(Set<TestCaseFields> testCaseFields) {
        if (this.testcasefieldsTemplatefields != null) {
            this.testcasefieldsTemplatefields.forEach(i -> i.setTemplateField(null));
        }
        if (testCaseFields != null) {
            testCaseFields.forEach(i -> i.setTemplateField(this));
        }
        this.testcasefieldsTemplatefields = testCaseFields;
    }

    public TemplateFields testcasefieldsTemplatefields(Set<TestCaseFields> testCaseFields) {
        this.setTestcasefieldsTemplatefields(testCaseFields);
        return this;
    }

    public TemplateFields addTestcasefieldsTemplatefield(TestCaseFields testCaseFields) {
        this.testcasefieldsTemplatefields.add(testCaseFields);
        testCaseFields.setTemplateField(this);
        return this;
    }

    public TemplateFields removeTestcasefieldsTemplatefield(TestCaseFields testCaseFields) {
        this.testcasefieldsTemplatefields.remove(testCaseFields);
        testCaseFields.setTemplateField(null);
        return this;
    }

    public Set<Templates> getTemplates() {
        return this.templates;
    }

    public void setTemplates(Set<Templates> templates) {
        if (this.templates != null) {
            this.templates.forEach(i -> i.removeTemplateField(this));
        }
        if (templates != null) {
            templates.forEach(i -> i.addTemplateField(this));
        }
        this.templates = templates;
    }

    public TemplateFields templates(Set<Templates> templates) {
        this.setTemplates(templates);
        return this;
    }

    public TemplateFields addTemplate(Templates templates) {
        this.templates.add(templates);
        templates.getTemplateFields().add(this);
        return this;
    }

    public TemplateFields removeTemplate(Templates templates) {
        this.templates.remove(templates);
        templates.getTemplateFields().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TemplateFields)) {
            return false;
        }
        return id != null && id.equals(((TemplateFields) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TemplateFields{" +
            "id=" + getId() +
            ", fieldName='" + getFieldName() + "'" +
            "}";
    }
}
