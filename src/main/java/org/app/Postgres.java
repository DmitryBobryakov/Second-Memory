package org.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Postgres {

    private static final String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
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

    public static void createTables() throws SQLException {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(createFileInfoTable)) {
            preparedStatement.execute();
        }
    }
}