package com.venturedive.blazetest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blazetest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestLevelsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestLevels.class);
        TestLevels testLevels1 = new TestLevels();
        testLevels1.setId(1L);
        TestLevels testLevels2 = new TestLevels();
        testLevels2.setId(testLevels1.getId());
        assertThat(testLevels1).isEqualTo(testLevels2);
        testLevels2.setId(2L);
        assertThat(testLevels1).isNotEqualTo(testLevels2);
        testLevels1.setId(null);
        assertThat(testLevels1).isNotEqualTo(testLevels2);
    }
}
