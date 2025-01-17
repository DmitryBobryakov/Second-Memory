package org.secondMemory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.secondMemory.entity.User;

/**
 * Класс, предоставляющий методы для работы с БД Postgres. Методы: insert, exists, getUserPassword,
 * initializeDB.
 */
public class DbUtils {
  private static Properties properties;

  public DbUtils(Properties properties) {
    this.properties = properties;
  }

  /**
   * Добавляет запись в БД.
   *
   * @param user пользователь, который будет добавлен в таблицу.
   * @throws SQLException если в процессе добавления в БД что-то пошло не так. Прокидывается в
   *     InMemoryUserRepository.
   */
  public static void insert(User user) throws SQLException {

    String sqlRequest = properties.getProperty("INSERT_USER");

    try (Connection connection =
            DriverManager.getConnection(
                properties.getProperty("JDBC_URL"),
                properties.getProperty("DB_USER"),
                properties.getProperty("DB_PASSWORD"));
        PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest)) {
      preparedStatement.setString(1, user.email());
      preparedStatement.setString(2, user.password());
      preparedStatement.setString(3, user.username());
      preparedStatement.executeUpdate();
    }
  }

  /**
   * Проверяет, есть ли запись с такой почтой в БД.
   *
   * @param email почта для проверки существования записи
   * @return результат проверки, что записей с нужной почтой 0. Если result != 0, выводит true, т.е.
   *     Запись существует. В противном случае выводит false.
   * @throws SQLException если в процессе проверки в БД что-то пошло не так. Прокидывается в
   *     InMemoryUserRepository.
   */
  public static boolean exists(String email) throws SQLException {

    String sqlRequest = properties.getProperty("USER_EXISTS");

    try (Connection connection =
            DriverManager.getConnection(
                properties.getProperty("JDBC_URL"),
                properties.getProperty("DB_USER"),
                properties.getProperty("DB_PASSWORD"));
        PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest)) {
      preparedStatement.setString(1, email);
      ResultSet rs = preparedStatement.executeQuery();

      int result = 0;
      while (rs.next()) {
        result = rs.getInt("total");
      }
      return result != 0;
    }
  }

  /**
   * Получает пароль пользователя из БД по его почте. Нет проверки на то, существует ли пользователь
   * - подразумевается, что сначала будет вызван метод Postgres.exists().
   *
   * @param email почта пользователя, по которой будет искаться пароль
   * @return пароль, полученный из БД
   * @throws SQLException если в процессе получения пароля из БД что-то пошло не так. Прокидывается
   *     в InMemoryUserRepository.
   */
  public static String getUserPassword(String email) throws SQLException {

    String sqlRequest = properties.getProperty("GET_PASSWORD");

    try (Connection connection =
            DriverManager.getConnection(
                properties.getProperty("JDBC_URL"),
                properties.getProperty("DB_USER"),
                properties.getProperty("DB_PASSWORD"));
        PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest)) {
      preparedStatement.setString(1, email);
      ResultSet rs = preparedStatement.executeQuery();

      String result = "EMPTY PASSWORD";
      while (rs.next()) {
        result = rs.getString(1);
      }
      return result;
    }
  }

  /**
   * Создает таблицу users в БД. Без DROP таблицу не создает, даже если ее не существует.
   *
   * @throws SQLException если в процессе создания таблицы в БД что-то пошло не так.
   */
  public static void initializeDB() throws SQLException {
    String sqlRequest = properties.getProperty("INITIALIZE_DB");

    try (Connection connection =
            DriverManager.getConnection(
                properties.getProperty("JDBC_URL"),
                properties.getProperty("DB_USER"),
                properties.getProperty("DB_PASSWORD"));
        PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest)) {
      preparedStatement.execute();
    }
  }
}
