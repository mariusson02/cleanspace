package de.schonvoll.cleanspace.main.config;

import de.schonvoll.cleanspace.domain.entities.User;
import de.schonvoll.cleanspace.domain.repositories.UserRepository;
import de.schonvoll.cleanspace.domain.repositories.WorkspaceRepository;
import de.schonvoll.cleanspace.domain.services.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Initializes default data on application startup. Creates a default admin account if none exists.
 * Disabled during tests.
 */
@Configuration
@Slf4j
@Profile("!test") // Deaktiviert den Runner während der Tests
public class DataInitializer {

  /**
   * Creates a default admin account on startup if no admin exists. Logs the default credentials for
   * development purposes.
   *
   * @param userRepository repository to check/save users
   * @param workspaceRepository workspace repository (injected for potential future use)
   * @param passwordEncoder service to encode the default password
   * @return CommandLineRunner that executes the initialization
   */
  @Bean
  CommandLineRunner initDatabase(
      UserRepository userRepository,
      WorkspaceRepository workspaceRepository,
      PasswordEncoder passwordEncoder) {
    return args -> {
      String email = "admin@cleanspace.de";
      if (userRepository.findByEmail(email) == null) {
        String name = "admin";
        String password = "password";
        String encodedPassword = passwordEncoder.encode(password);
        User admin = new User(name, name, email, encodedPassword);

        userRepository.save(admin);
        log.info("Created default admin account:\nE-Mail: {} \nPassword: {}", email, password);
      }
    };
  }
}
