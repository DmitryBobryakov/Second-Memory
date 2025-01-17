package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Postgres {

  private static final String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
  private static final String username = "postgres";
  private static final String password = "changeMe";

  private static final String selectFromFileInfo =
      "SELECT file_owner, created_date, last_update, tags, access_rights FROM file_info WHERE id = ?;";
  private static final String createFileInfoTable =
      """
             CREATE TABLE IF NOT EXISTS file_info (
             id SERIAL PRIMARY KEY,
             owner varchar(30),
             name varchar(50),
             bucket_name varchar(50),
             created_date varchar(10),
             last_update varchar(10),
             tags varchar(100),
             access_rights varchar(10)
             );
          """;
  private static final String insertIntoFileInfoTable =
      "INSERT INTO file_info (owner, name, bucket_name, created_date, last_update, tags, access_rights) VALUES(?, ?, ?, ?, ?, ?, ?) RETURNING id;";

  public static ResultSet selectAllFromFileInfo(String id) throws SQLException {
    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement(selectFromFileInfo)) {

      preparedStatement.setString(1, id);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
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

  public static void insertIntoFileInfoTable(File file) throws SQLException {
    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        PreparedStatement preparedStatement =
            connection.prepareStatement(insertIntoFileInfoTable)) {
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
}
