package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Templates.
 */
@Entity
@Table(name = "templates")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Templates implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "template_name", length = 255)
    private String templateName;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "projectsCompanies", "templatefieldsCompanies", "templatesCompanies", "usersCompanies" },
        allowSetters = true
    )
    private Companies company;

    @ManyToMany
    @JoinTable(
        name = "rel_templates__template_field",
        joinColumns = @JoinColumn(name = "templates_id"),
        inverseJoinColumns = @JoinColumn(name = "template_field_id")
    )
    @JsonIgnoreProperties(value = { "company", "templateFieldType", "testcasefieldsTemplatefields", "templates" }, allowSetters = true)
    private Set<TemplateFields> templateFields = new HashSet<>();

    @OneToMany(mappedBy = "defaultTemplate")
    @JsonIgnoreProperties(
        value = { "defaultTemplate", "company", "milestonesProjects", "testplansProjects", "testsuitesProjects", "users" },
        allowSetters = true
    )
    private Set<Projects> projectsDefaulttemplates = new HashSet<>();

    @OneToMany(mappedBy = "template")
    @JsonIgnoreProperties(
        value = {
            "testSuite",
            "section",
            "priority",
            "template",
            "milestone",
            "testLevels",
            "testcaseattachmentsTestcases",
            "testcasefieldsTestcases",
            "testrundetailsTestcases",
        },
        allowSetters = true
    )
    private Set<TestCases> testcasesTemplates = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Templates id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return this.templateName;
    }

    public Templates templateName(String templateName) {
        this.setTemplateName(templateName);
        return this;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Templates createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public Templates createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Companies getCompany() {
        return this.company;
    }

    public void setCompany(Companies companies) {
        this.company = companies;
    }

    public Templates company(Companies companies) {
        this.setCompany(companies);
        return this;
    }

    public Set<TemplateFields> getTemplateFields() {
        return this.templateFields;
    }

    public void setTemplateFields(Set<TemplateFields> templateFields) {
        this.templateFields = templateFields;
    }

    public Templates templateFields(Set<TemplateFields> templateFields) {
        this.setTemplateFields(templateFields);
        return this;
    }

    public Templates addTemplateField(TemplateFields templateFields) {
        this.templateFields.add(templateFields);
        templateFields.getTemplates().add(this);
        return this;
    }

    public Templates removeTemplateField(TemplateFields templateFields) {
        this.templateFields.remove(templateFields);
        templateFields.getTemplates().remove(this);
        return this;
    }

    public Set<Projects> getProjectsDefaulttemplates() {
        return this.projectsDefaulttemplates;
    }

    public void setProjectsDefaulttemplates(Set<Projects> projects) {
        if (this.projectsDefaulttemplates != null) {
            this.projectsDefaulttemplates.forEach(i -> i.setDefaultTemplate(null));
        }
        if (projects != null) {
            projects.forEach(i -> i.setDefaultTemplate(this));
        }
        this.projectsDefaulttemplates = projects;
    }

    public Templates projectsDefaulttemplates(Set<Projects> projects) {
        this.setProjectsDefaulttemplates(projects);
        return this;
    }

    public Templates addProjectsDefaulttemplate(Projects projects) {
        this.projectsDefaulttemplates.add(projects);
        projects.setDefaultTemplate(this);
        return this;
    }

    public Templates removeProjectsDefaulttemplate(Projects projects) {
        this.projectsDefaulttemplates.remove(projects);
        projects.setDefaultTemplate(null);
        return this;
    }

    public Set<TestCases> getTestcasesTemplates() {
        return this.testcasesTemplates;
    }

    public void setTestcasesTemplates(Set<TestCases> testCases) {
        if (this.testcasesTemplates != null) {
            this.testcasesTemplates.forEach(i -> i.setTemplate(null));
        }
        if (testCases != null) {
            testCases.forEach(i -> i.setTemplate(this));
        }
        this.testcasesTemplates = testCases;
    }

    public Templates testcasesTemplates(Set<TestCases> testCases) {
        this.setTestcasesTemplates(testCases);
        return this;
    }

    public Templates addTestcasesTemplate(TestCases testCases) {
        this.testcasesTemplates.add(testCases);
        testCases.setTemplate(this);
        return this;
    }

    public Templates removeTestcasesTemplate(TestCases testCases) {
        this.testcasesTemplates.remove(testCases);
        testCases.setTemplate(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Templates)) {
            return false;
        }
        return id != null && id.equals(((Templates) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Templates{" +
            "id=" + getId() +
            ", templateName='" + getTemplateName() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy=" + getCreatedBy() +
            "}";
    }
}
