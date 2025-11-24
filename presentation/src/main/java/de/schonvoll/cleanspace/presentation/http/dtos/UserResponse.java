package de.schonvoll.cleanspace.presentation.http.dtos;

import de.schonvoll.cleanspace.domain.entities.User;
import java.util.UUID;

public record UserResponse(UUID id, String firstName, String lastName, String email) {

  public static UserResponse fromDomain(User user) {
    return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
  }
}
