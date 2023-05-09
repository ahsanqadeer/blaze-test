package com.venturedive.blazetest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blazetest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestRunDetailAttachmentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestRunDetailAttachments.class);
        TestRunDetailAttachments testRunDetailAttachments1 = new TestRunDetailAttachments();
        testRunDetailAttachments1.setId(1L);
        TestRunDetailAttachments testRunDetailAttachments2 = new TestRunDetailAttachments();
        testRunDetailAttachments2.setId(testRunDetailAttachments1.getId());
        assertThat(testRunDetailAttachments1).isEqualTo(testRunDetailAttachments2);
        testRunDetailAttachments2.setId(2L);
        assertThat(testRunDetailAttachments1).isNotEqualTo(testRunDetailAttachments2);
        testRunDetailAttachments1.setId(null);
        assertThat(testRunDetailAttachments1).isNotEqualTo(testRunDetailAttachments2);
    }
}
