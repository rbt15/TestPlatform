package dev.kirac.testplatform.service;

import dev.kirac.testplatform.dto.DetailedTestResultDTO;
import dev.kirac.testplatform.dto.StudentAnswerDTO;
import dev.kirac.testplatform.dto.request.SubmitTestRequest;
import dev.kirac.testplatform.dto.response.TestSubmissionResult;
import dev.kirac.testplatform.entity.*;
import dev.kirac.testplatform.repository.TestParticipationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestParticipationServiceTest {

    @Mock
    private TestParticipationRepository testParticipationRepository;

    @Mock
    private TestService testService;

    @Mock
    private StudentService studentService;

    @Mock
    private QuestionService questionService;

    @Mock
    private StudentAnswerService studentAnswerService;

    @InjectMocks
    private TestParticipationService testParticipationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @org.junit.jupiter.api.Test
    void startTest_shouldCreateTestParticipation() {
        // Arrange
        Long studentId = 1L;
        Long testId = 1L;
        Student student = new Student();
        student.setId(studentId);
        Test test = new Test();
        test.setId(testId);
        test.setStartTime(LocalDateTime.now().minusHours(1));
        test.setEndTime(LocalDateTime.now().plusHours(1));

        when(studentService.getStudentById(studentId)).thenReturn(Optional.of(student));
        when(testService.getTestById(testId)).thenReturn(Optional.of(test));
        when(testParticipationRepository.findByStudentIdAndTestId(studentId, testId)).thenReturn(Optional.empty());

        // Act
        testParticipationService.startTest(studentId, testId);

        // Assert
        verify(testParticipationRepository, times(1)).save(any(TestParticipation.class));
    }

    @org.junit.jupiter.api.Test
    void startTest_shouldThrowException_whenTestAlreadyStarted() {
        // Arrange
        Long studentId = 1L;
        Long testId = 1L;
        Student student = new Student();
        student.setId(studentId);
        Test test = new Test();
        test.setId(testId);

        when(studentService.getStudentById(studentId)).thenReturn(Optional.of(student));
        when(testService.getTestById(testId)).thenReturn(Optional.of(test));
        when(testParticipationRepository.findByStudentIdAndTestId(studentId, testId)).thenReturn(Optional.of(new TestParticipation()));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> testParticipationService.startTest(studentId, testId));
    }

    @org.junit.jupiter.api.Test
    void submitTest_shouldReturnTestSubmissionResult() {
        Long studentId = 1L;
        Long testId = 1L;
        Long questionId = 1L;
        SubmitTestRequest request = new SubmitTestRequest();
        request.setAnswers(List.of(new StudentAnswerDTO(questionId, 42L)));

        Student student = new Student();
        student.setId(studentId);
        Test test = new Test();
        test.setId(testId);
        test.setDuration(60);

        TestParticipation testParticipation = new TestParticipation();
        testParticipation.setStartTime(LocalDateTime.now().minusMinutes(30));
        testParticipation.setStatus(TestParticipation.TestStatus.IN_PROGRESS);

        Question question1 = new Question();
        question1.setPoints(5);
        question1.setId(questionId);

        Choice correctChoice = new Choice();
        correctChoice.setId(42L);
        correctChoice.setIsCorrect(true);

        question1.setChoices(List.of(correctChoice));

        when(studentService.getStudentById(studentId)).thenReturn(Optional.of(student));
        when(testService.getTestById(testId)).thenReturn(Optional.of(test));
        when(testParticipationRepository.findByStudentIdAndTestId(studentId, testId)).thenReturn(Optional.of(testParticipation));
        when(questionService.getQuestionById(any())).thenReturn(Optional.of(question1));
        when(studentAnswerService.saveAllStudentAnswers(any())).thenReturn(Arrays.asList(new StudentAnswer(), new StudentAnswer()));

        TestSubmissionResult result = testParticipationService.submitTest(studentId, testId, request);

        assertNotNull(result);
        assertEquals(1, result.getTotalCount());
        verify(testParticipationRepository, times(1)).save(any(TestParticipation.class));
    }

    @org.junit.jupiter.api.Test
    void submitTest_shouldThrowException_whenTestNotInProgress() {
        // Arrange
        Long studentId = 1L;
        Long testId = 1L;
        SubmitTestRequest request = new SubmitTestRequest();

        TestParticipation testParticipation = new TestParticipation();
        testParticipation.setStatus(TestParticipation.TestStatus.COMPLETED);

        when(studentService.getStudentById(studentId)).thenReturn(Optional.of(new Student()));
        when(testService.getTestById(testId)).thenReturn(Optional.of(new dev.kirac.testplatform.entity.Test()));
        when(testParticipationRepository.findByStudentIdAndTestId(studentId, testId)).thenReturn(Optional.of(testParticipation));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> testParticipationService.submitTest(studentId, testId, request));
    }

    @org.junit.jupiter.api.Test
    void getDetailedTestResult_shouldReturnDetailedTestResultDTO() {
        // Arrange
        Long studentId = 1L;
        Long testId = 1L;

        Student student = new Student();
        student.setId(studentId);
        student.setFirstName("John");
        student.setLastName("Doe");

        Test test = new Test();
        test.setId(testId);
        test.setName("Math Test");

        TestParticipation testParticipation = new TestParticipation();
        testParticipation.setStatus(TestParticipation.TestStatus.COMPLETED);
        testParticipation.setStartTime(LocalDateTime.now().minusHours(1));
        testParticipation.setEndTime(LocalDateTime.now());
        testParticipation.setScore(80);

        Question question = new Question();
        question.setId(1L);
        question.setContent("What is 2+2?");
        question.setPoints(5);
        test.setQuestions(List.of(question));

        Choice choice = new Choice();
        choice.setId(1L);
        choice.setContent("4");
        choice.setIsCorrect(true);

        question.setChoices(List.of(choice));

        StudentAnswer studentAnswer = new StudentAnswer();
        studentAnswer.setQuestion(question);
        studentAnswer.setSelectedChoice(choice);
        studentAnswer.setIsCorrect(true);
        studentAnswer.setPointsEarned(5);

        when(studentService.getStudentById(studentId)).thenReturn(Optional.of(student));
        when(testService.getTestById(testId)).thenReturn(Optional.of(test));
        when(testParticipationRepository.findByStudentIdAndTestId(studentId, testId)).thenReturn(Optional.of(testParticipation));
        when(studentAnswerService.getStudentAnswersByTestParticipationId(testParticipation.getId())).thenReturn(List.of(studentAnswer));

        // Act
        DetailedTestResultDTO result = testParticipationService.getDetailedTestResult(studentId, testId);

        // Assert
        assertNotNull(result);
        assertEquals(testId, result.getTestId());
        assertEquals("Math Test", result.getTestName());
        assertEquals(studentId, result.getStudentId());
        assertEquals("John Doe", result.getStudentName());
        assertEquals(80, result.getEarnedPoints());
        assertEquals(1, result.getQuestionResults().size());
    }

    @org.junit.jupiter.api.Test
    void getDetailedTestResult_shouldThrowException_whenTestNotCompleted() {
        // Arrange
        Long studentId = 1L;
        Long testId = 1L;

        TestParticipation testParticipation = new TestParticipation();
        testParticipation.setStatus(TestParticipation.TestStatus.IN_PROGRESS);

        when(studentService.getStudentById(studentId)).thenReturn(Optional.of(new Student()));
        when(testService.getTestById(testId)).thenReturn(Optional.of(new dev.kirac.testplatform.entity.Test()));
        when(testParticipationRepository.findByStudentIdAndTestId(studentId, testId)).thenReturn(Optional.of(testParticipation));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> testParticipationService.getDetailedTestResult(studentId, testId));
    }
}