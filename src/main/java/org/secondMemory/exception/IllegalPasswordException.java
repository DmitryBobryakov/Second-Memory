package org.secondMemory.exception;

import lombok.experimental.StandardException;

/** Ошибка, возникающая при попытке использовать пароль, не попадающий под требуемый стандарт. */
@StandardException
public class IllegalPasswordException extends RuntimeException {}
