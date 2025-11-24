package de.schonvoll.cleanspace.presentation.http.dtos;

import de.schonvoll.cleanspace.domain.entities.Workspace;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/** REST API response for workspace information. */
public record WorkspaceResponse(
    UUID id,
    String name,
    LocalTime open,
    LocalTime close,
    int capacity,
    List<WorkspacePropertyDto> properties) {

  /** Converts domain entity to API response format. */
  public static WorkspaceResponse fromDomain(Workspace workspace) {
    return new WorkspaceResponse(
        workspace.getId(),
        workspace.getName(),
        workspace.getOpeningHours().open(),
        workspace.getOpeningHours().close(),
        workspace.getCapacity(),
        workspace.getProperties().stream()
            .map(prop -> new WorkspacePropertyDto(prop.key(), prop.value()))
            .toList());
  }
}
