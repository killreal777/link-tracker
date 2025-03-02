package backend.academy.scrapper.client.stackoverflow;

import backend.academy.scrapper.client.stackoverflow.dto.StackOverflowApiErrorResponse;
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
public class StackOverflowRestClientResponseErrorHandler extends DefaultResponseErrorHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handleError(@NotNull ClientHttpResponse response) throws IOException {
        try {
            StackOverflowApiErrorResponse apiErrorResponse = parseResponseBody(response);
            log.atInfo()
                    .setMessage("StackOverflow API error response")
                    .addKeyValue("response", apiErrorResponse)
                    .log();
            throw new StackOverflowRestClientApiErrorResponseException(apiErrorResponse);
        } catch (IOException e) {
            super.handleError(response);
        }
    }

    private StackOverflowApiErrorResponse parseResponseBody(ClientHttpResponse response) throws IOException {
        return objectMapper.readValue(response.getBody(), StackOverflowApiErrorResponse.class);
    }
}
