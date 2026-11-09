package by.vl.ws.micro2.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Конфигурация пула воркеров.
 */
@Slf4j
@Configuration
public class ThreadPoolConfig {

    @Value("${app.worker-pool.core-size:5}")
    private int corePoolSize;

    @Value("${app.worker-pool.max-size:10}")
    private int maxPoolSize;

    @Value("${app.worker-pool.queue-capacity:100}")
    private int queueCapacity;

    @Bean(name = "workerTaskExecutor")
    public ThreadPoolExecutor workerTaskExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueCapacity),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down worker pool...");
            executor.shutdown();
            try {
                if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }));

        log.info("Worker pool created: core={}, max={}, queue={}",
                corePoolSize, maxPoolSize, queueCapacity);

        return executor;
    }
}
