package be.vandenn3.quiestce.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.vandenn3.quiestce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GameCardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameCard.class);
        GameCard gameCard1 = new GameCard();
        gameCard1.setId(1L);
        GameCard gameCard2 = new GameCard();
        gameCard2.setId(gameCard1.getId());
        assertThat(gameCard1).isEqualTo(gameCard2);
        gameCard2.setId(2L);
        assertThat(gameCard1).isNotEqualTo(gameCard2);
        gameCard1.setId(null);
        assertThat(gameCard1).isNotEqualTo(gameCard2);
    }
}
