package backend.academy.bot.client;

import backend.academy.scrapper.api.ScrapperRestApiLinks;
import backend.academy.scrapper.api.dto.AddLinkRequest;
import backend.academy.scrapper.api.dto.LinkResponse;
import backend.academy.scrapper.api.dto.ListLinksResponse;
import backend.academy.scrapper.api.dto.RemoveLinkRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api")
public interface ScrapperRestClientLinks extends ScrapperRestApiLinks {

    @Override
    @DeleteExchange("/links")
    ResponseEntity<LinkResponse> untrackLink(
            @RequestHeader("Tg-Chat-Id") Long tgChatId, @RequestBody RemoveLinkRequest removeLinkRequest);

    @Override
    @GetExchange("/links")
    ResponseEntity<ListLinksResponse> getTrackedLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId);

    @Override
    @PostExchange("/links")
    ResponseEntity<LinkResponse> trackLink(
            @RequestHeader("Tg-Chat-Id") Long tgChatId, @RequestBody AddLinkRequest addLinkRequest);
}
