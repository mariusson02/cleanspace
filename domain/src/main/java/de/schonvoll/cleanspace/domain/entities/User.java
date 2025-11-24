package de.schonvoll.cleanspace.domain.entities;

import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Domain entity representing a user in the system. Contains user authentication and profile
 * information.
 */
@AllArgsConstructor
@Getter
public class User {
  private final UUID id;
  @Setter private String firstName;
  @Setter private String lastName;
  @Setter private String email;
  @Setter private String passwordHash;

  /**
   * Creates a new user without an ID (for new entities).
   *
   * @param firstName the user's first name
   * @param lastName the user's last name
   * @param email the user's email address
   * @param passwordHash the hashed password for authentication
   */
  public User(String firstName, String lastName, String email, String passwordHash) {
    this.id = null;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.passwordHash = passwordHash;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(getId(), user.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
