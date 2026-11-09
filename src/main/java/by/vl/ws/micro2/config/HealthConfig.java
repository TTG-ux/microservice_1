package by.vl.ws.micro2.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Кастомные health checks.
 */
@Configuration
public class HealthConfig {

    @Bean
    public HealthIndicator workerPoolHealthIndicator(ThreadPoolExecutor workerTaskExecutor) {
        return () -> {
            if (workerTaskExecutor.isShutdown()) {
                return Health.down()
                        .withDetail("status", "shutdown")
                        .build();
            }

            int queueSize = workerTaskExecutor.getQueue().size();
            int maxPoolSize = workerTaskExecutor.getMaximumPoolSize();

            if (queueSize > maxPoolSize * 0.8) {
                return Health.down()
                        .withDetail("queueSize", queueSize)
                        .withDetail("maxPoolSize", maxPoolSize)
                        .withDetail("status", "pool nearly full")
                        .build();
            }

            return Health.up()
                    .withDetail("queueSize", queueSize)
                    .withDetail("maxPoolSize", maxPoolSize)
                    .withDetail("activeCount", workerTaskExecutor.getActiveCount())
                    .build();
        };
    }
}