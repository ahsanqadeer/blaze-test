package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestRunDetails.
 */
@Entity
@Table(name = "test_run_details")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestRunDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "result_detail", length = 255)
    private String resultDetail;

    @Size(max = 255)
    @Column(name = "jira_id", length = 255)
    private String jiraId;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "executed_by")
    private Integer executedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "testLevel", "mileStone", "testrundetailsTestruns" }, allowSetters = true)
    private TestRuns testRun;

    @ManyToOne
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
    private TestCases testCase;

    @ManyToOne
    @JsonIgnoreProperties(value = { "testrundetailsStatuses", "testrunstepdetailsStatuses" }, allowSetters = true)
    private TestStatuses status;

    @OneToMany(mappedBy = "testRunDetail")
    @JsonIgnoreProperties(value = { "testRunDetail" }, allowSetters = true)
    private Set<TestRunDetailAttachments> testrundetailattachmentsTestrundetails = new HashSet<>();

    @OneToMany(mappedBy = "testRunDetail")
    @JsonIgnoreProperties(
        value = { "testRunDetail", "stepDetail", "status", "testrunstepdetailattachmentsTestrunstepdetails" },
        allowSetters = true
    )
    private Set<TestRunStepDetails> testrunstepdetailsTestrundetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestRunDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResultDetail() {
        return this.resultDetail;
    }

    public TestRunDetails resultDetail(String resultDetail) {
        this.setResultDetail(resultDetail);
        return this;
    }

    public void setResultDetail(String resultDetail) {
        this.resultDetail = resultDetail;
    }

    public String getJiraId() {
        return this.jiraId;
    }

    public TestRunDetails jiraId(String jiraId) {
        this.setJiraId(jiraId);
        return this;
    }

    public void setJiraId(String jiraId) {
        this.jiraId = jiraId;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public TestRunDetails createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getExecutedBy() {
        return this.executedBy;
    }

    public TestRunDetails executedBy(Integer executedBy) {
        this.setExecutedBy(executedBy);
        return this;
    }

    public void setExecutedBy(Integer executedBy) {
        this.executedBy = executedBy;
    }

    public TestRuns getTestRun() {
        return this.testRun;
    }

    public void setTestRun(TestRuns testRuns) {
        this.testRun = testRuns;
    }

    public TestRunDetails testRun(TestRuns testRuns) {
        this.setTestRun(testRuns);
        return this;
    }

    public TestCases getTestCase() {
        return this.testCase;
    }

    public void setTestCase(TestCases testCases) {
        this.testCase = testCases;
    }

    public TestRunDetails testCase(TestCases testCases) {
        this.setTestCase(testCases);
        return this;
    }

    public TestStatuses getStatus() {
        return this.status;
    }

    public void setStatus(TestStatuses testStatuses) {
        this.status = testStatuses;
    }

    public TestRunDetails status(TestStatuses testStatuses) {
        this.setStatus(testStatuses);
        return this;
    }

    public Set<TestRunDetailAttachments> getTestrundetailattachmentsTestrundetails() {
        return this.testrundetailattachmentsTestrundetails;
    }

    public void setTestrundetailattachmentsTestrundetails(Set<TestRunDetailAttachments> testRunDetailAttachments) {
        if (this.testrundetailattachmentsTestrundetails != null) {
            this.testrundetailattachmentsTestrundetails.forEach(i -> i.setTestRunDetail(null));
        }
        if (testRunDetailAttachments != null) {
            testRunDetailAttachments.forEach(i -> i.setTestRunDetail(this));
        }
        this.testrundetailattachmentsTestrundetails = testRunDetailAttachments;
    }

    public TestRunDetails testrundetailattachmentsTestrundetails(Set<TestRunDetailAttachments> testRunDetailAttachments) {
        this.setTestrundetailattachmentsTestrundetails(testRunDetailAttachments);
        return this;
    }

    public TestRunDetails addTestrundetailattachmentsTestrundetail(TestRunDetailAttachments testRunDetailAttachments) {
        this.testrundetailattachmentsTestrundetails.add(testRunDetailAttachments);
        testRunDetailAttachments.setTestRunDetail(this);
        return this;
    }

    public TestRunDetails removeTestrundetailattachmentsTestrundetail(TestRunDetailAttachments testRunDetailAttachments) {
        this.testrundetailattachmentsTestrundetails.remove(testRunDetailAttachments);
        testRunDetailAttachments.setTestRunDetail(null);
        return this;
    }

    public Set<TestRunStepDetails> getTestrunstepdetailsTestrundetails() {
        return this.testrunstepdetailsTestrundetails;
    }

    public void setTestrunstepdetailsTestrundetails(Set<TestRunStepDetails> testRunStepDetails) {
        if (this.testrunstepdetailsTestrundetails != null) {
            this.testrunstepdetailsTestrundetails.forEach(i -> i.setTestRunDetail(null));
        }
        if (testRunStepDetails != null) {
            testRunStepDetails.forEach(i -> i.setTestRunDetail(this));
        }
        this.testrunstepdetailsTestrundetails = testRunStepDetails;
    }

    public TestRunDetails testrunstepdetailsTestrundetails(Set<TestRunStepDetails> testRunStepDetails) {
        this.setTestrunstepdetailsTestrundetails(testRunStepDetails);
        return this;
    }

    public TestRunDetails addTestrunstepdetailsTestrundetail(TestRunStepDetails testRunStepDetails) {
        this.testrunstepdetailsTestrundetails.add(testRunStepDetails);
        testRunStepDetails.setTestRunDetail(this);
        return this;
    }

    public TestRunDetails removeTestrunstepdetailsTestrundetail(TestRunStepDetails testRunStepDetails) {
        this.testrunstepdetailsTestrundetails.remove(testRunStepDetails);
        testRunStepDetails.setTestRunDetail(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestRunDetails)) {
            return false;
        }
        return id != null && id.equals(((TestRunDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestRunDetails{" +
            "id=" + getId() +
            ", resultDetail='" + getResultDetail() + "'" +
            ", jiraId='" + getJiraId() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", executedBy=" + getExecutedBy() +
            "}";
    }
}
