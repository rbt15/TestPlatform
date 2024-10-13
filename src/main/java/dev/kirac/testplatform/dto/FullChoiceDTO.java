package dev.kirac.testplatform.dto;

import lombok.Data;

@Data
public class FullChoiceDTO {
    private Long id;
    private String content;
    private boolean isCorrect;
}
