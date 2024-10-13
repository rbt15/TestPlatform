package dev.kirac.testplatform.mapper;

import dev.kirac.testplatform.dto.StudentDTO;
import dev.kirac.testplatform.dto.request.CreateStudentRequest;
import dev.kirac.testplatform.dto.request.UpdateStudentRequest;
import dev.kirac.testplatform.entity.Student;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentMapperTest {

    private final StudentMapper studentMapper = Mappers.getMapper(StudentMapper.class);

    @Test
    void toDTO_shouldMapStudentToStudentDTO() {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setStudentNumber("S12345");

        StudentDTO dto = studentMapper.toDTO(student);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("S12345", dto.getStudentNumber());
    }

    @Test
    void toDTOList_shouldMapStudentListToStudentDTOList() {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setStudentNumber("S12345");

        Student student2 = new Student();
        student2.setId(2L);
        student2.setFirstName("Jane");
        student2.setLastName("Smith");
        student2.setStudentNumber("S67890");

        List<Student> students = Arrays.asList(student1, student2);

        List<StudentDTO> dtos = studentMapper.toDTOList(students);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("John", dtos.get(0).getFirstName());
        assertEquals("Jane", dtos.get(1).getFirstName());
    }

    @Test
    void toEntity_shouldMapCreateStudentRequestToStudent() {
        CreateStudentRequest request = new CreateStudentRequest();
        request.setFirstName("Alice");
        request.setLastName("Johnson");
        request.setStudentNumber("S11111");

        Student student = studentMapper.toEntity(request);

        assertNotNull(student);
        assertNull(student.getId());
        assertEquals("Alice", student.getFirstName());
        assertEquals("Johnson", student.getLastName());
        assertEquals("S11111", student.getStudentNumber());
        assertNull(student.getTestParticipations());
    }

    @Test
    void updateEntityFromDTO_shouldUpdateStudentFromUpdateStudentRequest() {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setStudentNumber("S12345");

        UpdateStudentRequest request = new UpdateStudentRequest();
        request.setFirstName("Johnny");
        request.setLastName("Smith");

        studentMapper.updateEntityFromDTO(request, student);

        assertEquals(1L, student.getId());
        assertEquals("Johnny", student.getFirstName());
        assertEquals("Smith", student.getLastName());
        assertEquals("S12345", student.getStudentNumber());
    }
}