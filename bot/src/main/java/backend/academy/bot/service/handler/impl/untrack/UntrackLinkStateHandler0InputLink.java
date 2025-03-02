package backend.academy.bot.service.handler.impl.untrack;

import backend.academy.bot.client.ScrapperRestClientApiErrorResponseException;
import backend.academy.bot.client.ScrapperRestClientLinks;
import backend.academy.bot.model.CommandState;
import backend.academy.bot.service.ChatStateService;
import backend.academy.bot.service.handler.StateHandler;
import backend.academy.scrapper.api.dto.LinkResponse;
import backend.academy.scrapper.api.dto.RemoveLinkRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UntrackLinkStateHandler0InputLink implements StateHandler {

    private static final CommandState HANDLED_COMMAND_STATE = UntrackLinkCommandState.INPUT_LINK;

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
        RemoveLinkRequest request = new RemoveLinkRequest(update.message().text());
        untrackLink(chatId, request);
        chatStateService.cleanChatState(chatId);
    }

    private void untrackLink(long chatId, RemoveLinkRequest request) {
        try {
            LinkResponse response =
                    scrapperRestClientLinks.untrackLink(chatId, request).getBody();
            if (Objects.isNull(response)) {
                log.atInfo()
                        .setMessage("Remove link response body is null")
                        .addKeyValue("request", request)
                        .log();
                bot.execute(new SendMessage(chatId, String.format("Ошибка: %s", "Response body is null")));
                return;
            }
            bot.execute(new SendMessage(chatId, String.format("Вы отписались от ссылки: %s", request.link())));
        } catch (ScrapperRestClientApiErrorResponseException e) {
            bot.execute(new SendMessage(
                    chatId, String.format("Ошибка: %s", e.response().description())));
        }
    }
}
