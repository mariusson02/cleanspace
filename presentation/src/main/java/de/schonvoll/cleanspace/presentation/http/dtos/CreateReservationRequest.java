package de.schonvoll.cleanspace.presentation.http.dtos;

import java.time.LocalDateTime;

public record CreateReservationRequest(
    String workspaceName, String userEmail, LocalDateTime start, int durationInMinutes) {}
