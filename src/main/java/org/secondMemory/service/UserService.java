package org.secondMemory.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.secondMemory.entity.User;
import org.secondMemory.exception.AuthenticationException;
import org.secondMemory.exception.DbAuthenticateException;
import org.secondMemory.exception.DbInsertException;
import org.secondMemory.exception.IllegalPasswordException;
import org.secondMemory.exception.NoSuchUserException;
import org.secondMemory.exception.PasswordsDontMatchExcpetion;
import org.secondMemory.exception.RegistrationException;
import org.secondMemory.exception.UserAlreadyExistsException;
import org.secondMemory.repository.UserRepository;

/**
 * Сервис, содержащий методы работы с пользователями. Инкапсулирует UserRepository. Предоставляет
 * более удобное API для верхних уровней приложения.
 */
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  /**
   * Обертка над userRepository.authenticate().
   *
   * @param email почта пользователя.
   * @param password пароль пользователя.
   */
  public void authenticate(String email, String password) {
    try {
      boolean successAuth = userRepository.authenticate(email, password);
      if (!successAuth) {
        throw new AuthenticationException("Wrong password for user " + email);
      }
    } catch (NoSuchUserException e) {
      throw new AuthenticationException("Cannot find such user");
    } catch (DbAuthenticateException e) {
      throw new AuthenticationException(
          "Cannot verify authentication. Something gone wrong in database");
    }
  }

  /**
   * Обертка над userRepository.registerUser(). Проверят пароль на соответствие стандарту.
   *
   * @param email почта пользователя.
   * @param password пароль пользователя.
   * @param username имя пользователя.
   */
  public void registerUser(String email, String password, String repeatPassword, String username) {
    Pattern pattern = Pattern.compile("[@_!#$%^&*()?/|}{~:]");
    Matcher matcher = pattern.matcher(password);
    if (password.length() < 8 || password.toLowerCase().equals(password) || !matcher.find()) {
      throw new IllegalPasswordException("Invalid password");
    }
    if (!password.equals(repeatPassword)) {
      throw new PasswordsDontMatchExcpetion("Passwords don't match.");
    }
    try {
      String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
      User user = new User(email, hashed, username);
      userRepository.registerUser(user);
    } catch (UserAlreadyExistsException e) {
      throw new RegistrationException("User " + username + " already exists");
    } catch (DbInsertException e) {
      throw new RegistrationException("Cannot add new user, something gone wrong in database");
    }
  }
}
