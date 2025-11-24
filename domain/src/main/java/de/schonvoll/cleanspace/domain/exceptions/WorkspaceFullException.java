package de.schonvoll.cleanspace.domain.exceptions;

public class WorkspaceFullException extends RuntimeException {
  public WorkspaceFullException(String message) {
    super(message);
  }
}
