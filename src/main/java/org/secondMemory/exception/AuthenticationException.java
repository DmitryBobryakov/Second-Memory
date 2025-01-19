package org.secondMemory.exception;

import lombok.experimental.StandardException;

/** Высокоуровневая ошибка, возникает при отрицательной попытке аутентификации. */
@StandardException
public class AuthenticationException extends RuntimeException {}
