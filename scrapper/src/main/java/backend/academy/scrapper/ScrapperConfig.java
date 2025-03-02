package backend.academy.scrapper;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ScrapperConfig(
        @NotEmpty @DefaultValue("http://localhost:8080") String botApiBase,
        GitHubConfig gitHub,
        StackOverflowConfig stackOverflow,
        SchedulingConfig scheduling) {

    public record GitHubConfig(
            @NotEmpty @DefaultValue("https://api.github.com") String apiBase, @NotEmpty String token) {}

    public record StackOverflowConfig(
            @NotEmpty @DefaultValue("https://api.stackexchange.com/2.3") String apiBase, @NotEmpty String key) {}

    public record SchedulingConfig(@NotEmpty String cron, @NotNull Long offsetMinutes) {
        public SchedulingConfig {
            if (offsetMinutes <= 0) {
                throw new IllegalArgumentException("Offset minutes must be greater than 0");
            }
        }
    }
}
