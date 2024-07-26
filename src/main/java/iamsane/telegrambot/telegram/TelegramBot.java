package iamsane.telegrambot.telegram;

import iamsane.telegrambot.telegram.callback.CallbackResolver;
import iamsane.telegrambot.telegram.command.CommandStrategy;
import iamsane.telegrambot.telegram.config.TelegramBotConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.updates.GetUpdates;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {
    @Autowired
    private Map<String, CommandStrategy> commands = new HashMap<>();
    private final CallbackResolver callbackResolver;

    private final OkHttpTelegramClient client;

    @Override
    @SneakyThrows
    public void consume(Update update) {
        if (update.hasCallbackQuery()) {
            callbackResolver.resolve(update);
            return;
        }

        if(!update.hasMessage() || !update.getMessage().hasText()) {
            log.info("Update is empty");
            return;
        }

        log.info(update.getMessage().getText());

        GetUpdates getUpdates = new GetUpdates();
        getUpdates.setOffset(0);
        getUpdates.setLimit(Integer.MAX_VALUE);
        var list = client.execute(getUpdates);

        for(Update i : list) {
            log.info("Message: {}", i.getMessage().getText());
        }

        var strategy = commands.get(update.getMessage().getText());

        strategy.execute(update);
    }
}
