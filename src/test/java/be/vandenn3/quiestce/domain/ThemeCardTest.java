package be.vandenn3.quiestce.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.vandenn3.quiestce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ThemeCardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ThemeCard.class);
        ThemeCard themeCard1 = new ThemeCard();
        themeCard1.setId(1L);
        ThemeCard themeCard2 = new ThemeCard();
        themeCard2.setId(themeCard1.getId());
        assertThat(themeCard1).isEqualTo(themeCard2);
        themeCard2.setId(2L);
        assertThat(themeCard1).isNotEqualTo(themeCard2);
        themeCard1.setId(null);
        assertThat(themeCard1).isNotEqualTo(themeCard2);
    }
}
