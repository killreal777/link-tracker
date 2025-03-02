package backend.academy.scrapper.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.With;

@With
public record RemoveLinkRequest(@JsonProperty(value = "link") String link) {}
