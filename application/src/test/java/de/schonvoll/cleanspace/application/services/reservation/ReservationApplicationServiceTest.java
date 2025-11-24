package de.schonvoll.cleanspace.application.services.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.schonvoll.cleanspace.application.Constants;
import de.schonvoll.cleanspace.application.commands.CreateReservationCommand;
import de.schonvoll.cleanspace.application.doubles.fake.FakeReservationRepository;
import de.schonvoll.cleanspace.application.doubles.fake.FakeUserRepository;
import de.schonvoll.cleanspace.application.doubles.fake.FakeWorkspaceRepository;
import de.schonvoll.cleanspace.application.services.ReservationApplicationService;
import de.schonvoll.cleanspace.domain.entities.Reservation;
import de.schonvoll.cleanspace.domain.exceptions.DuplicateReservationException;
import de.schonvoll.cleanspace.domain.exceptions.WorkspaceFullException;
import de.schonvoll.cleanspace.domain.exceptions.WorkspaceNotFoundException;
import de.schonvoll.cleanspace.domain.repositories.ReservationRepository;
import de.schonvoll.cleanspace.domain.repositories.UserRepository;
import de.schonvoll.cleanspace.domain.repositories.WorkspaceRepository;
import de.schonvoll.cleanspace.domain.valueobjects.TimeSlot;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservationApplicationServiceTest {

  private ReservationRepository reservationRepository;
  private WorkspaceRepository workspaceRepository;
  private UserRepository userRepository;

  private ReservationApplicationService reservationApplicationService;

  @BeforeEach
  void setUp() {
    reservationRepository = new FakeReservationRepository();
    workspaceRepository = new FakeWorkspaceRepository();
    userRepository = new FakeUserRepository();
    reservationApplicationService =
        new ReservationApplicationService(
            reservationRepository, workspaceRepository, userRepository);
  }

  @AfterEach
  void tearDown() {
    reservationRepository = null;
    workspaceRepository = null;
    userRepository = null;
    reservationApplicationService = null;
  }

  @Test
  void testHappyPath() {
    // Arrange
    workspaceRepository.save(Constants.getFirstWorkspace());
    userRepository.save(Constants.getFirstUser());
    TimeSlot timeSlot =
        new TimeSlot(
            LocalDateTime.of(
                LocalDate.now(), Constants.FIRST_WORKSPACE_OPENING_HOURS.open().plusHours(1)),
            Duration.ofHours(1));
    CreateReservationCommand command =
        new CreateReservationCommand(
            Constants.FIRST_WORKSPACE_NAME, Constants.FIRST_USER_EMAIL, timeSlot);

    // Act
    Reservation created = reservationApplicationService.create(command);

    // Assert
    var found = reservationRepository.findById(created.getId());
    assertEquals(created, found);
  }

  @Test
  void testInvalidWorkspace() {
    // Arrange
    userRepository.save(Constants.getFirstUser());
    TimeSlot timeSlot =
        new TimeSlot(
            LocalDateTime.of(
                LocalDate.now(), Constants.FIRST_WORKSPACE_OPENING_HOURS.open().plusHours(1)),
            Duration.ofHours(1));
    CreateReservationCommand command =
        new CreateReservationCommand(
            Constants.FIRST_WORKSPACE_NAME, Constants.FIRST_USER_EMAIL, timeSlot);

    // Act
    // Assert
    assertThrows(
        WorkspaceNotFoundException.class, () -> reservationApplicationService.create(command));
  }

  @Test
  void testInvalidUser() throws IllegalArgumentException {
    // Arrange
    workspaceRepository.save(Constants.getFirstWorkspace());
    TimeSlot timeSlot =
        new TimeSlot(
            LocalDateTime.of(
                LocalDate.now(), Constants.FIRST_WORKSPACE_OPENING_HOURS.open().plusHours(1)),
            Duration.ofHours(1));
    CreateReservationCommand command =
        new CreateReservationCommand(
            Constants.FIRST_WORKSPACE_NAME, Constants.FIRST_USER_EMAIL, timeSlot);

    // Act
    // Assert
    assertThrows(
        IllegalArgumentException.class, () -> reservationApplicationService.create(command));
  }

  @Test
  void testInvalidTimeSlot() throws IllegalArgumentException {
    // Arrange
    workspaceRepository.save(Constants.getFirstWorkspace());
    userRepository.save(Constants.getFirstUser());
    TimeSlot invalidTimeSlot =
        new TimeSlot(
            LocalDateTime.of(
                LocalDate.now(), Constants.FIRST_WORKSPACE_OPENING_HOURS.open().plusHours(8)),
            Duration.ofHours(100));
    CreateReservationCommand command =
        new CreateReservationCommand(
            Constants.FIRST_WORKSPACE_NAME, Constants.FIRST_USER_EMAIL, invalidTimeSlot);

    // Act
    // Assert
    assertThrows(
        IllegalArgumentException.class, () -> reservationApplicationService.create(command));
  }

  @Test
  void testThrowsWorkspaceFullException() throws WorkspaceFullException {
    // Arrange
    workspaceRepository.save(Constants.getFirstWorkspace());
    userRepository.save(Constants.getFirstUser());
    userRepository.save(Constants.getSecondUser());

    TimeSlot timeSlot =
        new TimeSlot(
            LocalDateTime.of(
                LocalDate.now(), Constants.FIRST_WORKSPACE_OPENING_HOURS.open().plusHours(1)),
            Duration.ofHours(1));

    CreateReservationCommand commandOne =
        new CreateReservationCommand(
            Constants.FIRST_WORKSPACE_NAME, Constants.FIRST_USER_EMAIL, timeSlot);

    CreateReservationCommand commandTwo =
        new CreateReservationCommand(
            Constants.FIRST_WORKSPACE_NAME, Constants.SECOND_USER_EMAIL, timeSlot);

    reservationApplicationService.create(commandOne);

    // Act
    // Assert
    assertThrows(
        WorkspaceFullException.class, () -> reservationApplicationService.create(commandTwo));
  }

  @Test
  void shouldThrowExceptionWhenUserAlreadyHasReservationInTimeSlot() {
    // Arrange
    workspaceRepository.save(Constants.getFirstWorkspace());
    userRepository.save(Constants.getFirstUser());

    // Create first reservation
    TimeSlot firstTimeSlot =
        new TimeSlot(
            LocalDateTime.of(
                LocalDate.now(), Constants.FIRST_WORKSPACE_OPENING_HOURS.open().plusHours(1)),
            Duration.ofHours(1));
    CreateReservationCommand firstCommand =
        new CreateReservationCommand(
            Constants.FIRST_WORKSPACE_NAME, Constants.FIRST_USER_EMAIL, firstTimeSlot);

    reservationApplicationService.create(firstCommand);

    // Try to create overlapping reservation for same user
    TimeSlot overlappingTimeSlot =
        new TimeSlot(
            LocalDateTime.of(
                LocalDate.now(), Constants.FIRST_WORKSPACE_OPENING_HOURS.open().plusHours(1)),
            Duration.ofHours(1));
    CreateReservationCommand overlappingCommand =
        new CreateReservationCommand(
            Constants.FIRST_WORKSPACE_NAME, Constants.FIRST_USER_EMAIL, overlappingTimeSlot);

    // Act & Assert
    assertThrows(
        DuplicateReservationException.class,
        () -> reservationApplicationService.create(overlappingCommand));
  }
}
