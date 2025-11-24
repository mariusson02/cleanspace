package de.schonvoll.cleanspace.infrastructure.postgres.repositories;

import de.schonvoll.cleanspace.domain.entities.Reservation;
import de.schonvoll.cleanspace.domain.repositories.ReservationRepository;
import de.schonvoll.cleanspace.domain.valueobjects.TimeSlot;
import de.schonvoll.cleanspace.infrastructure.postgres.entities.ReservationJpaEntity;
import de.schonvoll.cleanspace.infrastructure.postgres.jpa.ReservationJpaRepository;
import de.schonvoll.cleanspace.infrastructure.postgres.mapper.ReservationMapper;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 * PostgreSQL implementation of {@link ReservationRepository} using JPA. Delegates to {@link
 * ReservationJpaRepository} and handles domain-persistence mapping via {@link ReservationMapper}.
 */
@Repository
@Profile("postgres")
@AllArgsConstructor
public class ReservationPostgresRepository implements ReservationRepository {
  private final ReservationJpaRepository jpaRepository;

  /** {@inheritDoc} */
  @Override
  public Reservation save(Reservation reservation) {
    ReservationJpaEntity savedJpaReservation =
        jpaRepository.save(ReservationMapper.toJpaEntity(reservation));
    return ReservationMapper.toDomain(savedJpaReservation);
  }

  /** {@inheritDoc} */
  @Override
  public Reservation findById(UUID id) {
    return jpaRepository.findById(id).map(ReservationMapper::toDomain).orElse(null);
  }

  /** {@inheritDoc} */
  @Override
  public List<Reservation> findAll() {
    return jpaRepository.findAll().stream().map(ReservationMapper::toDomain).toList();
  }

  /** {@inheritDoc} */
  @Override
  public List<Reservation> findByUserId(UUID userId) {
    return jpaRepository.findByUserId(userId).stream().map(ReservationMapper::toDomain).toList();
  }

  /** {@inheritDoc} */
  @Override
  public List<Reservation> findByWorkspaceId(UUID workspaceId) {
    return jpaRepository.findByWorkspaceId(workspaceId).stream()
        .map(ReservationMapper::toDomain)
        .toList();
  }

  /** {@inheritDoc} */
  @Override
  public List<Reservation> findConflictingWithTimeslot(TimeSlot timeSlot) {
    List<ReservationJpaEntity> conflicting =
        jpaRepository.findConflictingWithTimeslot(timeSlot.start(), timeSlot.end());
    return conflicting.stream().map(ReservationMapper::toDomain).toList();
  }
}
