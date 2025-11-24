package de.schonvoll.cleanspace.domain.valueobjects;

import java.time.Duration;
import java.time.LocalDateTime;

public record TimeSlot(LocalDateTime start, Duration duration) {

  public LocalDateTime end() {
    return this.start.plus(this.duration);
  }

  public boolean conflictsWith(TimeSlot other) {
    return this.start().isBefore(other.end()) && this.end().isAfter(other.start());
  }
}
