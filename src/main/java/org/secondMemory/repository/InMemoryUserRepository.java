package org.secondMemory.repository;

import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;
import org.secondMemory.DbUtils;
import org.secondMemory.entity.User;
import org.secondMemory.exception.DbAuthenticateException;
import org.secondMemory.exception.DbInsertException;
import org.secondMemory.exception.NoSuchUserException;
import org.secondMemory.exception.UserAlreadyExistsException;

/**
 * Низкоуровневый класс, выполняет основные методы, выкидывает низкоуровневые ошибки, которые
 * подменяются на более высоких уровнях.
 */
public class InMemoryUserRepository implements UserRepository {

  @Override
  public boolean authenticate(String email, String password) {
    try {
      if (!DbUtils.exists(email)) {
        throw new NoSuchUserException("Cannot find user with email " + email);
      }
      String hashedPassword = DbUtils.getUserPassword(email);
      return BCrypt.checkpw(password, hashedPassword);
    } catch (SQLException e) {
      throw new DbAuthenticateException("Cannot check DB for information");
    }
  }

  @Override
  public void registerUser(User user) {
    try {
      if (DbUtils.exists(user.email())) {
        throw new UserAlreadyExistsException("User is already exists");
      }
      DbUtils.insert(user);
    } catch (SQLException e) {
      throw new DbInsertException("Cannot add User");
    }
  }
}
