package backend.academy.scrapper.client.bot;

import backend.academy.bot.api.BotRestApi;
import backend.academy.bot.api.dto.LinkUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api")
public interface BotRestClient extends BotRestApi {

    @Override
    @PostExchange("/updates")
    ResponseEntity<Void> sendLinkUpdate(@RequestBody LinkUpdate linkUpdate);
}
