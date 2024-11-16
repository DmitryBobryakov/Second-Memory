package org.app;

/**
 * Обертка над примитивом long. Используется как ID для пользователя.
 *
 * @author Samyrai47
 * @deprecated Используется ID из DB, могу убрать, если класс не понадобится.
 */
public class UserID {
  private final long ID;

  public UserID(long id) {
    this.ID = id;
  }
}
