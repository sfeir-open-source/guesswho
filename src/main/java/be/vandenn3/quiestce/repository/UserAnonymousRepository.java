package be.vandenn3.quiestce.repository;

import be.vandenn3.quiestce.domain.UserAnonymous;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserAnonymous entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAnonymousRepository extends JpaRepository<UserAnonymous, Long> {}
