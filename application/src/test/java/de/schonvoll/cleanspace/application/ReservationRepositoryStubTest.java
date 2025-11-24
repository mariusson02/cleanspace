package de.schonvoll.cleanspace.application;

import static org.junit.jupiter.api.Assertions.*;

import de.schonvoll.cleanspace.application.doubles.stub.ReservationRepositoryStub;
import de.schonvoll.cleanspace.domain.entities.Reservation;
import de.schonvoll.cleanspace.domain.valueobjects.TimeSlot;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationRepositoryStubTest {

  private ReservationRepositoryStub repository;

  @BeforeEach
  void setUp() {
    repository = new ReservationRepositoryStub();
  }

  @Test
  void shouldReturnConfiguredSaveResult() {
    Reservation reservation = createTestReservation();
    Reservation expectedResult = createTestReservation();

    repository.setSaveResult(expectedResult);

    Reservation result = repository.save(reservation);

    assertEquals(expectedResult, result);
  }

  @Test
  void shouldReturnConfiguredFindByIdResult() {
    UUID id = UUID.randomUUID();
    Reservation expectedReservation = createTestReservation();

    repository.setFindByIdResult(expectedReservation);

    Reservation result = repository.findById(id);

    assertEquals(expectedReservation, result);
  }

  @Test
  void shouldReturnConfiguredFindAllResult() {
    List<Reservation> expectedReservations =
        List.of(createTestReservation(), createTestReservation(), createTestReservation());

    repository.setFindAllResult(expectedReservations);

    List<Reservation> result = repository.findAll();

    assertEquals(expectedReservations, result);
  }

  @Test
  void shouldReturnConfiguredFindByUserIdResult() {
    UUID userId = UUID.randomUUID();
    List<Reservation> expectedReservations =
        List.of(createTestReservation(), createTestReservation());

    repository.setFindByUserIdResult(expectedReservations);

    List<Reservation> result = repository.findByUserId(userId);

    assertEquals(expectedReservations, result);
  }

  @Test
  void shouldReturnConfiguredFindByWorkspaceIdResult() {
    UUID workspaceId = UUID.randomUUID();
    List<Reservation> expectedReservations =
        List.of(createTestReservation(), createTestReservation());

    repository.setFindByWorkspaceIdResult(expectedReservations);

    List<Reservation> result = repository.findByWorkspaceId(workspaceId);

    assertEquals(expectedReservations, result);
  }

  @Test
  void shouldReturnConfiguredConflictingResult() {
    TimeSlot timeSlot = new TimeSlot(LocalDateTime.now(), Duration.ofHours(1));
    List<Reservation> expectedConflicting = List.of(createTestReservation());

    repository.setFindConflictingResult(expectedConflicting);

    List<Reservation> result = repository.findConflictingWithTimeslot(timeSlot);

    assertEquals(expectedConflicting, result);
  }

  @Test
  void shouldThrowConfiguredSaveException() {
    RuntimeException expectedException = new RuntimeException("Save exception");

    repository.setSaveException(expectedException);

    assertThrows(RuntimeException.class, () -> repository.save(createTestReservation()));
  }

  @Test
  void shouldThrowConfiguredFindByIdException() {
    UUID id = UUID.randomUUID();
    RuntimeException expectedException = new RuntimeException("FindById exception");

    repository.setFindByIdException(expectedException);

    assertThrows(RuntimeException.class, () -> repository.findById(id));
  }

  @Test
  void shouldReturnNullByDefault() {
    UUID id = UUID.randomUUID();

    Reservation result = repository.findById(id);

    assertNull(result);
  }

  @Test
  void shouldReturnEmptyFindAllByDefault() {
    List<Reservation> result = repository.findAll();

    assertTrue(result.isEmpty());
  }

  @Test
  void shouldReturnEmptyFindByUserIdByDefault() {
    UUID userId = UUID.randomUUID();

    List<Reservation> result = repository.findByUserId(userId);

    assertTrue(result.isEmpty());
  }

  @Test
  void shouldReturnEmptyFindByWorkspaceIdByDefault() {
    UUID workspaceId = UUID.randomUUID();

    List<Reservation> result = repository.findByWorkspaceId(workspaceId);

    assertTrue(result.isEmpty());
  }

  @Test
  void shouldReturnEmptyFindConflictingByDefault() {
    TimeSlot timeSlot = new TimeSlot(LocalDateTime.now(), Duration.ofHours(1));

    List<Reservation> result = repository.findConflictingWithTimeslot(timeSlot);

    assertTrue(result.isEmpty());
  }

  private Reservation createTestReservation() {
    return new Reservation(
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        new TimeSlot(LocalDateTime.now(), Duration.ofHours(2)));
  }
}
