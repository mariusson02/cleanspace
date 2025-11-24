package de.schonvoll.cleanspace.application.commands;

import de.schonvoll.cleanspace.domain.valueobjects.TimeSlot;

/**
 * Command for creating a new workspace reservation.
 *
 * @param workspaceName the name of the workspace to reserve
 * @param userEmail the email of the user making the reservation
 * @param timeSlot the {@link TimeSlot} when the workspace should be reserved
 */
public record CreateReservationCommand(String workspaceName, String userEmail, TimeSlot timeSlot) {}
