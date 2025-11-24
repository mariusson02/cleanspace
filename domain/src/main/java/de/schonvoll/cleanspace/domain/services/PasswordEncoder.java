package de.schonvoll.cleanspace.domain.services;

public interface PasswordEncoder {
  String encode(CharSequence rawPassword);

  boolean matches(CharSequence rawPassword, String encodedPassword);
}
