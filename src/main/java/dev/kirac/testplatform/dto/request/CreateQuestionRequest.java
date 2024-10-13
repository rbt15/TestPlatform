package dev.kirac.testplatform.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateQuestionRequest {
    @NotBlank(message = "Question content is required")
    @Size(max = 500, message = "Question content must be less than 500 characters")
    private String content;

    @NotNull(message = "Points are required")
    private Integer points;

    @NotNull(message = "Test ID is required")
    private Long testId;

    @NotNull(message = "Choices are required")
    private List<CreateChoiceRequest> choices;
}
