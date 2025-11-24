package de.schonvoll.cleanspace.presentation.http.dtos;

import de.schonvoll.cleanspace.domain.entities.Reservation;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationResponse(
    UUID id, UUID workspaceId, UUID userId, LocalDateTime start, LocalDateTime end) {

  public static ReservationResponse fromDomain(Reservation reservation) {
    return new ReservationResponse(
        reservation.getId(),
        reservation.getWorkspaceId(),
        reservation.getUserId(),
        reservation.getTimeSlot().start(),
        reservation.getTimeSlot().end());
  }
}
