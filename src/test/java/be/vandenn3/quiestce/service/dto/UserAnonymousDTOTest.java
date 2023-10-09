package be.vandenn3.quiestce.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.vandenn3.quiestce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAnonymousDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAnonymousDTO.class);
        UserAnonymousDTO userAnonymousDTO1 = new UserAnonymousDTO();
        userAnonymousDTO1.setId(1L);
        UserAnonymousDTO userAnonymousDTO2 = new UserAnonymousDTO();
        assertThat(userAnonymousDTO1).isNotEqualTo(userAnonymousDTO2);
        userAnonymousDTO2.setId(userAnonymousDTO1.getId());
        assertThat(userAnonymousDTO1).isEqualTo(userAnonymousDTO2);
        userAnonymousDTO2.setId(2L);
        assertThat(userAnonymousDTO1).isNotEqualTo(userAnonymousDTO2);
        userAnonymousDTO1.setId(null);
        assertThat(userAnonymousDTO1).isNotEqualTo(userAnonymousDTO2);
    }
}
