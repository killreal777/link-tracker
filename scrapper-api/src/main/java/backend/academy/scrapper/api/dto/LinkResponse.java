package backend.academy.scrapper.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.With;

@With
public record LinkResponse(
        @JsonProperty(value = "id") Long id,
        @JsonProperty(value = "url") String url,
        @JsonProperty(value = "tags") Set<String> tags,
        @JsonProperty(value = "filters") Set<String> filters) {}
