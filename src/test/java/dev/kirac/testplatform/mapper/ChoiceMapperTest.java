package dev.kirac.testplatform.mapper;

import dev.kirac.testplatform.dto.ChoiceDTO;
import dev.kirac.testplatform.dto.request.CreateChoiceRequest;
import dev.kirac.testplatform.entity.Choice;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChoiceMapperTest {

    private final ChoiceMapper choiceMapper = Mappers.getMapper(ChoiceMapper.class);

    @Test
    void toDTO_shouldMapChoiceToChoiceDTO() {
        Choice choice = new Choice();
        choice.setId(1L);
        choice.setContent("Test Choice");
        choice.setIsCorrect(true);

        ChoiceDTO dto = choiceMapper.toDTO(choice);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Choice", dto.getContent());
    }

    @Test
    void toDTOList_shouldMapChoiceListToChoiceDTOList() {
        Choice choice1 = new Choice();
        choice1.setId(1L);
        choice1.setContent("Choice 1");

        Choice choice2 = new Choice();
        choice2.setId(2L);
        choice2.setContent("Choice 2");

        List<Choice> choices = Arrays.asList(choice1, choice2);

        List<ChoiceDTO> dtos = choiceMapper.toDTOList(choices);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("Choice 1", dtos.get(0).getContent());
        assertEquals("Choice 2", dtos.get(1).getContent());
    }

    @Test
    void toEntity_shouldMapCreateChoiceRequestToChoice() {
        CreateChoiceRequest request = new CreateChoiceRequest();
        request.setContent("New Choice");
        request.setIsCorrect(true);

        Choice choice = choiceMapper.toEntity(request);

        assertNotNull(choice);
        assertNull(choice.getId());
        assertEquals("New Choice", choice.getContent());
        assertTrue(choice.getIsCorrect());
    }

    @Test
    void toEntityList_shouldMapCreateChoiceRequestListToChoiceList() {
        CreateChoiceRequest request1 = new CreateChoiceRequest();
        request1.setContent("Choice 1");
        request1.setIsCorrect(true);

        CreateChoiceRequest request2 = new CreateChoiceRequest();
        request2.setContent("Choice 2");
        request2.setIsCorrect(false);

        List<CreateChoiceRequest> requests = Arrays.asList(request1, request2);

        List<Choice> choices = choiceMapper.toEntityList(requests);

        assertNotNull(choices);
        assertEquals(2, choices.size());
        assertEquals("Choice 1", choices.get(0).getContent());
        assertTrue(choices.get(0).getIsCorrect());
        assertEquals("Choice 2", choices.get(1).getContent());
        assertFalse(choices.get(1).getIsCorrect());
    }
}