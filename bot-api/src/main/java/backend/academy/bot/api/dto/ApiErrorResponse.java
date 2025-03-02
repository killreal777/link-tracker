package backend.academy.bot.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.With;

@With
public record ApiErrorResponse(
        @JsonProperty(value = "description", required = true) String description,
        @JsonProperty(value = "code", required = true) String code,
        @JsonProperty(value = "exceptionName") String exceptionName,
        @JsonProperty(value = "exceptionMessage") String exceptionMessage,
        @JsonProperty(value = "stacktrace") List<String> stacktrace) {}
