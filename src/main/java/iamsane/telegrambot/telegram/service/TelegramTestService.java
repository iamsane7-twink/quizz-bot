package iamsane.telegrambot.telegram.service;

import iamsane.telegrambot.entity.Answer;
import iamsane.telegrambot.entity.Question;
import iamsane.telegrambot.entity.Test;

public interface TelegramTestService {
    Long startTest(String chatId);

    void stopTest(String chatId);

    void sendQuestion(String chatId, Question question, int questionNumber);

    void sendAnswerReply(String chatId, Answer answer);

    void sendTestAlreadyExists(String chatId);

    void sendNoActiveTest(String chatId);

    void sendTestResult(String chatId, Test test);
}
