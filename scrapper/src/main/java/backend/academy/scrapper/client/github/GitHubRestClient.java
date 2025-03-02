package backend.academy.scrapper.client.github;

import backend.academy.scrapper.client.github.dto.RepoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface GitHubRestClient {

    @GetExchange("/repos/{owner}/{repo}")
    ResponseEntity<RepoResponse> getRepository(@PathVariable String owner, @PathVariable String repo);
}
