package com.venturedive.blazetest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blazetest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestStatusesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestStatuses.class);
        TestStatuses testStatuses1 = new TestStatuses();
        testStatuses1.setId(1L);
        TestStatuses testStatuses2 = new TestStatuses();
        testStatuses2.setId(testStatuses1.getId());
        assertThat(testStatuses1).isEqualTo(testStatuses2);
        testStatuses2.setId(2L);
        assertThat(testStatuses1).isNotEqualTo(testStatuses2);
        testStatuses1.setId(null);
        assertThat(testStatuses1).isNotEqualTo(testStatuses2);
    }
}
