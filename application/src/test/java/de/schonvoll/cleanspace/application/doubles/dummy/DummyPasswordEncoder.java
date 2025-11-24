package de.schonvoll.cleanspace.application.doubles.dummy;

import de.schonvoll.cleanspace.domain.services.PasswordEncoder;

public class DummyPasswordEncoder implements PasswordEncoder {
  @Override
  public String encode(CharSequence rawPassword) {
    return "";
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return false;
  }
}
