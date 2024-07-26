package iamsane.telegrambot.service;

import iamsane.telegrambot.dto.QuestionDto;

import java.util.List;

public interface QuestionService {
    Long addQuestion(QuestionDto dto);

    QuestionDto findQuestion(Long id);

    List<QuestionDto> findAllQuestions();

    void deleteQuestion(Long id);
}
