package backend.academy.scrapper.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.With;

@With
public record ListLinksResponse(
        @JsonProperty(value = "links") List<LinkResponse> links, @JsonProperty(value = "size") Integer size) {}
