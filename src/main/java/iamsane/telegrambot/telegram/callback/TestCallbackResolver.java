package iamsane.telegrambot.telegram.callback;

import iamsane.telegrambot.entity.Answer;
import iamsane.telegrambot.repository.TestRepository;
import iamsane.telegrambot.telegram.service.TelegramTestService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component("testCallback")
@AllArgsConstructor
@Slf4j
public class TestCallbackResolver implements CallbackResolver {
    private final TestRepository testRepository;
    private final TelegramTestService telegramTestService;

    @Override
    @SneakyThrows
    @Transactional
    public void resolve(Update update) {
        var message = update.getCallbackQuery().getMessage();

        String messageData = update.getCallbackQuery().getData();

        String chatId = message.getChatId().toString();

        var test = testRepository.findByChatIdAndIsFinished(chatId, false)
                .orElseThrow(() -> {
                    telegramTestService.sendNoActiveTest(chatId);
                    return new RuntimeException();
                });

        int currentQuestion = test.getCurrentQuestion();

        Answer answer = test.getQuestions()
                .get(currentQuestion)
                .getAnswers()
                .stream()
                .filter(x -> x.getId() == Integer.parseInt(messageData))
                .findFirst()
                .orElseThrow();

        test.getAnswers().add(answer);

        if(answer.getIsCorrect()) test.setCorrectAnswers(1 + test.getCorrectAnswers());

        var questions = test.getQuestions();

        telegramTestService.sendAnswerReply(chatId, answer);

        if(questions.size() <= ++currentQuestion) {
            test.setFinished(true);

            testRepository.save(test);

            telegramTestService.sendTestResult(chatId, test);
            return;
        }

        var question = questions.get(currentQuestion);

        test.setCurrentQuestion(currentQuestion);

        testRepository.save(test);

        telegramTestService.sendQuestion(chatId, question, test.getCurrentQuestion()+1);
    }
}
