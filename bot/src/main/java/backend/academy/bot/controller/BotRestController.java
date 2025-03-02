package backend.academy.bot.controller;

import backend.academy.bot.api.BotRestApi;
import backend.academy.bot.api.dto.LinkUpdate;
import backend.academy.bot.service.LinkUpdateNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BotRestController implements BotRestApi {

    private final LinkUpdateNotificationService linkUpdateNotificationService;

    @Override
    public ResponseEntity<Void> sendLinkUpdate(LinkUpdate linkUpdate) {
        linkUpdateNotificationService.sendLinkUpdateNotification(linkUpdate);
        return ResponseEntity.ok().build();
    }
}
