package iamsane.telegrambot.telegram.service;

import iamsane.telegrambot.entity.Answer;
import iamsane.telegrambot.entity.Question;
import iamsane.telegrambot.entity.Test;
import iamsane.telegrambot.repository.QuestionRepository;
import iamsane.telegrambot.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Service
@RequiredArgsConstructor
public class TelegramTestServiceV1 implements TelegramTestService {
    private static final String TEST_ALREADY_EXISTS_TEMPLATE = """
            Test already exists!
            """;
    private static final String QUESTION_TEMPLATE = """
            Question %d:
            
            %s
            """;

    private static final String ANSWER_TEMPLATE = """
            𝙔𝙤𝙪𝙧 𝙖𝙣𝙨𝙬𝙚𝙧:
            %-20s
                        
            %s
            
            %s
            """;
    private static final String TEST_RESULT_TEMPLATE = """
            Test finished!
            
            𝙏𝙤𝙩𝙖𝙡 𝙦𝙪𝙚𝙨𝙩𝙞𝙤𝙣𝙨: %d
            𝘾𝙤𝙧𝙧𝙚𝙘𝙩 𝙖𝙣𝙨𝙬𝙚𝙧𝙨: %d
            """;
    private static final String NO_ACTIVE_TEST_TEMPLATE = """
            No active test!
            
            Use /startTest to start a new test.
            """;

    private final OkHttpTelegramClient client;
    private final QuestionRepository questionRepository;
    private final TestRepository testRepository;

    @Override
    @Transactional
    public Long startTest(String chatId) {
        if(testRepository.existsByChatIdAndIsFinished(chatId, false)) {
            sendTestAlreadyExists(chatId);
            throw new RuntimeException();
        }

        var questions = questionRepository.findQuestions(30);

        Test test = Test.builder()
                .chatId(chatId)
                .questions(questions)
                .build();

        sendQuestion(chatId, test.getQuestions().get(0), 1);

        return testRepository.save(test).getId();
    }

    @Override
    @Transactional
    public void stopTest(String chatId) {
        Test test = testRepository.findByChatIdAndIsFinished(chatId, false)
                .orElseThrow(() -> {
                    sendNoActiveTest(chatId);
                    return new RuntimeException();
                });

        test.setFinished(true);

        testRepository.save(test);

        sendTestResult(chatId, test);
    }

    @Override
    @SneakyThrows
    public void sendQuestion(String chatId, Question question, int questionNumber) {
        var sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(QUESTION_TEMPLATE.formatted(questionNumber, question.getQuestionText()))
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(question.getAnswers()
                                .stream()
                                .map(x -> new InlineKeyboardRow(
                                        InlineKeyboardButton.builder()
                                                .text(x.getAnswerText())
                                                .callbackData(x.getId().toString())
                                                .build()
                                ))
                                .toList())
                        .build())
                .build();

        client.execute(sendMessage);
    }

    @Override
    @SneakyThrows
    public void sendAnswerReply(String chatId, Answer answer) {
        var message = SendMessage.builder()
                .chatId(chatId)
                .text(ANSWER_TEMPLATE.formatted(answer.getAnswerText(), answer.getAnswerReply(), answer.getIsCorrect() ? "✅ 𝘾𝙤𝙧𝙧𝙚𝙘𝙩 " : "❌ 𝙒𝙧𝙤𝙣𝙜"))
                .build();

        client.execute(message);
    }

    @Override
    @SneakyThrows
    public void sendTestAlreadyExists(String chatId) {
        var message = SendMessage.builder()
                .chatId(chatId)
                .text(TEST_ALREADY_EXISTS_TEMPLATE)
                .build();

        client.execute(message);
    }

    @Override
    @SneakyThrows
    public void sendNoActiveTest(String chatId) {
        var message = SendMessage.builder()
                .chatId(chatId)
                .text(NO_ACTIVE_TEST_TEMPLATE)
                .build();

        client.execute(message);
    }

    @Override
    @SneakyThrows
    public void sendTestResult(String chatId, Test test) {
        var sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(TEST_RESULT_TEMPLATE.formatted(test.getQuestions().size(), test.getCorrectAnswers()))
                .build();

        client.execute(sendMessage);
    }
}
