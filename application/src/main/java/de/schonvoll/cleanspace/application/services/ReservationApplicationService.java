package de.schonvoll.cleanspace.application.services;

import de.schonvoll.cleanspace.application.commands.CreateReservationCommand;
import de.schonvoll.cleanspace.application.commands.FindUserReservationsQuery;
import de.schonvoll.cleanspace.domain.entities.Reservation;
import de.schonvoll.cleanspace.domain.entities.User;
import de.schonvoll.cleanspace.domain.entities.Workspace;
import de.schonvoll.cleanspace.domain.exceptions.DuplicateReservationException;
import de.schonvoll.cleanspace.domain.exceptions.WorkspaceFullException;
import de.schonvoll.cleanspace.domain.exceptions.WorkspaceNotFoundException;
import de.schonvoll.cleanspace.domain.repositories.ReservationRepository;
import de.schonvoll.cleanspace.domain.repositories.UserRepository;
import de.schonvoll.cleanspace.domain.repositories.WorkspaceRepository;
import de.schonvoll.cleanspace.domain.valueobjects.OpeningHours;
import java.util.List;
import lombok.AllArgsConstructor;

/** Application service for managing reservation operations. */
@AllArgsConstructor
public class ReservationApplicationService {
  private ReservationRepository reservationRepository;
  private WorkspaceRepository workspaceRepository;
  private UserRepository userRepository;

  /**
   * Creates a new reservation for a workspace.
   *
   * @param command {@link CreateReservationCommand} containing all necessary information for the
   *     reservation
   * @return The created and saved reservation
   * @throws WorkspaceNotFoundException when the workspace does not exist
   * @throws IllegalArgumentException when user is invalid or time slot is outside opening hours
   * @throws DuplicateReservationException when the user already has a reservation for this time
   *     period
   * @throws WorkspaceFullException when the workspace is already fully booked
   */
  public Reservation create(CreateReservationCommand command)
      throws IllegalArgumentException, WorkspaceFullException, DuplicateReservationException {
    Workspace workspace = workspaceRepository.findByName(command.workspaceName());
    if (workspace == null) {
      throw new WorkspaceNotFoundException("Command: " + command);
    }

    User user = userRepository.findByEmail(command.userEmail());
    if (user == null) {
      throw new IllegalArgumentException("No valid user. Command: " + command);
    }

    OpeningHours openingHours = workspace.getOpeningHours();
    if (!openingHours.isOpenWithin(command.timeSlot())) {
      throw new IllegalArgumentException("No valid timeslot. Command: " + command);
    }

    List<Reservation> concurrentReservations =
        reservationRepository.findConflictingWithTimeslot(command.timeSlot());

    boolean isDuplicate =
        concurrentReservations.stream()
            .anyMatch(reservation -> reservation.getUserId().equals(user.getId()));
    if (isDuplicate) {
      throw new DuplicateReservationException("User has already booked during this time slot.");
    }

    if (workspace.getCapacity() <= concurrentReservations.size()) {
      throw new WorkspaceFullException("Workspace is already full. Command: " + command);
    }

    Reservation reservation = new Reservation(workspace.getId(), user.getId(), command.timeSlot());

    return reservationRepository.save(reservation);
  }

  /**
   * Finds all reservations of a user by their email address.
   *
   * @param query Contains the user's email address
   * @return List of all reservations for the user
   * @throws IllegalArgumentException when email is invalid or user does not exist
   */
  public List<Reservation> findByUser(FindUserReservationsQuery query) {
    String email = query.userEmail();

    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("User email must not be null or empty. Query: " + query);
    }

    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new IllegalArgumentException("No valid user. Query: " + query);
    }

    return reservationRepository.findByUserId(user.getId());
  }
}
