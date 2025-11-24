package de.schonvoll.cleanspace.domain.exceptions;

public class WorkspaceNotFoundException extends RuntimeException {
  public WorkspaceNotFoundException(String message) {
    super(message);
  }
}
