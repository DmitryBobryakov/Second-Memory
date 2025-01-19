package org.app.exception;

import lombok.experimental.StandardException;

@StandardException
public class NoDirectoriesFound extends Exception{
  public NoDirectoriesFound(String message) {
    super(message);
  }
}