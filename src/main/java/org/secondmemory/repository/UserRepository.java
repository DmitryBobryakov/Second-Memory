package org.secondmemory.repository;

import org.secondmemory.entity.User;
import org.secondmemory.exception.DbAuthenticateException;
import org.secondmemory.exception.DbInsertException;
import org.secondmemory.exception.NoSuchUserException;
import org.secondmemory.exception.UserAlreadyExistsException;

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
    String[] authenticate(String email, String password);

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
    String registerUser(User user);
}