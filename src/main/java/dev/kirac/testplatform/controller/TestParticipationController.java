package dev.kirac.testplatform.controller;

import dev.kirac.testplatform.dto.DetailedTestResultDTO;
import dev.kirac.testplatform.dto.request.SubmitTestRequest;
import dev.kirac.testplatform.dto.response.TestSubmissionResult;
import dev.kirac.testplatform.service.TestParticipationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Test Participation", description = "Test participation management API")
public class TestParticipationController {

    private final TestParticipationService testParticipationService;

    public TestParticipationController(TestParticipationService testParticipationService) {
        this.testParticipationService = testParticipationService;
    }

    @Operation(summary = "Start a test", description = "Starts a test for a student")
    @PostMapping("/students/{studentId}/tests/{testId}/start")
    public ResponseEntity<Void> startTest(@PathVariable Long studentId, @PathVariable Long testId) {
        testParticipationService.startTest(studentId, testId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Submit a test", description = "Submits a test for a student and returns the result")
    @PostMapping("/students/{studentId}/tests/{testId}/submit")
    public ResponseEntity<TestSubmissionResult> submitTest(
            @PathVariable Long studentId,
            @PathVariable Long testId,
            @Valid @RequestBody SubmitTestRequest request) {
        TestSubmissionResult result = testParticipationService.submitTest(studentId, testId, request);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get detailed test result", description = "Retrieves detailed test result for a student")
    @GetMapping("/students/{studentId}/tests/{testId}/result")
    public ResponseEntity<DetailedTestResultDTO> getDetailedTestResult(
            @PathVariable Long studentId,
            @PathVariable Long testId) {
        DetailedTestResultDTO result = testParticipationService.getDetailedTestResult(studentId, testId);
        return ResponseEntity.ok(result);
    }
}
