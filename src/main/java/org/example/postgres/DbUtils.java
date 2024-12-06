package org.example.postgres;

import org.example.postgres.table.CrsUsersRolesTable;
import org.example.postgres.table.DctRolesTable;
import org.example.postgres.table.FilesTable;
import org.example.postgres.table.UsersTable;
import org.example.postgres.table.CrsFilesRolesChangeTable;
import org.example.postgres.table.CrsFilesRolesSeeTable;

import java.sql.SQLException;

public class DbUtils {
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
