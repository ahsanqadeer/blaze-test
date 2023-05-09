package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Companies.
 */
@Entity
@Table(name = "companies")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Companies implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "country", length = 255)
    private String country;

    @Size(max = 255)
    @Column(name = "url", length = 255)
    private String url;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "expected_no_of_users")
    private Integer expectedNoOfUsers;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(
        value = { "defaultTemplate", "company", "milestonesProjects", "testplansProjects", "testsuitesProjects", "users" },
        allowSetters = true
    )
    private Set<Projects> projectsCompanies = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "templateFieldType", "testcasefieldsTemplatefields", "templates" }, allowSetters = true)
    private Set<TemplateFields> templatefieldsCompanies = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "templateFields", "projectsDefaulttemplates", "testcasesTemplates" }, allowSetters = true)
    private Set<Templates> templatesCompanies = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(value = { "company", "projects", "roles" }, allowSetters = true)
    private Set<Users> usersCompanies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Companies id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return this.country;
    }

    public Companies country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUrl() {
        return this.url;
    }

    public Companies url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return this.name;
    }

    public Companies name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getExpectedNoOfUsers() {
        return this.expectedNoOfUsers;
    }

    public Companies expectedNoOfUsers(Integer expectedNoOfUsers) {
        this.setExpectedNoOfUsers(expectedNoOfUsers);
        return this;
    }

    public void setExpectedNoOfUsers(Integer expectedNoOfUsers) {
        this.expectedNoOfUsers = expectedNoOfUsers;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public Companies createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Companies createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public Companies updatedBy(Integer updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Companies updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Projects> getProjectsCompanies() {
        return this.projectsCompanies;
    }

    public void setProjectsCompanies(Set<Projects> projects) {
        if (this.projectsCompanies != null) {
            this.projectsCompanies.forEach(i -> i.setCompany(null));
        }
        if (projects != null) {
            projects.forEach(i -> i.setCompany(this));
        }
        this.projectsCompanies = projects;
    }

    public Companies projectsCompanies(Set<Projects> projects) {
        this.setProjectsCompanies(projects);
        return this;
    }

    public Companies addProjectsCompany(Projects projects) {
        this.projectsCompanies.add(projects);
        projects.setCompany(this);
        return this;
    }

    public Companies removeProjectsCompany(Projects projects) {
        this.projectsCompanies.remove(projects);
        projects.setCompany(null);
        return this;
    }

    public Set<TemplateFields> getTemplatefieldsCompanies() {
        return this.templatefieldsCompanies;
    }

    public void setTemplatefieldsCompanies(Set<TemplateFields> templateFields) {
        if (this.templatefieldsCompanies != null) {
            this.templatefieldsCompanies.forEach(i -> i.setCompany(null));
        }
        if (templateFields != null) {
            templateFields.forEach(i -> i.setCompany(this));
        }
        this.templatefieldsCompanies = templateFields;
    }

    public Companies templatefieldsCompanies(Set<TemplateFields> templateFields) {
        this.setTemplatefieldsCompanies(templateFields);
        return this;
    }

    public Companies addTemplatefieldsCompany(TemplateFields templateFields) {
        this.templatefieldsCompanies.add(templateFields);
        templateFields.setCompany(this);
        return this;
    }

    public Companies removeTemplatefieldsCompany(TemplateFields templateFields) {
        this.templatefieldsCompanies.remove(templateFields);
        templateFields.setCompany(null);
        return this;
    }

    public Set<Templates> getTemplatesCompanies() {
        return this.templatesCompanies;
    }

    public void setTemplatesCompanies(Set<Templates> templates) {
        if (this.templatesCompanies != null) {
            this.templatesCompanies.forEach(i -> i.setCompany(null));
        }
        if (templates != null) {
            templates.forEach(i -> i.setCompany(this));
        }
        this.templatesCompanies = templates;
    }

    public Companies templatesCompanies(Set<Templates> templates) {
        this.setTemplatesCompanies(templates);
        return this;
    }

    public Companies addTemplatesCompany(Templates templates) {
        this.templatesCompanies.add(templates);
        templates.setCompany(this);
        return this;
    }

    public Companies removeTemplatesCompany(Templates templates) {
        this.templatesCompanies.remove(templates);
        templates.setCompany(null);
        return this;
    }

    public Set<Users> getUsersCompanies() {
        return this.usersCompanies;
    }

    public void setUsersCompanies(Set<Users> users) {
        if (this.usersCompanies != null) {
            this.usersCompanies.forEach(i -> i.setCompany(null));
        }
        if (users != null) {
            users.forEach(i -> i.setCompany(this));
        }
        this.usersCompanies = users;
    }

    public Companies usersCompanies(Set<Users> users) {
        this.setUsersCompanies(users);
        return this;
    }

    public Companies addUsersCompany(Users users) {
        this.usersCompanies.add(users);
        users.setCompany(this);
        return this;
    }

    public Companies removeUsersCompany(Users users) {
        this.usersCompanies.remove(users);
        users.setCompany(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Companies)) {
            return false;
        }
        return id != null && id.equals(((Companies) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Companies{" +
            "id=" + getId() +
            ", country='" + getCountry() + "'" +
            ", url='" + getUrl() + "'" +
            ", name='" + getName() + "'" +
            ", expectedNoOfUsers=" + getExpectedNoOfUsers() +
            ", createdBy=" + getCreatedBy() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
