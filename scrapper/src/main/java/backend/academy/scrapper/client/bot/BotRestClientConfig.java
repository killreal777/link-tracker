package backend.academy.scrapper.client.bot;

import backend.academy.scrapper.ScrapperConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class BotRestClientConfig {

    private final ScrapperConfig scrapperConfig;
    private final BotRestClientResponseErrorHandler scrapperRestClientResponseErrorHandler;

    @Bean
    public BotRestClient botRestClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(scrapperConfig.botApiBase())
                .defaultStatusHandler(scrapperRestClientResponseErrorHandler)
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(BotRestClient.class);
    }
}
