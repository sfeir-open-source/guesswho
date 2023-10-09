package be.vandenn3.quiestce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.vandenn3.quiestce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ThemeCardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ThemeCardDTO.class);
        ThemeCardDTO themeCardDTO1 = new ThemeCardDTO();
        themeCardDTO1.setId(1L);
        ThemeCardDTO themeCardDTO2 = new ThemeCardDTO();
        assertThat(themeCardDTO1).isNotEqualTo(themeCardDTO2);
        themeCardDTO2.setId(themeCardDTO1.getId());
        assertThat(themeCardDTO1).isEqualTo(themeCardDTO2);
        themeCardDTO2.setId(2L);
        assertThat(themeCardDTO1).isNotEqualTo(themeCardDTO2);
        themeCardDTO1.setId(null);
        assertThat(themeCardDTO1).isNotEqualTo(themeCardDTO2);
    }
}
