package com.venturedive.blazetest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blazetest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SectionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sections.class);
        Sections sections1 = new Sections();
        sections1.setId(1L);
        Sections sections2 = new Sections();
        sections2.setId(sections1.getId());
        assertThat(sections1).isEqualTo(sections2);
        sections2.setId(2L);
        assertThat(sections1).isNotEqualTo(sections2);
        sections1.setId(null);
        assertThat(sections1).isNotEqualTo(sections2);
    }
}
