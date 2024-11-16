package org.app.exception;

/**
 * Низкоуровневая ошибка, возникающая при попытке добавления пользователя БД выбросила ошибку.
 *
 * @author Samyrai47
 */
public class DBInsertException extends RuntimeException {
  public DBInsertException(String message) {
    super(message);
  }
}
