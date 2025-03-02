package backend.academy.scrapper.service.update;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

import backend.academy.bot.api.dto.LinkUpdate;
import backend.academy.scrapper.api.dto.AddLinkRequest;
import backend.academy.scrapper.client.stackoverflow.StackOverflowRestClient;
import backend.academy.scrapper.client.stackoverflow.dto.ListQuestionsResponse;
import backend.academy.scrapper.client.stackoverflow.dto.Question;
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
public class StackOverflowUpdateServiceTest {

    @MockitoBean
    private StackOverflowRestClient stackOverflowRestClient;

    @Autowired
    private TgChatService tgChatService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private StackOverflowUpdateService stackOverflowUpdateService;

    @Test
    public void shouldReturnLinkUpdates() {
        String question1 = "https://stackoverflow.com/questions/1";
        String question2 = "https://stackoverflow.com/questions/2";
        String question3 = "https://stackoverflow.com/questions/3";
        OffsetDateTime now = OffsetDateTime.now();
        when(stackOverflowRestClient.getQuestionsByIds(List.of("1", "2", "3")))
                .thenReturn(ResponseEntity.ok(new ListQuestionsResponse(List.of(
                        new Question(1L, question1, now.toEpochSecond()),
                        new Question(2L, question2, now.toEpochSecond()),
                        new Question(3L, question3, now.toEpochSecond())))));
        long tgChatId1 = 1L;
        long tgChatId2 = 2L;
        tgChatService.registerTgChat(tgChatId1);
        tgChatService.registerTgChat(tgChatId2);
        subscriptionService.subscribe(tgChatId1, new AddLinkRequest(question1));
        subscriptionService.subscribe(tgChatId1, new AddLinkRequest(question2));
        subscriptionService.subscribe(tgChatId2, new AddLinkRequest(question1));
        subscriptionService.subscribe(tgChatId2, new AddLinkRequest(question3));
        List<LinkUpdate> updates = stackOverflowUpdateService.getAllUpdates(now.minusMonths(1));
        assertThat(updates)
                .isNotNull()
                .containsExactlyInAnyOrder(
                        new LinkUpdate(
                                1L,
                                question1,
                                "Something happened in StackOverflow question...",
                                Set.of(tgChatId1, tgChatId2)),
                        new LinkUpdate(
                                2L, question2, "Something happened in StackOverflow question...", Set.of(tgChatId1)),
                        new LinkUpdate(
                                3L, question3, "Something happened in StackOverflow question...", Set.of(tgChatId2)));
    }
}
