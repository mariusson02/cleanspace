package de.schonvoll.cleanspace.presentation.http;

import de.schonvoll.cleanspace.domain.exceptions.AuthenticationException;
import de.schonvoll.cleanspace.domain.exceptions.DuplicateReservationException;
import de.schonvoll.cleanspace.domain.exceptions.DuplicateWorkspaceException;
import de.schonvoll.cleanspace.domain.exceptions.WorkspaceFullException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for REST API endpoints. Maps domain exceptions to appropriate HTTP
 * status codes and standardized error responses.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  record ApiError(HttpStatus status, String message) {}

  /**
   * Handles {@link IllegalArgumentException} and maps it to an HTTP 400 Bad Request response.
   *
   * @param ex the exception that was thrown
   * @return a {@link ResponseEntity} with a 400 status and error message
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
    ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
    log.warn(error.message);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles {@link WorkspaceFullException} and maps it to an HTTP 409 Conflict response.
   *
   * @param ex the exception that was thrown
   * @return a {@link ResponseEntity} with a 409 status and error message
   */
  @ExceptionHandler(WorkspaceFullException.class)
  public ResponseEntity<ApiError> handleWorkspaceFullException(WorkspaceFullException ex) {
    ApiError error = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
    log.warn(error.message);
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  /**
   * Handles {@link AuthenticationException} and maps it to an HTTP 401 Unauthorized response.
   *
   * @param ex the exception that was thrown
   * @return a {@link ResponseEntity} with a 401 status and error message
   */
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex) {
    ApiError error = new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage());
    log.warn(error.message);
    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
  }

  /**
   * Handles {@link DuplicateReservationException} and maps it to an HTTP 409 Conflict response.
   *
   * @param ex the exception that was thrown
   * @return a {@link ResponseEntity} with a 409 status and error message
   */
  @ExceptionHandler(DuplicateReservationException.class)
  public ResponseEntity<ApiError> handleDuplicateReservationException(
      DuplicateReservationException ex) {
    ApiError error = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
    log.warn(error.message);
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  /**
   * Handles {@link DuplicateWorkspaceException} and maps it to an HTTP 409 Conflict response.
   *
   * @param ex the exception that was thrown
   * @return a {@link ResponseEntity} with a 409 status and error message
   */
  @ExceptionHandler(DuplicateWorkspaceException.class)
  public ResponseEntity<ApiError> handleDuplicateWorkspaceException(
      DuplicateWorkspaceException ex) {
    ApiError error = new ApiError(HttpStatus.CONFLICT, ex.getMessage());
    log.warn(error.message);
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  /**
   * Handles any unhandled {@link Exception} and maps it to an HTTP 500 Internal Server Error
   * response.
   *
   * @param ex the exception that was thrown
   * @return a {@link ResponseEntity} with a 500 status and generic error message
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGenericException(Exception ex) {
    ex.printStackTrace();
    ApiError error =
        new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
    log.warn(error.message);
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}