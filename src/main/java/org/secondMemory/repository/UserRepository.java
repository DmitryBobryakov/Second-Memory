package org.secondMemory.repository;

import org.secondMemory.entity.User;
import org.secondMemory.exception.DbAuthenticateException;
import org.secondMemory.exception.DbInsertException;
import org.secondMemory.exception.NoSuchUserException;
import org.secondMemory.exception.UserAlreadyExistsException;

/** Интерфейс репозитория пользователя. */
public interface UserRepository {

  /**
   * Аутентифицирует пользователя. Нет проверки переданных параметров на null - подразумевается, что
   * кнопка "Войти" будет доступна только при заполненных полях. Наследован от UserRepository.
   *
   * @param email почта.
   * @param password пароль.
   * @return результат проверки на идентичность паролей.
   * @throws NoSuchUserException
   * @throws DbAuthenticateException
   */
  boolean authenticate(String email, String password);

  /**
   * Регистрирует нового пользователя. Нет проверки переданных параметров на null - подразумевается,
   * что кнопка "Зарегистрироваться" будет доступна только при заполненных полях. Предполагается,
   * что соответствие пароля требованиям и валидность почты будут проверяться на фронте. Могу
   * сделать проверки, если требуется. Наследован от UserRepository.
   *
   * @param user представление пользователя.
   * @throws UserAlreadyExistsException
   * @throws DbInsertException
   */
  void registerUser(User user);
}
