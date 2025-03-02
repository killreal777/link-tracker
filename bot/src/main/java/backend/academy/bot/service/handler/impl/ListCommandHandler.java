package backend.academy.bot.service.handler.impl;

import backend.academy.bot.client.ScrapperRestClientApiErrorResponseException;
import backend.academy.bot.client.ScrapperRestClientLinks;
import backend.academy.bot.service.ChatStateService;
import backend.academy.bot.service.handler.CommandHandler;
import backend.academy.bot.service.handler.impl.util.FormatUtils;
import backend.academy.scrapper.api.dto.ListLinksResponse;
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
public class ListCommandHandler implements CommandHandler {

    private final TelegramBot bot;
    private final ChatStateService chatStateService;
    private final ScrapperRestClientLinks scrapperRestClientLinks;

    @Override
    public String commandName() {
        return "list";
    }

    @Override
    public String description() {
        return "вывести текущие подписки";
    }

    @Override
    public void handle(Update update) {
        long chatId = update.message().chat().id();
        listTrackedLinks(chatId);
        chatStateService.cleanChatState(chatId);
    }

    private void listTrackedLinks(long chatId) {
        try {
            ListLinksResponse response =
                    scrapperRestClientLinks.getTrackedLinks(chatId).getBody();
            if (Objects.isNull(response)) {
                log.atInfo()
                        .setMessage("List links response body is null")
                        .addKeyValue("chatId", chatId)
                        .log();
                bot.execute(new SendMessage(chatId, String.format("Ошибка: %s", "Response body is null")));
                return;
            }
            bot.execute(new SendMessage(chatId, FormatUtils.formatListLinksResponse(response)));
        } catch (ScrapperRestClientApiErrorResponseException e) {
            bot.execute(new SendMessage(
                    chatId, String.format("Ошибка: %s", e.response().description())));
        }
    }
}
