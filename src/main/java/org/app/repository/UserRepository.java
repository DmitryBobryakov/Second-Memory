package org.app.repository;

import org.app.Postgres;
import org.app.UserRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    public List<UserRoles> getAllUsersWithRoles() {
        List<UserRoles> users = new ArrayList<>();
        String query = "SELECT u.id, u.name, ur.role FROM users u LEFT JOIN users_roles ur ON u.id = ur.user_id";

        logger.info("Выполнение запроса: {}", query);
        try (Connection connection = Postgres.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UserRoles user = new UserRoles();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setRole(resultSet.getString("role"));
                users.add(user);
            }
            logger.info("Получено {} пользователей с ролями.", users.size());
        } catch (SQLException e) {
            logger.error("Ошибка при выполнении запроса: {}", e.getMessage());
        }

        return users;
    }

    public void updateUserRoles(Map<Integer, String> userRoles) {
        String deleteQuery = "DELETE FROM users_roles";
        String insertQuery = "INSERT INTO users_roles (user_id, role) VALUES (?, ?)";

        logger.info("Начало обновления ролей пользователей.");
        try (Connection connection = Postgres.getConnection();
             PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

            connection.setAutoCommit(false);

            deleteStmt.executeUpdate();
            logger.info("Старые роли удалены.");

            for (Map.Entry<Integer, String> entry : userRoles.entrySet()) {
                insertStmt.setInt(1, entry.getKey());
                insertStmt.setString(2, entry.getValue());
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();
            connection.commit();
            logger.info("Роли пользователей успешно обновлены.");
        } catch (SQLException e) {
            logger.error("Ошибка при обновлении ролей: {}", e.getMessage());
        }
    }
}
