package by.vl.ws.micro2.service;

import by.vl.ws.micro2.entity.Task;
import by.vl.ws.micro2.entity.TaskStatus;
import by.vl.ws.micro2.exception.TaskNotFoundException;
import by.vl.ws.micro2.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис повторного запуска FAILED задач.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskRetryService {

    private final TaskRepository taskRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean retryFailedTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        if (task.getStatus() != TaskStatus.FAILED) {
            log.warn("Task is not FAILED: id={}, status={}", id, task.getStatus());
            return false;
        }

        task.setStatus(TaskStatus.NEW);
        task.setExecutionId(null);
        task.setStartedAt(null);
        task.setFinishedAt(null);
        task.setProgress(0);
        task.setResult(null);

        taskRepository.save(task);

        log.info("Task retry scheduled: id={}", id);
        return true;
    }
}
