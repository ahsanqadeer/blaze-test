package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestCaseAttachments.
 */
@Entity
@Table(name = "test_case_attachments")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCaseAttachments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "url", nullable = false)
    private String url;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestCaseAttachments id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public TestCaseAttachments url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TestCases getTestCase() {
        return this.testCase;
    }

    public void setTestCase(TestCases testCases) {
        this.testCase = testCases;
    }

    public TestCaseAttachments testCase(TestCases testCases) {
        this.setTestCase(testCases);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCaseAttachments)) {
            return false;
        }
        return id != null && id.equals(((TestCaseAttachments) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCaseAttachments{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
