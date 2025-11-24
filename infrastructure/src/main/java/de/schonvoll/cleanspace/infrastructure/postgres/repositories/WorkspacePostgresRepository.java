package de.schonvoll.cleanspace.infrastructure.postgres.repositories;

import de.schonvoll.cleanspace.domain.entities.Workspace;
import de.schonvoll.cleanspace.domain.repositories.WorkspaceRepository;
import de.schonvoll.cleanspace.infrastructure.postgres.entities.WorkspaceJpaEntity;
import de.schonvoll.cleanspace.infrastructure.postgres.jpa.WorkspaceJpaRepository;
import de.schonvoll.cleanspace.infrastructure.postgres.mapper.WorkspaceMapper;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 * PostgreSQL implementation of WorkspaceRepository using JPA. Delegates to WorkspaceJpaRepository
 * and handles domain-persistence mapping via WorkspaceMapper.
 */
@Repository
@Profile("postgres")
@AllArgsConstructor
public class WorkspacePostgresRepository implements WorkspaceRepository {
  private final WorkspaceJpaRepository jpaRepository;

  /** {@inheritDoc} */
  @Override
  public Workspace save(Workspace workspace) {
    WorkspaceJpaEntity savedJpaWorkspace =
        jpaRepository.save(WorkspaceMapper.toJpaEntity(workspace));
    return WorkspaceMapper.toDomain(savedJpaWorkspace);
  }

  /** {@inheritDoc} */
  @Override
  public Workspace findById(UUID id) {
    return jpaRepository.findById(id).stream()
        .map(WorkspaceMapper::toDomain)
        .findFirst()
        .orElse(null);
  }

  /** {@inheritDoc} */
  @Override
  public Workspace findByName(String name) {
    return jpaRepository.findByName(name).stream()
        .map(WorkspaceMapper::toDomain)
        .findFirst()
        .orElse(null);
  }

  /** {@inheritDoc} */
  @Override
  public List<Workspace> findAll() {
    return jpaRepository.findAll().stream().map(WorkspaceMapper::toDomain).toList();
  }
}