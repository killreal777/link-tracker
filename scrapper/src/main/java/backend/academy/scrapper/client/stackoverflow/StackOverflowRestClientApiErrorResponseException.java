package backend.academy.scrapper.client.stackoverflow;

import backend.academy.scrapper.client.stackoverflow.dto.StackOverflowApiErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StackOverflowRestClientApiErrorResponseException extends RuntimeException {

    private final StackOverflowApiErrorResponse response;
}
