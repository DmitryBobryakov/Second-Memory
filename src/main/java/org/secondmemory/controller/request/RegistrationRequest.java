package org.secondmemory.controller.request;

/**
 * Запрос на регистрацию нового пользователя.
 *
 * @param email почта регистрирующегося пользователя
 * @param password пароль регистрирующегося пользователя
 * @param username имя регистрирующегося пользователя
 */
public record RegistrationRequest(String email, String password, String username) {}