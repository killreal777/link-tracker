package backend.academy.scrapper.client.github;

import backend.academy.scrapper.client.github.dto.GitHubApiErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GitHubRestClientApiErrorResponseException extends RuntimeException {

    private final GitHubApiErrorResponse response;
}
