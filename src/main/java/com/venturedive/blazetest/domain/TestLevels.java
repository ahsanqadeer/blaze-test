package com.venturedive.blazetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestLevels.
 */
@Entity
@Table(name = "test_levels")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestLevels implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @OneToMany(mappedBy = "testLevel")
    @JsonIgnoreProperties(value = { "testLevel", "mileStone", "testrundetailsTestruns" }, allowSetters = true)
    private Set<TestRuns> testrunsTestlevels = new HashSet<>();

    @ManyToMany(mappedBy = "testLevels")
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
    private Set<TestCases> testCases = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestLevels id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TestLevels name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<TestRuns> getTestrunsTestlevels() {
        return this.testrunsTestlevels;
    }

    public void setTestrunsTestlevels(Set<TestRuns> testRuns) {
        if (this.testrunsTestlevels != null) {
            this.testrunsTestlevels.forEach(i -> i.setTestLevel(null));
        }
        if (testRuns != null) {
            testRuns.forEach(i -> i.setTestLevel(this));
        }
        this.testrunsTestlevels = testRuns;
    }

    public TestLevels testrunsTestlevels(Set<TestRuns> testRuns) {
        this.setTestrunsTestlevels(testRuns);
        return this;
    }

    public TestLevels addTestrunsTestlevel(TestRuns testRuns) {
        this.testrunsTestlevels.add(testRuns);
        testRuns.setTestLevel(this);
        return this;
    }

    public TestLevels removeTestrunsTestlevel(TestRuns testRuns) {
        this.testrunsTestlevels.remove(testRuns);
        testRuns.setTestLevel(null);
        return this;
    }

    public Set<TestCases> getTestCases() {
        return this.testCases;
    }

    public void setTestCases(Set<TestCases> testCases) {
        if (this.testCases != null) {
            this.testCases.forEach(i -> i.removeTestLevel(this));
        }
        if (testCases != null) {
            testCases.forEach(i -> i.addTestLevel(this));
        }
        this.testCases = testCases;
    }

    public TestLevels testCases(Set<TestCases> testCases) {
        this.setTestCases(testCases);
        return this;
    }

    public TestLevels addTestCase(TestCases testCases) {
        this.testCases.add(testCases);
        testCases.getTestLevels().add(this);
        return this;
    }

    public TestLevels removeTestCase(TestCases testCases) {
        this.testCases.remove(testCases);
        testCases.getTestLevels().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestLevels)) {
            return false;
        }
        return id != null && id.equals(((TestLevels) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestLevels{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
