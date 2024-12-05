package org.app.exception;

public class DBSelectException extends RuntimeException {
  public DBSelectException(String message) {
    super(message);
  }
}