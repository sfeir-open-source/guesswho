package be.vandenn3.quiestce.repository;

import be.vandenn3.quiestce.domain.GameCard;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GameCard entity.
 */
@Repository
public interface GameCardRepository extends JpaRepository<GameCard, Long> {
    default Optional<GameCard> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<GameCard> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<GameCard> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    List<GameCard> findAllByGameId(Long gameId);

    @Query(
        value = "select gameCard from GameCard gameCard left join fetch gameCard.themeCard",
        countQuery = "select count(gameCard) from GameCard gameCard"
    )
    Page<GameCard> findAllWithToOneRelationships(Pageable pageable);

    @Query("select gameCard from GameCard gameCard left join fetch gameCard.themeCard")
    List<GameCard> findAllWithToOneRelationships();

    @Query("select gameCard from GameCard gameCard left join fetch gameCard.themeCard where gameCard.id =:id")
    Optional<GameCard> findOneWithToOneRelationships(@Param("id") Long id);
}
