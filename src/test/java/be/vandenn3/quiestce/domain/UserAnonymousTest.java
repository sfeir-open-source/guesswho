package be.vandenn3.quiestce.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.vandenn3.quiestce.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAnonymousTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAnonymous.class);
        UserAnonymous userAnonymous1 = new UserAnonymous();
        userAnonymous1.setId(1L);
        UserAnonymous userAnonymous2 = new UserAnonymous();
        userAnonymous2.setId(userAnonymous1.getId());
        assertThat(userAnonymous1).isEqualTo(userAnonymous2);
        userAnonymous2.setId(2L);
        assertThat(userAnonymous1).isNotEqualTo(userAnonymous2);
        userAnonymous1.setId(null);
        assertThat(userAnonymous1).isNotEqualTo(userAnonymous2);
    }
}
