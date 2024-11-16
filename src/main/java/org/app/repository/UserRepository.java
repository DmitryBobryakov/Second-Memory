package org.app.repository;

import org.app.entity.User;
import org.app.exception.DBAuthenticateException;
import org.app.exception.DBInsertException;
import org.app.exception.NoSuchUserException;
import org.app.exception.UserAlreadyExistsException;

/**
 * Интерфейс репозитория пользователя.
 *
 * @author Samyrai47
 */
public interface UserRepository {
  /**
   * @throws NoSuchUserException
   * @throws DBAuthenticateException
   */
  boolean authenticate(String username, String password);

  /**
   * @throws UserAlreadyExistsException
   * @throws DBInsertException
   */
  void registerUser(User user);
}
