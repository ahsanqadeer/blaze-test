package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestCasePriorities.
 */
@Entity
@Table(name = "test_case_priorities")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCasePriorities implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "priority")
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
    private Set<TestCases> testcasesPriorities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestCasePriorities id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TestCasePriorities name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<TestCases> getTestcasesPriorities() {
        return this.testcasesPriorities;
    }

    public void setTestcasesPriorities(Set<TestCases> testCases) {
        if (this.testcasesPriorities != null) {
            this.testcasesPriorities.forEach(i -> i.setPriority(null));
        }
        if (testCases != null) {
            testCases.forEach(i -> i.setPriority(this));
        }
        this.testcasesPriorities = testCases;
    }

    public TestCasePriorities testcasesPriorities(Set<TestCases> testCases) {
        this.setTestcasesPriorities(testCases);
        return this;
    }

    public TestCasePriorities addTestcasesPriority(TestCases testCases) {
        this.testcasesPriorities.add(testCases);
        testCases.setPriority(this);
        return this;
    }

    public TestCasePriorities removeTestcasesPriority(TestCases testCases) {
        this.testcasesPriorities.remove(testCases);
        testCases.setPriority(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCasePriorities)) {
            return false;
        }
        return id != null && id.equals(((TestCasePriorities) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCasePriorities{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
