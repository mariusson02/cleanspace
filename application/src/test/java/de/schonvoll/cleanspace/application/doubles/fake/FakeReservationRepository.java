package de.schonvoll.cleanspace.application.doubles.fake;

import de.schonvoll.cleanspace.domain.entities.Reservation;
import de.schonvoll.cleanspace.domain.repositories.ReservationRepository;
import de.schonvoll.cleanspace.domain.valueobjects.TimeSlot;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FakeReservationRepository implements ReservationRepository {
  private final HashMap<UUID, Reservation> reservationHashMap = new HashMap<>();

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

  @Override
  public Reservation findById(UUID id) {
    return reservationHashMap.get(id);
  }

  @Override
  public List<Reservation> findAll() {
    return reservationHashMap.values().stream().toList();
  }

  @Override
  public List<Reservation> findByUserId(UUID userId) {
    return reservationHashMap.values().stream()
        .filter(reservation -> reservation.getUserId().equals(userId))
        .toList();
  }

  @Override
  public List<Reservation> findByWorkspaceId(UUID workspaceId) {
    return reservationHashMap.values().stream()
        .filter(reservation -> reservation.getWorkspaceId().equals(workspaceId))
        .toList();
  }

  @Override
  public List<Reservation> findConflictingWithTimeslot(TimeSlot timeSlot) {
    return reservationHashMap.values().stream()
        .filter(reservation -> timeSlot.conflictsWith(reservation.getTimeSlot()))
        .toList();
  }
}
