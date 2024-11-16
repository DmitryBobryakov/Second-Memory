package org.app.exception;

/**
 * Низкоуровневая ошибка, возникающая, если регистрирующийся пользователь уже существует.
 *
 * @author Samyrai47
 */
public class UserAlreadyExistsException extends RuntimeException {
  public UserAlreadyExistsException(String message) {
    super(message);
  }
}
