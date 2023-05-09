package com.venturedive.blazetest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blazetest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestCaseFieldAttachmentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCaseFieldAttachments.class);
        TestCaseFieldAttachments testCaseFieldAttachments1 = new TestCaseFieldAttachments();
        testCaseFieldAttachments1.setId(1L);
        TestCaseFieldAttachments testCaseFieldAttachments2 = new TestCaseFieldAttachments();
        testCaseFieldAttachments2.setId(testCaseFieldAttachments1.getId());
        assertThat(testCaseFieldAttachments1).isEqualTo(testCaseFieldAttachments2);
        testCaseFieldAttachments2.setId(2L);
        assertThat(testCaseFieldAttachments1).isNotEqualTo(testCaseFieldAttachments2);
        testCaseFieldAttachments1.setId(null);
        assertThat(testCaseFieldAttachments1).isNotEqualTo(testCaseFieldAttachments2);
    }
}
