package backend.academy.scrapper.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
public interface ScrapperRestApiTgChat {

    @DeleteMapping("/tg-chat/{id}")
    ResponseEntity<Void> deleteTgChat(@PathVariable("id") Long tgChatId);

    @PostMapping("/tg-chat/{id}")
    ResponseEntity<Void> registerTgChat(@PathVariable("id") Long tgChatId);
}
