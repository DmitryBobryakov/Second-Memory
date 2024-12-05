package org.example.postgres.table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.example.postgres.Config;

public class CrsUsersRolesTable {
  private static final String jdbcUrl = Config.jdbcUrl;
  private static final String username = Config.username;
  private static final String password = Config.password;

  public static void initialize() throws SQLException {
    Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

    String sqlRequest =
        "DROP TABLE IF EXISTS crs.users_roles; "
            + "CREATE TABLE crs.users_roles (user_ID INT, role_ID INT, PRIMARY KEY(user_ID, role_ID),"
            + "FOREIGN KEY(user_ID) REFERENCES users(user_ID), FOREIGN KEY(role_ID) REFERENCES dct.roles(role_ID));";
    PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest);
    preparedStatement.execute();

    preparedStatement.close();
    connection.close();
  }

  public static void insert(int userID, int roleID) throws SQLException {
    Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

    String sqlRequest = "INSERT INTO crs.users_roles(user_ID, role_ID) " + "VALUES (?, ?)";
    PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest);
    preparedStatement.setInt(1, userID);
    preparedStatement.setInt(2, roleID);

    preparedStatement.executeUpdate();

    preparedStatement.close();
    connection.close();
  }
}
