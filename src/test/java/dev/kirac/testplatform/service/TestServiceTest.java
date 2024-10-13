package dev.kirac.testplatform.service;

import dev.kirac.testplatform.dto.TestDTO;
import dev.kirac.testplatform.dto.request.CreateTestRequest;
import dev.kirac.testplatform.dto.request.UpdateTestRequest;
import dev.kirac.testplatform.entity.Test;
import dev.kirac.testplatform.mapper.TestMapper;
import dev.kirac.testplatform.repository.TestRepository;
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

class TestServiceTest {

    @Mock
    private TestRepository testRepository;

    @Mock
    private TestMapper testMapper;

    @InjectMocks
    private TestService testService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @org.junit.jupiter.api.Test
    void getAllTests_shouldReturnListOfTestDTOs() {
        // Arrange
        Test test1 = new Test();
        Test test2 = new Test();
        List<Test> tests = Arrays.asList(test1, test2);
        TestDTO dto1 = new TestDTO();
        TestDTO dto2 = new TestDTO();
        List<TestDTO> expectedDTOs = Arrays.asList(dto1, dto2);

        when(testRepository.findAll()).thenReturn(tests);
        when(testMapper.toDTOList(tests)).thenReturn(expectedDTOs);

        // Act
        List<TestDTO> result = testService.getAllTests();

        // Assert
        assertEquals(2, result.size());
        verify(testRepository, times(1)).findAll();
        verify(testMapper, times(1)).toDTOList(tests);
    }

    @org.junit.jupiter.api.Test
    void getTestById_shouldReturnTest_whenTestExists() {
        // Arrange
        Long id = 1L;
        Test test = new Test();
        test.setId(id);

        when(testRepository.findById(id)).thenReturn(Optional.of(test));

        // Act
        Optional<Test> result = testService.getTestById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(testRepository, times(1)).findById(id);
    }

    @org.junit.jupiter.api.Test
    void getTestById_shouldReturnEmptyOptional_whenTestDoesNotExist() {
        // Arrange
        Long id = 1L;
        when(testRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Test> result = testService.getTestById(id);

        // Assert
        assertFalse(result.isPresent());
        verify(testRepository, times(1)).findById(id);
    }

    @org.junit.jupiter.api.Test
    void createTest_shouldReturnCreatedTestDTO() {
        // Arrange
        CreateTestRequest request = new CreateTestRequest();
        request.setName("Math Test");
        request.setDuration(60);
        request.setStartTime(LocalDateTime.now().plusDays(1));
        request.setEndTime(LocalDateTime.now().plusDays(2));

        Test test = new Test();
        test.setId(1L);
        test.setName("Math Test");
        test.setDuration(60);
        test.setStartTime(request.getStartTime());
        test.setEndTime(request.getEndTime());

        TestDTO testDTO = new TestDTO();
        testDTO.setId(1L);
        testDTO.setName("Math Test");
        testDTO.setDuration(60);
        testDTO.setStartTime(request.getStartTime());
        testDTO.setEndTime(request.getEndTime());

        when(testMapper.toEntity(request)).thenReturn(test);
        when(testRepository.save(test)).thenReturn(test);
        when(testMapper.toDTO(test)).thenReturn(testDTO);

        // Act
        TestDTO result = testService.createTest(request);

        // Assert
        assertNotNull(result);
        assertEquals("Math Test", result.getName());
        assertEquals(60, result.getDuration());
        verify(testMapper, times(1)).toEntity(request);
        verify(testRepository, times(1)).save(test);
        verify(testMapper, times(1)).toDTO(test);
    }

    @org.junit.jupiter.api.Test
    void updateTest_shouldReturnUpdatedTestDTO() {
        // Arrange
        Long id = 1L;
        UpdateTestRequest request = new UpdateTestRequest();
        request.setName("Updated Math Test");
        request.setDuration(90);

        Test existingTest = new Test();
        existingTest.setId(id);
        existingTest.setName("Math Test");
        existingTest.setDuration(60);

        Test updatedTest = new Test();
        updatedTest.setId(id);
        updatedTest.setName("Updated Math Test");
        updatedTest.setDuration(90);

        TestDTO updatedTestDTO = new TestDTO();
        updatedTestDTO.setId(id);
        updatedTestDTO.setName("Updated Math Test");
        updatedTestDTO.setDuration(90);

        when(testRepository.findById(id)).thenReturn(Optional.of(existingTest));
        when(testRepository.save(any(Test.class))).thenReturn(updatedTest);
        when(testMapper.toDTO(updatedTest)).thenReturn(updatedTestDTO);

        // Act
        TestDTO result = testService.updateTest(id, request);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Math Test", result.getName());
        assertEquals(90, result.getDuration());
        verify(testRepository, times(1)).findById(id);
        verify(testRepository, times(1)).save(any(Test.class));
        verify(testMapper, times(1)).toDTO(updatedTest);
    }

    @org.junit.jupiter.api.Test
    void updateTest_shouldThrowException_whenTestNotFound() {
        // Arrange
        Long id = 1L;
        UpdateTestRequest request = new UpdateTestRequest();
        when(testRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> testService.updateTest(id, request));
        verify(testRepository, times(1)).findById(id);
        verify(testRepository, never()).save(any(Test.class));
    }

    @org.junit.jupiter.api.Test
    void deleteTest_shouldDeleteTest_whenTestHasNotStarted() {
        // Arrange
        Long id = 1L;
        Test test = new Test();
        test.setId(id);
        test.setStartTime(LocalDateTime.now().plusDays(1));

        when(testRepository.findById(id)).thenReturn(Optional.of(test));

        // Act
        testService.deleteTest(id);

        // Assert
        verify(testRepository, times(1)).findById(id);
        verify(testRepository, times(1)).deleteById(id);
    }

    @org.junit.jupiter.api.Test
    void deleteTest_shouldThrowException_whenTestHasStarted() {
        // Arrange
        Long id = 1L;
        Test test = new Test();
        test.setId(id);
        test.setStartTime(LocalDateTime.now().minusDays(1));

        when(testRepository.findById(id)).thenReturn(Optional.of(test));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> testService.deleteTest(id));
        verify(testRepository, times(1)).findById(id);
        verify(testRepository, never()).deleteById(id);
    }
}