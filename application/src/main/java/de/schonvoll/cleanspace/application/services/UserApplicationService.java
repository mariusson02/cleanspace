package de.schonvoll.cleanspace.application.services;

import de.schonvoll.cleanspace.application.commands.LoginUserCommand;
import de.schonvoll.cleanspace.domain.entities.User;
import de.schonvoll.cleanspace.domain.exceptions.AuthenticationException;
import de.schonvoll.cleanspace.domain.repositories.UserRepository;
import de.schonvoll.cleanspace.domain.services.PasswordEncoder;
import lombok.AllArgsConstructor;

/**
 * Application Service for user management operations. Handles authentication and user-related
 * business logic.
 */
@AllArgsConstructor
public class UserApplicationService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * Authenticates a user with email and password.
   *
   * @param command Contains the user's email and password for authentication
   * @return The authenticated user if credentials are valid
   * @throws AuthenticationException when email is not found or password is incorrect
   */
  public User login(LoginUserCommand command) {
    User user = userRepository.findByEmail(command.email());

    if (user == null || !passwordEncoder.matches(command.password(), user.getPasswordHash())) {
      throw new AuthenticationException("Not authorized");
    }

    return user;
  }
}
