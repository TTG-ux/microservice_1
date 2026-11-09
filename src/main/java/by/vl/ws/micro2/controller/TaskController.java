package by.vl.ws.micro2.controller;

import by.vl.ws.micro2.dto.ErrorResponse;
import by.vl.ws.micro2.dto.TaskRequestDTO;
import by.vl.ws.micro2.dto.TaskResponseDTO;
import by.vl.ws.micro2.entity.Task;
import by.vl.ws.micro2.service.TaskQueryService;
import by.vl.ws.micro2.service.TaskRetryService;
import by.vl.ws.micro2.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task API", description = "API для управления задачами")
public class TaskController {

    private final TaskService taskService;
    private final TaskQueryService taskQueryService;
    private final TaskRetryService taskRetryService;

    @PostMapping
    @Operation(summary = "Создать новую задачу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Задача создана",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные данные",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO dto) {
        Task task = taskService.createTask(dto);
        TaskResponseDTO response = taskQueryService.findById(task.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача найдена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Задача не найдена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskQueryService.findById(id));
    }

    @PostMapping("/{id}/retry")
    @Operation(summary = "Повторить FAILED задачу")
    public ResponseEntity<?> retryTask(@PathVariable Long id) {
        boolean retried = taskRetryService.retryFailedTask(id);
        if (retried) {
            return ResponseEntity.ok().body("Task retry scheduled");
        }
        return ResponseEntity.badRequest().body("Task is not in FAILED state");
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
