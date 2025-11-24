package de.schonvoll.cleanspace.application.doubles.fake;

import de.schonvoll.cleanspace.domain.services.PasswordEncoder;

public class FakePasswordEncoder implements PasswordEncoder {
  @Override
  public String encode(CharSequence rawPassword) {
    return rawPassword.toString();
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return rawPassword.toString().equalsIgnoreCase(encodedPassword);
  }
}
