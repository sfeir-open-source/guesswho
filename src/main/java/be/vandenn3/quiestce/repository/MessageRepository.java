package be.vandenn3.quiestce.repository;

import be.vandenn3.quiestce.domain.Message;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Message entity.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    default Optional<Message> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Message> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Message> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    List<Message> findAllByGameId(Long gameId);

    List<Message> findAllByGameIdAndIdGreaterThan(Long gameId, Long id);

    @Query(
        value = "select message from Message message left join fetch message.author",
        countQuery = "select count(message) from Message message"
    )
    Page<Message> findAllWithToOneRelationships(Pageable pageable);

    @Query("select message from Message message left join fetch message.author")
    List<Message> findAllWithToOneRelationships();

    @Query("select message from Message message left join fetch message.author where message.id =:id")
    Optional<Message> findOneWithToOneRelationships(@Param("id") Long id);
}
