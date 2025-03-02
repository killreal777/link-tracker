package backend.academy.scrapper.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

import backend.academy.scrapper.api.dto.AddLinkRequest;
import backend.academy.scrapper.api.dto.LinkResponse;
import backend.academy.scrapper.api.dto.ListLinksResponse;
import backend.academy.scrapper.api.dto.RemoveLinkRequest;
import backend.academy.scrapper.model.entity.TgChat;
import backend.academy.scrapper.repository.LinkRepository;
import backend.academy.scrapper.repository.SubscriptionRepository;
import backend.academy.scrapper.repository.TgChatRepository;
import backend.academy.scrapper.service.check.GitHubLinkCheckService;
import backend.academy.scrapper.service.check.StackOverflowLinkCheckService;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest
public class SubscriptionServiceTest {

    @MockitoSpyBean
    private GitHubLinkCheckService gitHubLinkCheckService;

    @MockitoSpyBean
    private StackOverflowLinkCheckService stackOverflowLinkCheckService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private TgChatRepository tgChatRepository;

    @BeforeEach
    public void setup() {
        doNothing().when(gitHubLinkCheckService).checkExistence(anyString());
        doNothing().when(stackOverflowLinkCheckService).checkExistence(anyString());
    }

    @AfterEach
    public void tearDown() {
        Stream.of(subscriptionRepository, linkRepository, tgChatRepository)
                .forEach(repository -> repository.findAll().forEach(element -> repository.deleteById(element.id())));
    }

    @Test
    public void shouldSaveSubscription() {
        Long tgChatId = 1L;
        String url = "https://github.com/killreal777/is-project";
        Set<String> tags = Set.of("tag1", "tag2", "tag3");
        Set<String> filters = Set.of("filter1", "filter2", "filter3");
        AddLinkRequest request = new AddLinkRequest(url, tags, filters);
        tgChatRepository.save(new TgChat().id(tgChatId));
        LinkResponse response = subscriptionService.subscribe(tgChatId, request);
        assertThat(response).isNotNull();
        assertThat(response.url()).isNotNull().isEqualTo(url);
        assertThat(response.tags()).isNotNull().isEqualTo(tags);
        assertThat(response.filters()).isNotNull().isEqualTo(filters);
    }

    @Test
    public void shouldSaveOnlyOneSubscription() {
        Long tgChatId = 1L;
        String url = "https://stackoverflow.com/questions/12345";
        Set<String> tags = Set.of("tag1", "tag2", "tag3");
        Set<String> filters = Set.of("filter1", "filter2", "filter3");
        AddLinkRequest request = new AddLinkRequest(url, tags, filters);
        tgChatRepository.save(new TgChat().id(tgChatId));
        subscriptionService.subscribe(tgChatId, request);
        subscriptionService.subscribe(tgChatId, request);
        subscriptionService.subscribe(tgChatId, request);
        ListLinksResponse response = subscriptionService.getAllSubscriptionsByTgChatId(tgChatId);
        assertThat(response).isNotNull();
        assertThat(response.size()).isNotNull().isEqualTo(1);
        assertThat(response.links().getFirst().url()).isNotNull().isEqualTo(url);
        assertThat(response.links().getFirst().tags()).isNotNull().isEqualTo(tags);
        assertThat(response.links().getFirst().filters()).isNotNull().isEqualTo(filters);
    }

    @Test
    public void shouldIgnoreDuplicateUrlSubscriptions() {
        Long tgChatId = 1L;
        String url = "https://github.com/killreal777/is-labs";
        Set<String> tags = Set.of("tag1", "tag2", "tag3");
        Set<String> filters = Set.of("filter1", "filter2", "filter3");
        AddLinkRequest request1 = new AddLinkRequest(url, tags, filters);
        AddLinkRequest request2 = new AddLinkRequest(url, null, filters);
        AddLinkRequest request3 = new AddLinkRequest(url, null, null);
        tgChatRepository.save(new TgChat().id(tgChatId));
        subscriptionService.subscribe(tgChatId, request1);
        subscriptionService.subscribe(tgChatId, request2);
        subscriptionService.subscribe(tgChatId, request3);
        ListLinksResponse response = subscriptionService.getAllSubscriptionsByTgChatId(tgChatId);
        assertThat(response).isNotNull();
        assertThat(response.size()).isNotNull().isEqualTo(1);
        assertThat(response.links().getFirst().url()).isNotNull().isEqualTo(url);
        assertThat(response.links().getFirst().tags()).isNotNull().isEqualTo(tags);
        assertThat(response.links().getFirst().filters()).isNotNull().isEqualTo(filters);
    }

    @Test
    public void shouldDeleteSubscription() {
        Long tgChatId = 1L;
        String url = "https://stackoverflow.com/questions/12345";
        Set<String> tags = Set.of("tag1", "tag2", "tag3");
        Set<String> filters = Set.of("filter1", "filter2", "filter3");
        AddLinkRequest addLinkRequest = new AddLinkRequest(url, tags, filters);
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(url);
        tgChatRepository.save(new TgChat().id(tgChatId));
        subscriptionService.subscribe(tgChatId, addLinkRequest);
        var removeLinkResponse = subscriptionService.unsubscribe(tgChatId, removeLinkRequest);
        assertThat(removeLinkResponse).isNotNull();
        assertThat(removeLinkResponse.url()).isNotNull().isEqualTo(url);
        assertThat(removeLinkResponse.tags()).isNotNull().isEqualTo(tags);
        assertThat(removeLinkResponse.filters()).isNotNull().isEqualTo(filters);
        var listLinksResponse = subscriptionService.getAllSubscriptionsByTgChatId(tgChatId);
        assertThat(listLinksResponse).isNotNull();
        assertThat(listLinksResponse.size()).isNotNull().isEqualTo(0);
    }
}
