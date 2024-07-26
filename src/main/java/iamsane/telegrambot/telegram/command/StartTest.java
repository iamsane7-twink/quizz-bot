package iamsane.telegrambot.telegram.command;

import iamsane.telegrambot.telegram.annotation.TelegramCommand;
import iamsane.telegrambot.telegram.service.TelegramTestService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.Update;

@TelegramCommand("/startTest")
@RequiredArgsConstructor
public class StartTest implements CommandStrategy {
    private final TelegramTestService telegramTestService;

    @SneakyThrows
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();

        telegramTestService.startTest(chatId);
    }
}
