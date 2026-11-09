package by.vl.ws.micro2.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskMetrics {

    private final MeterRegistry meterRegistry;

    public void recordTaskCreated() {
        Counter.builder("task.created")
                .description("Number of tasks created")
                .register(meterRegistry)
                .increment();
    }

    public void recordTaskCompleted() {
        Counter.builder("task.completed")
                .description("Number of tasks completed")
                .register(meterRegistry)
                .increment();
    }

    public void recordTaskFailed() {
        Counter.builder("task.failed")
                .description("Number of tasks failed")
                .register(meterRegistry)
                .increment();
    }
}
