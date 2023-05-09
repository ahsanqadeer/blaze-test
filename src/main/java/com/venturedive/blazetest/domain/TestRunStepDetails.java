package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A TestRunStepDetails.
 */
@Entity
@Table(name = "test_run_step_details")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestRunStepDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "actual_result")
    private String actualResult;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "testRun", "testCase", "status", "testrundetailattachmentsTestrundetails", "testrunstepdetailsTestrundetails" },
        allowSetters = true
    )
    private TestRunDetails testRunDetail;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "templateField", "testCase", "testcasefieldattachmentsTestcasefields", "testrunstepdetailsStepdetails" },
        allowSetters = true
    )
    private TestCaseFields stepDetail;

    @ManyToOne
    @JsonIgnoreProperties(value = { "testrundetailsStatuses", "testrunstepdetailsStatuses" }, allowSetters = true)
    private TestStatuses status;

    @OneToMany(mappedBy = "testRunStepDetail")
    @JsonIgnoreProperties(value = { "testRunStepDetail" }, allowSetters = true)
    private Set<TestRunStepDetailAttachments> testrunstepdetailattachmentsTestrunstepdetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestRunStepDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActualResult() {
        return this.actualResult;
    }

    public TestRunStepDetails actualResult(String actualResult) {
        this.setActualResult(actualResult);
        return this;
    }

    public void setActualResult(String actualResult) {
        this.actualResult = actualResult;
    }

    public TestRunDetails getTestRunDetail() {
        return this.testRunDetail;
    }

    public void setTestRunDetail(TestRunDetails testRunDetails) {
        this.testRunDetail = testRunDetails;
    }

    public TestRunStepDetails testRunDetail(TestRunDetails testRunDetails) {
        this.setTestRunDetail(testRunDetails);
        return this;
    }

    public TestCaseFields getStepDetail() {
        return this.stepDetail;
    }

    public void setStepDetail(TestCaseFields testCaseFields) {
        this.stepDetail = testCaseFields;
    }

    public TestRunStepDetails stepDetail(TestCaseFields testCaseFields) {
        this.setStepDetail(testCaseFields);
        return this;
    }

    public TestStatuses getStatus() {
        return this.status;
    }

    public void setStatus(TestStatuses testStatuses) {
        this.status = testStatuses;
    }

    public TestRunStepDetails status(TestStatuses testStatuses) {
        this.setStatus(testStatuses);
        return this;
    }

    public Set<TestRunStepDetailAttachments> getTestrunstepdetailattachmentsTestrunstepdetails() {
        return this.testrunstepdetailattachmentsTestrunstepdetails;
    }

    public void setTestrunstepdetailattachmentsTestrunstepdetails(Set<TestRunStepDetailAttachments> testRunStepDetailAttachments) {
        if (this.testrunstepdetailattachmentsTestrunstepdetails != null) {
            this.testrunstepdetailattachmentsTestrunstepdetails.forEach(i -> i.setTestRunStepDetail(null));
        }
        if (testRunStepDetailAttachments != null) {
            testRunStepDetailAttachments.forEach(i -> i.setTestRunStepDetail(this));
        }
        this.testrunstepdetailattachmentsTestrunstepdetails = testRunStepDetailAttachments;
    }

    public TestRunStepDetails testrunstepdetailattachmentsTestrunstepdetails(
        Set<TestRunStepDetailAttachments> testRunStepDetailAttachments
    ) {
        this.setTestrunstepdetailattachmentsTestrunstepdetails(testRunStepDetailAttachments);
        return this;
    }

    public TestRunStepDetails addTestrunstepdetailattachmentsTestrunstepdetail(TestRunStepDetailAttachments testRunStepDetailAttachments) {
        this.testrunstepdetailattachmentsTestrunstepdetails.add(testRunStepDetailAttachments);
        testRunStepDetailAttachments.setTestRunStepDetail(this);
        return this;
    }

    public TestRunStepDetails removeTestrunstepdetailattachmentsTestrunstepdetail(
        TestRunStepDetailAttachments testRunStepDetailAttachments
    ) {
        this.testrunstepdetailattachmentsTestrunstepdetails.remove(testRunStepDetailAttachments);
        testRunStepDetailAttachments.setTestRunStepDetail(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestRunStepDetails)) {
            return false;
        }
        return id != null && id.equals(((TestRunStepDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestRunStepDetails{" +
            "id=" + getId() +
            ", actualResult='" + getActualResult() + "'" +
            "}";
    }
}
