package be.vandenn3.quiestce.web.rest;

import be.vandenn3.quiestce.IntegrationTest;
import be.vandenn3.quiestce.domain.Player;
import be.vandenn3.quiestce.domain.User;
import be.vandenn3.quiestce.domain.UserTypeEnum;
import be.vandenn3.quiestce.repository.PlayerRepository;
import be.vandenn3.quiestce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

import static be.vandenn3.quiestce.session.CurrentPlayerManager.SESSION_KEY_NAME;

@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(username = "test-user")
@Transactional
public class IntegrationTestBase {

    protected Map<String, Object> sessionattr;
    protected Player currentPlayer;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void initBaseTest() {
        User user = UserResourceIT.createEntity(null);
        user.setLogin("test-user");
        user = userRepository.save(user);
        currentPlayer = new Player().userType(UserTypeEnum.AUTHENTICATED_USER.toString()).userId(user.getId()).pseudo(user.getLogin());
        currentPlayer = playerRepository.save(currentPlayer);
        sessionattr = Collections.singletonMap(SESSION_KEY_NAME, currentPlayer);
    }

    public Player createValidPlayer(String name) {
        User user = UserResourceIT.createEntity(null);
        user.setLogin(name);
        user = userRepository.save(user);
        Player player = new Player().userType(UserTypeEnum.AUTHENTICATED_USER.toString()).userId(user.getId()).pseudo(user.getLogin());
        return playerRepository.save(player);
    }
}
