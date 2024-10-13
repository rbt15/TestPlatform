package dev.kirac.testplatform.controller;

import dev.kirac.testplatform.dto.QuestionDTO;
import dev.kirac.testplatform.dto.request.CreateQuestionRequest;
import dev.kirac.testplatform.dto.request.UpdateQuestionRequest;
import dev.kirac.testplatform.mapper.QuestionMapper;
import dev.kirac.testplatform.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@Tag(name = "Question", description = "Question management API")
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionMapper questionMapper;

    public QuestionController(QuestionService questionService, QuestionMapper questionMapper) {
        this.questionService = questionService;
        this.questionMapper = questionMapper;
    }

    @Operation(summary = "Get questions by test ID", description = "Retrieves all questions for a specific test")
    @GetMapping("/test/{testId}")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByTestId(@PathVariable Long testId) {
        List<QuestionDTO> questions = questionService.getQuestionsByTestId(testId);
        return ResponseEntity.ok(questions);
    }

    @Operation(summary = "Get a question by ID", description = "Retrieves a question by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id)
                .map(questionMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new question", description = "Creates a new question for a specific test")
    @PostMapping
    public ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody CreateQuestionRequest request) {
        QuestionDTO createdQuestion = questionService.createQuestion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    @Operation(summary = "Update an existing question", description = "Updates an existing question by ID")
    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long id, @Valid @RequestBody UpdateQuestionRequest request) {
        try {
            QuestionDTO updatedQuestion = questionService.updateQuestion(id, request);
            return ResponseEntity.ok(updatedQuestion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a question", description = "Deletes a question by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}