package org.secondmemory.controller.response;

/**
 * Отрицательный ответ на запросы.
 *
 * @param message сообщение от системы с описанием ошибки.
 */
public record ErrorResponse(String message) {}