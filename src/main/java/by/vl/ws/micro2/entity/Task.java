package by.vl.ws.micro2.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA сущность задачи.
 *
 * Hibernate автоматически маппит этот класс на таблицу tasks.
 * @Version обеспечивает оптимистичную блокировку.
 */
@Entity
@Table(name = "tasks",
        indexes = {
                @Index(name = "idx_task_status", columnList = "status"),
                @Index(name = "idx_task_event_id", columnList = "eventId", unique = true)
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID eventId;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private Long duration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Column(nullable = false)
    @Builder.Default
    private Integer progress = 0;

    @Column(columnDefinition = "TEXT")
    private String result;

    @Column(name = "execution_id")
    private UUID executionId;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "attempts")
    @Builder.Default
    private Integer attempts = 0;

    @Column(name = "max_attempts")
    @Builder.Default
    private Integer maxAttempts = 3;

    @Column(name = "lease_until")
    private Instant leaseUntil;

    @Column(name = "next_retry_at")
    private Instant nextRetryAt;
}
