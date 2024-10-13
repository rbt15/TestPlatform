package dev.kirac.testplatform.service;

import dev.kirac.testplatform.dto.StudentDTO;
import dev.kirac.testplatform.dto.request.CreateStudentRequest;
import dev.kirac.testplatform.dto.request.UpdateStudentRequest;
import dev.kirac.testplatform.entity.Student;
import dev.kirac.testplatform.mapper.StudentMapper;
import dev.kirac.testplatform.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllStudents_shouldReturnListOfStudents() {
        // Arrange
        Student student1 = new Student();
        Student student2 = new Student();
        List<Student> students = Arrays.asList(student1, student2);

        when(studentRepository.findAll()).thenReturn(students);

        // Act
        List<Student> result = studentService.getAllStudents();

        // Assert
        assertEquals(2, result.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getStudentById_shouldReturnStudent_whenStudentExists() {
        // Arrange
        Long id = 1L;
        Student student = new Student();
        student.setId(id);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        // Act
        Optional<Student> result = studentService.getStudentById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(studentRepository, times(1)).findById(id);
    }

    @Test
    void getStudentById_shouldReturnEmptyOptional_whenStudentDoesNotExist() {
        // Arrange
        Long id = 1L;
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Student> result = studentService.getStudentById(id);

        // Assert
        assertFalse(result.isPresent());
        verify(studentRepository, times(1)).findById(id);
    }

    @Test
    void getStudentByStudentNumber_shouldReturnStudent_whenStudentExists() {
        // Arrange
        String studentNumber = "S12345";
        Student student = new Student();
        student.setStudentNumber(studentNumber);

        when(studentRepository.findByStudentNumber(studentNumber)).thenReturn(student);

        // Act
        Optional<Student> result = studentService.getStudentByStudentNumber(studentNumber);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(studentNumber, result.get().getStudentNumber());
        verify(studentRepository, times(1)).findByStudentNumber(studentNumber);
    }

    @Test
    void getStudentByStudentNumber_shouldReturnEmptyOptional_whenStudentDoesNotExist() {
        // Arrange
        String studentNumber = "S12345";
        when(studentRepository.findByStudentNumber(studentNumber)).thenReturn(null);

        // Act
        Optional<Student> result = studentService.getStudentByStudentNumber(studentNumber);

        // Assert
        assertFalse(result.isPresent());
        verify(studentRepository, times(1)).findByStudentNumber(studentNumber);
    }

    @Test
    void createStudent_shouldReturnCreatedStudentDTO() {
        // Arrange
        CreateStudentRequest request = new CreateStudentRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setStudentNumber("S12345");

        Student student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setStudentNumber("S12345");

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setFirstName("John");
        studentDTO.setLastName("Doe");
        studentDTO.setStudentNumber("S12345");

        when(studentMapper.toEntity(request)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);

        // Act
        StudentDTO result = studentService.createStudent(request);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("S12345", result.getStudentNumber());
        verify(studentMapper, times(1)).toEntity(request);
        verify(studentRepository, times(1)).save(student);
        verify(studentMapper, times(1)).toDTO(student);
    }

    @Test
    void updateStudent_shouldReturnUpdatedStudentDTO() {
        // Arrange
        Long id = 1L;
        UpdateStudentRequest request = new UpdateStudentRequest();
        request.setFirstName("Jane");
        request.setLastName("Smith");

        Student existingStudent = new Student();
        existingStudent.setId(id);
        existingStudent.setFirstName("John");
        existingStudent.setLastName("Doe");
        existingStudent.setStudentNumber("S12345");

        Student updatedStudent = new Student();
        updatedStudent.setId(id);
        updatedStudent.setFirstName("Jane");
        updatedStudent.setLastName("Smith");
        updatedStudent.setStudentNumber("S12345");

        StudentDTO updatedStudentDTO = new StudentDTO();
        updatedStudentDTO.setId(id);
        updatedStudentDTO.setFirstName("Jane");
        updatedStudentDTO.setLastName("Smith");
        updatedStudentDTO.setStudentNumber("S12345");

        when(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);
        when(studentMapper.toDTO(updatedStudent)).thenReturn(updatedStudentDTO);

        // Act
        StudentDTO result = studentService.updateStudent(id, request);

        // Assert
        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("S12345", result.getStudentNumber());
        verify(studentRepository, times(1)).findById(id);
        verify(studentRepository, times(1)).save(any(Student.class));
        verify(studentMapper, times(1)).toDTO(updatedStudent);
    }

    @Test
    void deleteStudent_shouldCallRepositoryDeleteById() {
        // Arrange
        Long id = 1L;

        // Act
        studentService.deleteStudent(id);

        // Assert
        verify(studentRepository, times(1)).deleteById(id);
    }
}