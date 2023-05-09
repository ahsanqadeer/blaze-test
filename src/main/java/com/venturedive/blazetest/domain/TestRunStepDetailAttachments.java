package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestRunStepDetailAttachments.
 */
@Entity
@Table(name = "test_run_step_detail_attachments")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestRunStepDetailAttachments implements Serializable {

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
        value = { "testRunDetail", "stepDetail", "status", "testrunstepdetailattachmentsTestrunstepdetails" },
        allowSetters = true
    )
    private TestRunStepDetails testRunStepDetail;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestRunStepDetailAttachments id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public TestRunStepDetailAttachments url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TestRunStepDetails getTestRunStepDetail() {
        return this.testRunStepDetail;
    }

    public void setTestRunStepDetail(TestRunStepDetails testRunStepDetails) {
        this.testRunStepDetail = testRunStepDetails;
    }

    public TestRunStepDetailAttachments testRunStepDetail(TestRunStepDetails testRunStepDetails) {
        this.setTestRunStepDetail(testRunStepDetails);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestRunStepDetailAttachments)) {
            return false;
        }
        return id != null && id.equals(((TestRunStepDetailAttachments) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestRunStepDetailAttachments{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
