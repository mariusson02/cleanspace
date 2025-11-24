package de.schonvoll.cleanspace.infrastructure.postgres.jpa;

import de.schonvoll.cleanspace.infrastructure.postgres.entities.UserJpaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
  UserJpaEntity findByEmail(String email);
}
