package de.schonvoll.cleanspace.application.doubles.stub;

import de.schonvoll.cleanspace.domain.entities.Reservation;
import de.schonvoll.cleanspace.domain.repositories.ReservationRepository;
import de.schonvoll.cleanspace.domain.valueobjects.TimeSlot;
import java.util.*;
import lombok.Setter;

@Setter
public class ReservationRepositoryStub implements ReservationRepository {
  private Reservation saveResult;
  private Reservation findByIdResult;
  private List<Reservation> findAllResult = new ArrayList<>();
  private List<Reservation> findByUserIdResult = new ArrayList<>();
  private List<Reservation> findByWorkspaceIdResult = new ArrayList<>();
  private List<Reservation> findConflictingResult = new ArrayList<>();

  private RuntimeException saveException;
  private RuntimeException findByIdException;

  @Override
  public Reservation save(Reservation reservation) {
    if (saveException != null) {
      throw saveException;
    }
    return saveResult != null ? saveResult : reservation;
  }

  @Override
  public Reservation findById(UUID id) {
    if (findByIdException != null) {
      throw findByIdException;
    }
    return findByIdResult;
  }

  @Override
  public List<Reservation> findAll() {
    return findAllResult;
  }

  @Override
  public List<Reservation> findByUserId(UUID userId) {
    return findByUserIdResult;
  }

  @Override
  public List<Reservation> findByWorkspaceId(UUID workspaceId) {
    return findByWorkspaceIdResult;
  }

  @Override
  public List<Reservation> findConflictingWithTimeslot(TimeSlot timeSlot) {
    return findConflictingResult;
  }
}
