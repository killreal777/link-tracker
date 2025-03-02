package backend.academy.scrapper.controller;

import backend.academy.scrapper.api.ScrapperRestApiTgChat;
import backend.academy.scrapper.service.TgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScrapperRestControllerTgChat implements ScrapperRestApiTgChat {

    private final TgChatService tgChatService;

    @Override
    public ResponseEntity<Void> deleteTgChat(Long tgChatId) {
        tgChatService.deleteTgChat(tgChatId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> registerTgChat(Long tgChatId) {
        tgChatService.registerTgChat(tgChatId);
        return ResponseEntity.ok().build();
    }
}
