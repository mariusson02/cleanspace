package de.schonvoll.cleanspace.infrastructure.postgres.jpa;

import de.schonvoll.cleanspace.infrastructure.postgres.entities.ReservationJpaEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationJpaRepository extends JpaRepository<ReservationJpaEntity, UUID> {
  List<ReservationJpaEntity> findByUserId(UUID userId);

  List<ReservationJpaEntity> findByWorkspaceId(UUID workspaceId);

  @Query(
      value =
          "SELECT * FROM reservations r "
              + "WHERE (r.start_time, r.start_time + (r.duration / 1000000000.0) * interval '1 second') "
              + "OVERLAPS (:queryStart, :queryEnd)",
      nativeQuery = true)
  List<ReservationJpaEntity> findConflictingWithTimeslot(
      @Param("queryStart") LocalDateTime queryStart, @Param("queryEnd") LocalDateTime queryEnd);
}
