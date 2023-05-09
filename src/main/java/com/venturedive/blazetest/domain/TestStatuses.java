package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestStatuses.
 */
@Entity
@Table(name = "test_statuses")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestStatuses implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "status_name", length = 255)
    private String statusName;

    @OneToMany(mappedBy = "status")
    @JsonIgnoreProperties(
        value = { "testRun", "testCase", "status", "testrundetailattachmentsTestrundetails", "testrunstepdetailsTestrundetails" },
        allowSetters = true
    )
    private Set<TestRunDetails> testrundetailsStatuses = new HashSet<>();

    @OneToMany(mappedBy = "status")
    @JsonIgnoreProperties(
        value = { "testRunDetail", "stepDetail", "status", "testrunstepdetailattachmentsTestrunstepdetails" },
        allowSetters = true
    )
    private Set<TestRunStepDetails> testrunstepdetailsStatuses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestStatuses id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatusName() {
        return this.statusName;
    }

    public TestStatuses statusName(String statusName) {
        this.setStatusName(statusName);
        return this;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Set<TestRunDetails> getTestrundetailsStatuses() {
        return this.testrundetailsStatuses;
    }

    public void setTestrundetailsStatuses(Set<TestRunDetails> testRunDetails) {
        if (this.testrundetailsStatuses != null) {
            this.testrundetailsStatuses.forEach(i -> i.setStatus(null));
        }
        if (testRunDetails != null) {
            testRunDetails.forEach(i -> i.setStatus(this));
        }
        this.testrundetailsStatuses = testRunDetails;
    }

    public TestStatuses testrundetailsStatuses(Set<TestRunDetails> testRunDetails) {
        this.setTestrundetailsStatuses(testRunDetails);
        return this;
    }

    public TestStatuses addTestrundetailsStatus(TestRunDetails testRunDetails) {
        this.testrundetailsStatuses.add(testRunDetails);
        testRunDetails.setStatus(this);
        return this;
    }

    public TestStatuses removeTestrundetailsStatus(TestRunDetails testRunDetails) {
        this.testrundetailsStatuses.remove(testRunDetails);
        testRunDetails.setStatus(null);
        return this;
    }

    public Set<TestRunStepDetails> getTestrunstepdetailsStatuses() {
        return this.testrunstepdetailsStatuses;
    }

    public void setTestrunstepdetailsStatuses(Set<TestRunStepDetails> testRunStepDetails) {
        if (this.testrunstepdetailsStatuses != null) {
            this.testrunstepdetailsStatuses.forEach(i -> i.setStatus(null));
        }
        if (testRunStepDetails != null) {
            testRunStepDetails.forEach(i -> i.setStatus(this));
        }
        this.testrunstepdetailsStatuses = testRunStepDetails;
    }

    public TestStatuses testrunstepdetailsStatuses(Set<TestRunStepDetails> testRunStepDetails) {
        this.setTestrunstepdetailsStatuses(testRunStepDetails);
        return this;
    }

    public TestStatuses addTestrunstepdetailsStatus(TestRunStepDetails testRunStepDetails) {
        this.testrunstepdetailsStatuses.add(testRunStepDetails);
        testRunStepDetails.setStatus(this);
        return this;
    }

    public TestStatuses removeTestrunstepdetailsStatus(TestRunStepDetails testRunStepDetails) {
        this.testrunstepdetailsStatuses.remove(testRunStepDetails);
        testRunStepDetails.setStatus(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestStatuses)) {
            return false;
        }
        return id != null && id.equals(((TestStatuses) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestStatuses{" +
            "id=" + getId() +
            ", statusName='" + getStatusName() + "'" +
            "}";
    }
}
