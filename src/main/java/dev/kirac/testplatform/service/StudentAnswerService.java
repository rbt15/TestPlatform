package dev.kirac.testplatform.service;

import dev.kirac.testplatform.entity.StudentAnswer;
import dev.kirac.testplatform.repository.StudentAnswerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentAnswerService {

    private final StudentAnswerRepository studentAnswerRepository;

    public StudentAnswerService(StudentAnswerRepository studentAnswerRepository) {
        this.studentAnswerRepository = studentAnswerRepository;
    }

    @Transactional(readOnly = true)
    public List<StudentAnswer> getStudentAnswersByTestParticipationId(Long testParticipationId) {
        return studentAnswerRepository.findByTestParticipationId(testParticipationId);
    }

    @Transactional
    public List<StudentAnswer> saveAllStudentAnswers(List<StudentAnswer> studentAnswers) {
        return studentAnswerRepository.saveAll(studentAnswers);
    }
}
