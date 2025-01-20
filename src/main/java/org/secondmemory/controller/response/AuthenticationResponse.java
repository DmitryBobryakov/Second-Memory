package org.secondmemory.controller.response;

/**
 * Ответ на запрос об аутентификации пользователя
 *
 * @param message сообщение от системы. Поставлено для проверки работоспособности, в дальнейшем
 *     следует заменить на нечто более функциональное.
 */
public record AuthenticationResponse(String message) {}