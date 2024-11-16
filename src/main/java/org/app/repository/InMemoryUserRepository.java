package org.app.repository;

import java.sql.SQLException;
import org.app.Postgres;
import org.app.entity.User;
import org.app.exception.DBAuthenticateException;
import org.app.exception.DBInsertException;
import org.app.exception.NoSuchUserException;
import org.app.exception.UserAlreadyExistsException;

/**
 * Низкоуровневый класс, выполняет основные методы, выкидывает низкоуровневые ошибки, которые
 * подменяются на более высоких уровнях.
 *
 * @author Samyrai47
 */
public class InMemoryUserRepository implements UserRepository {

  /**
   * Аутентифицирует пользователя. Нет проверки переданных параметров на null - подразумевается, что
   * кнопка "Войти" будет доступна только при заполненных полях. Наследован от UserRepository.
   *
   * @param email почта.
   * @param password пароль.
   * @return результат проверки на идентичность паролей.
   */
  @Override
  public boolean authenticate(String email, String password) {
    try {
      if (!Postgres.exists(email)) {
        throw new NoSuchUserException("Cannot find such user");
      }
      password = String.valueOf(password.hashCode());
      String hashedPassword = Postgres.getUserPassword(email);
      return hashedPassword.equals(password);
    } catch (SQLException e) {
      throw new DBAuthenticateException("Cannot check DB for information");
    }
  }

  /**
   * Регистрирует нового пользователя. Нет проверки переданных параметров на null - подразумевается,
   * что кнопка "Зарегистрироваться" будет доступна только при заполненных полях. Предполагается,
   * что соответствие пароля требованиям и валидность почты будут проверяться на фронте. Могу
   * сделать проверки, если требуется. Наследован от UserRepository.
   *
   * @param user представление пользователя.
   */
  @Override
  public void registerUser(User user) {
    try {
      if (Postgres.exists(user.email())) {
        throw new UserAlreadyExistsException("User is already exists");
      }
      Postgres.insert(user);
    } catch (SQLException e) {
      throw new DBInsertException("Cannot add User");
    }
  }
}
