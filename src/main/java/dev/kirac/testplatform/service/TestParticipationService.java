package dev.kirac.testplatform.service;

import dev.kirac.testplatform.dto.DetailedTestResultDTO;
import dev.kirac.testplatform.dto.FullChoiceDTO;
import dev.kirac.testplatform.dto.QuestionResultDTO;
import dev.kirac.testplatform.dto.StudentAnswerDTO;
import dev.kirac.testplatform.dto.request.SubmitTestRequest;
import dev.kirac.testplatform.dto.response.TestSubmissionResult;
import dev.kirac.testplatform.entity.*;
import dev.kirac.testplatform.repository.TestParticipationRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestParticipationService {

    private final TestParticipationRepository testParticipationRepository;
    private final TestService testService;
    private final StudentService studentService;
    private final QuestionService questionService;
    private final StudentAnswerService studentAnswerService;

    public TestParticipationService(TestParticipationRepository testParticipationRepository,
                                    TestService testService,
                                    StudentService studentService,
                                    QuestionService questionService,
                                    StudentAnswerService studentAnswerService) {
        this.testParticipationRepository = testParticipationRepository;
        this.testService = testService;
        this.studentService = studentService;
        this.questionService = questionService;
        this.studentAnswerService = studentAnswerService;
    }

    @Transactional
    public void startTest(Long studentId, Long testId) {
        Student student = studentService.getStudentById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));

        Test test = testService.getTestById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test not found with id: " + testId));

        if (testParticipationRepository.findByStudentIdAndTestId(studentId, testId).isPresent()) {
            throw new IllegalArgumentException("Test already started for student: " + studentId);
        }

        if (test.getStartTime().isAfter(LocalDateTime.now()) || test.getEndTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Test is not available now");
        }

        TestParticipation testParticipation = new TestParticipation();
        testParticipation.setStudent(student);
        testParticipation.setTest(test);
        testParticipation.setStartTime(LocalDateTime.now());
        testParticipation.setStatus(TestParticipation.TestStatus.IN_PROGRESS);
        testParticipationRepository.save(testParticipation);
    }

    @Transactional
    public TestSubmissionResult submitTest(Long studentId, Long testId, SubmitTestRequest request) {
        studentService.getStudentById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));
        Test test = testService.getTestById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test not found with id: " + testId));
        TestParticipation testParticipation = testParticipationRepository.findByStudentIdAndTestId(studentId, testId)
                .orElseThrow(() -> new IllegalArgumentException("Test participation not found for student: " + studentId + " and test: " + testId));

        if (testParticipation.getStatus() != TestParticipation.TestStatus.IN_PROGRESS) {
            throw new IllegalStateException("Test is not in progress");
        }

        long duration = ChronoUnit.MINUTES.between(testParticipation.getStartTime(), LocalDateTime.now());
        if (duration > test.getDuration()) {
            throw new IllegalStateException("Test duration exceeded");
        }

        List<StudentAnswer> studentAnswers = new ArrayList<>();
        int correctCount = 0, currentPoints = 0, totalPoints = 0;

        for (StudentAnswerDTO answerDTO : request.getAnswers()) {
            Question question = questionService.getQuestionById(answerDTO.getQuestionId())
                    .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + answerDTO.getQuestionId()));
            Choice selectedChoice = question.getChoices().stream()
                    .filter(choice -> choice.getId().equals(answerDTO.getSelectedChoiceId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Choice not found with id: " + answerDTO.getSelectedChoiceId()));

            boolean isCorrect = selectedChoice.getIsCorrect();
            int pointsEarned = isCorrect ? question.getPoints() : 0;

            StudentAnswer studentAnswer = new StudentAnswer();
            studentAnswer.setTestParticipation(testParticipation);
            studentAnswer.setQuestion(question);
            studentAnswer.setSelectedChoice(selectedChoice);
            studentAnswer.setIsCorrect(isCorrect);
            studentAnswer.setPointsEarned(pointsEarned);

            studentAnswers.add(studentAnswer);
            if (isCorrect) correctCount++;
            currentPoints += pointsEarned;
            totalPoints += question.getPoints();
        }

        studentAnswerService.saveAllStudentAnswers(studentAnswers);

        testParticipation.setEndTime(LocalDateTime.now());
        testParticipation.setScore(currentPoints);
        testParticipation.setStatus(TestParticipation.TestStatus.COMPLETED);
        testParticipationRepository.save(testParticipation);

        return new TestSubmissionResult(correctCount, request.getAnswers().size(), currentPoints, totalPoints);
    }

    @Cacheable(value = "results", key = "#studentId + '-' + #testId")
    @Transactional(readOnly = true)
    public DetailedTestResultDTO getDetailedTestResult(Long studentId, Long testId) {
        Student student = studentService.getStudentById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));

        Test test = testService.getTestById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test not found with id: " + testId));

        TestParticipation testParticipation = testParticipationRepository.findByStudentIdAndTestId(studentId, testId)
                .orElseThrow(() -> new IllegalArgumentException("Test participation not found for student: " + studentId + " and test: " + testId));

        if (testParticipation.getStatus() != TestParticipation.TestStatus.COMPLETED) {
            throw new IllegalStateException("Test is not completed yet");
        }

        List<StudentAnswer> studentAnswers = studentAnswerService.getStudentAnswersByTestParticipationId(testParticipation.getId());

        DetailedTestResultDTO result = new DetailedTestResultDTO();
        result.setTestId(test.getId());
        result.setTestName(test.getName());
        result.setStudentId(student.getId());
        result.setStudentName(student.getFirstName() + " " + student.getLastName());
        result.setStartTime(testParticipation.getStartTime());
        result.setEndTime(testParticipation.getEndTime());
        result.setTotalPoints(test.getQuestions().stream().mapToInt(Question::getPoints).sum());
        result.setEarnedPoints(testParticipation.getScore());

        List<QuestionResultDTO> questionResults = new ArrayList<>();
        for (StudentAnswer answer : studentAnswers) {
            QuestionResultDTO questionResult = new QuestionResultDTO();
            questionResult.setQuestionId(answer.getQuestion().getId());
            questionResult.setQuestionContent(answer.getQuestion().getContent());
            questionResult.setQuestionPoints(answer.getQuestion().getPoints());
            questionResult.setSelectedChoiceId(answer.getSelectedChoice().getId());
            questionResult.setSelectedChoiceContent(answer.getSelectedChoice().getContent());
            questionResult.setCorrect(answer.getIsCorrect());
            questionResult.setEarnedPoints(answer.getPointsEarned());

            List<FullChoiceDTO> choices = answer.getQuestion().getChoices().stream()
                    .map(choice -> {
                        FullChoiceDTO choiceDTO = new FullChoiceDTO();
                        choiceDTO.setId(choice.getId());
                        choiceDTO.setContent(choice.getContent());
                        choiceDTO.setCorrect(choice.getIsCorrect());
                        return choiceDTO;
                    })
                    .collect(Collectors.toList());
            questionResult.setAllChoices(choices);

            questionResults.add(questionResult);
        }
        result.setQuestionResults(questionResults);

        return result;
    }
}
