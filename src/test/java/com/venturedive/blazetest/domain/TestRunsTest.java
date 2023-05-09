package com.venturedive.blazetest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blazetest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestRunsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestRuns.class);
        TestRuns testRuns1 = new TestRuns();
        testRuns1.setId(1L);
        TestRuns testRuns2 = new TestRuns();
        testRuns2.setId(testRuns1.getId());
        assertThat(testRuns1).isEqualTo(testRuns2);
        testRuns2.setId(2L);
        assertThat(testRuns1).isNotEqualTo(testRuns2);
        testRuns1.setId(null);
        assertThat(testRuns1).isNotEqualTo(testRuns2);
    }
}
