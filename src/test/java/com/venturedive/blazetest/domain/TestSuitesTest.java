package com.venturedive.blazetest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blazetest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestSuitesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestSuites.class);
        TestSuites testSuites1 = new TestSuites();
        testSuites1.setId(1L);
        TestSuites testSuites2 = new TestSuites();
        testSuites2.setId(testSuites1.getId());
        assertThat(testSuites1).isEqualTo(testSuites2);
        testSuites2.setId(2L);
        assertThat(testSuites1).isNotEqualTo(testSuites2);
        testSuites1.setId(null);
        assertThat(testSuites1).isNotEqualTo(testSuites2);
    }
}
