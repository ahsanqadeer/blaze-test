package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Projects.
 */
@Entity
@Table(name = "projects")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Projects implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "project_name", length = 255, nullable = false, unique = true)
    private String projectName;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "isactive")
    private Boolean isactive;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JsonIgnoreProperties(value = { "company", "templateFields", "projectsDefaulttemplates", "testcasesTemplates" }, allowSetters = true)
    private Templates defaultTemplate;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "projectsCompanies", "templatefieldsCompanies", "templatesCompanies", "usersCompanies" },
        allowSetters = true
    )
    private Companies company;

    @OneToMany(mappedBy = "project")
    @JsonIgnoreProperties(
        value = { "parentMilestone", "project", "milestonesParentmilestones", "testcasesMilestones", "testrunsMilestones" },
        allowSetters = true
    )
    private Set<Milestones> milestonesProjects = new HashSet<>();

    @OneToMany(mappedBy = "project")
    @JsonIgnoreProperties(value = { "project" }, allowSetters = true)
    private Set<TestPlans> testplansProjects = new HashSet<>();

    @OneToMany(mappedBy = "project")
    @JsonIgnoreProperties(value = { "project", "sectionsTestsuites", "testcasesTestsuites" }, allowSetters = true)
    private Set<TestSuites> testsuitesProjects = new HashSet<>();

    @ManyToMany(mappedBy = "projects")
    @JsonIgnoreProperties(value = { "company", "projects", "roles" }, allowSetters = true)
    private Set<Users> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Projects id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public Projects projectName(String projectName) {
        this.setProjectName(projectName);
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return this.description;
    }

    public Projects description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsactive() {
        return this.isactive;
    }

    public Projects isactive(Boolean isactive) {
        this.setIsactive(isactive);
        return this;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public Projects createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Projects createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public Projects updatedBy(Integer updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Projects updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Templates getDefaultTemplate() {
        return this.defaultTemplate;
    }

    public void setDefaultTemplate(Templates templates) {
        this.defaultTemplate = templates;
    }

    public Projects defaultTemplate(Templates templates) {
        this.setDefaultTemplate(templates);
        return this;
    }

    public Companies getCompany() {
        return this.company;
    }

    public void setCompany(Companies companies) {
        this.company = companies;
    }

    public Projects company(Companies companies) {
        this.setCompany(companies);
        return this;
    }

    public Set<Milestones> getMilestonesProjects() {
        return this.milestonesProjects;
    }

    public void setMilestonesProjects(Set<Milestones> milestones) {
        if (this.milestonesProjects != null) {
            this.milestonesProjects.forEach(i -> i.setProject(null));
        }
        if (milestones != null) {
            milestones.forEach(i -> i.setProject(this));
        }
        this.milestonesProjects = milestones;
    }

    public Projects milestonesProjects(Set<Milestones> milestones) {
        this.setMilestonesProjects(milestones);
        return this;
    }

    public Projects addMilestonesProject(Milestones milestones) {
        this.milestonesProjects.add(milestones);
        milestones.setProject(this);
        return this;
    }

    public Projects removeMilestonesProject(Milestones milestones) {
        this.milestonesProjects.remove(milestones);
        milestones.setProject(null);
        return this;
    }

    public Set<TestPlans> getTestplansProjects() {
        return this.testplansProjects;
    }

    public void setTestplansProjects(Set<TestPlans> testPlans) {
        if (this.testplansProjects != null) {
            this.testplansProjects.forEach(i -> i.setProject(null));
        }
        if (testPlans != null) {
            testPlans.forEach(i -> i.setProject(this));
        }
        this.testplansProjects = testPlans;
    }

    public Projects testplansProjects(Set<TestPlans> testPlans) {
        this.setTestplansProjects(testPlans);
        return this;
    }

    public Projects addTestplansProject(TestPlans testPlans) {
        this.testplansProjects.add(testPlans);
        testPlans.setProject(this);
        return this;
    }

    public Projects removeTestplansProject(TestPlans testPlans) {
        this.testplansProjects.remove(testPlans);
        testPlans.setProject(null);
        return this;
    }

    public Set<TestSuites> getTestsuitesProjects() {
        return this.testsuitesProjects;
    }

    public void setTestsuitesProjects(Set<TestSuites> testSuites) {
        if (this.testsuitesProjects != null) {
            this.testsuitesProjects.forEach(i -> i.setProject(null));
        }
        if (testSuites != null) {
            testSuites.forEach(i -> i.setProject(this));
        }
        this.testsuitesProjects = testSuites;
    }

    public Projects testsuitesProjects(Set<TestSuites> testSuites) {
        this.setTestsuitesProjects(testSuites);
        return this;
    }

    public Projects addTestsuitesProject(TestSuites testSuites) {
        this.testsuitesProjects.add(testSuites);
        testSuites.setProject(this);
        return this;
    }

    public Projects removeTestsuitesProject(TestSuites testSuites) {
        this.testsuitesProjects.remove(testSuites);
        testSuites.setProject(null);
        return this;
    }

    public Set<Users> getUsers() {
        return this.users;
    }

    public void setUsers(Set<Users> users) {
        if (this.users != null) {
            this.users.forEach(i -> i.removeProject(this));
        }
        if (users != null) {
            users.forEach(i -> i.addProject(this));
        }
        this.users = users;
    }

    public Projects users(Set<Users> users) {
        this.setUsers(users);
        return this;
    }

    public Projects addUser(Users users) {
        this.users.add(users);
        users.getProjects().add(this);
        return this;
    }

    public Projects removeUser(Users users) {
        this.users.remove(users);
        users.getProjects().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Projects)) {
            return false;
        }
        return id != null && id.equals(((Projects) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Projects{" +
            "id=" + getId() +
            ", projectName='" + getProjectName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isactive='" + getIsactive() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
