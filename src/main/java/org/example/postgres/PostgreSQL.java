package org.example.postgres;

import org.example.postgres.table.*;

import java.sql.SQLException;

public class PostgreSQL {
  private static final String jdbcUrl = Config.jdbcUrl;
  private static final String username = Config.username;
  private static final String password = Config.password;

  public static void initialize() throws SQLException {
    UsersTable.initialize();
    FilesTable.initialize();
    DctRolesTable.initialize();

    CrsUsersRolesTable.initialize();
    CrsFilesRolesSeeTable.initialize();
    CrsFilesRolesChangeTable.initialize();
  }
}
