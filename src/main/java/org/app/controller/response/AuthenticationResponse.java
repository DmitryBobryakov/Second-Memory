package org.app.controller.response;

/**
 * Ответ на запрос об аутентификации пользователя
 *
 * @param message сообщение от системы. Поставлено для проверки работоспособности, в дальнейшем
 *     следует заменить на нечто более функциональное.
 * @author Samyrai47
 */
public record AuthenticationResponse(String message) {}
