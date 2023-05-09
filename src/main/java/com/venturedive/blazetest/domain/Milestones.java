package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Milestones.
 */
@Entity
@Table(name = "milestones")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Milestones implements Serializable {

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

    @Lob
    @Column(name = "reference")
    private String reference;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "parentMilestone", "project", "milestonesParentmilestones", "testcasesMilestones", "testrunsMilestones" },
        allowSetters = true
    )
    private Milestones parentMilestone;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "defaultTemplate", "company", "milestonesProjects", "testplansProjects", "testsuitesProjects", "users" },
        allowSetters = true
    )
    private Projects project;

    @OneToMany(mappedBy = "parentMilestone")
    @JsonIgnoreProperties(
        value = { "parentMilestone", "project", "milestonesParentmilestones", "testcasesMilestones", "testrunsMilestones" },
        allowSetters = true
    )
    private Set<Milestones> milestonesParentmilestones = new HashSet<>();

    @OneToMany(mappedBy = "milestone")
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
    private Set<TestCases> testcasesMilestones = new HashSet<>();

    @OneToMany(mappedBy = "mileStone")
    @JsonIgnoreProperties(value = { "testLevel", "mileStone", "testrundetailsTestruns" }, allowSetters = true)
    private Set<TestRuns> testrunsMilestones = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Milestones id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Milestones name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Milestones description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return this.reference;
    }

    public Milestones reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Milestones startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Milestones endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsCompleted() {
        return this.isCompleted;
    }

    public Milestones isCompleted(Boolean isCompleted) {
        this.setIsCompleted(isCompleted);
        return this;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Milestones getParentMilestone() {
        return this.parentMilestone;
    }

    public void setParentMilestone(Milestones milestones) {
        this.parentMilestone = milestones;
    }

    public Milestones parentMilestone(Milestones milestones) {
        this.setParentMilestone(milestones);
        return this;
    }

    public Projects getProject() {
        return this.project;
    }

    public void setProject(Projects projects) {
        this.project = projects;
    }

    public Milestones project(Projects projects) {
        this.setProject(projects);
        return this;
    }

    public Set<Milestones> getMilestonesParentmilestones() {
        return this.milestonesParentmilestones;
    }

    public void setMilestonesParentmilestones(Set<Milestones> milestones) {
        if (this.milestonesParentmilestones != null) {
            this.milestonesParentmilestones.forEach(i -> i.setParentMilestone(null));
        }
        if (milestones != null) {
            milestones.forEach(i -> i.setParentMilestone(this));
        }
        this.milestonesParentmilestones = milestones;
    }

    public Milestones milestonesParentmilestones(Set<Milestones> milestones) {
        this.setMilestonesParentmilestones(milestones);
        return this;
    }

    public Milestones addMilestonesParentmilestone(Milestones milestones) {
        this.milestonesParentmilestones.add(milestones);
        milestones.setParentMilestone(this);
        return this;
    }

    public Milestones removeMilestonesParentmilestone(Milestones milestones) {
        this.milestonesParentmilestones.remove(milestones);
        milestones.setParentMilestone(null);
        return this;
    }

    public Set<TestCases> getTestcasesMilestones() {
        return this.testcasesMilestones;
    }

    public void setTestcasesMilestones(Set<TestCases> testCases) {
        if (this.testcasesMilestones != null) {
            this.testcasesMilestones.forEach(i -> i.setMilestone(null));
        }
        if (testCases != null) {
            testCases.forEach(i -> i.setMilestone(this));
        }
        this.testcasesMilestones = testCases;
    }

    public Milestones testcasesMilestones(Set<TestCases> testCases) {
        this.setTestcasesMilestones(testCases);
        return this;
    }

    public Milestones addTestcasesMilestone(TestCases testCases) {
        this.testcasesMilestones.add(testCases);
        testCases.setMilestone(this);
        return this;
    }

    public Milestones removeTestcasesMilestone(TestCases testCases) {
        this.testcasesMilestones.remove(testCases);
        testCases.setMilestone(null);
        return this;
    }

    public Set<TestRuns> getTestrunsMilestones() {
        return this.testrunsMilestones;
    }

    public void setTestrunsMilestones(Set<TestRuns> testRuns) {
        if (this.testrunsMilestones != null) {
            this.testrunsMilestones.forEach(i -> i.setMileStone(null));
        }
        if (testRuns != null) {
            testRuns.forEach(i -> i.setMileStone(this));
        }
        this.testrunsMilestones = testRuns;
    }

    public Milestones testrunsMilestones(Set<TestRuns> testRuns) {
        this.setTestrunsMilestones(testRuns);
        return this;
    }

    public Milestones addTestrunsMilestone(TestRuns testRuns) {
        this.testrunsMilestones.add(testRuns);
        testRuns.setMileStone(this);
        return this;
    }

    public Milestones removeTestrunsMilestone(TestRuns testRuns) {
        this.testrunsMilestones.remove(testRuns);
        testRuns.setMileStone(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Milestones)) {
            return false;
        }
        return id != null && id.equals(((Milestones) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Milestones{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", reference='" + getReference() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", isCompleted='" + getIsCompleted() + "'" +
            "}";
    }
}
