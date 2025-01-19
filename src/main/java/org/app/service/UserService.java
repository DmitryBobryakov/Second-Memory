package org.app.service;

import org.app.UserRoles;
import org.app.exception.DbSelectException;
import org.app.repository.UserRepository;

import java.util.List;
import java.util.Map;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserRoles> getAllUsersWithRoles() throws DbSelectException {
        try {
            return userRepository.getAllUsersWithRoles();
        } catch (Exception e) {
            throw new DbSelectException("Ошибка при получении пользователей с ролями", e);
        }
    }

    public void updateUserRoles(Map<Integer, String> userRoles) throws DbSelectException {
        if (userRoles == null || userRoles.isEmpty()) {
            throw new IllegalArgumentException("Список ролей пользователей не может быть пустым");
        }
        try {
            userRepository.updateUserRoles(userRoles);
        } catch (Exception e) {
            throw new DbSelectException("Ошибка при обновлении ролей пользователей", e);
        }
    }
}
