package backend.academy.bot.client;

import backend.academy.scrapper.api.dto.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScrapperRestClientResponseErrorHandler extends DefaultResponseErrorHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handleError(@NotNull ClientHttpResponse response) throws IOException {
        try {
            ApiErrorResponse apiErrorResponse = parseResponseBody(response);
            log.atInfo()
                    .setMessage("Scrapper API error response")
                    .addKeyValue("response", apiErrorResponse)
                    .log();
            throw new ScrapperRestClientApiErrorResponseException(apiErrorResponse);
        } catch (IOException e) {
            super.handleError(response);
        }
    }

    private ApiErrorResponse parseResponseBody(ClientHttpResponse response) throws IOException {
        return objectMapper.readValue(response.getBody(), ApiErrorResponse.class);
    }
}
