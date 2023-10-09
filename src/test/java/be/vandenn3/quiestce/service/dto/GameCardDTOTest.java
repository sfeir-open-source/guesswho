package be.vandenn3.quiestce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.vandenn3.quiestce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GameCardDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameCardDTO.class);
        GameCardDTO gameCardDTO1 = new GameCardDTO();
        gameCardDTO1.setId(1L);
        GameCardDTO gameCardDTO2 = new GameCardDTO();
        assertThat(gameCardDTO1).isNotEqualTo(gameCardDTO2);
        gameCardDTO2.setId(gameCardDTO1.getId());
        assertThat(gameCardDTO1).isEqualTo(gameCardDTO2);
        gameCardDTO2.setId(2L);
        assertThat(gameCardDTO1).isNotEqualTo(gameCardDTO2);
        gameCardDTO1.setId(null);
        assertThat(gameCardDTO1).isNotEqualTo(gameCardDTO2);
    }
}
