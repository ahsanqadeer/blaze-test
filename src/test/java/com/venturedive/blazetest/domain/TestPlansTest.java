package com.venturedive.blazetest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blazetest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestPlansTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestPlans.class);
        TestPlans testPlans1 = new TestPlans();
        testPlans1.setId(1L);
        TestPlans testPlans2 = new TestPlans();
        testPlans2.setId(testPlans1.getId());
        assertThat(testPlans1).isEqualTo(testPlans2);
        testPlans2.setId(2L);
        assertThat(testPlans1).isNotEqualTo(testPlans2);
        testPlans1.setId(null);
        assertThat(testPlans1).isNotEqualTo(testPlans2);
    }
}
