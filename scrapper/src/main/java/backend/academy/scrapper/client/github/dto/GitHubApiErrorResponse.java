package backend.academy.scrapper.client.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubApiErrorResponse(
        @JsonProperty(value = "message", required = true) String message,
        @JsonProperty(value = "status", required = true) String status) {}
