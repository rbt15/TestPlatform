package dev.kirac.testplatform.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAnswerDTO {
    @NotNull
    private Long questionId;
    @NotNull
    private Long selectedChoiceId;
}
