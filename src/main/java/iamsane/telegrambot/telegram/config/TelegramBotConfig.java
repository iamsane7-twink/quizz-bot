package iamsane.telegrambot.telegram.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
public class TelegramBotConfig {
    @Value("${telegram-token}")
    private String telegramToken;

    @Bean
    public TelegramBotsLongPollingApplication telegramBotsLongPollingApplication(LongPollingSingleThreadUpdateConsumer bot) throws TelegramApiException {
        var app =  new TelegramBotsLongPollingApplication();

        app.registerBot(telegramToken, bot);

        return app;
    }

    @Bean
    public OkHttpTelegramClient telegramClient() {
        return new OkHttpTelegramClient(telegramToken);
    }

}
