package net.kylejones.recipebook.repository;

import java.time.LocalDate;
import java.util.List;
import net.kylejones.recipebook.domain.PersistentToken;
import net.kylejones.recipebook.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link PersistentToken} entity.
 */
public interface PersistentTokenRepository extends JpaRepository<PersistentToken, String> {
    List<PersistentToken> findByUser(User user);

    List<PersistentToken> findByTokenDateBefore(LocalDate localDate);
}
