package backend.academy.bot.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.With;

@With
public record LinkUpdate(
        @JsonProperty(value = "id") Long id,
        @JsonProperty(value = "url") String url,
        @JsonProperty(value = "description") String description,
        @JsonProperty(value = "tgChatIds") Set<Long> tgChatIds) {}
