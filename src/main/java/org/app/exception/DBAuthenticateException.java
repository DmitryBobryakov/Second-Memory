package org.app.exception;

/**
 * Низкоуровневая ошибка, возникающая если при попытке аутентифицировать пользователя БД выбросила
 * ошибку.
 *
 * @author Samyrai47
 */
public class DBAuthenticateException extends RuntimeException {
  public DBAuthenticateException(String message) {
    super(message);
  }
}
