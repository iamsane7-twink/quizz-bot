package iamsane.telegrambot.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ComponentScan(basePackages = "iamsane.telegrambot.telegram")
@Profile({"dev","prod"})
public class TelegramScan {
}
