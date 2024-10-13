package dev.kirac.testplatform.mapper;

import dev.kirac.testplatform.dto.FullTestDTO;
import dev.kirac.testplatform.dto.TestDTO;
import dev.kirac.testplatform.dto.request.CreateTestRequest;
import dev.kirac.testplatform.dto.request.UpdateTestRequest;
import dev.kirac.testplatform.entity.Test;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface TestMapper {

    TestDTO toDTO(Test test);

    FullTestDTO toFullDTO(Test test);

    List<TestDTO> toDTOList(List<Test> tests);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "questions", ignore = true)
    Test toEntity(CreateTestRequest createTestRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "questions", ignore = true)
    void updateEntityFromDTO(UpdateTestRequest updateTestRequest, @MappingTarget Test test);
}