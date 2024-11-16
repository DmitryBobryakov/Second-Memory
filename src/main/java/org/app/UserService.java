package org.app;

import org.app.entity.User;
import org.app.exception.*;
import org.app.repository.UserRepository;

/**
 * Сервис, содержащий методы работы с пользователями. Инкапсулирует UserRepository. Предоставляет
 * более удобное API для верхних уровней приложения.
 *
 * @author Samyrai47
 */
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Обертка над userRepository.authenticate().
   *
   * @param email почта пользователя.
   * @param password пароль пользователя.
   */
  public void authenticate(String email, String password) {
    try {
      boolean verdict = userRepository.authenticate(email, password);
      if (!verdict) {
        throw new AuthenticationException("Wrong password for user");
      }
    } catch (NoSuchUserException e) {
      throw new AuthenticationException("Wrong information for authentication");
    } catch (DBAuthenticateException e) {
      throw new AuthenticationException("Cannot verify authentication.");
    }
  }

  /**
   * Обертка над userRepository.registerUser().
   *
   * @param email почта пользователя.
   * @param password пароль пользователя.
   * @param username имя пользователя.
   */
  public void registerUser(String email, String password, String username) {
    try {
      User user = new User(email, String.valueOf(password.hashCode()), username);
      userRepository.registerUser(user);
    } catch (UserAlreadyExistsException e) {
      throw new RegistrationException("User already exists");
    } catch (DBInsertException e) {
      throw new RegistrationException("Cannot add new user");
    }
  }
}
