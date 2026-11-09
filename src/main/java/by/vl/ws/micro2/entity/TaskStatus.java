package by.vl.ws.micro2.entity;

/**
 * Статусы задачи в жизненном цикле.
 */
public enum TaskStatus {
    NEW,           // Создана, ожидает выполнения
    IN_PROGRESS,   // Выполняется воркером
    COMPLETED,     // Успешно завершена
    FAILED         // Завершена с ошибкой
}
