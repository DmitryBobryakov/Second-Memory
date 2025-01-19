package org.example.exception;

import lombok.experimental.StandardException;

@StandardException
public class DbSelectException extends Exception {
    public DbSelectException(String message) {
        super(message);
    }
}