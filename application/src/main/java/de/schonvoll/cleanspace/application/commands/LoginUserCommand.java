package de.schonvoll.cleanspace.application.commands;

/**
 * Command for user authentication.
 *
 * @param email the user's email address
 * @param password the user's password
 */
public record LoginUserCommand(String email, String password) {}
