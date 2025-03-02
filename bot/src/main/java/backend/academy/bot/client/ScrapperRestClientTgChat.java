package backend.academy.bot.client;

import backend.academy.scrapper.api.ScrapperRestApiTgChat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api")
public interface ScrapperRestClientTgChat extends ScrapperRestApiTgChat {

    @Override
    @DeleteExchange("/tg-chat/{id}")
    ResponseEntity<Void> deleteTgChat(@PathVariable("id") Long tgChatId);

    @Override
    @PostExchange("/tg-chat/{id}")
    ResponseEntity<Void> registerTgChat(@PathVariable("id") Long tgChatId);
}
