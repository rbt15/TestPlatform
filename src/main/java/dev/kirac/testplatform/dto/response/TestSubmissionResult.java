package dev.kirac.testplatform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestSubmissionResult {
    private int correctCount;
    private int totalCount;
    private int earnedPoints;
    private int totalPoints;
}
