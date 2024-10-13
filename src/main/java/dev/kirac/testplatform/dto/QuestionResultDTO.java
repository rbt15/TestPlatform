package dev.kirac.testplatform.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionResultDTO {
    private Long questionId;
    private String questionContent;
    private int questionPoints;
    private Long selectedChoiceId;
    private String selectedChoiceContent;
    private boolean isCorrect;
    private int earnedPoints;
    private List<FullChoiceDTO> allChoices;
}
