package de.schonvoll.cleanspace.presentation.http.controllers;

import de.schonvoll.cleanspace.application.commands.LoginUserCommand;
import de.schonvoll.cleanspace.application.services.UserApplicationService;
import de.schonvoll.cleanspace.domain.entities.User;
import de.schonvoll.cleanspace.presentation.http.dtos.LoginRequest;
import de.schonvoll.cleanspace.presentation.http.dtos.UserResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for user authentication operations. Handles login functionality and returns user
 * information upon successful authentication.
 */
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

  private final UserApplicationService userApplicationService;

  /**
   * Authenticates a user with email and password.
   *
   * @param request login credentials containing email and password
   * @return authenticated user information
   */
  @PostMapping("/login")
  public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
    log.info("POST Request: Login - Request: {}", request);
    LoginUserCommand command = new LoginUserCommand(request.email(), request.password());

    User loggedInUser = userApplicationService.login(command);

    log.info("Success - Login: {}", loggedInUser);
    return ResponseEntity.ok(UserResponse.fromDomain(loggedInUser));
  }
}
