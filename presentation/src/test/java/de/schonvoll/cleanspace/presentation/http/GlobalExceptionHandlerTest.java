package de.schonvoll.cleanspace.presentation.http;

import static org.junit.jupiter.api.Assertions.*;

import de.schonvoll.cleanspace.domain.exceptions.AuthenticationException;
import de.schonvoll.cleanspace.domain.exceptions.DuplicateReservationException;
import de.schonvoll.cleanspace.domain.exceptions.DuplicateWorkspaceException;
import de.schonvoll.cleanspace.domain.exceptions.WorkspaceFullException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  @InjectMocks private GlobalExceptionHandler globalExceptionHandler;

  @Test
  void shouldHandleIllegalArgumentException() {
    // Arrange
    IllegalArgumentException ex = new IllegalArgumentException("Invalid input");

    // Act
    ResponseEntity<GlobalExceptionHandler.ApiError> response =
        globalExceptionHandler.handleIllegalArgumentException(ex);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    GlobalExceptionHandler.ApiError body = response.getBody();
    assertNotNull(body);
    assertEquals("Invalid input", body.message());
    assertEquals(HttpStatus.BAD_REQUEST, body.status());
  }

  @Test
  void shouldHandleWorkspaceFullException() {
    // Arrange
    WorkspaceFullException ex = new WorkspaceFullException("Workspace is full");

    // Act
    ResponseEntity<GlobalExceptionHandler.ApiError> response =
        globalExceptionHandler.handleWorkspaceFullException(ex);

    // Assert
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    GlobalExceptionHandler.ApiError body = response.getBody();
    assertNotNull(body);
    assertEquals("Workspace is full", body.message());
    assertEquals(HttpStatus.CONFLICT, body.status());
  }

  @Test
  void shouldHandleAuthenticationException() {
    // Arrange
    AuthenticationException ex = new AuthenticationException("Unauthorized access");

    // Act
    ResponseEntity<GlobalExceptionHandler.ApiError> response =
        globalExceptionHandler.handleAuthenticationException(ex);

    // Assert
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    GlobalExceptionHandler.ApiError body = response.getBody();
    assertNotNull(body);
    assertEquals("Unauthorized access", body.message());
    assertEquals(HttpStatus.UNAUTHORIZED, body.status());
  }

  @Test
  void shouldHandleDuplicateReservationException() {
    // Arrange
    DuplicateReservationException ex =
        new DuplicateReservationException("Reservation already exists");

    // Act
    ResponseEntity<GlobalExceptionHandler.ApiError> response =
        globalExceptionHandler.handleDuplicateReservationException(ex);

    // Assert
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    GlobalExceptionHandler.ApiError body = response.getBody();
    assertNotNull(body);
    assertEquals("Reservation already exists", body.message());
    assertEquals(HttpStatus.CONFLICT, body.status());
  }

  @Test
  void shouldHandleDuplicateWorkspaceException() {
    // Arrange
    DuplicateWorkspaceException ex = new DuplicateWorkspaceException("Workspace already exists");

    // Act
    ResponseEntity<GlobalExceptionHandler.ApiError> response =
        globalExceptionHandler.handleDuplicateWorkspaceException(ex);

    // Assert
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    GlobalExceptionHandler.ApiError body = response.getBody();
    assertNotNull(body);
    assertEquals("Workspace already exists", body.message());
    assertEquals(HttpStatus.CONFLICT, body.status());
  }

  @Test
  void shouldHandleGenericException() {
    // Arrange
    Exception ex = new RuntimeException("Unexpected error");

    // Act
    ResponseEntity<GlobalExceptionHandler.ApiError> response =
        globalExceptionHandler.handleGenericException(ex);

    // Assert
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    GlobalExceptionHandler.ApiError body = response.getBody();
    assertNotNull(body);
    assertEquals("An unexpected error occurred.", body.message());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, body.status());
  }
}