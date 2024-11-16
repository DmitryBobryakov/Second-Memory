package org.app.controller.request;

/**
 * Запрос на аутентификацию пользователя.
 *
 * @param email почта аутентифицирующегося пользователя
 * @param password пароль аутентифицирующегося пользователя
 * @author Samyrai47
 */
public record AuthenticationRequest(String email, String password) {}
