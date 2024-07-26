package iamsane.telegrambot.mapper;

import iamsane.telegrambot.dto.QuestionDto;
import iamsane.telegrambot.entity.Question;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = AnswerMapper.class, collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface QuestionMapper {
    Question toEntity(QuestionDto dto);

    QuestionDto toDto(Question entity);

    List<Question> toEntity(List<QuestionDto> dtos);

    List<QuestionDto> toDto(List<Question> entites);
}
