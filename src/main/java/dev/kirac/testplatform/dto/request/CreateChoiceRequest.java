package dev.kirac.testplatform.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateChoiceRequest {
    @NotBlank(message = "Choice content is required")
    @Size(max = 200, message = "Choice content must be less than 200 characters")
    private String content;

    @NotNull(message = "Is correct flag is required")
    private Boolean isCorrect;
}
