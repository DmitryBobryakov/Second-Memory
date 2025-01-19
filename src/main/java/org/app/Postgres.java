package org.app;

import java.sql.*;

public class Postgres {

    private static final String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
    private static final String username = "postgres";
    private static final String password = "changeme";

    private static final String selectFromFileInfo = "SELECT file_owner, created_date, last_update, tags, access_rights FROM file_info WHERE id = ?";
    private static final String createFileInfoTable =
        "DROP TABLE IF EXISTS file_info; "
        + "CREATE TABLE file_info (id SERIAL PRIMARY KEY, file_owner varchar(30), created_date varchar(10), last_update varchar(10), tags varchar(100), access_rights varchar(10));";
    private static final String createUsersTable =
            "CREATE TABLE IF NOT EXISTS users ("
                    + "id SERIAL PRIMARY KEY, "
                    + "username VARCHAR(50) NOT NULL UNIQUE);";

    private static final String createUsersRolesTable =
            "CREATE TABLE IF NOT EXISTS users_roles ("
                    + "user_id INT NOT NULL, "
                    + "role VARCHAR(50) NOT NULL, "
                    + "PRIMARY KEY (user_id), "
                    + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE);";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    public static void insertUser(String username) throws SQLException {
        String insertUserQuery = "INSERT INTO users (username) VALUES (?) ON CONFLICT DO NOTHING;";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery)) {

            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        }
    }

    public static void updateUserRole(int userId, String role) throws SQLException {
        String updateRoleQuery =
                "INSERT INTO users_roles (user_id, role) VALUES (?, ?) "
                        + "ON CONFLICT (user_id) DO UPDATE SET role = ?;";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(updateRoleQuery)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, role);
            preparedStatement.setString(3, role);
            preparedStatement.executeUpdate();
        }
    }



    public static ResultSet selectAllFromFileInfo(String id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(selectFromFileInfo)) {

            preparedStatement.setString(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery();) {
                return resultSet;
            }
        }
    }

    public static ResultSet fetchUsersWithRoles() throws SQLException {
        String query = "SELECT u.id, u.username, r.role FROM users u LEFT JOIN users_roles r ON u.id = r.user_id;";
        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
    }

    public static void createTables() throws SQLException {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {
            statement.execute(createFileInfoTable);
            statement.execute(createUsersTable);
            statement.execute(createUsersRolesTable);
        }
    }
}