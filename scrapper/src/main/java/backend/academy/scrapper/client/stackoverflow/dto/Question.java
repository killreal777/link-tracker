package backend.academy.scrapper.client.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Question(
        @JsonProperty(value = "question_id") Long questionId,
        @JsonProperty(value = "link") String link,
        @JsonProperty(value = "last_activity_date") Long lastActivityDate) {}
