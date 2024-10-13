package dev.kirac.testplatform.mapper;

import dev.kirac.testplatform.dto.ChoiceDTO;
import dev.kirac.testplatform.dto.request.CreateChoiceRequest;
import dev.kirac.testplatform.entity.Choice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChoiceMapper {

    ChoiceDTO toDTO(Choice choice);

    List<ChoiceDTO> toDTOList(List<Choice> choices);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", ignore = true)
    Choice toEntity(CreateChoiceRequest createChoiceRequest);

    List<Choice> toEntityList(List<CreateChoiceRequest> choices);
}
