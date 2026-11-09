package by.vl.ws.micro2.entity;

import java.util.UUID;

/**
 * Record для передачи данных о захваченной задаче.
 * Неизменяемый объект (immutable).
 */
public record TaskRun(
        Long id,
        UUID executionId,
        Long duration
) {}
