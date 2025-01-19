package org.secondMemory.exception;

import lombok.experimental.StandardException;

/** Высокоуровневая ошибка, возникает при попытке зарегистрировать существующего пользователя. */
@StandardException
public class RegistrationException extends RuntimeException {}
