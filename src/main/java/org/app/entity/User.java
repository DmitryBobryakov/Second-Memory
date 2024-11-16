package org.app.entity;

/**
 * Представление пользователя.
 *
 * @author Samyrai47
 */
public class User {
  private String username;
  private String password;
  private String email;

  public User(String email, String password, String username) {
    this.email = email;
    this.password = password;
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return email;
  }

  public String getUsername() {
    return username;
  }
}
