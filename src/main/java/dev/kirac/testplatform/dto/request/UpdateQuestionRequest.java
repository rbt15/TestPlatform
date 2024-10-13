package dev.kirac.testplatform.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateQuestionRequest {
    @Size(max = 500, message = "Question content must be less than 500 characters")
    private String content;

    private Integer points;
}
