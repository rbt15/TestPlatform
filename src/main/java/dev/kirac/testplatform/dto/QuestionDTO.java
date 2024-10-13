package dev.kirac.testplatform.dto;


import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private String content;
    private Integer points;
    private List<ChoiceDTO> choices;
}