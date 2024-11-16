package org.app.controller.request;

/**
 * Запрос на регистрацию нового пользователя.
 *
 * @param email почта регистрирующегося пользователя
 * @param password пароль регистрирующегося пользователя
 * @param username имя регистрирующегося пользователя
 * @author Samyrai47
 */
public record RegistrationRequest(String email, String password, String username) {}
