package iamsane.telegrambot.controller;

import iamsane.telegrambot.dto.QuestionDto;
import iamsane.telegrambot.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping(Endpoints.CREATE_QUESTION)
    @ResponseStatus(HttpStatus.CREATED)
    public Long createQuestion(@RequestBody QuestionDto question) {
        log.info(question.toString());

        return questionService.addQuestion(question);
    }

    @GetMapping(Endpoints.FIND_QUESTION)
    public QuestionDto findQuestion(@PathVariable Long id) {
        return questionService.findQuestion(id);
    }

    @GetMapping(Endpoints.FIND_QUESTIONS)
    public List<QuestionDto> findAllQuestions() {
        return questionService.findAllQuestions();
    }

    @DeleteMapping(Endpoints.DELETE_QUESTION)
    public void deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
    }

    @UtilityClass
    static class Endpoints {
        public static final String CREATE_QUESTION = "/api/v1/questions";
        public static final String FIND_QUESTION = "/api/v1/questions/{id}";
        public static final String FIND_QUESTIONS = "/api/v1/questions";
        public static final String DELETE_QUESTION = "/api/v1/questions/{id}";
    }
}
