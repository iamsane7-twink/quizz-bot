package iamsane.telegrambot.service;

import iamsane.telegrambot.dto.QuestionDto;
import iamsane.telegrambot.entity.Question;
import iamsane.telegrambot.exception.NotFoundException;
import iamsane.telegrambot.mapper.QuestionMapper;
import iamsane.telegrambot.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceV1 implements QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    @Override
    public Long addQuestion(QuestionDto dto) {
        Question question = questionMapper.toEntity(dto);

        return questionRepository.save(question).getId();
    }

    @Override
    public QuestionDto findQuestion(Long id) {
        return questionRepository.findById(id)
                .map(questionMapper::toDto)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public List<QuestionDto> findAllQuestions() {
        return questionMapper.toDto(questionRepository.findAll());
    }

    @Override
    public void deleteQuestion(Long id) {
        var question = questionRepository.findById(id)
                        .orElseThrow(NotFoundException::new);

        question.setDeleted(true);

        questionRepository.save(question);
    }
}
