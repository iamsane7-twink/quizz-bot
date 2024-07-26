package iamsane.telegrambot.mapper;

import iamsane.telegrambot.dto.AnswerDto;
import iamsane.telegrambot.entity.Answer;
import org.mapstruct.Mapper;

@Mapper
public interface AnswerMapper {
    AnswerDto toDto(Answer answer);

    Answer toEntity(AnswerDto dto);
}
