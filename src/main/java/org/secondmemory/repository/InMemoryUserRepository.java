package org.secondmemory.repository;

import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;
import org.secondmemory.Postgres;
import org.secondmemory.entity.User;
import org.secondmemory.exception.DbAuthenticateException;
import org.secondmemory.exception.DbInsertException;
import org.secondmemory.exception.NoSuchUserException;
import org.secondmemory.exception.UserAlreadyExistsException;

/**
 * Низкоуровневый класс, выполняет основные методы, выкидывает низкоуровневые ошибки, которые
 * подменяются на более высоких уровнях.
 */
public class InMemoryUserRepository implements UserRepository {

    @Override
    public boolean authenticate(String email, String password) {
        try {
            if (!Postgres.exists(email)) {
                throw new NoSuchUserException("Cannot find user with email " + email);
            }
            String hashedPassword = Postgres.getUserPassword(email);
            return BCrypt.checkpw(password, hashedPassword);
        } catch (SQLException e) {
            throw new DbAuthenticateException("Cannot check DB for information");
        }
    }

    @Override
    public void registerUser(User user) {
        try {
            if (Postgres.exists(user.email())) {
                throw new UserAlreadyExistsException("User is already exists");
            }
            Postgres.insert(user);
        } catch (SQLException e) {
            throw new DbInsertException("Cannot add User");
        }
    }
}