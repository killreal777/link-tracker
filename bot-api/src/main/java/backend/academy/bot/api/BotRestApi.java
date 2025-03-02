package backend.academy.bot.api;

import backend.academy.bot.api.dto.LinkUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
public interface BotRestApi {

    @PostMapping("/updates")
    ResponseEntity<Void> sendLinkUpdate(@RequestBody LinkUpdate linkUpdate);
}
