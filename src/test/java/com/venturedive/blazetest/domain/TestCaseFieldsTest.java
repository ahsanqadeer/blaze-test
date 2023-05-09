package com.venturedive.blazetest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blazetest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestCaseFieldsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCaseFields.class);
        TestCaseFields testCaseFields1 = new TestCaseFields();
        testCaseFields1.setId(1L);
        TestCaseFields testCaseFields2 = new TestCaseFields();
        testCaseFields2.setId(testCaseFields1.getId());
        assertThat(testCaseFields1).isEqualTo(testCaseFields2);
        testCaseFields2.setId(2L);
        assertThat(testCaseFields1).isNotEqualTo(testCaseFields2);
        testCaseFields1.setId(null);
        assertThat(testCaseFields1).isNotEqualTo(testCaseFields2);
    }
}
