package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestRuns.
 */
@Entity
@Table(name = "test_runs")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestRuns implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "testrunsTestlevels", "testCases" }, allowSetters = true)
    private TestLevels testLevel;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "parentMilestone", "project", "milestonesParentmilestones", "testcasesMilestones", "testrunsMilestones" },
        allowSetters = true
    )
    private Milestones mileStone;

    @OneToMany(mappedBy = "testRun")
    @JsonIgnoreProperties(
        value = { "testRun", "testCase", "status", "testrundetailattachmentsTestrundetails", "testrunstepdetailsTestrundetails" },
        allowSetters = true
    )
    private Set<TestRunDetails> testrundetailsTestruns = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestRuns id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TestRuns name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public TestRuns description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public TestRuns createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public TestRuns createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public TestLevels getTestLevel() {
        return this.testLevel;
    }

    public void setTestLevel(TestLevels testLevels) {
        this.testLevel = testLevels;
    }

    public TestRuns testLevel(TestLevels testLevels) {
        this.setTestLevel(testLevels);
        return this;
    }

    public Milestones getMileStone() {
        return this.mileStone;
    }

    public void setMileStone(Milestones milestones) {
        this.mileStone = milestones;
    }

    public TestRuns mileStone(Milestones milestones) {
        this.setMileStone(milestones);
        return this;
    }

    public Set<TestRunDetails> getTestrundetailsTestruns() {
        return this.testrundetailsTestruns;
    }

    public void setTestrundetailsTestruns(Set<TestRunDetails> testRunDetails) {
        if (this.testrundetailsTestruns != null) {
            this.testrundetailsTestruns.forEach(i -> i.setTestRun(null));
        }
        if (testRunDetails != null) {
            testRunDetails.forEach(i -> i.setTestRun(this));
        }
        this.testrundetailsTestruns = testRunDetails;
    }

    public TestRuns testrundetailsTestruns(Set<TestRunDetails> testRunDetails) {
        this.setTestrundetailsTestruns(testRunDetails);
        return this;
    }

    public TestRuns addTestrundetailsTestrun(TestRunDetails testRunDetails) {
        this.testrundetailsTestruns.add(testRunDetails);
        testRunDetails.setTestRun(this);
        return this;
    }

    public TestRuns removeTestrundetailsTestrun(TestRunDetails testRunDetails) {
        this.testrundetailsTestruns.remove(testRunDetails);
        testRunDetails.setTestRun(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestRuns)) {
            return false;
        }
        return id != null && id.equals(((TestRuns) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestRuns{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy=" + getCreatedBy() +
            "}";
    }
}
