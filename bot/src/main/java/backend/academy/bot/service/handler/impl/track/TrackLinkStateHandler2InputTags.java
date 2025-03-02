package backend.academy.bot.service.handler.impl.track;

import backend.academy.bot.client.ScrapperRestClientApiErrorResponseException;
import backend.academy.bot.client.ScrapperRestClientLinks;
import backend.academy.bot.model.ChatState;
import backend.academy.bot.model.CommandState;
import backend.academy.bot.service.ChatStateService;
import backend.academy.bot.service.handler.StateHandler;
import backend.academy.bot.service.handler.impl.util.FormatUtils;
import backend.academy.scrapper.api.dto.AddLinkRequest;
import backend.academy.scrapper.api.dto.LinkResponse;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrackLinkStateHandler2InputTags implements StateHandler {

    private static final CommandState HANDLED_COMMAND_STATE = TrackLinkCommandState.INPUT_TAGS;

    private static final String NULL_TAGS = "/null";

    private final TelegramBot bot;
    private final ChatStateService chatStateService;
    private final ScrapperRestClientLinks scrapperRestClientLinks;

    @Override
    public CommandState handledState() {
        return HANDLED_COMMAND_STATE;
    }

    @Override
    public void handle(Update update) {
        long chatId = update.message().chat().id();
        ChatState<AddLinkRequest> chatState = chatStateService.getChatState(chatId, AddLinkRequest.class);
        AddLinkRequest request = chatState.data();
        if (!update.message().text().equals(NULL_TAGS)) {
            Set<String> tags =
                    Arrays.stream(update.message().text().split("\\s+")).collect(Collectors.toSet());
            request = request.withTags(tags);
            bot.execute(new SendMessage(chatId, String.format("Получены теги: %s", tags)));
        }
        trackLink(chatId, request);
        chatStateService.cleanChatState(chatId);
    }

    private void trackLink(long chatId, AddLinkRequest request) {
        try {
            LinkResponse response =
                    scrapperRestClientLinks.trackLink(chatId, request).getBody();
            if (Objects.isNull(response)) {
                log.atInfo()
                        .setMessage("Add link response body is null")
                        .addKeyValue("request", request)
                        .log();
                bot.execute(new SendMessage(chatId, String.format("Ошибка: %s", "Response body is null")));
                return;
            }
            Objects.requireNonNull(response);
            String message = String.format("Вы подписаны на ссылку%n%s", FormatUtils.formatLinkResponse(response));
            bot.execute(new SendMessage(chatId, message));
        } catch (ScrapperRestClientApiErrorResponseException e) {
            bot.execute(new SendMessage(
                    chatId, String.format("Ошибка: %s", e.response().description())));
        }
    }
}
