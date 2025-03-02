package backend.academy.bot.service.handler.impl;

import backend.academy.bot.service.ChatStateService;
import backend.academy.bot.service.handler.UpdateHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnknownCommandHandler implements UpdateHandler {

    private static final String UNKNOWN_COMMAND_MESSAGE = "Unknown command";

    private final TelegramBot bot;
    private final ChatStateService chatStateService;

    @Override
    public void handle(Update update) {
        long chatId = update.message().chat().id();
        chatStateService.cleanChatState(chatId);
        bot.execute(new SendMessage(chatId, UNKNOWN_COMMAND_MESSAGE));
    }
}
