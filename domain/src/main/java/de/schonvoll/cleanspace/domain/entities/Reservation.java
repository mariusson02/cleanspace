package de.schonvoll.cleanspace.domain.entities;

import de.schonvoll.cleanspace.domain.valueobjects.TimeSlot;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Domain entity representing a workspace reservation in the system. Links a user to a workspace for
 * a specific {@link TimeSlot}.
 */
@AllArgsConstructor
@Getter
public class Reservation {
  private final UUID id;
  @Setter private UUID workspaceId;
  @Setter private UUID userId;
  @Setter private TimeSlot timeSlot;

  /**
   * Creates a new reservation without an ID (for new entities).
   *
   * @param workspaceId the {@link UUID} of the workspace being reserved
   * @param userId the {@link UUID} of the user making the reservation
   * @param timeSlot the {@link TimeSlot} when the workspace is reserved
   */
  public Reservation(UUID workspaceId, UUID userId, TimeSlot timeSlot) {
    this.id = null;
    this.workspaceId = workspaceId;
    this.userId = userId;
    this.timeSlot = timeSlot;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Reservation that = (Reservation) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
