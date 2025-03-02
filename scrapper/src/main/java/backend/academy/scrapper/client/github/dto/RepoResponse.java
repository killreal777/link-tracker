package backend.academy.scrapper.client.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record RepoResponse(
        @JsonProperty(value = "id") Long id, @JsonProperty(value = "updated_at") OffsetDateTime updatedAt) {}
