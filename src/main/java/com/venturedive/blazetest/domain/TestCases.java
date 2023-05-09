package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestCases.
 */
@Entity
@Table(name = "test_cases")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCases implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "title", length = 255)
    private String title;

    @Size(max = 255)
    @Column(name = "estimate", length = 255)
    private String estimate;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Size(max = 255)
    @Column(name = "precondition", length = 255)
    private String precondition;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "is_automated")
    private Boolean isAutomated;

    @ManyToOne
    @JsonIgnoreProperties(value = { "project", "sectionsTestsuites", "testcasesTestsuites" }, allowSetters = true)
    private TestSuites testSuite;

    @ManyToOne
    @JsonIgnoreProperties(value = { "testSuite", "parentSection", "sectionsParentsections", "testcasesSections" }, allowSetters = true)
    private Sections section;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "testcasesPriorities" }, allowSetters = true)
    private TestCasePriorities priority;

    @ManyToOne
    @JsonIgnoreProperties(value = { "company", "templateFields", "projectsDefaulttemplates", "testcasesTemplates" }, allowSetters = true)
    private Templates template;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "parentMilestone", "project", "milestonesParentmilestones", "testcasesMilestones", "testrunsMilestones" },
        allowSetters = true
    )
    private Milestones milestone;

    @ManyToMany
    @JoinTable(
        name = "rel_test_cases__test_level",
        joinColumns = @JoinColumn(name = "test_cases_id"),
        inverseJoinColumns = @JoinColumn(name = "test_level_id")
    )
    @JsonIgnoreProperties(value = { "testrunsTestlevels", "testCases" }, allowSetters = true)
    private Set<TestLevels> testLevels = new HashSet<>();

    @OneToMany(mappedBy = "testCase")
    @JsonIgnoreProperties(value = { "testCase" }, allowSetters = true)
    private Set<TestCaseAttachments> testcaseattachmentsTestcases = new HashSet<>();

    @OneToMany(mappedBy = "testCase")
    @JsonIgnoreProperties(
        value = { "templateField", "testCase", "testcasefieldattachmentsTestcasefields", "testrunstepdetailsStepdetails" },
        allowSetters = true
    )
    private Set<TestCaseFields> testcasefieldsTestcases = new HashSet<>();

    @OneToMany(mappedBy = "testCase")
    @JsonIgnoreProperties(
        value = { "testRun", "testCase", "status", "testrundetailattachmentsTestrundetails", "testrunstepdetailsTestrundetails" },
        allowSetters = true
    )
    private Set<TestRunDetails> testrundetailsTestcases = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestCases id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public TestCases title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEstimate() {
        return this.estimate;
    }

    public TestCases estimate(String estimate) {
        this.setEstimate(estimate);
        return this;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public TestCases createdBy(Integer createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public TestCases createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public TestCases updatedBy(Integer updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public TestCases updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPrecondition() {
        return this.precondition;
    }

    public TestCases precondition(String precondition) {
        this.setPrecondition(precondition);
        return this;
    }

    public void setPrecondition(String precondition) {
        this.precondition = precondition;
    }

    public String getDescription() {
        return this.description;
    }

    public TestCases description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsAutomated() {
        return this.isAutomated;
    }

    public TestCases isAutomated(Boolean isAutomated) {
        this.setIsAutomated(isAutomated);
        return this;
    }

    public void setIsAutomated(Boolean isAutomated) {
        this.isAutomated = isAutomated;
    }

    public TestSuites getTestSuite() {
        return this.testSuite;
    }

    public void setTestSuite(TestSuites testSuites) {
        this.testSuite = testSuites;
    }

    public TestCases testSuite(TestSuites testSuites) {
        this.setTestSuite(testSuites);
        return this;
    }

    public Sections getSection() {
        return this.section;
    }

    public void setSection(Sections sections) {
        this.section = sections;
    }

    public TestCases section(Sections sections) {
        this.setSection(sections);
        return this;
    }

    public TestCasePriorities getPriority() {
        return this.priority;
    }

    public void setPriority(TestCasePriorities testCasePriorities) {
        this.priority = testCasePriorities;
    }

    public TestCases priority(TestCasePriorities testCasePriorities) {
        this.setPriority(testCasePriorities);
        return this;
    }

    public Templates getTemplate() {
        return this.template;
    }

    public void setTemplate(Templates templates) {
        this.template = templates;
    }

    public TestCases template(Templates templates) {
        this.setTemplate(templates);
        return this;
    }

    public Milestones getMilestone() {
        return this.milestone;
    }

    public void setMilestone(Milestones milestones) {
        this.milestone = milestones;
    }

    public TestCases milestone(Milestones milestones) {
        this.setMilestone(milestones);
        return this;
    }

    public Set<TestLevels> getTestLevels() {
        return this.testLevels;
    }

    public void setTestLevels(Set<TestLevels> testLevels) {
        this.testLevels = testLevels;
    }

    public TestCases testLevels(Set<TestLevels> testLevels) {
        this.setTestLevels(testLevels);
        return this;
    }

    public TestCases addTestLevel(TestLevels testLevels) {
        this.testLevels.add(testLevels);
        testLevels.getTestCases().add(this);
        return this;
    }

    public TestCases removeTestLevel(TestLevels testLevels) {
        this.testLevels.remove(testLevels);
        testLevels.getTestCases().remove(this);
        return this;
    }

    public Set<TestCaseAttachments> getTestcaseattachmentsTestcases() {
        return this.testcaseattachmentsTestcases;
    }

    public void setTestcaseattachmentsTestcases(Set<TestCaseAttachments> testCaseAttachments) {
        if (this.testcaseattachmentsTestcases != null) {
            this.testcaseattachmentsTestcases.forEach(i -> i.setTestCase(null));
        }
        if (testCaseAttachments != null) {
            testCaseAttachments.forEach(i -> i.setTestCase(this));
        }
        this.testcaseattachmentsTestcases = testCaseAttachments;
    }

    public TestCases testcaseattachmentsTestcases(Set<TestCaseAttachments> testCaseAttachments) {
        this.setTestcaseattachmentsTestcases(testCaseAttachments);
        return this;
    }

    public TestCases addTestcaseattachmentsTestcase(TestCaseAttachments testCaseAttachments) {
        this.testcaseattachmentsTestcases.add(testCaseAttachments);
        testCaseAttachments.setTestCase(this);
        return this;
    }

    public TestCases removeTestcaseattachmentsTestcase(TestCaseAttachments testCaseAttachments) {
        this.testcaseattachmentsTestcases.remove(testCaseAttachments);
        testCaseAttachments.setTestCase(null);
        return this;
    }

    public Set<TestCaseFields> getTestcasefieldsTestcases() {
        return this.testcasefieldsTestcases;
    }

    public void setTestcasefieldsTestcases(Set<TestCaseFields> testCaseFields) {
        if (this.testcasefieldsTestcases != null) {
            this.testcasefieldsTestcases.forEach(i -> i.setTestCase(null));
        }
        if (testCaseFields != null) {
            testCaseFields.forEach(i -> i.setTestCase(this));
        }
        this.testcasefieldsTestcases = testCaseFields;
    }

    public TestCases testcasefieldsTestcases(Set<TestCaseFields> testCaseFields) {
        this.setTestcasefieldsTestcases(testCaseFields);
        return this;
    }

    public TestCases addTestcasefieldsTestcase(TestCaseFields testCaseFields) {
        this.testcasefieldsTestcases.add(testCaseFields);
        testCaseFields.setTestCase(this);
        return this;
    }

    public TestCases removeTestcasefieldsTestcase(TestCaseFields testCaseFields) {
        this.testcasefieldsTestcases.remove(testCaseFields);
        testCaseFields.setTestCase(null);
        return this;
    }

    public Set<TestRunDetails> getTestrundetailsTestcases() {
        return this.testrundetailsTestcases;
    }

    public void setTestrundetailsTestcases(Set<TestRunDetails> testRunDetails) {
        if (this.testrundetailsTestcases != null) {
            this.testrundetailsTestcases.forEach(i -> i.setTestCase(null));
        }
        if (testRunDetails != null) {
            testRunDetails.forEach(i -> i.setTestCase(this));
        }
        this.testrundetailsTestcases = testRunDetails;
    }

    public TestCases testrundetailsTestcases(Set<TestRunDetails> testRunDetails) {
        this.setTestrundetailsTestcases(testRunDetails);
        return this;
    }

    public TestCases addTestrundetailsTestcase(TestRunDetails testRunDetails) {
        this.testrundetailsTestcases.add(testRunDetails);
        testRunDetails.setTestCase(this);
        return this;
    }

    public TestCases removeTestrundetailsTestcase(TestRunDetails testRunDetails) {
        this.testrundetailsTestcases.remove(testRunDetails);
        testRunDetails.setTestCase(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCases)) {
            return false;
        }
        return id != null && id.equals(((TestCases) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCases{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", estimate='" + getEstimate() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy=" + getUpdatedBy() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", precondition='" + getPrecondition() + "'" +
            ", description='" + getDescription() + "'" +
            ", isAutomated='" + getIsAutomated() + "'" +
            "}";
    }
}
