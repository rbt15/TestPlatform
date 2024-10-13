package dev.kirac.testplatform.service;

import dev.kirac.testplatform.entity.StudentAnswer;
import dev.kirac.testplatform.repository.StudentAnswerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentAnswerServiceTest {

    @Mock
    private StudentAnswerRepository studentAnswerRepository;

    @InjectMocks
    private StudentAnswerService studentAnswerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getStudentAnswersByTestParticipationId_shouldReturnListOfStudentAnswers() {
        // Arrange
        Long testParticipationId = 1L;
        StudentAnswer answer1 = new StudentAnswer();
        StudentAnswer answer2 = new StudentAnswer();
        List<StudentAnswer> expectedAnswers = Arrays.asList(answer1, answer2);

        when(studentAnswerRepository.findByTestParticipationId(testParticipationId)).thenReturn(expectedAnswers);

        // Act
        List<StudentAnswer> result = studentAnswerService.getStudentAnswersByTestParticipationId(testParticipationId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedAnswers, result);
        verify(studentAnswerRepository, times(1)).findByTestParticipationId(testParticipationId);
    }

    @Test
    void getStudentAnswersByTestParticipationId_shouldReturnEmptyList_whenNoAnswersFound() {
        // Arrange
        Long testParticipationId = 1L;
        when(studentAnswerRepository.findByTestParticipationId(testParticipationId)).thenReturn(List.of());

        // Act
        List<StudentAnswer> result = studentAnswerService.getStudentAnswersByTestParticipationId(testParticipationId);

        // Assert
        assertTrue(result.isEmpty());
        verify(studentAnswerRepository, times(1)).findByTestParticipationId(testParticipationId);
    }

    @Test
    void saveAllStudentAnswers_shouldReturnSavedStudentAnswers() {
        // Arrange
        StudentAnswer answer1 = new StudentAnswer();
        StudentAnswer answer2 = new StudentAnswer();
        List<StudentAnswer> answersToSave = Arrays.asList(answer1, answer2);

        when(studentAnswerRepository.saveAll(answersToSave)).thenReturn(answersToSave);

        // Act
        List<StudentAnswer> result = studentAnswerService.saveAllStudentAnswers(answersToSave);

        // Assert
        assertEquals(2, result.size());
        assertEquals(answersToSave, result);
        verify(studentAnswerRepository, times(1)).saveAll(answersToSave);
    }

    @Test
    void saveAllStudentAnswers_shouldReturnEmptyList_whenNoAnswersProvided() {
        // Arrange
        List<StudentAnswer> emptyList = List.of();

        when(studentAnswerRepository.saveAll(emptyList)).thenReturn(emptyList);

        // Act
        List<StudentAnswer> result = studentAnswerService.saveAllStudentAnswers(emptyList);

        // Assert
        assertTrue(result.isEmpty());
        verify(studentAnswerRepository, times(1)).saveAll(emptyList);
    }
}