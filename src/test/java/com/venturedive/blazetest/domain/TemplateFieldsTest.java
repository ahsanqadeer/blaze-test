package com.venturedive.blazetest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blazetest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TemplateFieldsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TemplateFields.class);
        TemplateFields templateFields1 = new TemplateFields();
        templateFields1.setId(1L);
        TemplateFields templateFields2 = new TemplateFields();
        templateFields2.setId(templateFields1.getId());
        assertThat(templateFields1).isEqualTo(templateFields2);
        templateFields2.setId(2L);
        assertThat(templateFields1).isNotEqualTo(templateFields2);
        templateFields1.setId(null);
        assertThat(templateFields1).isNotEqualTo(templateFields2);
    }
}
