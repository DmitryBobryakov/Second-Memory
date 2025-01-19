package org.example;

import org.example.entity.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class Postgres {
  private static Properties properties = null;

  public Postgres(Properties properties) {
    this.properties = properties;
  }

  public static ResultSet selectAllFromFileInfo(String id) throws SQLException {
    try (Connection connection =
            DriverManager.getConnection(
                properties.getProperty("JDBC_URL"),
                properties.getProperty("DB_USER"),
                properties.getProperty("DB_PASSWORD"));
        PreparedStatement preparedStatement =
            connection.prepareStatement(properties.getProperty("SELECT_FROM_FILE_INFO"))) {

      preparedStatement.setString(1, id);

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

  public static void insertIntoFileInfoTable(File file) throws SQLException {
    try (Connection connection =
            DriverManager.getConnection(
                properties.getProperty("JDBC_URL"),
                properties.getProperty("DB_USER"),
                properties.getProperty("DB_PASSWORD"));
        PreparedStatement preparedStatement =
            connection.prepareStatement(properties.getProperty("INSERT_INTO_FILE_INFO_TABLE"))) {
      preparedStatement.setString(1, file.owner());
      preparedStatement.setString(2, file.name());
      preparedStatement.setString(3, file.bucketName());
      preparedStatement.setString(4, file.createdDate());
      preparedStatement.setString(5, file.lastUpdate());
      preparedStatement.setString(6, file.tags());
      preparedStatement.setString(7, file.accessRights());

      preparedStatement.executeUpdate();
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
}
