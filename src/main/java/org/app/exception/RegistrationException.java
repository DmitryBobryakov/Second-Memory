package org.app.exception;

/**
 * Высокоуровневая ошибка, возникает при попытке зарегистрировать существующего пользователя.
 *
 * @author Samyrai47
 */
public class RegistrationException extends RuntimeException {
  public RegistrationException(String message) {
    super(message);
  }
}
