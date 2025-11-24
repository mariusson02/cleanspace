package de.schonvoll.cleanspace.presentation.http.controllers;

import de.schonvoll.cleanspace.application.commands.CreateWorkspaceCommand;
import de.schonvoll.cleanspace.application.commands.FindAvailableWorkspacesQuery;
import de.schonvoll.cleanspace.application.services.WorkspaceApplicationService;
import de.schonvoll.cleanspace.domain.entities.Workspace;
import de.schonvoll.cleanspace.domain.valueobjects.OpeningHours;
import de.schonvoll.cleanspace.domain.valueobjects.TimeSlot;
import de.schonvoll.cleanspace.domain.valueobjects.WorkspaceProperty;
import de.schonvoll.cleanspace.presentation.http.dtos.CreateWorkspaceRequest;
import de.schonvoll.cleanspace.presentation.http.dtos.FindAvailableWorkspacesQueryDto;
import de.schonvoll.cleanspace.presentation.http.dtos.WorkspaceResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for workspace management operations. Handles workspace creation and availability
 * searches with filtering capabilities.
 */
@RestController
@RequestMapping("/api/workspaces")
@AllArgsConstructor
@Slf4j
public class WorkspaceController {

  private final WorkspaceApplicationService workspaceApplicationService;

  /**
   * Creates a new workspace with specified properties and opening hours.
   *
   * @param request workspace details including name, capacity, hours, and properties
   * @return created workspace information
   */
  @PostMapping
  public ResponseEntity<WorkspaceResponse> createWorkspace(
      @RequestBody CreateWorkspaceRequest request) {
    log.info("POST Request: Create Workspace - Request: {}", request);

    OpeningHours openingHours = new OpeningHours(request.open(), request.close());

    List<WorkspaceProperty> properties =
        request.properties() == null
            ? null
            : request.properties().stream()
                .map(prop -> new WorkspaceProperty(prop.key(), prop.value()))
                .toList();

    CreateWorkspaceCommand command =
        new CreateWorkspaceCommand(request.name(), openingHours, request.capacity(), properties);

    Workspace newWorkspace = workspaceApplicationService.create(command);

    WorkspaceResponse response = WorkspaceResponse.fromDomain(newWorkspace);
    log.info("Success - Workspace created: {}", newWorkspace);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  /**
   * Finds available workspaces for a given time slot with optional filtering. Supports filtering by
   * minimum capacity and required properties. Properties should be formatted as "key:value"
   * strings.
   *
   * @param queryDto search criteria including time slot, capacity, and property requirements
   * @return list of available workspaces matching the criteria
   */
  @GetMapping("/available")
  public ResponseEntity<List<WorkspaceResponse>> findAvailableWorkspaces(
      @RequestBody FindAvailableWorkspacesQueryDto queryDto) {
    log.info("GET Request: Find available Workspaces - Query: {}", queryDto);
    TimeSlot timeSlot =
        new TimeSlot(queryDto.start(), Duration.ofMinutes(queryDto.durationInMinutes()));

    List<WorkspaceProperty> requiredWorkspacePropertiesList = new ArrayList<>();
    if (queryDto.requiredProperties() != null) {
      for (String propString : queryDto.requiredProperties()) {
        String[] keyValuePair = propString.split(":", 2);
        if (keyValuePair.length == 2) {
          requiredWorkspacePropertiesList.add(
              new WorkspaceProperty(keyValuePair[0], keyValuePair[1]));
        }
      }
    }
    FindAvailableWorkspacesQuery query =
        new FindAvailableWorkspacesQuery(
            timeSlot,
            Optional.ofNullable(queryDto.minCapacity()),
            requiredWorkspacePropertiesList.isEmpty()
                ? Optional.empty()
                : Optional.of(requiredWorkspacePropertiesList));

    List<Workspace> availableWorkspaces = workspaceApplicationService.findAvailable(query);
    List<WorkspaceResponse> response =
        availableWorkspaces.stream().map(WorkspaceResponse::fromDomain).toList();
    log.info("Success - Found {} available Workspaces", availableWorkspaces.size());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
