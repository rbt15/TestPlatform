package dev.kirac.testplatform.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FullTestDTO {
    private Long id;
    private String name;
    private Integer duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<QuestionDTO> questions;
}