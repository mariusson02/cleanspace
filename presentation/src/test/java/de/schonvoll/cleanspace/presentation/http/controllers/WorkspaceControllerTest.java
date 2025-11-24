package de.schonvoll.cleanspace.presentation.http.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import de.schonvoll.cleanspace.application.commands.CreateWorkspaceCommand;
import de.schonvoll.cleanspace.application.commands.FindAvailableWorkspacesQuery;
import de.schonvoll.cleanspace.application.services.WorkspaceApplicationService;
import de.schonvoll.cleanspace.domain.entities.Workspace;
import de.schonvoll.cleanspace.domain.valueobjects.OpeningHours;
import de.schonvoll.cleanspace.presentation.http.dtos.CreateWorkspaceRequest;
import de.schonvoll.cleanspace.presentation.http.dtos.FindAvailableWorkspacesQueryDto;
import de.schonvoll.cleanspace.presentation.http.dtos.WorkspacePropertyDto;
import de.schonvoll.cleanspace.presentation.http.dtos.WorkspaceResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
class WorkspaceControllerTest {

  @Mock
  private WorkspaceApplicationService workspaceApplicationService;

  @InjectMocks
  private WorkspaceController workspaceController;

  private Workspace createMockWorkspace() {
    Workspace mockWorkspace = mock(Workspace.class);
    OpeningHours mockOpeningHours = mock(OpeningHours.class);
    when(mockOpeningHours.open()).thenReturn(LocalTime.of(9, 0));
    when(mockOpeningHours.close()).thenReturn(LocalTime.of(17, 0));
    when(mockWorkspace.getOpeningHours()).thenReturn(mockOpeningHours);
    return mockWorkspace;
  }

  @Test
  void shouldCreateWorkspaceWithProperties() {
    // Arrange
    CreateWorkspaceRequest request =
            new CreateWorkspaceRequest(
                    "Conference Room",
                    LocalTime.of(9, 0),
                    LocalTime.of(17, 0),
                    10,
                    List.of(new WorkspacePropertyDto("projector", "available")));

    Workspace mockWorkspace = createMockWorkspace();
    when(workspaceApplicationService.create(any(CreateWorkspaceCommand.class)))
            .thenReturn(mockWorkspace);

    // Act
    ResponseEntity<WorkspaceResponse> response = workspaceController.createWorkspace(request);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(workspaceApplicationService).create(any(CreateWorkspaceCommand.class));
  }

  @Test
  void shouldCreateWorkspaceWithNullProperties() {
    // Arrange
    CreateWorkspaceRequest request =
            new CreateWorkspaceRequest("Simple Room", LocalTime.of(9, 0), LocalTime.of(17, 0), 5, null);

    Workspace mockWorkspace = createMockWorkspace();
    when(workspaceApplicationService.create(any(CreateWorkspaceCommand.class)))
            .thenReturn(mockWorkspace);

    // Act
    ResponseEntity<WorkspaceResponse> response = workspaceController.createWorkspace(request);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());

    ArgumentCaptor<CreateWorkspaceCommand> captor =
            ArgumentCaptor.forClass(CreateWorkspaceCommand.class);
    verify(workspaceApplicationService).create(captor.capture());
    assertNull(captor.getValue().properties());
  }

  @Test
  void shouldFindAvailableWorkspacesWithValidProperties() {
    // Arrange
    FindAvailableWorkspacesQueryDto queryDto =
            new FindAvailableWorkspacesQueryDto(
                    LocalDateTime.of(2024, 3, 15, 10, 0),
                    120,
                    8,
                    List.of("projector:available", "wifi:enabled"));

    List<Workspace> mockWorkspaces = List.of(createMockWorkspace());
    when(workspaceApplicationService.findAvailable(any(FindAvailableWorkspacesQuery.class)))
            .thenReturn(mockWorkspaces);

    // Act
    ResponseEntity<List<WorkspaceResponse>> response =
            workspaceController.findAvailableWorkspaces(queryDto);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());

    ArgumentCaptor<FindAvailableWorkspacesQuery> captor =
            ArgumentCaptor.forClass(FindAvailableWorkspacesQuery.class);
    verify(workspaceApplicationService).findAvailable(captor.capture());
    assertTrue(captor.getValue().requiredProperties().isPresent());
    assertEquals(2, captor.getValue().requiredProperties().get().size());
  }

  @Test
  void shouldFindAvailableWorkspacesWithNullProperties() {
    // Arrange
    FindAvailableWorkspacesQueryDto queryDto =
            new FindAvailableWorkspacesQueryDto(LocalDateTime.of(2024, 3, 15, 10, 0), 60, 5, null);

    List<Workspace> mockWorkspaces = List.of(createMockWorkspace());
    when(workspaceApplicationService.findAvailable(any(FindAvailableWorkspacesQuery.class)))
            .thenReturn(mockWorkspaces);

    // Act
    ResponseEntity<List<WorkspaceResponse>> response =
            workspaceController.findAvailableWorkspaces(queryDto);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());

    ArgumentCaptor<FindAvailableWorkspacesQuery> captor =
            ArgumentCaptor.forClass(FindAvailableWorkspacesQuery.class);
    verify(workspaceApplicationService).findAvailable(captor.capture());
    assertTrue(captor.getValue().requiredProperties().isEmpty());
  }

  @Test
  void shouldHandleInvalidPropertyFormat() {
    // Arrange
    FindAvailableWorkspacesQueryDto queryDto =
            new FindAvailableWorkspacesQueryDto(
                    LocalDateTime.of(2024, 3, 15, 10, 0),
                    60,
                    5,
                    List.of("invalid-format", "valid:property", "another-invalid"));

    List<Workspace> mockWorkspaces = Collections.emptyList();
    when(workspaceApplicationService.findAvailable(any(FindAvailableWorkspacesQuery.class)))
            .thenReturn(mockWorkspaces);

    // Act
    ResponseEntity<List<WorkspaceResponse>> response =
            workspaceController.findAvailableWorkspaces(queryDto);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());

    ArgumentCaptor<FindAvailableWorkspacesQuery> captor =
            ArgumentCaptor.forClass(FindAvailableWorkspacesQuery.class);
    verify(workspaceApplicationService).findAvailable(captor.capture());
    assertTrue(captor.getValue().requiredProperties().isPresent());
    assertEquals(1, captor.getValue().requiredProperties().get().size());
  }

  @Test
  void shouldHandleEmptyPropertiesList() {
    // Arrange
    FindAvailableWorkspacesQueryDto queryDto =
            new FindAvailableWorkspacesQueryDto(
                    LocalDateTime.of(2024, 3, 15, 10, 0), 60, 5, Collections.emptyList());

    List<Workspace> mockWorkspaces = Collections.emptyList();
    when(workspaceApplicationService.findAvailable(any(FindAvailableWorkspacesQuery.class)))
            .thenReturn(mockWorkspaces);

    // Act
    ResponseEntity<List<WorkspaceResponse>> response =
            workspaceController.findAvailableWorkspaces(queryDto);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());

    ArgumentCaptor<FindAvailableWorkspacesQuery> captor =
            ArgumentCaptor.forClass(FindAvailableWorkspacesQuery.class);
    verify(workspaceApplicationService).findAvailable(captor.capture());
    assertTrue(captor.getValue().requiredProperties().isEmpty());
  }
}