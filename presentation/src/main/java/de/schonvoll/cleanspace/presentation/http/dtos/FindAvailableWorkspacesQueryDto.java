package de.schonvoll.cleanspace.presentation.http.dtos;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Query for finding available workspaces within a specific time slot.
 *
 * @param start the start time for the availability check
 * @param durationInMinutes the duration of the requested time slot in minutes
 * @param minCapacity the minimum capacity required for the workspace (optional)
 * @param requiredProperties the list of required workspace properties (optional)
 */
public record FindAvailableWorkspacesQueryDto(
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
    int durationInMinutes,
    Integer minCapacity,
    List<String> requiredProperties) {}
