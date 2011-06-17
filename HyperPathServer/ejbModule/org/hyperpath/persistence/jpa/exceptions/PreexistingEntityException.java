package org.hyperpath.persistence.jpa.exceptions;

public class PreexistingEntityException extends Exception {
  private static final long serialVersionUID = -283890342665693113L;

  public PreexistingEntityException(String message, Throwable cause) {
    super(message, cause);
  }

  public PreexistingEntityException(String message) {
    super(message);
  }
}
