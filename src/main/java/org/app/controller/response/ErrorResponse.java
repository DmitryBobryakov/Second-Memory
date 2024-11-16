package org.app.controller.response;

/**
 * Отрицательный ответ на запросы.
 *
 * @param message сообщение от системы с описанием ошибки.
 * @author Samyrai47
 */
public record ErrorResponse(String message) {}
