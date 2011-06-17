package org.hyperpath.persistence.jpa.exceptions;

public class NonexistentEntityException extends Exception {
  private static final long serialVersionUID = -5576374572198762218L;

  public NonexistentEntityException(String message, Throwable cause) {
    super(message, cause);
  }

  public NonexistentEntityException(String message) {
    super(message);
  }
}
