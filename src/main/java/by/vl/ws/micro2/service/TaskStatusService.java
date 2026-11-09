package by.vl.ws.micro2.service;

import by.vl.ws.micro2.entity.TaskRun;
import by.vl.ws.micro2.entity.TaskStatus;
import by.vl.ws.micro2.metrics.TaskMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Сервис выполнения задач.
 * Симулирует работу через Thread.sleep.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskStatusService {

    private final TaskUpdateService taskUpdateService;
    private final TaskMetrics taskMetrics;

    public void executeTask(TaskRun run) {
        log.info("Executing task: id={}, duration={}ms", run.id(), run.duration());

        long elapsed = 0;
        int progress = 0;

        try {
            while (elapsed < run.duration()) {
                if (Thread.currentThread().isInterrupted()) {
                    log.info("Task interrupted: id={}", run.id());
                    saveFinal(run, TaskStatus.FAILED, progress, "Task interrupted");
                    return;
                }

                long step = Math.min(1000, run.duration() / 10);
                Thread.sleep(step);
                elapsed += step;

                progress = (int) ((elapsed * 100) / run.duration());
                progress = Math.min(progress, 90);

                boolean updated = taskUpdateService.updateProgress(
                        run.id(), run.executionId(), progress
                );

                if (!updated) {
                    log.warn("Progress update failed: id={}", run.id());
                    return;
                }
            }

            log.info("Task completed: id={}", run.id());
            taskMetrics.recordTaskCompleted();
            saveFinal(run, TaskStatus.COMPLETED, 100, "Task completed successfully");

        } catch (InterruptedException e) {
            log.info("Task interrupted: id={}", run.id());
            Thread.currentThread().interrupt();
            saveFinal(run, TaskStatus.FAILED, progress, "Task interrupted");
        } catch (Exception e) {
            log.error("Task execution error: id={}", run.id(), e);
            taskMetrics.recordTaskFailed();
            saveFinal(run, TaskStatus.FAILED, progress, "Error: " + e.getMessage());
        }
    }

    private void saveFinal(TaskRun run, TaskStatus status, int progress, String result) {
        try {
            taskUpdateService.updateTaskStatus(
                    run.id(), run.executionId(), status, progress, result
            );
        } catch (Exception e) {
            log.error("Failed to save final result: task id={}", run.id(), e);
        }
    }
}