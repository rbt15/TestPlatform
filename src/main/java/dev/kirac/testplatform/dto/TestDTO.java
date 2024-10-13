package dev.kirac.testplatform.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestDTO {
    private Long id;
    private String name;
    private Integer duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}