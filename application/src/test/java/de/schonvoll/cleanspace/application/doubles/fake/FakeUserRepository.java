package de.schonvoll.cleanspace.application.doubles.fake;

import de.schonvoll.cleanspace.domain.entities.User;
import de.schonvoll.cleanspace.domain.repositories.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FakeUserRepository implements UserRepository {
  private final HashMap<UUID, User> userHashMap = new HashMap<>();

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

  @Override
  public User findById(UUID id) {
    return userHashMap.get(id);
  }

  @Override
  public User findByEmail(String email) {
    return userHashMap.values().stream()
        .filter(user -> user.getEmail().equalsIgnoreCase(email))
        .findFirst()
        .orElse(null);
  }

  @Override
  public List<User> findAll() {
    return userHashMap.values().stream().toList();
  }
}
