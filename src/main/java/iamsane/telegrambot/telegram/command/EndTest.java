package iamsane.telegrambot.telegram.command;

import iamsane.telegrambot.telegram.annotation.TelegramCommand;
import iamsane.telegrambot.telegram.service.TelegramTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

@TelegramCommand("/endTest")
@RequiredArgsConstructor
public class EndTest implements CommandStrategy {
    private final TelegramTestService telegramTestService;

    @Override
    @Transactional
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();

        telegramTestService.stopTest(chatId);
    }
}
