package backend.academy.scrapper.controller;

import backend.academy.scrapper.api.ScrapperRestApiLinks;
import backend.academy.scrapper.api.dto.AddLinkRequest;
import backend.academy.scrapper.api.dto.LinkResponse;
import backend.academy.scrapper.api.dto.ListLinksResponse;
import backend.academy.scrapper.api.dto.RemoveLinkRequest;
import backend.academy.scrapper.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScrapperRestControllerLinks implements ScrapperRestApiLinks {

    private final SubscriptionService subscriptionService;

    @Override
    public ResponseEntity<LinkResponse> untrackLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        return ResponseEntity.ok(subscriptionService.unsubscribe(tgChatId, removeLinkRequest));
    }

    @Override
    public ResponseEntity<ListLinksResponse> getTrackedLinks(Long tgChatId) {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptionsByTgChatId(tgChatId));
    }

    @Override
    public ResponseEntity<LinkResponse> trackLink(Long tgChatId, AddLinkRequest addLinkRequest) {
        return ResponseEntity.ok(subscriptionService.subscribe(tgChatId, addLinkRequest));
    }
}
