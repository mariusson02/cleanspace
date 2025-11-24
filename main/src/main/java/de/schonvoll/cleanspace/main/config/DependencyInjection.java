package de.schonvoll.cleanspace.main.config;

import de.schonvoll.cleanspace.application.services.ReservationApplicationService;
import de.schonvoll.cleanspace.application.services.UserApplicationService;
import de.schonvoll.cleanspace.application.services.WorkspaceApplicationService;
import de.schonvoll.cleanspace.domain.repositories.ReservationRepository;
import de.schonvoll.cleanspace.domain.repositories.UserRepository;
import de.schonvoll.cleanspace.domain.repositories.WorkspaceRepository;
import de.schonvoll.cleanspace.domain.services.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DependencyInjection {

  @Bean
  public ReservationApplicationService reservationApplicationService(
      ReservationRepository reservationRepository,
      WorkspaceRepository workspaceRepository,
      UserRepository userRepository) {
    return new ReservationApplicationService(
        reservationRepository, workspaceRepository, userRepository);
  }

  @Bean
  public UserApplicationService userApplicationService(
      UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return new UserApplicationService(userRepository, passwordEncoder);
  }

  @Bean
  public WorkspaceApplicationService workspaceApplicationService(
      WorkspaceRepository workspaceRepository, ReservationRepository reservationRepository) {
    return new WorkspaceApplicationService(workspaceRepository, reservationRepository);
  }
}
