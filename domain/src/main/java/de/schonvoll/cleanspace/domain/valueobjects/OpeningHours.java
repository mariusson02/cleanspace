package de.schonvoll.cleanspace.domain.valueobjects;

import java.time.Duration;
import java.time.LocalTime;

public record OpeningHours(LocalTime open, LocalTime close) {

  public boolean isOpenWithin(TimeSlot timeSlot) {
    boolean opensBeforeStart = open.isBefore(timeSlot.start().toLocalTime());
    boolean closesAfterEnd = close.isAfter(timeSlot.end().toLocalTime());

    return opensBeforeStart && closesAfterEnd;
  }

  public boolean isValid() {
    return open.isBefore(close);
  }

  public Duration getDuration() {
    return Duration.between(open, close);
  }
}
