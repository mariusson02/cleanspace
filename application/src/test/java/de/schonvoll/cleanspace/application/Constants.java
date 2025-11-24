package de.schonvoll.cleanspace.application;

import de.schonvoll.cleanspace.domain.entities.User;
import de.schonvoll.cleanspace.domain.entities.Workspace;
import de.schonvoll.cleanspace.domain.valueobjects.OpeningHours;
import de.schonvoll.cleanspace.domain.valueobjects.WorkspaceProperty;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class Constants {

  public static String FIRST_PROPERTY_KEY = "Parking";
  public static String FIRST_PROPERTY_VALUE = "Free";

  public static WorkspaceProperty getFirstWorkspaceProperty() {
    return new WorkspaceProperty(FIRST_PROPERTY_KEY, FIRST_PROPERTY_VALUE);
  }

  public static UUID FIRST_WORKSPACE_ID = UUID.randomUUID();
  public static String FIRST_WORKSPACE_NAME = "FIRST_WORKSPACE_NAME";
  public static OpeningHours FIRST_WORKSPACE_OPENING_HOURS =
      new OpeningHours(LocalTime.of(10, 0, 0), LocalTime.of(18, 0, 0));
  public static int FIRST_WORKSPACE_CAPACITY = 1;
  public static List<WorkspaceProperty> FIRST_WORKSPACE_PROPERTY_LIST =
      List.of(getFirstWorkspaceProperty());

  public static Workspace getFirstWorkspace() {
    return Workspace.builder()
        .id(FIRST_WORKSPACE_ID)
        .name(FIRST_WORKSPACE_NAME)
        .openingHours(FIRST_WORKSPACE_OPENING_HOURS)
        .capacity(FIRST_WORKSPACE_CAPACITY)
        .properties(FIRST_WORKSPACE_PROPERTY_LIST)
        .build();
  }

  public static UUID FIRST_USER_ID = UUID.randomUUID();
  public static String FIRST_USER_FIRST_NAME = "John";
  public static String FIRST_USER_LAST_NAME = "Doe";
  public static String FIRST_USER_EMAIL = "john.doe@example.com";
  public static String FIRST_USER_PASSWORD = "securePassword123";

  public static User getFirstUser() {
    return new User(
        FIRST_USER_ID,
        FIRST_USER_FIRST_NAME,
        FIRST_USER_LAST_NAME,
        FIRST_USER_EMAIL,
        FIRST_USER_PASSWORD);
  }

  public static UUID SECOND_USER_ID = UUID.randomUUID();
  public static String SECOND_USER_FIRST_NAME = "Jane";
  public static String SECOND_USER_LAST_NAME = "Smith";
  public static String SECOND_USER_EMAIL = "jane.smith@example.com";
  public static String SECOND_USER_PASSWORD = "anotherPassword456";

  public static User getSecondUser() {
    return new User(
        SECOND_USER_ID,
        SECOND_USER_FIRST_NAME,
        SECOND_USER_LAST_NAME,
        SECOND_USER_EMAIL,
        SECOND_USER_PASSWORD);
  }
}
