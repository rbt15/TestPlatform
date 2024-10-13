package dev.kirac.testplatform.service;

import dev.kirac.testplatform.dto.StudentDTO;
import dev.kirac.testplatform.dto.request.CreateStudentRequest;
import dev.kirac.testplatform.dto.request.UpdateStudentRequest;
import dev.kirac.testplatform.entity.Student;
import dev.kirac.testplatform.mapper.StudentMapper;
import dev.kirac.testplatform.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Student> getStudentByStudentNumber(String studentNumber) {
        return Optional.ofNullable(studentRepository.findByStudentNumber(studentNumber));
    }

    @Transactional
    public StudentDTO createStudent(CreateStudentRequest request) {
        Student student = studentMapper.toEntity(request);
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDTO(savedStudent);
    }

    @Transactional
    public StudentDTO updateStudent(Long id, UpdateStudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));

        studentMapper.updateEntityFromDTO(request, student);
        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toDTO(updatedStudent);
    }

    @Transactional
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
