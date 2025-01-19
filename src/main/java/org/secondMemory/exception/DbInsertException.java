package org.secondMemory.exception;

import lombok.experimental.StandardException;

/** Низкоуровневая ошибка, возникающая при попытке добавления пользователя БД выбросила ошибку. */
@StandardException
public class DbInsertException extends RuntimeException {}
