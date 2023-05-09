package com.venturedive.blazetest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blazetest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestCasesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCases.class);
        TestCases testCases1 = new TestCases();
        testCases1.setId(1L);
        TestCases testCases2 = new TestCases();
        testCases2.setId(testCases1.getId());
        assertThat(testCases1).isEqualTo(testCases2);
        testCases2.setId(2L);
        assertThat(testCases1).isNotEqualTo(testCases2);
        testCases1.setId(null);
        assertThat(testCases1).isNotEqualTo(testCases2);
    }
}
