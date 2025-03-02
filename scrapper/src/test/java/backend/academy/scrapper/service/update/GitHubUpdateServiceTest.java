package backend.academy.scrapper.service.update;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

import backend.academy.bot.api.dto.LinkUpdate;
import backend.academy.scrapper.api.dto.AddLinkRequest;
import backend.academy.scrapper.client.github.GitHubRestClient;
import backend.academy.scrapper.client.github.dto.RepoResponse;
import backend.academy.scrapper.service.SubscriptionService;
import backend.academy.scrapper.service.TgChatService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class GitHubUpdateServiceTest {

    @MockitoBean
    private GitHubRestClient gitHubRestClient;

    @Autowired
    private TgChatService tgChatService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private GitHubUpdateService gitHubUpdateService;

    @Test
    public void shouldReturnLinkUpdates() {
        String repository1 = "https://github.com/killreal777/is-project";
        String repository2 = "https://github.com/killreal777/is-labs";
        String repository3 = "https://github.com/killreal777/social-network";
        OffsetDateTime now = OffsetDateTime.now();
        when(gitHubRestClient.getRepository("killreal777", "is-project"))
                .thenReturn(ResponseEntity.ok(new RepoResponse(1L, now)));
        when(gitHubRestClient.getRepository("killreal777", "is-labs"))
                .thenReturn(ResponseEntity.ok(new RepoResponse(2L, now)));
        when(gitHubRestClient.getRepository("killreal777", "social-network"))
                .thenReturn(ResponseEntity.ok(new RepoResponse(3L, now)));
        long tgChatId1 = 1L;
        long tgChatId2 = 2L;
        tgChatService.registerTgChat(tgChatId1);
        tgChatService.registerTgChat(tgChatId2);
        subscriptionService.subscribe(tgChatId1, new AddLinkRequest(repository1));
        subscriptionService.subscribe(tgChatId1, new AddLinkRequest(repository2));
        subscriptionService.subscribe(tgChatId2, new AddLinkRequest(repository1));
        subscriptionService.subscribe(tgChatId2, new AddLinkRequest(repository3));
        List<LinkUpdate> updates = gitHubUpdateService.getAllUpdates(now.minusMonths(1));
        assertThat(updates)
                .isNotNull()
                .containsExactlyInAnyOrder(
                        new LinkUpdate(
                                1L,
                                repository1,
                                "Something happened in GitHub repository...",
                                Set.of(tgChatId1, tgChatId2)),
                        new LinkUpdate(
                                2L, repository2, "Something happened in GitHub repository...", Set.of(tgChatId1)),
                        new LinkUpdate(
                                3L, repository3, "Something happened in GitHub repository...", Set.of(tgChatId2)));
    }
}
