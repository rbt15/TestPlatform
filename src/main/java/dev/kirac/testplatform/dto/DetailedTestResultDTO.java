package dev.kirac.testplatform.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DetailedTestResultDTO {
    private Long testId;
    private String testName;
    private Long studentId;
    private String studentName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int totalPoints;
    private int earnedPoints;
    private List<QuestionResultDTO> questionResults;
}
