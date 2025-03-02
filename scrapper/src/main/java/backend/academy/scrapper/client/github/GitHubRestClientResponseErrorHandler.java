package backend.academy.scrapper.client.github;

import backend.academy.scrapper.client.github.dto.GitHubApiErrorResponse;
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
public class GitHubRestClientResponseErrorHandler extends DefaultResponseErrorHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handleError(@NotNull ClientHttpResponse response) throws IOException {
        try {
            GitHubApiErrorResponse apiErrorResponse = parseResponseBody(response);
            log.atInfo()
                    .setMessage("GitHub API error response")
                    .addKeyValue("response", apiErrorResponse)
                    .log();
            throw new GitHubRestClientApiErrorResponseException(apiErrorResponse);
        } catch (IOException e) {
            super.handleError(response);
        }
    }

    private GitHubApiErrorResponse parseResponseBody(ClientHttpResponse response) throws IOException {
        return objectMapper.readValue(response.getBody(), GitHubApiErrorResponse.class);
    }
}
