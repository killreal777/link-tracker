package backend.academy.scrapper.client.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ListQuestionsResponse(@JsonProperty(value = "items") List<Question> items) {}
