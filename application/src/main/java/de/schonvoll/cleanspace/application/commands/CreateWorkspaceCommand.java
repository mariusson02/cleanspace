package de.schonvoll.cleanspace.application.commands;

import de.schonvoll.cleanspace.domain.valueobjects.OpeningHours;
import de.schonvoll.cleanspace.domain.valueobjects.WorkspaceProperty;
import java.util.List;

/**
 * Command for creating a new workspace.
 *
 * @param name the name of the workspace
 * @param openingHours the {@link OpeningHours} when the workspace is available
 * @param capacity the maximum number of people the workspace can accommodate
 * @param properties the list of {@link WorkspaceProperty} associated with the workspace
 */
public record CreateWorkspaceCommand(
    String name, OpeningHours openingHours, int capacity, List<WorkspaceProperty> properties) {}
