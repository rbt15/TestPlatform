package dev.kirac.testplatform.controller;

import dev.kirac.testplatform.dto.StudentDTO;
import dev.kirac.testplatform.dto.request.CreateStudentRequest;
import dev.kirac.testplatform.dto.request.UpdateStudentRequest;
import dev.kirac.testplatform.entity.Student;
import dev.kirac.testplatform.mapper.StudentMapper;
import dev.kirac.testplatform.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@Tag(name = "Student", description = "Student management API")
public class StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;

    public StudentController(StudentService studentService, StudentMapper studentMapper) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
    }

    @Operation(summary = "Get all students", description = "Retrieves a list of all students")
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(studentMapper.toDTOList(students));
    }

    @Operation(summary = "Get a student by ID", description = "Retrieves a student by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(studentMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get a student by student number", description = "Retrieves a student by their student number")
    @GetMapping("/number/{studentNumber}")
    public ResponseEntity<StudentDTO> getStudentByStudentNumber(@PathVariable String studentNumber) {
        return studentService.getStudentByStudentNumber(studentNumber)
                .map(studentMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new student", description = "Creates a new student")
    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody CreateStudentRequest request) {
        StudentDTO createdStudent = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @Operation(summary = "Update an existing student", description = "Updates an existing student by ID")
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody UpdateStudentRequest request) {
        StudentDTO updatedStudent = studentService.updateStudent(id, request);
        return ResponseEntity.ok(updatedStudent);
    }

    @Operation(summary = "Delete a student", description = "Deletes a student by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}