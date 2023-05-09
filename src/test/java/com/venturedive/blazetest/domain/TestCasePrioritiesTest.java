package com.venturedive.blazetest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blazetest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestCasePrioritiesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCasePriorities.class);
        TestCasePriorities testCasePriorities1 = new TestCasePriorities();
        testCasePriorities1.setId(1L);
        TestCasePriorities testCasePriorities2 = new TestCasePriorities();
        testCasePriorities2.setId(testCasePriorities1.getId());
        assertThat(testCasePriorities1).isEqualTo(testCasePriorities2);
        testCasePriorities2.setId(2L);
        assertThat(testCasePriorities1).isNotEqualTo(testCasePriorities2);
        testCasePriorities1.setId(null);
        assertThat(testCasePriorities1).isNotEqualTo(testCasePriorities2);
    }
}
