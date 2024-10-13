package dev.kirac.testplatform.service;

import dev.kirac.testplatform.dto.QuestionDTO;
import dev.kirac.testplatform.dto.request.CreateQuestionRequest;
import dev.kirac.testplatform.dto.request.UpdateQuestionRequest;
import dev.kirac.testplatform.entity.Question;
import dev.kirac.testplatform.entity.Test;
import dev.kirac.testplatform.mapper.QuestionMapper;
import dev.kirac.testplatform.repository.QuestionRepository;
import dev.kirac.testplatform.repository.TestRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final TestRepository testRepository;
    private final QuestionMapper questionMapper;

    public QuestionService(QuestionRepository questionRepository, TestRepository testRepository, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.testRepository = testRepository;
        this.questionMapper = questionMapper;
    }

    @Transactional(readOnly = true)
    public List<QuestionDTO> getQuestionsByTestId(Long testId) {
        return questionMapper.toDTOList(questionRepository.findByTestId(testId));
    }

    @Transactional(readOnly = true)
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    @CacheEvict(value = "tests", allEntries = true)
    @Transactional
    public QuestionDTO createQuestion(CreateQuestionRequest request) {
        Test test = testRepository.findById(request.getTestId())
                .orElseThrow(() -> new IllegalArgumentException("Test not found with id: " + request.getTestId()));

        Question question = questionMapper.toEntity(request);
        question.setTest(test);
        question.getChoices().forEach(choice -> choice.setQuestion(question));
        Question savedQuestion = questionRepository.save(question);
        return questionMapper.toDTO(savedQuestion);
    }

    @CacheEvict(value = "tests", allEntries = true)
    @Transactional
    public QuestionDTO updateQuestion(Long id, UpdateQuestionRequest request) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + id));

        questionMapper.updateEntityFromDTO(request, question);
        Question updatedQuestion = questionRepository.save(question);
        return questionMapper.toDTO(updatedQuestion);
    }

    @CacheEvict(value = "tests", allEntries = true)
    @Transactional
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}
