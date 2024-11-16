package org.app.exception;

/**
 * Низкоуровневая ошибка, возникающая, если при попытке аутентификации пользователь не был найден в
 * БД.
 *
 * @author Samyrai47
 */
public class NoSuchUserException extends RuntimeException {
  public NoSuchUserException(String message) {
    super(message);
  }
}
