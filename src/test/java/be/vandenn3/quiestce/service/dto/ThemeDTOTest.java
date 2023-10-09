package be.vandenn3.quiestce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.vandenn3.quiestce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ThemeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ThemeDTO.class);
        ThemeDTO themeDTO1 = new ThemeDTO();
        themeDTO1.setId(1L);
        ThemeDTO themeDTO2 = new ThemeDTO();
        assertThat(themeDTO1).isNotEqualTo(themeDTO2);
        themeDTO2.setId(themeDTO1.getId());
        assertThat(themeDTO1).isEqualTo(themeDTO2);
        themeDTO2.setId(2L);
        assertThat(themeDTO1).isNotEqualTo(themeDTO2);
        themeDTO1.setId(null);
        assertThat(themeDTO1).isNotEqualTo(themeDTO2);
    }
}
