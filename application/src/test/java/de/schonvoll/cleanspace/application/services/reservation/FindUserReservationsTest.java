package de.schonvoll.cleanspace.application.services.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.schonvoll.cleanspace.application.Constants;
import de.schonvoll.cleanspace.application.commands.CreateReservationCommand;
import de.schonvoll.cleanspace.application.commands.FindUserReservationsQuery;
import de.schonvoll.cleanspace.application.doubles.fake.FakeReservationRepository;
import de.schonvoll.cleanspace.application.doubles.fake.FakeUserRepository;
import de.schonvoll.cleanspace.application.doubles.fake.FakeWorkspaceRepository;
import de.schonvoll.cleanspace.application.services.ReservationApplicationService;
import de.schonvoll.cleanspace.domain.entities.Reservation;
import de.schonvoll.cleanspace.domain.entities.User;
import de.schonvoll.cleanspace.domain.entities.Workspace;
import de.schonvoll.cleanspace.domain.repositories.ReservationRepository;
import de.schonvoll.cleanspace.domain.repositories.UserRepository;
import de.schonvoll.cleanspace.domain.repositories.WorkspaceRepository;
import de.schonvoll.cleanspace.domain.valueobjects.TimeSlot;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FindUserReservationsTest {

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
    User user = Constants.getFirstUser();
    Workspace workspace = Constants.getFirstWorkspace();
    userRepository.save(user);
    workspaceRepository.save(workspace);

    // Multiple reservations for the user
    TimeSlot timeSlot1 =
        new TimeSlot(
            LocalDateTime.of(
                LocalDate.now(), Constants.FIRST_WORKSPACE_OPENING_HOURS.open().plusHours(1)),
            Duration.ofHours(1));
    TimeSlot timeSlot2 =
        new TimeSlot(
            LocalDateTime.of(
                LocalDate.now().plusDays(1),
                Constants.FIRST_WORKSPACE_OPENING_HOURS.open().plusHours(1)),
            Duration.ofHours(1));

    CreateReservationCommand command1 =
        new CreateReservationCommand(
            Constants.FIRST_WORKSPACE_NAME, Constants.FIRST_USER_EMAIL, timeSlot1);
    CreateReservationCommand command2 =
        new CreateReservationCommand(
            Constants.FIRST_WORKSPACE_NAME, Constants.FIRST_USER_EMAIL, timeSlot2);

    Reservation reservation1 = reservationApplicationService.create(command1);
    Reservation reservation2 = reservationApplicationService.create(command2);

    FindUserReservationsQuery query = new FindUserReservationsQuery(Constants.FIRST_USER_EMAIL);

    // Act
    List<Reservation> userReservations = reservationApplicationService.findByUser(query);

    // Assert
    assertEquals(2, userReservations.size());
    assertTrue(userReservations.contains(reservation1));
    assertTrue(userReservations.contains(reservation2));
  }

  @Test
  void testNoReservations() {
    // Arrange
    User user = Constants.getFirstUser();
    userRepository.save(user);

    FindUserReservationsQuery query = new FindUserReservationsQuery(Constants.FIRST_USER_EMAIL);

    // Act
    List<Reservation> userReservations = reservationApplicationService.findByUser(query);

    // Assert
    assertTrue(userReservations.isEmpty());
  }

  @Test
  void testUserNotFound() {
    // Arrange
    String nonExistentEmail = "nonexistent@example.com";
    FindUserReservationsQuery query = new FindUserReservationsQuery(nonExistentEmail);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> reservationApplicationService.findByUser(query));
  }

  @Test
  void testOnlyUserSpecificReservations() {
    // Arrange
    User user1 = Constants.getFirstUser();
    User user2 = Constants.getSecondUser();
    Workspace workspace = Constants.getFirstWorkspace();

    userRepository.save(user1);
    userRepository.save(user2);
    workspaceRepository.save(workspace);

    // Create reservations for both users
    TimeSlot timeSlot1 =
        new TimeSlot(
            LocalDateTime.of(
                LocalDate.now(), Constants.FIRST_WORKSPACE_OPENING_HOURS.open().plusHours(1)),
            Duration.ofHours(1));
    TimeSlot timeSlot2 =
        new TimeSlot(
            LocalDateTime.of(
                LocalDate.now().plusDays(1),
                Constants.FIRST_WORKSPACE_OPENING_HOURS.open().plusHours(1)),
            Duration.ofHours(1));

    CreateReservationCommand command1 =
        new CreateReservationCommand(
            Constants.FIRST_WORKSPACE_NAME, Constants.FIRST_USER_EMAIL, timeSlot1);
    CreateReservationCommand command2 =
        new CreateReservationCommand(
            Constants.FIRST_WORKSPACE_NAME, Constants.SECOND_USER_EMAIL, timeSlot2);

    Reservation user1Reservation = reservationApplicationService.create(command1);
    Reservation user2Reservation = reservationApplicationService.create(command2);

    FindUserReservationsQuery query = new FindUserReservationsQuery(Constants.FIRST_USER_EMAIL);

    // Act
    List<Reservation> userReservations = reservationApplicationService.findByUser(query);

    // Assert
    assertEquals(1, userReservations.size());
    assertTrue(userReservations.contains(user1Reservation));
    assertTrue(!userReservations.contains(user2Reservation));
  }

  @Test
  void testNullEmail() {
    // Arrange
    FindUserReservationsQuery query = new FindUserReservationsQuery(null);

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> reservationApplicationService.findByUser(query));
  }

  @Test
  void testEmptyEmail() {
    // Arrange
    FindUserReservationsQuery query = new FindUserReservationsQuery("");

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> reservationApplicationService.findByUser(query));
  }
}
