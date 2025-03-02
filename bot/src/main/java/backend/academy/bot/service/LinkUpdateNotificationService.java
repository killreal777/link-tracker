package backend.academy.bot.service;

import backend.academy.bot.api.dto.LinkUpdate;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkUpdateNotificationService {

    private final TelegramBot bot;

    public void sendLinkUpdateNotification(LinkUpdate linkUpdate) {
        linkUpdate.tgChatIds().forEach(chatId -> bot.execute(new SendMessage(chatId, linkUpdate.url())));
    }
}
