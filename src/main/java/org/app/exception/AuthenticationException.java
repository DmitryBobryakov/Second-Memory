package org.app.exception;

/**
 * Высокоуровневая ошибка, возникает при отрицательной попытке аутентификации.
 *
 * @author Samyrai47
 */
public class AuthenticationException extends RuntimeException {
  public AuthenticationException(String message) {
    super(message);
  }
}
