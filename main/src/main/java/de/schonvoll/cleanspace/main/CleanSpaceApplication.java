package de.schonvoll.cleanspace.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
    basePackages = {
      "de.schonvoll.cleanspace.infrastructure",
      "de.schonvoll.cleanspace.presentation",
      "de.schonvoll.cleanspace.main"
    })
public class CleanSpaceApplication {

  public static void main(String[] args) {
    SpringApplication.run(CleanSpaceApplication.class, args);
  }
}
