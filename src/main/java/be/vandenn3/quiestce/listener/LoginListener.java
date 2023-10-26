package be.vandenn3.quiestce.listener;

import be.vandenn3.quiestce.domain.Player;
import be.vandenn3.quiestce.service.PlayerService;
import be.vandenn3.quiestce.session.CurrentPlayerManager;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class LoginListener implements
    ApplicationListener<AuthenticationSuccessEvent> {
    private final Logger log = LoggerFactory.getLogger(LoginListener.class);

    private final CurrentPlayerManager currentPlayerManager;
    private final HttpSession httpSession;
    private final PlayerService playerService;

    public LoginListener(CurrentPlayerManager currentPlayerManager, HttpSession httpSession, PlayerService playerService) {
        this.currentPlayerManager = currentPlayerManager;
        this.httpSession = httpSession;
        this.playerService = playerService;
    }

    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String username = ((UserDetails) event.getAuthentication().getPrincipal()).getUsername();
        Player player = playerService.getPlayerForLoggedInUser(username);
        currentPlayerManager.setCurrentPlayer(httpSession, player);
        log.debug("Player set for authenticated user : {}", player);
    }
}
