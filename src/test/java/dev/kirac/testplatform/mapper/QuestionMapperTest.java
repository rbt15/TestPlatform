package dev.kirac.testplatform.mapper;

import dev.kirac.testplatform.dto.QuestionDTO;
import dev.kirac.testplatform.dto.request.CreateQuestionRequest;
import dev.kirac.testplatform.dto.request.UpdateQuestionRequest;
import dev.kirac.testplatform.entity.Choice;
import dev.kirac.testplatform.entity.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionMapperTest {

    @Autowired
    private QuestionMapper questionMapper;

    @Test
    void toDTO_shouldMapQuestionToQuestionDTO() {
        Question question = new Question();
        question.setId(1L);
        question.setContent("Test Question");
        question.setPoints(5);

        Choice choice1 = new Choice();
        choice1.setId(1L);
        choice1.setContent("Choice 1");
        choice1.setIsCorrect(true);

        Choice choice2 = new Choice();
        choice2.setId(2L);
        choice2.setContent("Choice 2");
        choice2.setIsCorrect(false);

        question.setChoices(Arrays.asList(choice1, choice2));

        QuestionDTO dto = questionMapper.toDTO(question);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Question", dto.getContent());
        assertEquals(5, dto.getPoints());
        assertNotNull(dto.getChoices());
        assertEquals(2, dto.getChoices().size());
        assertEquals("Choice 1", dto.getChoices().get(0).getContent());
        assertEquals("Choice 2", dto.getChoices().get(1).getContent());
    }

    @Test
    void toDTOList_shouldMapQuestionListToQuestionDTOList() {
        Question question1 = new Question();
        question1.setId(1L);
        question1.setContent("Question 1");
        question1.setPoints(5);

        Question question2 = new Question();
        question2.setId(2L);
        question2.setContent("Question 2");
        question2.setPoints(10);

        List<Question> questions = Arrays.asList(question1, question2);

        List<QuestionDTO> dtos = questionMapper.toDTOList(questions);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("Question 1", dtos.get(0).getContent());
        assertEquals("Question 2", dtos.get(1).getContent());
    }

    @Test
    void toEntity_shouldMapCreateQuestionRequestToQuestion() {
        CreateQuestionRequest request = new CreateQuestionRequest();
        request.setContent("New Question");
        request.setPoints(15);

        Question question = questionMapper.toEntity(request);

        assertNotNull(question);
        assertNull(question.getId());
        assertEquals("New Question", question.getContent());
        assertEquals(15, question.getPoints());
        assertNull(question.getTest());
    }

    @Test
    void updateEntityFromDTO_shouldUpdateQuestionFromUpdateQuestionRequest() {
        Question question = new Question();
        question.setId(1L);
        question.setContent("Old Question");
        question.setPoints(5);

        UpdateQuestionRequest request = new UpdateQuestionRequest();
        request.setContent("Updated Question");
        request.setPoints(10);

        questionMapper.updateEntityFromDTO(request, question);

        assertEquals(1L, question.getId());
        assertEquals("Updated Question", question.getContent());
        assertEquals(10, question.getPoints());
    }
}