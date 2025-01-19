package org.secondmemory.exception;

import lombok.experimental.StandardException;

@StandardException
public class NoDirectoriesFound extends RuntimeException{
    public NoDirectoriesFound(String message) {
        super(message);
    }
}