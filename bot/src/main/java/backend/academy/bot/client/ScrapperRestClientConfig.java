package backend.academy.bot.client;

import backend.academy.bot.BotConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class ScrapperRestClientConfig {

    private final BotConfig botConfig;
    private final ScrapperRestClientResponseErrorHandler scrapperRestClientResponseErrorHandler;

    @Bean
    public RestClient scrapperRestClient() {
        return RestClient.builder()
                .baseUrl(botConfig.scrapperApiBase())
                .defaultStatusHandler(scrapperRestClientResponseErrorHandler)
                .build();
    }

    @Bean
    public HttpServiceProxyFactory scrapperHttpServiceProxyFactory(RestClient restClient) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build();
    }

    @Bean
    public ScrapperRestClientLinks scrapperRestClientLinks(HttpServiceProxyFactory factory) {
        return factory.createClient(ScrapperRestClientLinks.class);
    }

    @Bean
    public ScrapperRestClientTgChat scrapperRestClientTgChat(HttpServiceProxyFactory factory) {
        return factory.createClient(ScrapperRestClientTgChat.class);
    }
}
