package be.vandenn3.quiestce.service;

import be.vandenn3.quiestce.domain.Player;
import be.vandenn3.quiestce.domain.UserTypeEnum;
import be.vandenn3.quiestce.repository.PlayerRepository;
import be.vandenn3.quiestce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;
    private UserRepository userRepository;

    public PlayerService(PlayerRepository playerRepository, UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
    }

    public Player getPlayerForLoggedInUser(String login) {
        Long userId = userRepository.findOneByLogin(login).orElseThrow(() -> new RuntimeException("invalid logged in user")).getId();
        List<Player> players = playerRepository.findAllByUserTypeAndUserId(UserTypeEnum.AUTHENTICATED_USER.toString(), userId);
        if (players.isEmpty()) {
            Player player = new Player().userType(UserTypeEnum.AUTHENTICATED_USER.toString()).userId(userId).pseudo(login);
            return playerRepository.save(player);
        } else if (players.size() > 1) {
            throw new RuntimeException("data integrity issue - several players found for user " + userId);
        }
        return players.get(0);
    }
}
