package database;

import org.jdbi.v3.core.Jdbi;

public class Database {
    private static final String DATABASE_URL = "jdbc:sqlite:database.db";
    private static final String DATABASE_USER = "admin";
    private static final String DATABASE_PASSWORD = "admin123";

    public static Jdbi getJdbi() {
        return Jdbi.create(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }
}
