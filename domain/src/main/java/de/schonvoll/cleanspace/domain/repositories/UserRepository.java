package de.schonvoll.cleanspace.domain.repositories;

import de.schonvoll.cleanspace.domain.entities.User;
import java.util.List;
import java.util.UUID;

public interface UserRepository {
  /**
   * Saves a user to the repository.
   *
   * @param user the {@link User} to save
   * @return the saved user with generated ID if it was null
   */
  User save(User user);

  /**
   * Finds a user by their unique identifier.
   *
   * @param id the unique identifier of the user
   * @return the user with the given ID, or null if not found
   */
  User findById(UUID id);

  /**
   * Finds a user by their email address.
   *
   * @param email the email address of the user
   * @return the user with the given email, or null if not found
   */
  User findByEmail(String email);

  /**
   * Retrieves all users from the repository.
   *
   * @return list of all users
   */
  List<User> findAll();
}
