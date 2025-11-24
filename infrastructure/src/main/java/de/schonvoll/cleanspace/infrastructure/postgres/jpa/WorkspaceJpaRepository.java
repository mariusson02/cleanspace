package de.schonvoll.cleanspace.infrastructure.postgres.jpa;

import de.schonvoll.cleanspace.infrastructure.postgres.entities.WorkspaceJpaEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceJpaRepository extends JpaRepository<WorkspaceJpaEntity, UUID> {
  Optional<WorkspaceJpaEntity> findByName(String name);
}
