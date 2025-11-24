package de.schonvoll.cleanspace.infrastructure.postgres.repositories;

import de.schonvoll.cleanspace.domain.entities.User;
import de.schonvoll.cleanspace.domain.repositories.UserRepository;
import de.schonvoll.cleanspace.infrastructure.postgres.entities.UserJpaEntity;
import de.schonvoll.cleanspace.infrastructure.postgres.jpa.UserJpaRepository;
import de.schonvoll.cleanspace.infrastructure.postgres.mapper.UserMapper;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 * PostgreSQL implementation of UserRepository using JPA. Delegates to UserJpaRepository and handles
 * domain-persistence mapping via UserMapper.
 */
@Repository
@Profile("postgres")
@AllArgsConstructor
public class UserPostgresRepository implements UserRepository {
  private final UserJpaRepository jpaRepository;

  /** {@inheritDoc} */
  @Override
  public User save(User user) {
    UserJpaEntity savedJpaUser = jpaRepository.save(UserMapper.toJpaEntity(user));
    return UserMapper.toDomain(savedJpaUser);
  }

  /** {@inheritDoc} */
  @Override
  public User findById(UUID id) {
    return jpaRepository.findById(id).stream().map(UserMapper::toDomain).findFirst().orElse(null);
  }

  /** {@inheritDoc} */
  @Override
  public User findByEmail(String email) {
    UserJpaEntity foundUser = jpaRepository.findByEmail(email);
    return foundUser == null ? null : UserMapper.toDomain(foundUser);
  }

  /** {@inheritDoc} */
  @Override
  public List<User> findAll() {
    return jpaRepository.findAll().stream().map(UserMapper::toDomain).toList();
  }
}
