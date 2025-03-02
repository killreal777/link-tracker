package backend.academy.scrapper.repository;

import backend.academy.scrapper.model.entity.Subscription;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionRepository extends InMemoryRepository<Subscription> {

    public List<Subscription> findAllByTgChatId(Long tgChatId) {
        return data.values().stream()
                .filter(subscription -> Objects.equals(subscription.tgChatId(), tgChatId))
                .toList();
    }

    public Optional<Subscription> findByTgChatIdAndLinkId(Long tgChatId, Long linkId) {
        return data.values().stream()
                .filter(subscription -> Objects.equals(subscription.linkId(), linkId))
                .filter(subscription -> Objects.equals(subscription.tgChatId(), tgChatId))
                .findFirst();
    }
}
