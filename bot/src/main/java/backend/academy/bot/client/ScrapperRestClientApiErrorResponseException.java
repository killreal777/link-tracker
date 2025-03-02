package backend.academy.bot.client;

import backend.academy.scrapper.api.dto.ApiErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScrapperRestClientApiErrorResponseException extends RuntimeException {

    private final ApiErrorResponse response;
}
