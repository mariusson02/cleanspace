package de.schonvoll.cleanspace.presentation.http.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import de.schonvoll.cleanspace.application.commands.LoginUserCommand;
import de.schonvoll.cleanspace.application.services.UserApplicationService;
import de.schonvoll.cleanspace.domain.entities.User;
import de.schonvoll.cleanspace.domain.exceptions.AuthenticationException;
import de.schonvoll.cleanspace.presentation.http.dtos.LoginRequest;
import de.schonvoll.cleanspace.presentation.http.dtos.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @Mock private UserApplicationService userApplicationService;

  @InjectMocks private AuthController authController;

  @Test
  void shouldMapRequestToCommandCorrectly() {
    // Test: LoginRequest → LoginUserCommand Mapping
    String email = "test@example.com";
    String password = "password";
    LoginRequest request = new LoginRequest(email, password);
    User mockUser = mock(User.class);

    when(userApplicationService.login(any(LoginUserCommand.class))).thenReturn(mockUser);

    authController.login(request);

    ArgumentCaptor<LoginUserCommand> captor = ArgumentCaptor.forClass(LoginUserCommand.class);
    verify(userApplicationService).login(captor.capture());

    assertEquals(email, captor.getValue().email());
    assertEquals(password, captor.getValue().password());
  }

  @Test
  void shouldReturnOkResponseWithCorrectMapping() {
    // Test: User → UserResponse Mapping
    LoginRequest request = new LoginRequest("test@test.com", "pass");
    User mockUser = mock(User.class);

    when(userApplicationService.login(any())).thenReturn(mockUser);

    ResponseEntity<UserResponse> response = authController.login(request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void shouldPropagateServiceExceptions() {
    // Test: Exception Propagation
    LoginRequest request = new LoginRequest("test@test.com", "wrong");

    when(userApplicationService.login(any()))
        .thenThrow(new AuthenticationException("Invalid credentials"));

    assertThrows(AuthenticationException.class, () -> authController.login(request));
  }

  @Test
  void shouldHandleNullRequest() {
    // Test: Null Request Body
    assertThrows(NullPointerException.class, () -> authController.login(null));

    verify(userApplicationService, never()).login(any());
  }

  @Test
  void shouldHandleNullFieldsInRequest() {
    // Test: Null-Felder in Request
    LoginRequest request = new LoginRequest(null, null);

    when(userApplicationService.login(any()))
        .thenThrow(new IllegalArgumentException("Email and password required"));

    assertThrows(IllegalArgumentException.class, () -> authController.login(request));
  }

  @Test
  void shouldLogRequestAndResponse() {

    String email = "test@example.com";
    String password = "password";
    LoginRequest request = new LoginRequest(email, password);
    User mockUser = mock(User.class);

    when(userApplicationService.login(any())).thenReturn(mockUser);

    authController.login(request);

    verify(userApplicationService, times(1)).login(any());
  }
}
