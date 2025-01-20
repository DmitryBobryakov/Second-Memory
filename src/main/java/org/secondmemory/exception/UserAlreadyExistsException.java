package org.secondmemory.exception;

import lombok.experimental.StandardException;

/** Низкоуровневая ошибка, возникающая, если регистрирующийся пользователь уже существует. */
@StandardException
public class UserAlreadyExistsException extends RuntimeException {}