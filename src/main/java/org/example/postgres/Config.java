package org.example.postgres;

public record Config() {
  public static final String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
  public static final String username = "postgres";
  public static final String password = "738212";
}
