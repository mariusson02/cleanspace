package de.schonvoll.cleanspace.application.commands;

import de.schonvoll.cleanspace.domain.valueobjects.TimeSlot;
import de.schonvoll.cleanspace.domain.valueobjects.WorkspaceProperty;
import java.util.List;
import java.util.Optional;

/**
 * Query for finding available workspaces within a specific time slot.
 *
 * @param timeSlot the {@link TimeSlot} to check for availability
 */
public record FindAvailableWorkspacesQuery(
    TimeSlot timeSlot,
    Optional<Integer> minCapacity,
    Optional<List<WorkspaceProperty>> requiredProperties) {}
