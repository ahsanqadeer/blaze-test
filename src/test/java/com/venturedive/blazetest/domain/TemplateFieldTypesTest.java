package com.venturedive.blazetest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blazetest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TemplateFieldTypesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TemplateFieldTypes.class);
        TemplateFieldTypes templateFieldTypes1 = new TemplateFieldTypes();
        templateFieldTypes1.setId(1L);
        TemplateFieldTypes templateFieldTypes2 = new TemplateFieldTypes();
        templateFieldTypes2.setId(templateFieldTypes1.getId());
        assertThat(templateFieldTypes1).isEqualTo(templateFieldTypes2);
        templateFieldTypes2.setId(2L);
        assertThat(templateFieldTypes1).isNotEqualTo(templateFieldTypes2);
        templateFieldTypes1.setId(null);
        assertThat(templateFieldTypes1).isNotEqualTo(templateFieldTypes2);
    }
}
