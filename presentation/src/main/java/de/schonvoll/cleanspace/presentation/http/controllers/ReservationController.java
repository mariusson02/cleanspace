package de.schonvoll.cleanspace.presentation.http.controllers;

import de.schonvoll.cleanspace.application.commands.CreateReservationCommand;
import de.schonvoll.cleanspace.application.commands.FindUserReservationsQuery;
import de.schonvoll.cleanspace.application.services.ReservationApplicationService;
import de.schonvoll.cleanspace.domain.entities.Reservation;
import de.schonvoll.cleanspace.domain.valueobjects.TimeSlot;
import de.schonvoll.cleanspace.presentation.http.dtos.CreateReservationRequest;
import de.schonvoll.cleanspace.presentation.http.dtos.ReservationResponse;
import de.schonvoll.cleanspace.presentation.http.dtos.UserReservationRequest;
import java.time.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for workspace reservation operations. Handles creation of new reservations and
 * retrieval of user reservations.
 */
@RestController
@RequestMapping("/api/reservations")
@AllArgsConstructor
@Slf4j
public class ReservationController {

  private final ReservationApplicationService reservationApplicationServiceUseCase;

  /**
   * Creates a new workspace reservation.
   *
   * @param request reservation details including workspace, user, and time slot
   * @return created reservation information
   */
  @PostMapping
  public ResponseEntity<ReservationResponse> createReservation(
      @RequestBody CreateReservationRequest request) {
    log.info("POST Request: Create Reservation - Request: {}", request);

    TimeSlot timeSlot =
        new TimeSlot(request.start(), Duration.ofMinutes(request.durationInMinutes()));

    CreateReservationCommand command =
        new CreateReservationCommand(request.workspaceName(), request.userEmail(), timeSlot);

    Reservation newReservation = reservationApplicationServiceUseCase.create(command);

    ReservationResponse response = ReservationResponse.fromDomain(newReservation);
    log.info("Success - Reservation created: {}", newReservation);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  /**
   * Retrieves all reservations for a specific user.
   *
   * @param request contains the user email to search for
   * @return list of user's reservations
   */
  @GetMapping("/user")
  public ResponseEntity<List<ReservationResponse>> getUserReservations(
      @RequestBody UserReservationRequest request) {
    log.info("GET Request: Find User Reservations - User Email: {}", request.userEmail());

    FindUserReservationsQuery query = new FindUserReservationsQuery(request.userEmail());
    List<Reservation> userReservations = reservationApplicationServiceUseCase.findByUser(query);

    List<ReservationResponse> response =
        userReservations.stream().map(ReservationResponse::fromDomain).toList();

    log.info(
        "Success - Found {} reservations for user: {}",
        userReservations.size(),
        request.userEmail());
    return ResponseEntity.ok(response);
  }
}
