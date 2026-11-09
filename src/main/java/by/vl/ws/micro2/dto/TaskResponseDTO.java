package by.vl.ws.micro2.dto;

import by.vl.ws.micro2.entity.TaskStatus;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO ответа REST API.
 * Record - неизменяемый объект.
 */
public record TaskResponseDTO(
        Long id,
        UUID eventId,
        String name,
        Long duration,
        TaskStatus status,
        int progress,
        String result,
        Instant createdAt,
        Instant startedAt,
        Instant finishedAt
) {}
