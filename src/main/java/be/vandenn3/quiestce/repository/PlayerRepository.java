package be.vandenn3.quiestce.repository;

import be.vandenn3.quiestce.domain.Player;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Player entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findAllByUserTypeAndUserId(String userType, Long userId);
}
