package org.secondMemory.exception;

import lombok.experimental.StandardException;

/**
 * Низкоуровневая ошибка, возникающая, если при попытке аутентификации пользователь не был найден в
 * БД.
 */
@StandardException
public class NoSuchUserException extends RuntimeException {}
