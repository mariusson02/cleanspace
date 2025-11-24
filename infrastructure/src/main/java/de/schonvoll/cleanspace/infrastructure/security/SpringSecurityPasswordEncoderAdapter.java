package de.schonvoll.cleanspace.infrastructure.security;

import de.schonvoll.cleanspace.domain.services.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SpringSecurityPasswordEncoderAdapter implements PasswordEncoder {

  private final BCryptPasswordEncoder springEncoder;

  @Override
  public String encode(CharSequence rawPassword) {
    return springEncoder.encode(rawPassword);
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return springEncoder.matches(rawPassword, encodedPassword);
  }
}
