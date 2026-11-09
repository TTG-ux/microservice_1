CREATE TABLE IF NOT EXISTS tasks (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     event_id UUID NOT NULL UNIQUE,
                                     name VARCHAR(255) NOT NULL,
    duration BIGINT NOT NULL CHECK (duration > 0),
    status VARCHAR(50) NOT NULL DEFAULT 'NEW',
    progress INT NOT NULL DEFAULT 0 CHECK (progress >= 0 AND progress <= 100),
    result CLOB,
    execution_id UUID,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    started_at TIMESTAMP,
    finished_at TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    attempts INT NOT NULL DEFAULT 0,
    max_attempts INT NOT NULL DEFAULT 3,
    lease_until TIMESTAMP,
    next_retry_at TIMESTAMP
    );

CREATE INDEX IF NOT EXISTS idx_task_status ON tasks(status);
CREATE INDEX IF NOT EXISTS idx_task_event_id ON tasks(event_id);