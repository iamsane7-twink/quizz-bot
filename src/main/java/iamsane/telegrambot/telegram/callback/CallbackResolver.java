package iamsane.telegrambot.telegram.callback;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallbackResolver {
    void resolve(Update update);
}
