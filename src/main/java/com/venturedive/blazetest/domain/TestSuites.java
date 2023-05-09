package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestSuites.
 */
@Entity
@Table(name = "test_suites")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestSuites implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "test_suite_name", length = 255)
    private String testSuiteName;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "defaultTemplate", "company", "milestonesProjects", "testplansProjects", "testsuitesProjects", "users" },
        allowSetters = true
    )
    private Projects project;

    @OneToMany(mappedBy = "testSuite")
    @JsonIgnoreProperties(value = { "testSuite", "parentSection", "sectionsParentsections", "testcasesSections" }, allowSetters = true)
    private Set<Sections> sectionsTestsuites = new HashSet<>();

    @OneToMany(mappedBy = "testSuite")
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
    private Set<TestCases> testcasesTestsuites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestSuites id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestSuiteName() {
        return this.testSuiteName;
    }

    public TestSuites testSuiteName(String testSuiteName) {
        this.setTestSuiteName(testSuiteName);
        return this;
    }

    public void setTestSuiteName(String testSuiteName) {
        this.testSuiteName = testSuiteName;
    }

    public String getDescription() {
        return this.description;
    }

    public TestSuites description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public TestSuites createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public TestSuites createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public TestSuites updatedBy(Integer updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public TestSuites updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Projects getProject() {
        return this.project;
    }

    public void setProject(Projects projects) {
        this.project = projects;
    }

    public TestSuites project(Projects projects) {
        this.setProject(projects);
        return this;
    }

    public Set<Sections> getSectionsTestsuites() {
        return this.sectionsTestsuites;
    }

    public void setSectionsTestsuites(Set<Sections> sections) {
        if (this.sectionsTestsuites != null) {
            this.sectionsTestsuites.forEach(i -> i.setTestSuite(null));
        }
        if (sections != null) {
            sections.forEach(i -> i.setTestSuite(this));
        }
        this.sectionsTestsuites = sections;
    }

    public TestSuites sectionsTestsuites(Set<Sections> sections) {
        this.setSectionsTestsuites(sections);
        return this;
    }

    public TestSuites addSectionsTestsuite(Sections sections) {
        this.sectionsTestsuites.add(sections);
        sections.setTestSuite(this);
        return this;
    }

    public TestSuites removeSectionsTestsuite(Sections sections) {
        this.sectionsTestsuites.remove(sections);
        sections.setTestSuite(null);
        return this;
    }

    public Set<TestCases> getTestcasesTestsuites() {
        return this.testcasesTestsuites;
    }

    public void setTestcasesTestsuites(Set<TestCases> testCases) {
        if (this.testcasesTestsuites != null) {
            this.testcasesTestsuites.forEach(i -> i.setTestSuite(null));
        }
        if (testCases != null) {
            testCases.forEach(i -> i.setTestSuite(this));
        }
        this.testcasesTestsuites = testCases;
    }

    public TestSuites testcasesTestsuites(Set<TestCases> testCases) {
        this.setTestcasesTestsuites(testCases);
        return this;
    }

    public TestSuites addTestcasesTestsuite(TestCases testCases) {
        this.testcasesTestsuites.add(testCases);
        testCases.setTestSuite(this);
        return this;
    }

    public TestSuites removeTestcasesTestsuite(TestCases testCases) {
        this.testcasesTestsuites.remove(testCases);
        testCases.setTestSuite(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestSuites)) {
            return false;
        }
        return id != null && id.equals(((TestSuites) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestSuites{" +
            "id=" + getId() +
            ", testSuiteName='" + getTestSuiteName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
