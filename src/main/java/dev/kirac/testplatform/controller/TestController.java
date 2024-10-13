package dev.kirac.testplatform.controller;

import dev.kirac.testplatform.dto.FullTestDTO;
import dev.kirac.testplatform.dto.TestDTO;
import dev.kirac.testplatform.dto.request.CreateTestRequest;
import dev.kirac.testplatform.dto.request.UpdateTestRequest;
import dev.kirac.testplatform.mapper.TestMapper;
import dev.kirac.testplatform.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tests")
@Tag(name = "Test", description = "Test management API")
public class TestController {

    private final TestService testService;
    private final TestMapper testMapper;

    public TestController(TestService testService, TestMapper testMapper) {
        this.testService = testService;
        this.testMapper = testMapper;
    }

    @Operation(summary = "Get all tests", description = "Retrieves a list of all tests")
    @GetMapping
    public ResponseEntity<List<TestDTO>> getAllTests() {
        List<TestDTO> tests = testService.getAllTests();
        return ResponseEntity.ok(tests);
    }

    @Operation(summary = "Get a test by ID", description = "Retrieves a test by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<FullTestDTO> getTestById(@PathVariable Long id) {
        return testService.getTestById(id)
                .map(testMapper::toFullDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new test", description = "Creates a new test")
    @PostMapping
    public ResponseEntity<TestDTO> createTest(@Valid @RequestBody CreateTestRequest request) {
        TestDTO createdTest = testService.createTest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTest);
    }

    @Operation(summary = "Update an existing test", description = "Updates an existing test by ID")
    @PutMapping("/{id}")
    public ResponseEntity<TestDTO> updateTest(@PathVariable Long id, @Valid @RequestBody UpdateTestRequest request) {
        try {
            TestDTO updatedTest = testService.updateTest(id, request);
            return ResponseEntity.ok(updatedTest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a test", description = "Deletes a test by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        testService.deleteTest(id);
        return ResponseEntity.noContent().build();
    }
}
