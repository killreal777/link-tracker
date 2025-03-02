package backend.academy.bot.service.handler.impl.track;

import backend.academy.bot.model.ChatState;
import backend.academy.bot.model.CommandState;
import backend.academy.bot.service.ChatStateService;
import backend.academy.bot.service.handler.StateHandler;
import backend.academy.scrapper.api.dto.AddLinkRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrackLinkStateHandler0InputLink implements StateHandler {

    private static final CommandState HANDLED_COMMAND_STATE = TrackLinkCommandState.INPUT_LINK;
    private static final CommandState NEXT_COMMAND_STATE = TrackLinkCommandState.INPUT_FILTERS;

    private static final String MESSAGE_FILTERS =
            """
            Введите фильтры через пробел
            Если не хотите вводить фильтры, введите /null""";

    private final TelegramBot bot;
    private final ChatStateService chatStateService;

    @Override
    public CommandState handledState() {
        return HANDLED_COMMAND_STATE;
    }

    @Override
    public void handle(Update update) {
        long chatId = update.message().chat().id();
        String link = update.message().text();
        AddLinkRequest request = new AddLinkRequest(link);
        chatStateService.saveChatState(new ChatState<>(chatId, NEXT_COMMAND_STATE, request));
        bot.execute(new SendMessage(chatId, String.format("Получена ссылка: %s", link)));
        bot.execute(new SendMessage(chatId, MESSAGE_FILTERS));
    }
}
