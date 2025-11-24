package de.schonvoll.cleanspace.infrastructure.inmemory;

import de.schonvoll.cleanspace.domain.entities.User;
import de.schonvoll.cleanspace.domain.repositories.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 * In-memory implementation of UserRepository for development and testing. Uses a HashMap to store
 * User entities.
 */
@Repository
@Profile("in-memory")
public class InMemoryUserRepository implements UserRepository {
  private final HashMap<UUID, User> userHashMap;

  public InMemoryUserRepository() {
    this.userHashMap = new HashMap<>();
  }

  /** {@inheritDoc} */
  @Override
  public User save(User user) {
    if (user.getId() == null) {
      User persistedUser =
          new User(
              UUID.randomUUID(),
              user.getFirstName(),
              user.getLastName(),
              user.getEmail(),
              user.getPasswordHash());
      userHashMap.put(persistedUser.getId(), persistedUser);
      return persistedUser;
    }
    userHashMap.put(user.getId(), user);
    return user;
  }

  /** {@inheritDoc} */
  @Override
  public User findById(UUID id) {
    return userHashMap.get(id);
  }

  /** {@inheritDoc} */
  @Override
  public User findByEmail(String email) {
    return userHashMap.values().stream()
        .filter(user -> user.getEmail().equalsIgnoreCase(email))
        .findFirst()
        .orElse(null);
  }

  /** {@inheritDoc} */
  @Override
  public List<User> findAll() {
    return userHashMap.values().stream().toList();
  }
}
