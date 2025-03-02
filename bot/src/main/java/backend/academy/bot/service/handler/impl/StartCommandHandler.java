package backend.academy.bot.service.handler.impl;

import backend.academy.bot.service.ChatStateService;
import backend.academy.bot.service.handler.CommandHandler;
import backend.academy.scrapper.api.ScrapperRestApiTgChat;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements CommandHandler {

    private static final String GREETING_MESSAGE =
            """
            LinkTracker Bot

            Author @killreal777
            https://github.com/killreal777

            Бот позволяет отслеживать обновления в репозиториях GitHub и вопросах StackOverflow

            Для получения справки по командам введите /help""";

    private final TelegramBot bot;
    private final ChatStateService chatStateService;
    private final ScrapperRestApiTgChat scrapperRestApiTgChat;

    @Override
    public String commandName() {
        return "start";
    }

    @Override
    public String description() {
        return "зарегистрироваться и начать работу с ботом";
    }

    @Override
    public void handle(Update update) {
        long chatId = update.message().chat().id();
        scrapperRestApiTgChat.registerTgChat(chatId);
        chatStateService.cleanChatState(chatId);
        bot.execute(new SendMessage(chatId, GREETING_MESSAGE));
    }
}
