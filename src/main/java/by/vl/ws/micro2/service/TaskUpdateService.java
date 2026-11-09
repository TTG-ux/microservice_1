package by.vl.ws.micro2.service;

import by.vl.ws.micro2.entity.TaskStatus;
import by.vl.ws.micro2.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Сервис обновления прогресса и статуса задач.
 * REQUIRES_NEW - каждая операция в отдельной транзакции.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskUpdateService {

    private final TaskRepository taskRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean updateProgress(Long id, UUID executionId, int progress) {
        if (progress < 0 || progress > 99) {
            throw new IllegalArgumentException("Progress must be between 0 and 99");
        }

        int updated = taskRepository.updateProgress(
                id, executionId, TaskStatus.IN_PROGRESS, progress
        );

        if (updated == 0) {
            log.warn("Failed to update progress: id={}, executionId={}", id, executionId);
        }

        return updated > 0;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean updateTaskStatus(
            Long id,
            UUID executionId,
            TaskStatus newStatus,
            int progress,
            String result) {

        if (newStatus != TaskStatus.COMPLETED && newStatus != TaskStatus.FAILED) {
            throw new IllegalArgumentException("Expected final status");
        }

        int finalProgress = (newStatus == TaskStatus.COMPLETED) ? 100 : Math.min(progress, 99);

        int updated = taskRepository.finishExecution(
                id, executionId, TaskStatus.IN_PROGRESS,
                newStatus, finalProgress, result, Instant.now()
        );

        if (updated == 0) {
            log.warn("Failed to update task status: id={}, status={}", id, newStatus);
        } else {
            log.info("Task status updated: id={}, status={}", id, newStatus);
        }

        return updated > 0;
    }
}
