package backend.academy.scrapper.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.With;

@With
public record AddLinkRequest(
        @JsonProperty(value = "link") String link,
        @JsonProperty(value = "tags") Set<String> tags,
        @JsonProperty(value = "filters") Set<String> filters) {

    public AddLinkRequest(String link) {
        this(link, null, null);
    }
}
