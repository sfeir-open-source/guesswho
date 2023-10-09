package be.vandenn3.quiestce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.vandenn3.quiestce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerDTO.class);
        PlayerDTO playerDTO1 = new PlayerDTO();
        playerDTO1.setId(1L);
        PlayerDTO playerDTO2 = new PlayerDTO();
        assertThat(playerDTO1).isNotEqualTo(playerDTO2);
        playerDTO2.setId(playerDTO1.getId());
        assertThat(playerDTO1).isEqualTo(playerDTO2);
        playerDTO2.setId(2L);
        assertThat(playerDTO1).isNotEqualTo(playerDTO2);
        playerDTO1.setId(null);
        assertThat(playerDTO1).isNotEqualTo(playerDTO2);
    }
}
