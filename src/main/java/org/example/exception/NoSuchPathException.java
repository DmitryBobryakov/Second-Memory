package org.example.exception;

import lombok.experimental.StandardException;

@StandardException
public class NoSuchPathException extends Exception {
  public NoSuchPathException(String message) {
    super(message);
  }
}
