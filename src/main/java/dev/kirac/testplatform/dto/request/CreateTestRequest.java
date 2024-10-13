package dev.kirac.testplatform.dto.request;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateTestRequest {
    @NotBlank(message = "Test name is required")
    @Size(max = 100, message = "Test name must be less than 100 characters")
    private String name;

    @NotNull(message = "Duration is required")
    private Integer duration;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;
}
