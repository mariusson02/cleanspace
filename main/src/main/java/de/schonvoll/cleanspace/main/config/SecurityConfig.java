package de.schonvoll.cleanspace.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for CleanSpace workspace management.
 *
 * <p>This implementation provides a simplified security setup suitable for the current project
 * requirements, with CSRF protection disabled and unrestricted endpoint access.
 */
@Configuration
public class SecurityConfig {

  /**
   * Configures the security filter chain for development and testing purposes. Permits all requests
   * to ensure HTTP tests run without authentication barriers.
   *
   * <p>Note: This open configuration is intentional for this university project.
   *
   * @param http the HttpSecurity to configure
   * @return configured SecurityFilterChain
   * @throws Exception if configuration fails
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
    return http.build();
  }

  @Bean
  public BCryptPasswordEncoder springSecurityPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
