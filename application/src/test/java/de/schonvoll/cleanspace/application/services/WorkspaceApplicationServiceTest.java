package de.schonvoll.cleanspace.application.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.schonvoll.cleanspace.application.Constants;
import de.schonvoll.cleanspace.application.commands.CreateWorkspaceCommand;
import de.schonvoll.cleanspace.application.commands.FindAvailableWorkspacesQuery;
import de.schonvoll.cleanspace.application.doubles.fake.FakeReservationRepository;
import de.schonvoll.cleanspace.application.doubles.fake.FakeWorkspaceRepository;
import de.schonvoll.cleanspace.domain.entities.Reservation;
import de.schonvoll.cleanspace.domain.entities.User;
import de.schonvoll.cleanspace.domain.entities.Workspace;
import de.schonvoll.cleanspace.domain.exceptions.DuplicateWorkspaceException;
import de.schonvoll.cleanspace.domain.exceptions.OpeningHoursInvalidException;
import de.schonvoll.cleanspace.domain.repositories.ReservationRepository;
import de.schonvoll.cleanspace.domain.repositories.WorkspaceRepository;
import de.schonvoll.cleanspace.domain.valueobjects.OpeningHours;
import de.schonvoll.cleanspace.domain.valueobjects.TimeSlot;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class WorkspaceApplicationServiceTest {
  private WorkspaceRepository workspaceRepository;
  private ReservationRepository reservationRepository;

  private WorkspaceApplicationService workspaceApplicationService;

  @Test
  void testCreateWorkspaceHappyPath() {
    // Arrange
    workspaceRepository = new FakeWorkspaceRepository();
    reservationRepository = new FakeReservationRepository();
    workspaceApplicationService =
        new WorkspaceApplicationService(workspaceRepository, reservationRepository);
    CreateWorkspaceCommand command =
        new CreateWorkspaceCommand(
            Constants.FIRST_WORKSPACE_NAME,
            Constants.FIRST_WORKSPACE_OPENING_HOURS,
            Constants.FIRST_WORKSPACE_CAPACITY,
            Constants.FIRST_WORKSPACE_PROPERTY_LIST);

    // Act
    Workspace created = workspaceApplicationService.create(command);

    // Assert
    assertNotNull(created);
    assertEquals(1, workspaceRepository.findAll().size());
  }

  @Test
  void testFindAvailableHappyPath() {
    // Arrange
    workspaceRepository = new FakeWorkspaceRepository();
    reservationRepository = new FakeReservationRepository();
    workspaceApplicationService =
        new WorkspaceApplicationService(workspaceRepository, reservationRepository);
    Workspace workspace = Constants.getFirstWorkspace();
    workspaceRepository.save(workspace);
    TimeSlot timeSlot =
        new TimeSlot(
            LocalDateTime.of(
                LocalDate.now(), Constants.FIRST_WORKSPACE_OPENING_HOURS.open().plusMinutes(10)),
            Constants.FIRST_WORKSPACE_OPENING_HOURS.getDuration().minusMinutes(10));
    Integer minCapacity = workspace.getCapacity();
    FindAvailableWorkspacesQuery query =
        new FindAvailableWorkspacesQuery(
            timeSlot,
            Optional.of(minCapacity),
            Optional.of(Constants.FIRST_WORKSPACE_PROPERTY_LIST));

    List<Workspace> results = workspaceApplicationService.findAvailable(query);

    assertEquals(1, results.size());
    Workspace found = results.getFirst();
    assertEquals(Constants.FIRST_WORKSPACE_NAME, found.getName());
    assertEquals(Constants.FIRST_WORKSPACE_OPENING_HOURS, found.getOpeningHours());
    assertEquals(Constants.FIRST_WORKSPACE_CAPACITY, found.getCapacity());
    assertEquals(found.getProperties(), Constants.FIRST_WORKSPACE_PROPERTY_LIST);
  }

  @Test
  void shouldThrowExceptionWhenNameIsNull() {
    // Arrange
    workspaceRepository = new FakeWorkspaceRepository();
    reservationRepository = new FakeReservationRepository();
    workspaceApplicationService =
        new WorkspaceApplicationService(workspaceRepository, reservationRepository);

    CreateWorkspaceCommand command =
        new CreateWorkspaceCommand(
            null,
            Constants.FIRST_WORKSPACE_OPENING_HOURS,
            Constants.FIRST_WORKSPACE_CAPACITY,
            Constants.FIRST_WORKSPACE_PROPERTY_LIST);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> workspaceApplicationService.create(command));
  }

  @Test
  void shouldThrowExceptionWhenNameIsBlank() {
    // Arrange
    workspaceRepository = new FakeWorkspaceRepository();
    reservationRepository = new FakeReservationRepository();
    workspaceApplicationService =
        new WorkspaceApplicationService(workspaceRepository, reservationRepository);

    CreateWorkspaceCommand command =
        new CreateWorkspaceCommand(
            "   ",
            Constants.FIRST_WORKSPACE_OPENING_HOURS,
            Constants.FIRST_WORKSPACE_CAPACITY,
            Constants.FIRST_WORKSPACE_PROPERTY_LIST);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> workspaceApplicationService.create(command));
  }

  @Test
  void shouldThrowExceptionWhenNameTooLong() {
    // Arrange
    workspaceRepository = new FakeWorkspaceRepository();
    reservationRepository = new FakeReservationRepository();
    workspaceApplicationService =
        new WorkspaceApplicationService(workspaceRepository, reservationRepository);

    String longName = "A".repeat(126);
    CreateWorkspaceCommand command =
        new CreateWorkspaceCommand(
            longName,
            Constants.FIRST_WORKSPACE_OPENING_HOURS,
            Constants.FIRST_WORKSPACE_CAPACITY,
            Constants.FIRST_WORKSPACE_PROPERTY_LIST);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> workspaceApplicationService.create(command));
  }

  @Test
  void shouldThrowExceptionWhenWorkspaceNameAlreadyExists() {
    // Arrange
    workspaceRepository = new FakeWorkspaceRepository();
    workspaceRepository.save(Constants.getFirstWorkspace());
    reservationRepository = new FakeReservationRepository();
    workspaceApplicationService =
        new WorkspaceApplicationService(workspaceRepository, reservationRepository);

    CreateWorkspaceCommand command =
        new CreateWorkspaceCommand(
            Constants.FIRST_WORKSPACE_NAME, Constants.FIRST_WORKSPACE_OPENING_HOURS,
            Constants.FIRST_WORKSPACE_CAPACITY, Constants.FIRST_WORKSPACE_PROPERTY_LIST);

    // Act & Assert
    assertThrows(
        DuplicateWorkspaceException.class, () -> workspaceApplicationService.create(command));
  }

  @Test
  void shouldThrowExceptionWhenOpeningHoursIsNull() {
    // Arrange
    workspaceRepository = new FakeWorkspaceRepository();
    reservationRepository = new FakeReservationRepository();
    workspaceApplicationService =
        new WorkspaceApplicationService(workspaceRepository, reservationRepository);

    CreateWorkspaceCommand command =
        new CreateWorkspaceCommand(
            "ValidName",
            null,
            Constants.FIRST_WORKSPACE_CAPACITY,
            Constants.FIRST_WORKSPACE_PROPERTY_LIST);

    // Act & Assert
    assertThrows(
        OpeningHoursInvalidException.class, () -> workspaceApplicationService.create(command));
  }

  @Test
  void shouldThrowExceptionWhenCapacityIsZero() {
    // Arrange
    workspaceRepository = new FakeWorkspaceRepository();
    reservationRepository = new FakeReservationRepository();
    workspaceApplicationService =
        new WorkspaceApplicationService(workspaceRepository, reservationRepository);

    CreateWorkspaceCommand command =
        new CreateWorkspaceCommand(
            "ValidName",
            Constants.FIRST_WORKSPACE_OPENING_HOURS,
            0,
            Constants.FIRST_WORKSPACE_PROPERTY_LIST);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> workspaceApplicationService.create(command));
  }

  @Test
  void shouldThrowExceptionWhenCapacityIsNegative() {
    // Arrange
    workspaceRepository = new FakeWorkspaceRepository();
    reservationRepository = new FakeReservationRepository();
    workspaceApplicationService =
        new WorkspaceApplicationService(workspaceRepository, reservationRepository);

    CreateWorkspaceCommand command =
        new CreateWorkspaceCommand(
            "ValidName",
            Constants.FIRST_WORKSPACE_OPENING_HOURS,
            -1,
            Constants.FIRST_WORKSPACE_PROPERTY_LIST);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> workspaceApplicationService.create(command));
  }

  @Test
  void shouldThrowExceptionWhenOpeningHoursIsInvalid() {
    // Arrange
    workspaceRepository = new FakeWorkspaceRepository();
    reservationRepository = new FakeReservationRepository();
    workspaceApplicationService =
        new WorkspaceApplicationService(workspaceRepository, reservationRepository);

    // Create invalid opening hours (you need to check how to make OpeningHours invalid)
    OpeningHours invalidOpeningHours =
        new OpeningHours(
            LocalTime.of(10, 0), LocalTime.of(9, 0) // Close before open = invalid
            );

    CreateWorkspaceCommand command =
        new CreateWorkspaceCommand(
            "ValidName",
            invalidOpeningHours,
            Constants.FIRST_WORKSPACE_CAPACITY,
            Constants.FIRST_WORKSPACE_PROPERTY_LIST);

    // Act & Assert
    assertThrows(
        OpeningHoursInvalidException.class, () -> workspaceApplicationService.create(command));
  }

  @Test
  void shouldFindWorkspacesWithSufficientCapacity() {
    // Arrange
    workspaceRepository = new FakeWorkspaceRepository();
    reservationRepository = new FakeReservationRepository();
    workspaceApplicationService =
        new WorkspaceApplicationService(workspaceRepository, reservationRepository);

    Workspace workspace = Constants.getFirstWorkspace();
    workspaceRepository.save(workspace);

    TimeSlot timeSlot =
        new TimeSlot(
            LocalDateTime.of(
                LocalDate.now(), Constants.FIRST_WORKSPACE_OPENING_HOURS.open().plusMinutes(10)),
            Constants.FIRST_WORKSPACE_OPENING_HOURS.getDuration().minusMinutes(10));

    // Verwenden Sie eine Kapazität kleiner als die des Workspace
    int workspaceCapacity = Constants.FIRST_WORKSPACE_CAPACITY;
    FindAvailableWorkspacesQuery query =
        new FindAvailableWorkspacesQuery(
            timeSlot,
            Optional.of(workspaceCapacity - 1), // Weniger als workspace capacity
            Optional.empty());

    // Act
    List<Workspace> results = workspaceApplicationService.findAvailable(query);

    // Assert
    assertEquals(1, results.size());
  }

  @Test
  void shouldNotFindWorkspacesWithInsufficientCapacity() {
    // Arrange
    workspaceRepository = new FakeWorkspaceRepository();
    reservationRepository = new FakeReservationRepository();
    workspaceApplicationService =
        new WorkspaceApplicationService(workspaceRepository, reservationRepository);

    Workspace workspace = Constants.getFirstWorkspace();
    workspaceRepository.save(workspace);

    TimeSlot timeSlot =
        new TimeSlot(
            LocalDateTime.of(
                LocalDate.now(), Constants.FIRST_WORKSPACE_OPENING_HOURS.open().plusMinutes(10)),
            Constants.FIRST_WORKSPACE_OPENING_HOURS.getDuration().minusMinutes(10));

    FindAvailableWorkspacesQuery query =
        new FindAvailableWorkspacesQuery(
            timeSlot,
            Optional.of(999), // minCapacity höher als workspace capacity
            Optional.empty()); // Keine Properties-Filter

    // Act
    List<Workspace> results = workspaceApplicationService.findAvailable(query);

    // Assert
    assertEquals(0, results.size());
  }

  @Test
  void shouldFilterWorkspacesByAvailableCapacity() {
    // Arrange
    workspaceRepository = new FakeWorkspaceRepository();
    reservationRepository = new FakeReservationRepository();
    workspaceApplicationService =
        new WorkspaceApplicationService(workspaceRepository, reservationRepository);

    Workspace workspace = Constants.getFirstWorkspace();
    workspaceRepository.save(workspace);

    TimeSlot timeSlot =
        new TimeSlot(
            LocalDateTime.of(
                LocalDate.now(), Constants.FIRST_WORKSPACE_OPENING_HOURS.open().plusMinutes(10)),
            Constants.FIRST_WORKSPACE_OPENING_HOURS.getDuration().minusMinutes(10));

    User user = Constants.getFirstUser();

    // Fülle den Workspace bis zur Kapazität mit Reservierungen
    for (int i = 0; i < workspace.getCapacity(); i++) {
      Reservation reservation = new Reservation(workspace.getId(), user.getId(), timeSlot);
      reservationRepository.save(reservation);
    }

    FindAvailableWorkspacesQuery query =
        new FindAvailableWorkspacesQuery(timeSlot, Optional.empty(), Optional.empty());

    // Act
    List<Workspace> results = workspaceApplicationService.findAvailable(query);

    // Assert - Workspace sollte NICHT verfügbar sein
    assertEquals(0, results.size());
  }
}
