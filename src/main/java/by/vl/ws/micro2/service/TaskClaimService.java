package by.vl.ws.micro2.service;

import by.vl.ws.micro2.entity.Task;
import by.vl.ws.micro2.entity.TaskRun;
import by.vl.ws.micro2.entity.TaskStatus;
import by.vl.ws.micro2.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskClaimService {

    private final TaskRepository taskRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<TaskRun> claimNextTask() {
        // ✅ ИСПРАВЛЕНО: получаем List вместо Optional
        List<Task> tasks = taskRepository.findFirstByStatusOrderByAsc(
                TaskStatus.NEW, PageRequest.of(0, 1)
        );

        if (tasks.isEmpty()) {
            return Optional.empty();
        }

        Task task = tasks.get(0);

        try {
            task.setStatus(TaskStatus.IN_PROGRESS);
            task.setExecutionId(UUID.randomUUID());
            task.setStartedAt(Instant.now());
            task.setFinishedAt(null);
            task.setProgress(0);
            task.setResult(null);
            task.setAttempts(task.getAttempts() + 1);
            task.setLeaseUntil(Instant.now().plus(5, ChronoUnit.MINUTES));

            taskRepository.saveAndFlush(task);

            log.info("Task claimed: id={}, executionId={}",
                    task.getId(), task.getExecutionId());

            return Optional.of(new TaskRun(
                    task.getId(),
                    task.getExecutionId(),
                    task.getDuration()
            ));

        } catch (OptimisticLockingFailureException e) {
            log.warn("Optimistic lock conflict for task: {}", task.getId());
            throw e;
        }
    }
}
