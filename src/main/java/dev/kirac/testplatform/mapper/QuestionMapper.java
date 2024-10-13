package dev.kirac.testplatform.mapper;


import dev.kirac.testplatform.dto.QuestionDTO;
import dev.kirac.testplatform.dto.request.CreateQuestionRequest;
import dev.kirac.testplatform.dto.request.UpdateQuestionRequest;
import dev.kirac.testplatform.entity.Question;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ChoiceMapper.class})
public interface QuestionMapper {

    QuestionDTO toDTO(Question question);

    List<QuestionDTO> toDTOList(List<Question> questions);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "test", ignore = true)
    Question toEntity(CreateQuestionRequest createQuestionRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "test", ignore = true)
    @Mapping(target = "choices", ignore = true)
    void updateEntityFromDTO(UpdateQuestionRequest updateQuestionRequest, @MappingTarget Question question);
}
