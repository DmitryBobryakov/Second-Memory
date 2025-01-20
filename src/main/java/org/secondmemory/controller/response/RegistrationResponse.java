package org.secondmemory.controller.response;

/**
 * Ответ на запрос о регистрации пользователя.
 *
 * @param message сообщение от системы. Поставлено для проверки работоспособности, в дальнейшем
 *     следует заменить на нечто более функциональное.
 */
public record RegistrationResponse(String message) {}