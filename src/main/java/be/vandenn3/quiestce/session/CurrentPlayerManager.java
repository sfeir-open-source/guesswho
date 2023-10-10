package be.vandenn3.quiestce.session;

import be.vandenn3.quiestce.domain.Player;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CurrentPlayerManager {
    public static String SESSION_KEY_NAME = Player.class.getCanonicalName();
    public Player getCurrentPlayer(HttpSession session) {
        Player player = (Player) session.getAttribute(SESSION_KEY_NAME);
        if (Objects.isNull(player)) {
            throw new AccessDeniedException("No player has been found.");
        }
        return player;
    }

    public void setCurrentPlayer(HttpSession session, Player player) {
        session.setAttribute(SESSION_KEY_NAME, player);
    }
}
