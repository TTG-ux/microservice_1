package by.vl.ws.micro2.repository;

import by.vl.ws.micro2.entity.Task;
import by.vl.ws.micro2.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByEventId(UUID eventId);

    // ✅ ИСПРАВЛЕНО: возвращает List вместо Optional
    @Query("SELECT t FROM Task t WHERE t.status = :status ORDER BY t.id ASC")
    List<Task> findFirstByStatusOrderByAsc(
            @Param("status") TaskStatus status,
            org.springframework.data.domain.Pageable pageable
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Task t WHERE t.status = :status ORDER BY t.id ASC")
    Optional<Task> findAndLockFirstNewTask(@Param("status") TaskStatus status);

    @Modifying
    @Query("UPDATE Task t SET t.progress = :progress WHERE t.id = :id " +
            "AND t.executionId = :executionId AND t.status = :status")
    int updateProgress(
            @Param("id") Long id,
            @Param("executionId") UUID executionId,
            @Param("status") TaskStatus status,
            @Param("progress") int progress
    );

    @Modifying
    @Query("UPDATE Task t SET t.status = :newStatus, t.progress = :progress, " +
            "t.result = :result, t.finishedAt = :finishedAt WHERE t.id = :id " +
            "AND t.executionId = :executionId AND t.status = :runningStatus")
    int finishExecution(
            @Param("id") Long id,
            @Param("executionId") UUID executionId,
            @Param("runningStatus") TaskStatus runningStatus,
            @Param("newStatus") TaskStatus newStatus,
            @Param("progress") int progress,
            @Param("result") String result,
            @Param("finishedAt") Instant finishedAt
    );

    List<Task> findByStatus(TaskStatus status);
}