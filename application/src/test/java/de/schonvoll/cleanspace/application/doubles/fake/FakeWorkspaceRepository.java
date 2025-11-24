package de.schonvoll.cleanspace.application.doubles.fake;

import de.schonvoll.cleanspace.domain.entities.Workspace;
import de.schonvoll.cleanspace.domain.repositories.WorkspaceRepository;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FakeWorkspaceRepository implements WorkspaceRepository {
  private final HashMap<UUID, Workspace> workspaceHashMap = new HashMap<>();

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

  @Override
  public Workspace findById(UUID id) {
    return workspaceHashMap.get(id);
  }

  @Override
  public Workspace findByName(String name) {
    return workspaceHashMap.values().stream()
        .filter(workspace -> workspace.getName().equalsIgnoreCase(name))
        .findFirst()
        .orElse(null);
  }

  @Override
  public List<Workspace> findAll() {
    return workspaceHashMap.values().stream().toList();
  }
}
