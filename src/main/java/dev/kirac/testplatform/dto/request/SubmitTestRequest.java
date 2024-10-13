package dev.kirac.testplatform.dto.request;

import dev.kirac.testplatform.dto.StudentAnswerDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SubmitTestRequest {
    @NotNull
    private List<StudentAnswerDTO> answers;
}