package by.vl.ws.micro2.dto;

/**
 * Универсальный формат ответа об ошибке.
 */
public record ErrorResponse(
        String code,
        String message
) {}