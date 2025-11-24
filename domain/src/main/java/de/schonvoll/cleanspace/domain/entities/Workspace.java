package de.schonvoll.cleanspace.domain.entities;

import de.schonvoll.cleanspace.domain.valueobjects.OpeningHours;
import de.schonvoll.cleanspace.domain.valueobjects.WorkspaceProperty;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Domain entity representing a workspace in the system. Contains workspace details including {@link
 * OpeningHours}, capacity, and {@link WorkspaceProperty} specifications.
 */
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Workspace {
  private final UUID id;
  @Setter private String name;
  @Setter private OpeningHours openingHours;
  @Setter private int capacity;
  @Setter private List<WorkspaceProperty> properties;

  /**
   * Creates a new workspace without an ID (for new entities).
   *
   * @param name the name of the workspace
   * @param openingHours the {@link OpeningHours} when the workspace is available
   * @param capacity the maximum number of people the workspace can accommodate
   * @param properties a {@link List} of {@link WorkspaceProperty} specifications
   */
  public Workspace(
      String name, OpeningHours openingHours, int capacity, List<WorkspaceProperty> properties) {
    this.id = null;
    this.name = name;
    this.openingHours = openingHours;
    this.capacity = capacity;
    this.properties = properties;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Workspace that = (Workspace) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
