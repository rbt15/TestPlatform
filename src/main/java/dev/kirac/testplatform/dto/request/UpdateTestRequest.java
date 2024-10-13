package dev.kirac.testplatform.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateTestRequest {
    @Size(max = 100, message = "Test name must be less than 100 characters")
    private String name;

    private Integer duration;

    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;
}
