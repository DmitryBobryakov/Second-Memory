package org.secondmemory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class Postgres {
    private static Properties properties = null;

    public Postgres(Properties properties) throws SQLException {
        this.properties = properties;
        createTables();
    }

    private static final String jdbcUrl = "jdbc:postgresql://localhost:5435/postgres";
    private static final String username = "postgres";
    private static final String password = "changeme";

    private static final String selectFromFileInfo = "SELECT file_owner, created_date, last_update, tags, access_rights FROM file_info WHERE id = ?";
    private static final String createFileInfoTable =
            "DROP TABLE IF EXISTS file_info; "
                    + "CREATE TABLE file_info (id SERIAL PRIMARY KEY, file_owner varchar(30), created_date varchar(10), last_update varchar(10), tags varchar(100), access_rights varchar(10));";

    public static ResultSet selectAllFromFileInfo(String id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(selectFromFileInfo)) {

            preparedStatement.setString(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery();) {
                return resultSet;
            }
        }
    }
    public static ResultSet selectPathToFileFromFileInfo(String fileId) throws SQLException {
        try (Connection connection =
                     DriverManager.getConnection(
                             properties.getProperty("JDBC_URL"),
                             properties.getProperty("DB_USER"),
                             properties.getProperty("DB_PASSWORD"));
             PreparedStatement preparedStatement =
                     connection.prepareStatement(properties.getProperty("SELECT_PATH_FROM_FILE_INFO"))) {
            preparedStatement.setString(1, fileId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet;
            }
        }
    }

    public static ResultSet checkTheUserSeeTheFile(String userId, String fileId) throws SQLException {
        try (Connection connection =
                     DriverManager.getConnection(
                             properties.getProperty("JDBC_URL"),
                             properties.getProperty("DB_USER"),
                             properties.getProperty("DB_PASSWORD"));
             PreparedStatement preparedStatement =
                     connection.prepareStatement(properties.getProperty("CHECK_THE_USER_CAN_SEE_FILES"))) {
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, fileId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet;
            }
        }
    }

    public static void createTables() throws SQLException {

        try (Connection connection =
                     DriverManager.getConnection(
                             properties.getProperty("JDBC_URL"),
                             properties.getProperty("DB_USER"),
                             properties.getProperty("DB_PASSWORD"));
             PreparedStatement preparedStatement =
                     connection.prepareStatement(
                             properties.getProperty("CREATE_FILE_INFO_TABLE")
                                     + properties.getProperty("CREATE_USER_TABLE")
                                     + properties.getProperty("CREATE_ROLE_TABLE")
                                     + properties.getProperty("CREATE_CRS_USERS_ROLES_TABLE")
                                     + properties.getProperty("CREATE_CRS_FILES_ROLES_SEE_TABLE")
                                     + properties.getProperty("CREATE_CRS_FILES_ROLES_CHANGE_TABLE"))) {
            preparedStatement.execute();
        }
    }
}