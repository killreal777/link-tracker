package backend.academy.scrapper.service.update;

import backend.academy.bot.api.dto.LinkUpdate;
import backend.academy.scrapper.client.github.GitHubRestClient;
import backend.academy.scrapper.client.github.GitHubRestClientApiErrorResponseException;
import backend.academy.scrapper.client.github.dto.RepoResponse;
import backend.academy.scrapper.model.entity.Link;
import backend.academy.scrapper.model.entity.Subscription;
import backend.academy.scrapper.model.value.ParsedGitHubLink;
import backend.academy.scrapper.repository.LinkRepository;
import backend.academy.scrapper.service.parser.GitHubLinkParser;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubUpdateService implements UpdateService {

    private static final String UPDATE_MESSAGE_STUB = "Something happened in GitHub repository...";

    private final LinkRepository linkRepository;
    private final GitHubLinkParser gitHubLinkParser;
    private final GitHubRestClient gitHubRestClient;

    @Override
    public Link.Type linkType() {
        return Link.Type.GITHUB_REPOSITORY;
    }

    @Override
    public List<LinkUpdate> getAllUpdates(OffsetDateTime from) {
        return linkRepository.findAllByType(Link.Type.GITHUB_REPOSITORY).stream()
                .filter(link -> isUpdated(link, from))
                .map(this::createLinkUpdate)
                .toList();
    }

    private boolean isUpdated(Link link, OffsetDateTime from) {
        try {
            ParsedGitHubLink parsedLink = gitHubLinkParser.parseStandardUrl(link.standardUrl());
            RepoResponse response = gitHubRestClient
                    .getRepository(parsedLink.owner(), parsedLink.repo())
                    .getBody();
            if (Objects.isNull(response)) {
                log.error("Response body is null");
                return false;
            }
            return response.updatedAt().isAfter(from);
        } catch (GitHubRestClientApiErrorResponseException e) {
            log.info(
                    "Github API Error for URL {}: {} {}",
                    link.standardUrl(),
                    e.response().status(),
                    e.response().message());
            return false;
        }
    }

    private LinkUpdate createLinkUpdate(Link link) {
        Set<Long> tgChatIds =
                link.subscriptions().stream().map(Subscription::tgChatId).collect(Collectors.toSet());

        return new LinkUpdate(link.id(), link.standardUrl(), UPDATE_MESSAGE_STUB, tgChatIds);
    }
}
