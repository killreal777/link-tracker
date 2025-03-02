package backend.academy.scrapper.service;

import backend.academy.scrapper.api.dto.AddLinkRequest;
import backend.academy.scrapper.api.dto.LinkResponse;
import backend.academy.scrapper.api.dto.ListLinksResponse;
import backend.academy.scrapper.api.dto.RemoveLinkRequest;
import backend.academy.scrapper.model.entity.Link;
import backend.academy.scrapper.model.entity.Subscription;
import backend.academy.scrapper.model.entity.TgChat;
import backend.academy.scrapper.model.value.ParsedLink;
import backend.academy.scrapper.repository.LinkRepository;
import backend.academy.scrapper.repository.SubscriptionRepository;
import backend.academy.scrapper.repository.TgChatRepository;
import backend.academy.scrapper.service.check.LinkCheckServiceRegistry;
import backend.academy.scrapper.service.parser.LinkParsingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final TgChatRepository tgChatRepository;
    private final LinkRepository linkRepository;

    private final LinkParsingService linkParsingService;
    private final LinkCheckServiceRegistry linkCheckServiceRegistry;

    public LinkResponse subscribe(Long tgChatId, AddLinkRequest addLinkRequest) {
        TgChat tgChat = tgChatRepository
                .findById(tgChatId)
                .orElseThrow(() -> new EntityNotFoundWithIdException(TgChat.class, tgChatId));
        Link link = findOrCreateLink(addLinkRequest);

        Subscription subscription = subscriptionRepository
                .findByTgChatIdAndLinkId(tgChatId, link.id())
                .orElseGet(() -> createSubscription(tgChat, link, addLinkRequest));

        return createLinkResponse(subscription, link);
    }

    private Link findOrCreateLink(AddLinkRequest addLinkRequest) {
        ParsedLink parsedLink = linkParsingService.parseLink(addLinkRequest.link());
        String url = parsedLink.standardUrl();
        return linkRepository.findByUrl(url).orElseGet(() -> createLink(url, parsedLink.linkType()));
    }

    private Link createLink(String standardUrl, Link.Type type) {
        linkCheckServiceRegistry.get(type).checkExistence(standardUrl);
        Link link = new Link().standardUrl(standardUrl).type(type);
        link = linkRepository.save(link);
        log.atInfo()
                .setMessage("Link created")
                .addKeyValue("id", link.id())
                .addKeyValue("standardUrl", standardUrl)
                .addKeyValue("type", type)
                .log();
        return link;
    }

    private Subscription createSubscription(TgChat tgChat, Link link, AddLinkRequest addLinkRequest) {
        Subscription subscription = new Subscription()
                .tgChatId(tgChat.id())
                .linkId(link.id())
                .tags(addLinkRequest.tags())
                .filters(addLinkRequest.filters());
        subscription = subscriptionRepository.save(subscription);

        tgChat.subscriptions().add(subscription);
        tgChatRepository.save(tgChat);

        link.subscriptions().add(subscription);
        linkRepository.save(link);

        log.atInfo()
                .setMessage("Subscription created")
                .addKeyValue("id", subscription.id())
                .addKeyValue("tgChatId", tgChat.id())
                .addKeyValue("linkId", link.id())
                .addKeyValue("url", link.standardUrl())
                .log();
        return subscription;
    }

    public LinkResponse unsubscribe(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        TgChat tgChat = tgChatRepository
                .findById(tgChatId)
                .orElseThrow(() -> new EntityNotFoundWithIdException(TgChat.class, tgChatId));
        Link link = findLinkByUrlInAnyFormatOrElseThrow(removeLinkRequest.link());
        Subscription subscription = findSubscriptionByTgChatIdAndLinkIdOrElseThrow(tgChatId, link.id());

        subscriptionRepository.deleteById(subscription.id());

        tgChat.subscriptions().removeIf(s -> s.id().equals(subscription.id()));
        tgChatRepository.save(tgChat);

        link.subscriptions().removeIf(s -> s.id().equals(subscription.id()));
        linkRepository.save(link);

        log.atInfo()
                .setMessage("Subscription deleted")
                .addKeyValue("id", subscription.id())
                .addKeyValue("tgChatId", tgChat.id())
                .addKeyValue("linkId", link.id())
                .addKeyValue("url", link.standardUrl())
                .log();
        return createLinkResponse(subscription, link);
    }

    public ListLinksResponse getAllSubscriptionsByTgChatId(Long tgChatId) {
        TgChat tgChat = tgChatRepository
                .findById(tgChatId)
                .orElseThrow(() -> new EntityNotFoundWithIdException(TgChat.class, tgChatId));
        List<LinkResponse> linkResponses =
                tgChat.subscriptions().stream().map(this::createLinkResponse).toList();
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    private LinkResponse createLinkResponse(Subscription subscription) {
        Link link = linkRepository
                .findById(subscription.linkId())
                .orElseThrow(() -> new EntityNotFoundWithIdException(Link.class, subscription.linkId()));
        return createLinkResponse(subscription, link);
    }

    private LinkResponse createLinkResponse(Subscription subscription, Link link) {
        return new LinkResponse(link.id(), link.standardUrl(), subscription.tags(), subscription.filters());
    }

    private Link findLinkByUrlInAnyFormatOrElseThrow(String url) {
        ParsedLink parsedLink = linkParsingService.parseLink(url);
        return linkRepository
                .findByUrl(parsedLink.standardUrl())
                .orElseThrow(() -> new LinkNotFoundWithUrlException(url));
    }

    private Subscription findSubscriptionByTgChatIdAndLinkIdOrElseThrow(Long tgChatId, Long linkId) {
        return subscriptionRepository
                .findByTgChatIdAndLinkId(tgChatId, linkId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Subscription not found with TG chat ID: %s; and Link ID: %s", tgChatId, linkId)));
    }
}
