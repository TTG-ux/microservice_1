package by.vl.ws.micro2.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO для создания задачи через REST API.
 * Валидация через Bean Validation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequestDTO {

    @NotNull(message = "Event ID обязателен")
    private UUID eventId;

    @NotBlank(message = "Название задачи обязательно")
    @Size(min = 1, max = 255, message = "Название должно быть от 1 до 255 символов")
    private String name;

    @NotNull(message = "Длительность обязательна")
    @Min(value = 1, message = "Длительность должна быть больше 0")
    private Long duration;
}
