package backend.academy.bot.service.handler.impl.untrack;

import backend.academy.bot.model.ChatState;
import backend.academy.bot.model.CommandState;
import backend.academy.bot.service.ChatStateService;
import backend.academy.bot.service.handler.CommandHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UntrackLinkCommandHandler implements CommandHandler {

    private static final CommandState NEXT_COMMAND_STATE = UntrackLinkCommandState.INPUT_LINK;

    private static final String MESSAGE = "Введите ссылку";

    private final TelegramBot bot;
    private final ChatStateService chatStateService;

    @Override
    public String commandName() {
        return "untrack";
    }

    @Override
    public String description() {
        return "отменить подписку на обновления ссылки";
    }

    @Override
    public void handle(Update update) {
        long chatId = update.message().chat().id();
        bot.execute(new SendMessage(chatId, MESSAGE));
        chatStateService.saveChatState(new ChatState<>(chatId, NEXT_COMMAND_STATE));
    }
}
