package backend.academy.scrapper.client.github;

import backend.academy.scrapper.ScrapperConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class GitHubRestClientConfig {

    private final ScrapperConfig scrapperConfig;
    private final GitHubRestClientResponseErrorHandler gitHubRestClientResponseErrorHandler;

    @Bean
    public GitHubRestClient gitHubRestClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(scrapperConfig.gitHub().apiBase())
                .defaultStatusHandler(gitHubRestClientResponseErrorHandler)
                .defaultHeaders(headers -> {
                    headers.set("Accept", "application/vnd.github+json");
                    headers.set(
                            "Authorization", "Bearer " + scrapperConfig.gitHub().token());
                    headers.set("X-GitHub-Api-Version", "2022-11-28");
                })
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(GitHubRestClient.class);
    }
}
