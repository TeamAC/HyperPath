package org.hyperpath.persistence.jpa.exceptions;

public class RollbackFailureException extends Exception {
  private static final long serialVersionUID = 4122255567380743345L;

  public RollbackFailureException(String message, Throwable cause) {
    super(message, cause);
  }

  public RollbackFailureException(String message) {
    super(message);
  }
}
