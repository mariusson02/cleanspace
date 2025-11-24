package de.schonvoll.cleanspace.application.commands;

/**
 * Query for finding all reservations for a specific user.
 *
 * @param userEmail the email of the user whose reservations to find
 */
public record FindUserReservationsQuery(String userEmail) {}
