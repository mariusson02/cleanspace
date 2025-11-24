package de.schonvoll.cleanspace.application.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.schonvoll.cleanspace.application.Constants;
import de.schonvoll.cleanspace.application.commands.LoginUserCommand;
import de.schonvoll.cleanspace.application.doubles.dummy.DummyPasswordEncoder;
import de.schonvoll.cleanspace.application.doubles.fake.FakePasswordEncoder;
import de.schonvoll.cleanspace.application.doubles.fake.FakeUserRepository;
import de.schonvoll.cleanspace.domain.entities.User;
import de.schonvoll.cleanspace.domain.exceptions.AuthenticationException;
import de.schonvoll.cleanspace.domain.repositories.UserRepository;
import de.schonvoll.cleanspace.domain.services.PasswordEncoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class UserApplicationServiceTest {

  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  private UserApplicationService userApplicationService;

  @AfterEach
  void tearDown() {
    userRepository = null;
    passwordEncoder = null;
    userApplicationService = null;
  }

  @Test
  void testHappyPath() {
    // Arrange
    userRepository = new FakeUserRepository();
    passwordEncoder = new FakePasswordEncoder();
    userApplicationService = new UserApplicationService(userRepository, passwordEncoder);
    User savedUser = Constants.getFirstUser();
    userRepository.save(savedUser);
    LoginUserCommand command =
        new LoginUserCommand(Constants.FIRST_USER_EMAIL, Constants.FIRST_USER_PASSWORD);

    // Act
    User loggedInUser = userApplicationService.login(command);

    // Assert
    assertEquals(loggedInUser, savedUser);
  }

  @Test
  void testThrowsWhenPasswordWrong() {
    // Arrange
    userRepository = new FakeUserRepository();
    passwordEncoder = new FakePasswordEncoder();
    userApplicationService = new UserApplicationService(userRepository, passwordEncoder);
    User savedUser = Constants.getFirstUser();
    userRepository.save(savedUser);
    LoginUserCommand command = new LoginUserCommand(Constants.FIRST_USER_EMAIL, "wrong-password");

    // Act
    // Assert
    assertThrows(AuthenticationException.class, () -> userApplicationService.login(command));
  }

  @Test
  void testThrowsWhenUserNotFound() {
    // Arrange
    userRepository = new FakeUserRepository();
    passwordEncoder = new DummyPasswordEncoder();
    userApplicationService = new UserApplicationService(userRepository, passwordEncoder);
    LoginUserCommand command =
        new LoginUserCommand(Constants.FIRST_USER_EMAIL, Constants.FIRST_USER_PASSWORD);

    // Act
    // Assert
    assertThrows(AuthenticationException.class, () -> userApplicationService.login(command));
  }
}
