package org.example;

import org.example.entity.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Postgres {

  private static final String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
  private static final String username = "postgres";
  private static final String password = "changeMe";

  private static final String selectFileUUID =
      "SELECT id FROM file_info WHERE owner=? and name=? and bucket_name=?;";
  private static final String selectFromFileInfo =
      "SELECT file_owner, created_date, last_update, tags, access_rights FROM file_info WHERE id = ?;";
  private static final String selectPathFromFileInfo =
      "SELECT bucketName, name FROM file_info WHERE id = ?;";
  private static final String createFileInfoTable =
      "CREATE TABLE IF NOT EXISTS file_info (id SERIAL PRIMARY KEY, owner varchar(30), name varchar(50), bucket_name varchar(50), created_date varchar(10), last_update varchar(10), tags varchar(100), access_rights varchar(10));";
  private static final String createUserTable =
      "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, email VARCHAR(30), password VARCHAR(20), username VARCHAR(30));";
  private static final String createRoleTable =
      "CREATE TABLE IF NOT EXISTS dct_roles (id SERIAL PRIMARY KEY, name VARCHAR(30));";
  private static final String createCrsUsersRolesTable =
      "CREATE TABLE IF NOT EXISTS crs_users_roles (user_id INT, role_ID INT, PRIMARY KEY(user_id, role_ID), FOREIGN KEY(user_id) REFERENCES users(id), FOREIGN KEY(role_id) REFERENCES dct_roles(id));";
  private static final String createCrsFilesRolesSeeTable =
      "CREATE TABLE IF NOT EXISTS crs_files_roles_see (file_ID INT, role_ID INT, PRIMARY KEY(file_ID, role_ID), FOREIGN KEY(file_ID) REFERENCES file_info(id), FOREIGN KEY(role_ID) REFERENCES dct_roles(id));";
  private static final String createCrsFilesRolesChangeTable =
      "CREATE TABLE IF NOT EXISTS crs_files_roles_change (file_ID INT, role_ID INT, PRIMARY KEY(file_ID, role_ID), FOREIGN KEY(file_ID) REFERENCES file_info(id), FOREIGN KEY(role_ID) REFERENCES dct_roles(id));";

  private static final String insertIntoFileInfoTable =
      "INSERT INTO file_info (owner, name, bucket_name, created_date, last_update, tags, access_rights) VALUES(?, ?, ?, ?, ?, ?, ?) RETURNING id;"; // RETURNING id нужен ли?

  private static final String canTheUserSeeTheFile =
      "SELECT EXISTS (SELECT COUNT(id) FROM dct_roles where id in (SELECT role_id FROM crs_users_roles where user_id=?) and id in (SELECT role_id FROM crs_files_roles_see where file_id=?));";

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
        PreparedStatement preparedStatement =
            connection.prepareStatement(
                createFileInfoTable
                    + createUserTable
                    + createRoleTable
                    + createCrsUsersRolesTable
                    + createCrsFilesRolesSeeTable
                    + createCrsFilesRolesChangeTable)) {
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

  public static int selectFileUUID(File file) throws SQLException {
    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement(selectFileUUID)) {
      preparedStatement.setString(1, file.owner());
      preparedStatement.setString(2, file.name());
      preparedStatement.setString(3, file.bucketName());

      ResultSet rs = preparedStatement.executeQuery();

      Integer result = null;
      while (rs.next()) {
        result = rs.getInt(1);
      }
      if (result == null) {
        throw new SQLException("not found FileUUID");
      }
      return result;
    }
  }

  /*public static int selectRoleUUID(File file) throws SQLException {
    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement(selectFileUUID)) {
      preparedStatement.setString(1, file.owner());
      preparedStatement.setString(2, file.name());
      preparedStatement.setString(3, file.bucketName());

      ResultSet rs = preparedStatement.executeQuery();

      Integer result = null;
      while (rs.next()) {
        result = rs.getInt(1);
      }
      if (result == null) {
        throw new SQLException("not found FileUUID");
      }
      return result;
    }
  }*/

  public static ResultSet selectPathToFileFromFileInfo(String fileId) throws SQLException {
    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement(selectPathFromFileInfo)) {
      preparedStatement.setString(1, fileId);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        return resultSet;
      }
    }
  }

  public static ResultSet checkTheUserSeeTheFile(String userId, String fileId) throws SQLException {
    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement(canTheUserSeeTheFile)) {
      preparedStatement.setString(1, userId);
      preparedStatement.setString(2, fileId);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        return resultSet;
      }
    }
  }
}
