package com.venturedive.blazetest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blazetest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MilestonesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Milestones.class);
        Milestones milestones1 = new Milestones();
        milestones1.setId(1L);
        Milestones milestones2 = new Milestones();
        milestones2.setId(milestones1.getId());
        assertThat(milestones1).isEqualTo(milestones2);
        milestones2.setId(2L);
        assertThat(milestones1).isNotEqualTo(milestones2);
        milestones1.setId(null);
        assertThat(milestones1).isNotEqualTo(milestones2);
    }
}
