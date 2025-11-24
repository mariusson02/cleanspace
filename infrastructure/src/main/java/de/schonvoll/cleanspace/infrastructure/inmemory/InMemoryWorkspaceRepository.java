package de.schonvoll.cleanspace.infrastructure.inmemory;

import de.schonvoll.cleanspace.domain.entities.Workspace;
import de.schonvoll.cleanspace.domain.repositories.WorkspaceRepository;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 * In-memory implementation of WorkspaceRepository for development and testing. Uses a HashMap to
 * store Workspace entities.
 */
@Repository
@Profile("in-memory")
public class InMemoryWorkspaceRepository implements WorkspaceRepository {
  private final HashMap<UUID, Workspace> workspaceHashMap;

  public InMemoryWorkspaceRepository() {
    this.workspaceHashMap = new HashMap<>();
  }

  /** {@inheritDoc} */
  @Override
  public Workspace save(Workspace workspace) {
    if (workspace.getId() == null) {
      Workspace persistedWorkspace =
          Workspace.builder()
              .id(UUID.randomUUID())
              .name(workspace.getName())
              .openingHours(workspace.getOpeningHours())
              .capacity(workspace.getCapacity())
              .properties(workspace.getProperties())
              .build();
      workspaceHashMap.put(persistedWorkspace.getId(), persistedWorkspace);
      return persistedWorkspace;
    }
    workspaceHashMap.put(workspace.getId(), workspace);
    return workspace;
  }

  /** {@inheritDoc} */
  @Override
  public Workspace findById(UUID id) {
    return workspaceHashMap.get(id);
  }

  /** {@inheritDoc} */
  @Override
  public Workspace findByName(String name) {
    return workspaceHashMap.values().stream()
        .filter(workspace -> workspace.getName().equalsIgnoreCase(name))
        .findFirst()
        .orElse(null);
  }

  /** {@inheritDoc} */
  @Override
  public List<Workspace> findAll() {
    return workspaceHashMap.values().stream().toList();
  }
}
