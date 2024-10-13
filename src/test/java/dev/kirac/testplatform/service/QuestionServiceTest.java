package dev.kirac.testplatform.service;

import dev.kirac.testplatform.dto.QuestionDTO;
import dev.kirac.testplatform.dto.request.CreateChoiceRequest;
import dev.kirac.testplatform.dto.request.CreateQuestionRequest;
import dev.kirac.testplatform.dto.request.UpdateQuestionRequest;
import dev.kirac.testplatform.entity.Choice;
import dev.kirac.testplatform.entity.Question;
import dev.kirac.testplatform.entity.Test;
import dev.kirac.testplatform.mapper.QuestionMapper;
import dev.kirac.testplatform.repository.QuestionRepository;
import dev.kirac.testplatform.repository.TestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private TestRepository testRepository;

    @Mock
    private QuestionMapper questionMapper;

    @InjectMocks
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @org.junit.jupiter.api.Test
    void getQuestionsByTestId_shouldReturnListOfQuestions() {
        // Arrange
        Long testId = 1L;
        Question question1 = new Question();
        Question question2 = new Question();
        List<Question> questions = Arrays.asList(question1, question2);
        QuestionDTO dto1 = new QuestionDTO();
        QuestionDTO dto2 = new QuestionDTO();
        List<QuestionDTO> expectedDTOs = Arrays.asList(dto1, dto2);

        when(questionRepository.findByTestId(testId)).thenReturn(questions);
        when(questionMapper.toDTOList(questions)).thenReturn(expectedDTOs);

        // Act
        List<QuestionDTO> result = questionService.getQuestionsByTestId(testId);

        // Assert
        assertEquals(2, result.size());
        verify(questionRepository, times(1)).findByTestId(testId);
        verify(questionMapper, times(1)).toDTOList(questions);
    }

    @org.junit.jupiter.api.Test
    void getQuestionById_shouldReturnQuestion_whenQuestionExists() {
        // Arrange
        Long id = 1L;
        Question question = new Question();
        question.setId(id);

        when(questionRepository.findById(id)).thenReturn(Optional.of(question));

        // Act
        Optional<Question> result = questionService.getQuestionById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(questionRepository, times(1)).findById(id);
    }

    @org.junit.jupiter.api.Test
    void getQuestionById_shouldReturnEmptyOptional_whenQuestionDoesNotExist() {
        // Arrange
        Long id = 1L;
        when(questionRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Question> result = questionService.getQuestionById(id);

        // Assert
        assertFalse(result.isPresent());
        verify(questionRepository, times(1)).findById(id);
    }

    @org.junit.jupiter.api.Test
    void createQuestion_shouldReturnCreatedQuestionDTO() {
        // Arrange
        CreateQuestionRequest request = new CreateQuestionRequest();
        request.setContent("What is 2+2?");
        request.setPoints(5);
        request.setTestId(1L);

        Test test = new Test();
        test.setId(1L);

        Question question = new Question();
        question.setId(1L);
        question.setContent("What is 2+2?");
        question.setPoints(5);
        question.setTest(test);

        Choice choice = new Choice();
        choice.setId(1L);
        choice.setContent("4");
        choice.setIsCorrect(true);
        question.setChoices(List.of(choice));

        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(1L);
        questionDTO.setContent("What is 2+2?");
        questionDTO.setPoints(5);

        when(testRepository.findById(1L)).thenReturn(Optional.of(test));
        when(questionMapper.toEntity(request)).thenReturn(question);
        when(questionRepository.save(question)).thenReturn(question);
        when(questionMapper.toDTO(question)).thenReturn(questionDTO);

        // Act
        QuestionDTO result = questionService.createQuestion(request);

        // Assert
        assertNotNull(result);
        assertEquals("What is 2+2?", result.getContent());
        assertEquals(5, result.getPoints());
        verify(testRepository, times(1)).findById(1L);
        verify(questionMapper, times(1)).toEntity(request);
        verify(questionRepository, times(1)).save(question);
        verify(questionMapper, times(1)).toDTO(question);
    }

    @org.junit.jupiter.api.Test
    void createQuestion_shouldThrowException_whenTestNotFound() {
        // Arrange
        CreateQuestionRequest request = new CreateQuestionRequest();
        request.setTestId(1L);

        when(testRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> questionService.createQuestion(request));
        verify(testRepository, times(1)).findById(1L);
        verify(questionRepository, never()).save(any(Question.class));
    }

    @org.junit.jupiter.api.Test
    void updateQuestion_shouldReturnUpdatedQuestionDTO() {
        // Arrange
        Long id = 1L;
        UpdateQuestionRequest request = new UpdateQuestionRequest();
        request.setContent("Updated question");
        request.setPoints(10);

        Question existingQuestion = new Question();
        existingQuestion.setId(id);
        existingQuestion.setContent("Original question");
        existingQuestion.setPoints(5);

        Question updatedQuestion = new Question();
        updatedQuestion.setId(id);
        updatedQuestion.setContent("Updated question");
        updatedQuestion.setPoints(10);

        QuestionDTO updatedQuestionDTO = new QuestionDTO();
        updatedQuestionDTO.setId(id);
        updatedQuestionDTO.setContent("Updated question");
        updatedQuestionDTO.setPoints(10);

        when(questionRepository.findById(id)).thenReturn(Optional.of(existingQuestion));
        when(questionRepository.save(any(Question.class))).thenReturn(updatedQuestion);
        when(questionMapper.toDTO(updatedQuestion)).thenReturn(updatedQuestionDTO);

        // Act
        QuestionDTO result = questionService.updateQuestion(id, request);

        // Assert
        assertNotNull(result);
        assertEquals("Updated question", result.getContent());
        assertEquals(10, result.getPoints());
        verify(questionRepository, times(1)).findById(id);
        verify(questionRepository, times(1)).save(any(Question.class));
        verify(questionMapper, times(1)).toDTO(updatedQuestion);
    }

    @org.junit.jupiter.api.Test
    void updateQuestion_shouldThrowException_whenQuestionNotFound() {
        // Arrange
        Long id = 1L;
        UpdateQuestionRequest request = new UpdateQuestionRequest();
        when(questionRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> questionService.updateQuestion(id, request));
        verify(questionRepository, times(1)).findById(id);
        verify(questionRepository, never()).save(any(Question.class));
    }

    @org.junit.jupiter.api.Test
    void deleteQuestion_shouldCallRepositoryDeleteById() {
        // Arrange
        Long id = 1L;

        // Act
        questionService.deleteQuestion(id);

        // Assert
        verify(questionRepository, times(1)).deleteById(id);
    }
}