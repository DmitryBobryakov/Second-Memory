package org.example.postgres.table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.example.postgres.Config;

public class CrsFilesRolesSeeTable {
  private static final String jdbcUrl = Config.jdbcUrl;
  private static final String username = Config.username;
  private static final String password = Config.password;

  public static void initialize() throws SQLException {

    String sqlRequest =
        """
        IF NOT EXISTS crs.files_roles_see
        THEN
            CREATE TABLE crs.files_roles_see (file_ID INT, role_ID INT, PRIMARY KEY(file_ID, role_ID), FOREIGN KEY(file_ID) REFERENCES files(file_ID), FOREIGN KEY(role_ID) REFERENCES dct.roles(role_ID));
        """;
    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest)) {
      preparedStatement.execute();
    }
  }

  public static void insert(int fileID, int roleID) throws SQLException {

    String sqlRequest = "INSERT INTO crs.files_roles_see(file_ID, role_ID) VALUES (?, ?)";
    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest)) {
      preparedStatement.setInt(1, fileID);
      preparedStatement.setInt(2, roleID);
      preparedStatement.executeUpdate();
    }
  }
}
