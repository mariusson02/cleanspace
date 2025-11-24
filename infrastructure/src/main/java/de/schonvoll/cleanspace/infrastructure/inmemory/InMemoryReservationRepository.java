package de.schonvoll.cleanspace.infrastructure.inmemory;

import de.schonvoll.cleanspace.domain.entities.Reservation;
import de.schonvoll.cleanspace.domain.repositories.ReservationRepository;
import de.schonvoll.cleanspace.domain.valueobjects.TimeSlot;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 * In-memory implementation of ReservationRepository for development and testing. Uses a HashMap to
 * store Reservation entities.
 */
@Repository
@Profile("in-memory")
public class InMemoryReservationRepository implements ReservationRepository {
  private final HashMap<UUID, Reservation> reservationHashMap;

  public InMemoryReservationRepository() {
    this.reservationHashMap = new HashMap<>();
  }

  /** {@inheritDoc} */
  @Override
  public Reservation save(Reservation reservation) {
    if (reservation.getId() == null) {
      Reservation persistedReservation =
          new Reservation(
              UUID.randomUUID(),
              reservation.getWorkspaceId(),
              reservation.getUserId(),
              reservation.getTimeSlot());
      reservationHashMap.put(persistedReservation.getId(), persistedReservation);
      return persistedReservation;
    }
    reservationHashMap.put(reservation.getId(), reservation);
    return reservation;
  }

  /** {@inheritDoc} */
  @Override
  public Reservation findById(UUID id) {
    return reservationHashMap.get(id);
  }

  /** {@inheritDoc} */
  @Override
  public List<Reservation> findAll() {
    return reservationHashMap.values().stream().toList();
  }

  /** {@inheritDoc} */
  @Override
  public List<Reservation> findByUserId(UUID userId) {
    return reservationHashMap.values().stream()
        .filter(reservation -> reservation.getUserId().equals(userId))
        .toList();
  }

  /** {@inheritDoc} */
  @Override
  public List<Reservation> findByWorkspaceId(UUID workspaceId) {
    return reservationHashMap.values().stream()
        .filter(reservation -> reservation.getWorkspaceId().equals(workspaceId))
        .toList();
  }

  /** {@inheritDoc} */
  @Override
  public List<Reservation> findConflictingWithTimeslot(TimeSlot timeSlot) {
    return reservationHashMap.values().stream()
        .filter(reservation -> timeSlot.conflictsWith(reservation.getTimeSlot()))
        .toList();
  }
}
