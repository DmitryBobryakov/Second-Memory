package org.secondmemory.exception;

import lombok.experimental.StandardException;

/**
 * Низкоуровневая ошибка, возникающая если при попытке аутентифицировать пользователя БД выбросила
 * ошибку.
 */
@StandardException
public class DbAuthenticateException extends RuntimeException {}