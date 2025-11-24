package de.schonvoll.cleanspace.application.services;

import de.schonvoll.cleanspace.application.commands.CreateWorkspaceCommand;
import de.schonvoll.cleanspace.application.commands.FindAvailableWorkspacesQuery;
import de.schonvoll.cleanspace.domain.entities.Reservation;
import de.schonvoll.cleanspace.domain.entities.Workspace;
import de.schonvoll.cleanspace.domain.exceptions.DuplicateWorkspaceException;
import de.schonvoll.cleanspace.domain.exceptions.OpeningHoursInvalidException;
import de.schonvoll.cleanspace.domain.repositories.ReservationRepository;
import de.schonvoll.cleanspace.domain.repositories.WorkspaceRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

/** Application service for managing workspace operations. */
@AllArgsConstructor
public class WorkspaceApplicationService {
  private final WorkspaceRepository workspaceRepository;
  private final ReservationRepository reservationRepository;

  /**
   * Creates a new workspace in the system.
   *
   * @param command the {@link CreateWorkspaceCommand} containing workspace details
   * @return the created {@link Workspace}
   * @throws IllegalArgumentException when name is invalid or capacity is less than 1
   * @throws OpeningHoursInvalidException when opening hours are null or invalid
   * @throws DuplicateWorkspaceException when a workspace with the same name already exists
   */
  private static final int MAX_WORKSPACE_NAME_LENGTH = 125;
  private static final int MIN_WORKSPACE_CAPACITY = 1;

  public Workspace create(CreateWorkspaceCommand command)
      throws IllegalArgumentException, OpeningHoursInvalidException, DuplicateWorkspaceException {
    if (command.name() == null || command.name().isBlank() || command.name().length() > MAX_WORKSPACE_NAME_LENGTH) {
      throw new IllegalArgumentException(
          "Workspace name must be set and shorter than " + MAX_WORKSPACE_NAME_LENGTH + " characters.");
    }
    if (workspaceRepository.findByName(command.name()) != null) {
      throw new DuplicateWorkspaceException(
          "It already exists a workspace with the name: " + command.name());
    }
    if (command.openingHours() == null || !command.openingHours().isValid()) {
      throw new OpeningHoursInvalidException("Opening Hours must be set and valid.");
    }
    if (command.capacity() < MIN_WORKSPACE_CAPACITY) {
      throw new IllegalArgumentException("Workspace capacity needs to be at least " + MIN_WORKSPACE_CAPACITY + ".");
    }
    Workspace workspace =
        Workspace.builder()
            .name(command.name())
            .openingHours(command.openingHours())
            .capacity(command.capacity())
            .properties(command.properties())
            .build();
    return workspaceRepository.save(workspace);
  }

  /**
   * Finds all workspaces available for the specified time slot and criteria. Filters by minimum
   * capacity, required properties, and checks for conflicting reservations.
   *
   * @param query {@link FindAvailableWorkspacesQuery} Contains time slot, optional minimum capacity
   *     and required properties
   * @return List of workspaces that meet all criteria and have available capacity
   */
  public List<Workspace> findAvailable(FindAvailableWorkspacesQuery query) {
    List<Workspace> workspaces = workspaceRepository.findAll();

    List<Workspace> candidates =
        workspaces.stream()
            .filter(
                workspace ->
                    query
                        .minCapacity()
                        .map(minCap -> workspace.getCapacity() >= minCap)
                        .orElse(true))
            .filter(
                workspace ->
                    query
                        .requiredProperties()
                        .map(properties -> workspace.getProperties().containsAll(properties))
                        .orElse(true))
            .toList();

    List<Reservation> conflictingReservations =
        reservationRepository.findConflictingWithTimeslot(query.timeSlot());

    Map<UUID, Long> reservationCountByWorkspace =
        conflictingReservations.stream()
            .collect(Collectors.groupingBy(Reservation::getWorkspaceId, Collectors.counting()));

    return candidates.stream()
        .filter(
            workspace -> {
              long existingReservations =
                  reservationCountByWorkspace.getOrDefault(workspace.getId(), 0L);
              return workspace.getCapacity() > existingReservations;
            })
        .toList();
  }
}
