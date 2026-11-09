package by.vl.ws.micro2.service;

import by.vl.ws.micro2.dto.TaskRequestDTO;
import by.vl.ws.micro2.entity.Task;
import by.vl.ws.micro2.entity.TaskStatus;
import by.vl.ws.micro2.metrics.TaskMetrics;
import by.vl.ws.micro2.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Сервис создания задач через REST API.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMetrics taskMetrics;

    @Transactional
    public Task createTask(TaskRequestDTO dto) {
        Optional<Task> existing = taskRepository.findByEventId(dto.getEventId());

        if (existing.isPresent()) {
            log.info("Task already exists: eventId={}", dto.getEventId());
            return existing.get();
        }

        Task task = Task.builder()
                .eventId(dto.getEventId())
                .name(dto.getName())
                .duration(dto.getDuration())
                .status(TaskStatus.NEW)
                .progress(0)
                .attempts(0)
                .maxAttempts(3)
                .build();

        Task saved = taskRepository.save(task);
        taskMetrics.recordTaskCreated();

        log.info("Task created: id={}, eventId={}", saved.getId(), saved.getEventId());
        return saved;
    }
}
