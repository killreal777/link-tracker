package backend.academy.scrapper.client.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowApiErrorResponse(
        @JsonProperty(value = "error_id", required = true) Integer errorId,
        @JsonProperty(value = "error_message", required = true) String errorMessage,
        @JsonProperty(value = "error_name", required = true) String errorName) {}
