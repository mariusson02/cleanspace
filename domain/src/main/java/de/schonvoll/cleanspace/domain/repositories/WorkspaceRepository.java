package de.schonvoll.cleanspace.domain.repositories;

import de.schonvoll.cleanspace.domain.entities.Workspace;
import java.util.List;
import java.util.UUID;

public interface WorkspaceRepository {
  /**
   * Saves a workspace to the repository.
   *
   * @param workspace the {@link Workspace} to save
   * @return the saved workspace with generated ID if it was null
   */
  Workspace save(Workspace workspace);

  /**
   * Finds a workspace by its unique identifier.
   *
   * @param id the unique identifier of the workspace
   * @return the workspace with the given ID, or null if not found
   */
  Workspace findById(UUID id);

  /**
   * Finds a workspace by its name.
   *
   * @param name the name of the workspace
   * @return the workspace with the given name, or null if not found
   */
  Workspace findByName(String name);

  /**
   * Retrieves all workspaces from the repository.
   *
   * @return list of all workspaces
   */
  List<Workspace> findAll();
}
