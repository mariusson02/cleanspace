package de.schonvoll.cleanspace.presentation.http.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import de.schonvoll.cleanspace.application.commands.CreateReservationCommand;
import de.schonvoll.cleanspace.application.commands.FindUserReservationsQuery;
import de.schonvoll.cleanspace.application.services.ReservationApplicationService;
import de.schonvoll.cleanspace.domain.entities.Reservation;
import de.schonvoll.cleanspace.domain.exceptions.WorkspaceNotFoundException;
import de.schonvoll.cleanspace.domain.valueobjects.TimeSlot;
import de.schonvoll.cleanspace.presentation.http.dtos.CreateReservationRequest;
import de.schonvoll.cleanspace.presentation.http.dtos.ReservationResponse;
import de.schonvoll.cleanspace.presentation.http.dtos.UserReservationRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

  @Mock private ReservationApplicationService reservationApplicationService;

  @InjectMocks private ReservationController reservationController;

  @Test
  void shouldCreateReservationSuccessfully() {
    // Arrange
    CreateReservationRequest request =
        new CreateReservationRequest(
            "Room A", "user@test.com", LocalDateTime.of(2024, 1, 15, 10, 0), 60);

    Reservation mockReservation = createMockReservation(LocalDateTime.of(2024, 1, 15, 10, 0), 60);

    when(reservationApplicationService.create(any(CreateReservationCommand.class)))
        .thenReturn(mockReservation);

    // Act
    ResponseEntity<ReservationResponse> response = reservationController.createReservation(request);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void shouldMapRequestToCommandCorrectly() {
    // Arrange
    LocalDateTime start = LocalDateTime.of(2024, 2, 20, 14, 30);
    CreateReservationRequest request =
        new CreateReservationRequest("Meeting Room", "test@company.com", start, 90);

    Reservation mockReservation = createMockReservation(start, 90);

    when(reservationApplicationService.create(any(CreateReservationCommand.class)))
        .thenReturn(mockReservation);

    // Act
    reservationController.createReservation(request);

    // Assert
    ArgumentCaptor<CreateReservationCommand> captor =
        ArgumentCaptor.forClass(CreateReservationCommand.class);
    verify(reservationApplicationService).create(captor.capture());

    CreateReservationCommand command = captor.getValue();
    assertEquals("Meeting Room", command.workspaceName());
    assertEquals("test@company.com", command.userEmail());
    assertEquals(start, command.timeSlot().start());
    assertEquals(Duration.ofMinutes(90), command.timeSlot().duration());
  }

  @Test
  void shouldFindUserReservationsSuccessfully() {
    // Arrange
    UserReservationRequest request = new UserReservationRequest("user@example.com");
    Reservation mockReservation = createMockReservation(LocalDateTime.now(), 60);
    List<Reservation> mockReservations = List.of(mockReservation);

    when(reservationApplicationService.findByUser(any(FindUserReservationsQuery.class)))
        .thenReturn(mockReservations);

    // Act
    ResponseEntity<List<ReservationResponse>> response =
        reservationController.getUserReservations(request);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void shouldMapUserRequestToQueryCorrectly() {
    // Arrange
    UserReservationRequest request = new UserReservationRequest("specific@user.com");

    when(reservationApplicationService.findByUser(any(FindUserReservationsQuery.class)))
        .thenReturn(Collections.emptyList());

    // Act
    reservationController.getUserReservations(request);

    // Assert
    ArgumentCaptor<FindUserReservationsQuery> captor =
        ArgumentCaptor.forClass(FindUserReservationsQuery.class);
    verify(reservationApplicationService).findByUser(captor.capture());

    assertEquals("specific@user.com", captor.getValue().userEmail());
  }

  @Test
  void shouldPropagateServiceExceptions() {
    // Arrange
    CreateReservationRequest request =
        new CreateReservationRequest("NonExistentRoom", "user@test.com", LocalDateTime.now(), 60);

    when(reservationApplicationService.create(any(CreateReservationCommand.class)))
        .thenThrow(new WorkspaceNotFoundException("Workspace not found"));

    // Act & Assert
    assertThrows(
        WorkspaceNotFoundException.class, () -> reservationController.createReservation(request));
  }

  private Reservation createMockReservation(LocalDateTime start, int durationMinutes) {
    Reservation reservation = mock(Reservation.class);
    TimeSlot timeSlot = new TimeSlot(start, Duration.ofMinutes(durationMinutes));

    when(reservation.getTimeSlot()).thenReturn(timeSlot);

    return reservation;
  }
}
