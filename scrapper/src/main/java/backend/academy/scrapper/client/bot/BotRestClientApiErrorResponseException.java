package backend.academy.scrapper.client.bot;

import backend.academy.bot.api.dto.ApiErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BotRestClientApiErrorResponseException extends RuntimeException {

    private final ApiErrorResponse response;
}
