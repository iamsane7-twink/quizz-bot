package iamsane.telegrambot.telegram.command;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandStrategy {
    void execute(Update update);
}
