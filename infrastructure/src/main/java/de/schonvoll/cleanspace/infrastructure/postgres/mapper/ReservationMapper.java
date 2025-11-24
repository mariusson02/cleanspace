package de.schonvoll.cleanspace.infrastructure.postgres.mapper;

import de.schonvoll.cleanspace.domain.entities.Reservation;
import de.schonvoll.cleanspace.domain.valueobjects.TimeSlot;
import de.schonvoll.cleanspace.infrastructure.postgres.entities.ReservationJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Maps between Reservation domain entities and ReservationJpaEntity persistence objects. Handles
 * bidirectional conversion including TimeSlot value object decomposition.
 */
@Component
public class ReservationMapper {

  /**
   * Converts a JPA entity to a domain entity.
   *
   * @param jpaEntity the JPA entity to convert
   * @return the corresponding domain entity
   */
  public static Reservation toDomain(ReservationJpaEntity jpaEntity) {
    return new Reservation(
        jpaEntity.getId(),
        jpaEntity.getWorkspaceId(),
        jpaEntity.getUserId(),
        new TimeSlot(jpaEntity.getStartTime(), jpaEntity.getDuration()));
  }

  /**
   * Converts a domain entity to a JPA entity.
   *
   * @param domainEntity the domain entity to convert
   * @return the corresponding JPA entity
   */
  public static ReservationJpaEntity toJpaEntity(Reservation domainEntity) {
    ReservationJpaEntity jpaEntity = new ReservationJpaEntity();
    jpaEntity.setId(domainEntity.getId());
    jpaEntity.setWorkspaceId(domainEntity.getWorkspaceId());
    jpaEntity.setUserId(domainEntity.getUserId());
    jpaEntity.setStartTime(domainEntity.getTimeSlot().start());
    jpaEntity.setDuration(domainEntity.getTimeSlot().duration());
    return jpaEntity;
  }
}
