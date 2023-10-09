package be.vandenn3.quiestce.repository;

import be.vandenn3.quiestce.domain.ThemeCard;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ThemeCard entity.
 */
@Repository
public interface ThemeCardRepository extends JpaRepository<ThemeCard, Long> {
    default Optional<ThemeCard> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ThemeCard> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ThemeCard> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select themeCard from ThemeCard themeCard left join fetch themeCard.theme",
        countQuery = "select count(themeCard) from ThemeCard themeCard"
    )
    Page<ThemeCard> findAllWithToOneRelationships(Pageable pageable);

    @Query("select themeCard from ThemeCard themeCard left join fetch themeCard.theme")
    List<ThemeCard> findAllWithToOneRelationships();

    @Query("select themeCard from ThemeCard themeCard left join fetch themeCard.theme where themeCard.id =:id")
    Optional<ThemeCard> findOneWithToOneRelationships(@Param("id") Long id);
}
