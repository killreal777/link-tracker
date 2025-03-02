package backend.academy.bot.service.handler.impl.track;

import backend.academy.bot.model.ChatState;
import backend.academy.bot.model.CommandState;
import backend.academy.bot.service.ChatStateService;
import backend.academy.bot.service.handler.StateHandler;
import backend.academy.scrapper.api.dto.AddLinkRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrackLinkStateHandler1InputFilters implements StateHandler {

    private static final CommandState HANDLED_COMMAND_STATE = TrackLinkCommandState.INPUT_FILTERS;
    private static final CommandState NEXT_COMMAND_STATE = TrackLinkCommandState.INPUT_TAGS;

    private static final String MESSAGE_TAGS =
            """
            Введите теги
            Если не хотите вводить теги, введите /null""";

    private static final String NULL_FILTERS = "/null";

    private final TelegramBot bot;
    private final ChatStateService chatStateService;

    @Override
    public CommandState handledState() {
        return HANDLED_COMMAND_STATE;
    }

    @Override
    public void handle(Update update) {
        long chatId = update.message().chat().id();
        ChatState<AddLinkRequest> chatState = chatStateService.getChatState(chatId, AddLinkRequest.class);
        AddLinkRequest request = chatState.data();
        if (!update.message().text().equals(NULL_FILTERS)) {
            Set<String> filters =
                    Arrays.stream(update.message().text().split("\\s+")).collect(Collectors.toSet());
            request = request.withFilters(filters);
            bot.execute(new SendMessage(chatId, String.format("Получены фильтры: %s", filters)));
        }
        chatStateService.saveChatState(new ChatState<>(chatId, NEXT_COMMAND_STATE, request));
        bot.execute(new SendMessage(chatId, MESSAGE_TAGS));
    }
}
