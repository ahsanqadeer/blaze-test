package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestCaseFields.
 */
@Entity
@Table(name = "test_case_fields")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCaseFields implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "expected_result")
    private String expectedResult;

    @Size(max = 255)
    @Column(name = "value", length = 255)
    private String value;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "company", "templateFieldType", "testcasefieldsTemplatefields", "templates" }, allowSetters = true)
    private TemplateFields templateField;

    @ManyToOne(optional = false)
    @NotNull
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

    @OneToMany(mappedBy = "testCaseField")
    @JsonIgnoreProperties(value = { "testCaseField" }, allowSetters = true)
    private Set<TestCaseFieldAttachments> testcasefieldattachmentsTestcasefields = new HashSet<>();

    @OneToMany(mappedBy = "stepDetail")
    @JsonIgnoreProperties(
        value = { "testRunDetail", "stepDetail", "status", "testrunstepdetailattachmentsTestrunstepdetails" },
        allowSetters = true
    )
    private Set<TestRunStepDetails> testrunstepdetailsStepdetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestCaseFields id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExpectedResult() {
        return this.expectedResult;
    }

    public TestCaseFields expectedResult(String expectedResult) {
        this.setExpectedResult(expectedResult);
        return this;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public String getValue() {
        return this.value;
    }

    public TestCaseFields value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TemplateFields getTemplateField() {
        return this.templateField;
    }

    public void setTemplateField(TemplateFields templateFields) {
        this.templateField = templateFields;
    }

    public TestCaseFields templateField(TemplateFields templateFields) {
        this.setTemplateField(templateFields);
        return this;
    }

    public TestCases getTestCase() {
        return this.testCase;
    }

    public void setTestCase(TestCases testCases) {
        this.testCase = testCases;
    }

    public TestCaseFields testCase(TestCases testCases) {
        this.setTestCase(testCases);
        return this;
    }

    public Set<TestCaseFieldAttachments> getTestcasefieldattachmentsTestcasefields() {
        return this.testcasefieldattachmentsTestcasefields;
    }

    public void setTestcasefieldattachmentsTestcasefields(Set<TestCaseFieldAttachments> testCaseFieldAttachments) {
        if (this.testcasefieldattachmentsTestcasefields != null) {
            this.testcasefieldattachmentsTestcasefields.forEach(i -> i.setTestCaseField(null));
        }
        if (testCaseFieldAttachments != null) {
            testCaseFieldAttachments.forEach(i -> i.setTestCaseField(this));
        }
        this.testcasefieldattachmentsTestcasefields = testCaseFieldAttachments;
    }

    public TestCaseFields testcasefieldattachmentsTestcasefields(Set<TestCaseFieldAttachments> testCaseFieldAttachments) {
        this.setTestcasefieldattachmentsTestcasefields(testCaseFieldAttachments);
        return this;
    }

    public TestCaseFields addTestcasefieldattachmentsTestcasefield(TestCaseFieldAttachments testCaseFieldAttachments) {
        this.testcasefieldattachmentsTestcasefields.add(testCaseFieldAttachments);
        testCaseFieldAttachments.setTestCaseField(this);
        return this;
    }

    public TestCaseFields removeTestcasefieldattachmentsTestcasefield(TestCaseFieldAttachments testCaseFieldAttachments) {
        this.testcasefieldattachmentsTestcasefields.remove(testCaseFieldAttachments);
        testCaseFieldAttachments.setTestCaseField(null);
        return this;
    }

    public Set<TestRunStepDetails> getTestrunstepdetailsStepdetails() {
        return this.testrunstepdetailsStepdetails;
    }

    public void setTestrunstepdetailsStepdetails(Set<TestRunStepDetails> testRunStepDetails) {
        if (this.testrunstepdetailsStepdetails != null) {
            this.testrunstepdetailsStepdetails.forEach(i -> i.setStepDetail(null));
        }
        if (testRunStepDetails != null) {
            testRunStepDetails.forEach(i -> i.setStepDetail(this));
        }
        this.testrunstepdetailsStepdetails = testRunStepDetails;
    }

    public TestCaseFields testrunstepdetailsStepdetails(Set<TestRunStepDetails> testRunStepDetails) {
        this.setTestrunstepdetailsStepdetails(testRunStepDetails);
        return this;
    }

    public TestCaseFields addTestrunstepdetailsStepdetail(TestRunStepDetails testRunStepDetails) {
        this.testrunstepdetailsStepdetails.add(testRunStepDetails);
        testRunStepDetails.setStepDetail(this);
        return this;
    }

    public TestCaseFields removeTestrunstepdetailsStepdetail(TestRunStepDetails testRunStepDetails) {
        this.testrunstepdetailsStepdetails.remove(testRunStepDetails);
        testRunStepDetails.setStepDetail(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCaseFields)) {
            return false;
        }
        return id != null && id.equals(((TestCaseFields) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCaseFields{" +
            "id=" + getId() +
            ", expectedResult='" + getExpectedResult() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
