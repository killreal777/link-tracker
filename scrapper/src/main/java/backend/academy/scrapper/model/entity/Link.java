package backend.academy.scrapper.model.entity;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Link implements InMemoryStoredEntity {

    private Long id;

    /**
     * URL in standard format.
     *
     * <p>For GitHub: {@code https://github.com/{owner}/{repo}}.
     *
     * <p>For StackOverflow: {@code https://stackoverflow.com/questions/{questionId}}.
     */
    private String standardUrl;

    private Type type;

    private Set<Subscription> subscriptions = new HashSet<>();

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        GITHUB_REPOSITORY("GitHub repository"),
        STACKOVERFLOW_QUESTION("StackOverflow question");

        private final String typeName;
    }
}
