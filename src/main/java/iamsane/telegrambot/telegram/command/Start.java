package iamsane.telegrambot.telegram.command;

import iamsane.telegrambot.telegram.annotation.TelegramCommand;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@TelegramCommand("/start")
@RequiredArgsConstructor
public class Start implements CommandStrategy {
    private final OkHttpTelegramClient client;

    @SneakyThrows
    public void execute(Update update) {
        var text =  """
                Nice to see you, %s!
                
                Use /startTest to start the test
                """;

        text = text.formatted(update.getMessage().getChat().getFirstName());

        String chatId = update.getMessage().getChatId().toString();

        var message = new SendMessage(chatId, text);

        client.execute(message);
    }
}
