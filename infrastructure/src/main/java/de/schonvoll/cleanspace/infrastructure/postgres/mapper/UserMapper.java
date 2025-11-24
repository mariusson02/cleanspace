package de.schonvoll.cleanspace.infrastructure.postgres.mapper;

import de.schonvoll.cleanspace.domain.entities.User;
import de.schonvoll.cleanspace.infrastructure.postgres.entities.UserJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Maps between User domain entities and UserJpaEntity persistence objects. Handles bidirectional
 * conversion for database operations.
 */
@Component
public class UserMapper {

  /**
   * Converts a JPA entity to a domain entity.
   *
   * @param jpaEntity the JPA entity to convert
   * @return the corresponding domain entity
   */
  public static User toDomain(UserJpaEntity jpaEntity) {
    return new User(
        jpaEntity.getId(),
        jpaEntity.getFirstName(),
        jpaEntity.getLastName(),
        jpaEntity.getEmail(),
        jpaEntity.getPasswordHash());
  }

  /**
   * Converts a domain entity to a JPA entity.
   *
   * @param domainEntity the domain entity to convert
   * @return the corresponding JPA entity
   */
  public static UserJpaEntity toJpaEntity(User domainEntity) {
    UserJpaEntity jpaEntity = new UserJpaEntity();
    jpaEntity.setId(domainEntity.getId());
    jpaEntity.setFirstName(domainEntity.getFirstName());
    jpaEntity.setLastName(domainEntity.getLastName());
    jpaEntity.setEmail(domainEntity.getEmail());
    jpaEntity.setPasswordHash(domainEntity.getPasswordHash());
    return jpaEntity;
  }
}
