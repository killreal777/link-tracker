package backend.academy.bot;

import com.pengrad.telegrambot.TelegramBot;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record BotConfig(
        @NotEmpty String telegramToken, @NotEmpty @DefaultValue("http://localhost:8081") String scrapperApiBase) {

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(telegramToken);
    }
}
