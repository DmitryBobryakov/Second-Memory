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
    public String[] authenticate(String email, String password) {
        try {
            if (!Postgres.exists(email)) {
                throw new NoSuchUserException("Cannot find user with email " + email);
            }
            String hashedPassword = Postgres.getUserPassword(email);
            boolean check = BCrypt.checkpw(password, hashedPassword);
            String pr1 = "0";
            if (check) {
                pr1 = "1";
            }
            String[] res = new String[2];
            res[0] = pr1;
            res[1] = Postgres.getUserId(email);
            return res;
        } catch (SQLException e) {
            throw new DbAuthenticateException("Cannot check DB for information");
        }
    }

    @Override
    public String registerUser(User user) {
        try {
            if (Postgres.exists(user.email())) {
                throw new UserAlreadyExistsException("User is already exists");
            }
            String check = Postgres.insert(user);
            return check;
        } catch (SQLException e) {
            throw new DbInsertException("Cannot add User");
        }
    }
}