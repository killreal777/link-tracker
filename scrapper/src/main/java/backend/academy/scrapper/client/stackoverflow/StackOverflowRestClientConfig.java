package backend.academy.scrapper.client.stackoverflow;

import backend.academy.scrapper.ScrapperConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class StackOverflowRestClientConfig {

    public static final String SITE = "stackoverflow";
    public static final String FILTER =
            "!sR(bXEDbAu-B4iWsqhv2ex4UOSjxU_xl6G)ErJWATYSP8RcspmZzi8pbjNxQyYH5uvgA5gUw5Bg.8";

    private final ScrapperConfig scrapperConfig;
    private final StackOverflowRestClientResponseErrorHandler stackOverflowRestClientResponseErrorHandler;

    @Bean
    public StackOverflowRestClient stackOverflowRestClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(scrapperConfig.stackOverflow().apiBase())
                .defaultStatusHandler(stackOverflowRestClientResponseErrorHandler)
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(StackOverflowRestClient.class);
    }
}
