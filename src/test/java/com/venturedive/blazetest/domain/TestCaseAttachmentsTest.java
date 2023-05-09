package com.venturedive.blazetest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blazetest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestCaseAttachmentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCaseAttachments.class);
        TestCaseAttachments testCaseAttachments1 = new TestCaseAttachments();
        testCaseAttachments1.setId(1L);
        TestCaseAttachments testCaseAttachments2 = new TestCaseAttachments();
        testCaseAttachments2.setId(testCaseAttachments1.getId());
        assertThat(testCaseAttachments1).isEqualTo(testCaseAttachments2);
        testCaseAttachments2.setId(2L);
        assertThat(testCaseAttachments1).isNotEqualTo(testCaseAttachments2);
        testCaseAttachments1.setId(null);
        assertThat(testCaseAttachments1).isNotEqualTo(testCaseAttachments2);
    }
}
