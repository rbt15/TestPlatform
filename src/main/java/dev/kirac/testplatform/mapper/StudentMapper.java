package dev.kirac.testplatform.mapper;

import dev.kirac.testplatform.dto.StudentDTO;
import dev.kirac.testplatform.dto.request.CreateStudentRequest;
import dev.kirac.testplatform.dto.request.UpdateStudentRequest;
import dev.kirac.testplatform.entity.Student;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentDTO toDTO(Student student);

    List<StudentDTO> toDTOList(List<Student> students);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "testParticipations", ignore = true)
    Student toEntity(CreateStudentRequest createStudentRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "testParticipations", ignore = true)
    void updateEntityFromDTO(UpdateStudentRequest updateStudentRequest, @MappingTarget Student student);
}