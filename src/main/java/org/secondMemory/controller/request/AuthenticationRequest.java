package org.secondMemory.controller.request;

/**
 * Запрос на аутентификацию пользователя.
 *
 * @param email почта аутентифицирующегося пользователя
 * @param password пароль аутентифицирующегося пользователя
 */
public record AuthenticationRequest(String email, String password) {}
