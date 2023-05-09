package com.venturedive.blazetest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blazetest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestRunStepDetailAttachmentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestRunStepDetailAttachments.class);
        TestRunStepDetailAttachments testRunStepDetailAttachments1 = new TestRunStepDetailAttachments();
        testRunStepDetailAttachments1.setId(1L);
        TestRunStepDetailAttachments testRunStepDetailAttachments2 = new TestRunStepDetailAttachments();
        testRunStepDetailAttachments2.setId(testRunStepDetailAttachments1.getId());
        assertThat(testRunStepDetailAttachments1).isEqualTo(testRunStepDetailAttachments2);
        testRunStepDetailAttachments2.setId(2L);
        assertThat(testRunStepDetailAttachments1).isNotEqualTo(testRunStepDetailAttachments2);
        testRunStepDetailAttachments1.setId(null);
        assertThat(testRunStepDetailAttachments1).isNotEqualTo(testRunStepDetailAttachments2);
    }
}
