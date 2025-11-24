package de.schonvoll.cleanspace.domain.exceptions;

public class DuplicateWorkspaceException extends RuntimeException {
  public DuplicateWorkspaceException(String message) {
    super(message);
  }
}
