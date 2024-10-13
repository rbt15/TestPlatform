package dev.kirac.testplatform.service;

import dev.kirac.testplatform.dto.TestDTO;
import dev.kirac.testplatform.dto.request.CreateTestRequest;
import dev.kirac.testplatform.dto.request.UpdateTestRequest;
import dev.kirac.testplatform.entity.Test;
import dev.kirac.testplatform.mapper.TestMapper;
import dev.kirac.testplatform.repository.TestRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    private final TestRepository testRepository;
    private final TestMapper testMapper;

    public TestService(TestRepository testRepository, TestMapper testMapper) {
        this.testRepository = testRepository;
        this.testMapper = testMapper;
    }

    @Cacheable(value = "tests", key = "'all'")
    @Transactional(readOnly = true)
    public List<TestDTO> getAllTests() {
        return testMapper.toDTOList(testRepository.findAll());
    }

    @Cacheable(value = "tests", key = "#id")
    @Transactional(readOnly = true)
    public Optional<Test> getTestById(Long id) {
        return testRepository.findById(id);
    }

    @CacheEvict(value = "tests", allEntries = true)
    @Transactional
    public TestDTO createTest(CreateTestRequest request) {
        Test test = testMapper.toEntity(request);
        Test savedTest = testRepository.save(test);
        return testMapper.toDTO(savedTest);
    }

    @CacheEvict(value = "tests", allEntries = true)
    @Transactional
    public TestDTO updateTest(Long id, UpdateTestRequest request) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Test not found with id: " + id));

        testMapper.updateEntityFromDTO(request, test);
        Test updatedTest = testRepository.save(test);
        return testMapper.toDTO(updatedTest);
    }

    @CacheEvict(value = "tests", allEntries = true)
    @Transactional
    public void deleteTest(Long id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Test not found with id: " + id));

        if (test.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Started tests cannot be deleted");
        }
        testRepository.deleteById(id);
    }
}
