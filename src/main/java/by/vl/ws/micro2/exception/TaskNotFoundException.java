package by.vl.ws.micro2.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("Task not found: " + id);
    }
}
