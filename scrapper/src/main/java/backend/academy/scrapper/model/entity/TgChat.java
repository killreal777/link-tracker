package backend.academy.scrapper.model.entity;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TgChat implements InMemoryStoredEntity {

    private Long id;

    private Set<Subscription> subscriptions = new HashSet<>();
}
