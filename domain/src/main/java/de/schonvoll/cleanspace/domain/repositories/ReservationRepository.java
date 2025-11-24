package de.schonvoll.cleanspace.domain.repositories;

import de.schonvoll.cleanspace.domain.entities.Reservation;
import de.schonvoll.cleanspace.domain.valueobjects.TimeSlot;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository {
  /**
   * Saves a reservation to the repository.
   *
   * @param reservation the {@link Reservation} to save
   * @return the saved reservation with generated ID if it was null
   */
  Reservation save(Reservation reservation);

  /**
   * Finds a reservation by its unique identifier.
   *
   * @param id the unique identifier of the reservation
   * @return the reservation with the given ID, or null if not found
   */
  Reservation findById(UUID id);

  /**
   * Retrieves all reservations from the repository.
   *
   * @return list of all reservations
   */
  List<Reservation> findAll();

  /**
   * Finds all reservations made by a specific user.
   *
   * @param userId the unique identifier of the user
   * @return list of reservations made by the user
   */
  List<Reservation> findByUserId(UUID userId);

  /**
   * Finds all reservations for a specific workspace.
   *
   * @param workspaceId the unique identifier of the workspace
   * @return list of reservations for the workspace
   */
  List<Reservation> findByWorkspaceId(UUID workspaceId);

  /**
   * Finds all reservations that conflict with the given TimeSlot.
   *
   * @param timeSlot the {@link TimeSlot} to check for conflicts
   * @return list of all conflicting reservations
   */
  List<Reservation> findConflictingWithTimeslot(TimeSlot timeSlot);
}
