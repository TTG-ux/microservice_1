package by.vl.ws.micro2.service;

import by.vl.ws.micro2.dto.TaskResponseDTO;
import by.vl.ws.micro2.entity.Task;
import by.vl.ws.micro2.exception.TaskNotFoundException;
import by.vl.ws.micro2.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис чтения задач для REST API.
 */
@Service
@RequiredArgsConstructor
public class TaskQueryService {

    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public TaskResponseDTO findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        return new TaskResponseDTO(
                task.getId(),
                task.getEventId(),
                task.getName(),
                task.getDuration(),
                task.getStatus(),
                task.getProgress(),
                task.getResult(),
                task.getCreatedAt(),
                task.getStartedAt(),
                task.getFinishedAt()
        );
    }
}
