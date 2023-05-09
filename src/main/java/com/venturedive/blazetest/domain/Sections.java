package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Sections.
 */
@Entity
@Table(name = "sections")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sections implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "project", "sectionsTestsuites", "testcasesTestsuites" }, allowSetters = true)
    private TestSuites testSuite;

    @ManyToOne
    @JsonIgnoreProperties(value = { "testSuite", "parentSection", "sectionsParentsections", "testcasesSections" }, allowSetters = true)
    private Sections parentSection;

    @OneToMany(mappedBy = "parentSection")
    @JsonIgnoreProperties(value = { "testSuite", "parentSection", "sectionsParentsections", "testcasesSections" }, allowSetters = true)
    private Set<Sections> sectionsParentsections = new HashSet<>();

    @OneToMany(mappedBy = "section")
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
    private Set<TestCases> testcasesSections = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sections id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Sections name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Sections description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Sections createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public Sections createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Sections updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public Sections updatedBy(Integer updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public TestSuites getTestSuite() {
        return this.testSuite;
    }

    public void setTestSuite(TestSuites testSuites) {
        this.testSuite = testSuites;
    }

    public Sections testSuite(TestSuites testSuites) {
        this.setTestSuite(testSuites);
        return this;
    }

    public Sections getParentSection() {
        return this.parentSection;
    }

    public void setParentSection(Sections sections) {
        this.parentSection = sections;
    }

    public Sections parentSection(Sections sections) {
        this.setParentSection(sections);
        return this;
    }

    public Set<Sections> getSectionsParentsections() {
        return this.sectionsParentsections;
    }

    public void setSectionsParentsections(Set<Sections> sections) {
        if (this.sectionsParentsections != null) {
            this.sectionsParentsections.forEach(i -> i.setParentSection(null));
        }
        if (sections != null) {
            sections.forEach(i -> i.setParentSection(this));
        }
        this.sectionsParentsections = sections;
    }

    public Sections sectionsParentsections(Set<Sections> sections) {
        this.setSectionsParentsections(sections);
        return this;
    }

    public Sections addSectionsParentsection(Sections sections) {
        this.sectionsParentsections.add(sections);
        sections.setParentSection(this);
        return this;
    }

    public Sections removeSectionsParentsection(Sections sections) {
        this.sectionsParentsections.remove(sections);
        sections.setParentSection(null);
        return this;
    }

    public Set<TestCases> getTestcasesSections() {
        return this.testcasesSections;
    }

    public void setTestcasesSections(Set<TestCases> testCases) {
        if (this.testcasesSections != null) {
            this.testcasesSections.forEach(i -> i.setSection(null));
        }
        if (testCases != null) {
            testCases.forEach(i -> i.setSection(this));
        }
        this.testcasesSections = testCases;
    }

    public Sections testcasesSections(Set<TestCases> testCases) {
        this.setTestcasesSections(testCases);
        return this;
    }

    public Sections addTestcasesSection(TestCases testCases) {
        this.testcasesSections.add(testCases);
        testCases.setSection(this);
        return this;
    }

    public Sections removeTestcasesSection(TestCases testCases) {
        this.testcasesSections.remove(testCases);
        testCases.setSection(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sections)) {
            return false;
        }
        return id != null && id.equals(((Sections) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sections{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            "}";
    }
}
