package backend.academy.scrapper.service.update;

import backend.academy.bot.api.dto.LinkUpdate;
import backend.academy.scrapper.client.stackoverflow.StackOverflowRestClient;
import backend.academy.scrapper.client.stackoverflow.StackOverflowRestClientApiErrorResponseException;
import backend.academy.scrapper.client.stackoverflow.dto.ListQuestionsResponse;
import backend.academy.scrapper.client.stackoverflow.dto.Question;
import backend.academy.scrapper.model.entity.Link;
import backend.academy.scrapper.model.entity.Subscription;
import backend.academy.scrapper.repository.LinkRepository;
import backend.academy.scrapper.service.parser.StackOverflowLinkParser;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StackOverflowUpdateService implements UpdateService {

    private static final String UPDATE_MESSAGE_STUB = "Something happened in StackOverflow question...";
    private static final int BATCH_SIZE = StackOverflowRestClient.MAX_IDS;

    private final LinkRepository linkRepository;
    private final StackOverflowLinkParser stackOverflowLinkParser;
    private final StackOverflowRestClient stackOverflowRestClient;

    @Override
    public Link.Type linkType() {
        return Link.Type.STACKOVERFLOW_QUESTION;
    }

    @Override
    public List<LinkUpdate> getAllUpdates(OffsetDateTime from) {
        List<Link> links = linkRepository.findAllByType(Link.Type.STACKOVERFLOW_QUESTION);

        return IntStream.iterate(0, i -> i + 1)
                .mapToObj(i -> links.stream().skip((long) i * BATCH_SIZE).limit(BATCH_SIZE))
                .map(Stream::toList)
                .takeWhile(batch -> !batch.isEmpty())
                .flatMap(batch -> getLinksUpdatesStream(batch, from))
                .toList();
    }

    private Stream<LinkUpdate> getLinksUpdatesStream(List<Link> links, OffsetDateTime from) {
        Map<Link, Question> questionsByLinks = getQuestionsByLinks(links);

        return questionsByLinks.entrySet().stream()
                .filter(entry ->
                        parseOffsetDateTime(entry.getValue().lastActivityDate()).isAfter(from))
                .map(entry -> createLinkUpdate(entry.getKey()));
    }

    private OffsetDateTime parseOffsetDateTime(long unixTime) {
        return OffsetDateTime.ofInstant(Instant.ofEpochSecond(unixTime), ZoneOffset.UTC);
    }

    private LinkUpdate createLinkUpdate(Link link) {
        Set<Long> tgChatIds =
                link.subscriptions().stream().map(Subscription::tgChatId).collect(Collectors.toSet());

        return new LinkUpdate(link.id(), link.standardUrl(), UPDATE_MESSAGE_STUB, tgChatIds);
    }

    private Map<Link, Question> getQuestionsByLinks(List<Link> links) {
        Map<Long, Link> linksByIds = links.stream()
                .collect(Collectors.toMap(
                        link -> stackOverflowLinkParser
                                .parseStandardUrl(link.standardUrl())
                                .questionId(),
                        Function.identity()));

        return getBatchQuestions(linksByIds).stream()
                .collect(Collectors.toMap(question -> linksByIds.get(question.questionId()), Function.identity()));
    }

    private List<Question> getBatchQuestions(Map<Long, Link> linksByIds) {
        try {
            List<String> ids =
                    linksByIds.keySet().stream().map(Object::toString).toList();
            ResponseEntity<ListQuestionsResponse> responseEntity = stackOverflowRestClient.getQuestionsByIds(ids);
            Objects.requireNonNull(responseEntity);
            ListQuestionsResponse response = responseEntity.getBody();
            if (Objects.isNull(response)) {
                log.error("Response body is null");
                return new ArrayList<>();
            }
            return response.items();
        } catch (StackOverflowRestClientApiErrorResponseException e) {
            log.info(
                    "StackOverflow API Error: {} {} {}",
                    e.response().errorId(),
                    e.response().errorName(),
                    e.response().errorMessage());
            return new ArrayList<>();
        }
    }
}
