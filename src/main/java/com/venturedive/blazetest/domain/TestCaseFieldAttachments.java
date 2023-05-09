package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestCaseFieldAttachments.
 */
@Entity
@Table(name = "test_case_field_attachments")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCaseFieldAttachments implements Serializable {

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
        value = { "templateField", "testCase", "testcasefieldattachmentsTestcasefields", "testrunstepdetailsStepdetails" },
        allowSetters = true
    )
    private TestCaseFields testCaseField;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestCaseFieldAttachments id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public TestCaseFieldAttachments url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TestCaseFields getTestCaseField() {
        return this.testCaseField;
    }

    public void setTestCaseField(TestCaseFields testCaseFields) {
        this.testCaseField = testCaseFields;
    }

    public TestCaseFieldAttachments testCaseField(TestCaseFields testCaseFields) {
        this.setTestCaseField(testCaseFields);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCaseFieldAttachments)) {
            return false;
        }
        return id != null && id.equals(((TestCaseFieldAttachments) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCaseFieldAttachments{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
