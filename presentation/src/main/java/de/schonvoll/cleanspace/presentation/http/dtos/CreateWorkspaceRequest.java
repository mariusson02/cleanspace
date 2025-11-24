package de.schonvoll.cleanspace.presentation.http.dtos;

import java.time.LocalTime;
import java.util.List;

public record CreateWorkspaceRequest(
    String name,
    LocalTime open,
    LocalTime close,
    int capacity,
    List<WorkspacePropertyDto> properties) {}
