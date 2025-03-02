package backend.academy.scrapper.model.entity;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Subscription implements InMemoryStoredEntity {

    private Long id;

    private Long linkId;

    private Long tgChatId;

    private Set<String> tags;

    private Set<String> filters;
}
