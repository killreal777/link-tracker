package backend.academy.scrapper.service;

import backend.academy.scrapper.ScrapperConfig;
import backend.academy.scrapper.client.bot.BotRestClient;
import backend.academy.scrapper.client.bot.BotRestClientApiErrorResponseException;
import backend.academy.scrapper.service.update.UpdateService;
import jakarta.annotation.PostConstruct;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackingService {

    private final ScrapperConfig scrapperConfig;
    private final TaskScheduler taskScheduler;

    private final BotRestClient botRestClient;
    private final Set<UpdateService> updateServices;

    @PostConstruct
    private void scheduleTask() {
        taskScheduler.schedule(
                () -> {
                    OffsetDateTime now = OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES);
                    OffsetDateTime from =
                            now.minusMinutes(scrapperConfig.scheduling().offsetMinutes());
                    sendUpdates(from);
                },
                new CronTrigger(scrapperConfig.scheduling().cron()));
    }

    private void sendUpdates(OffsetDateTime from) {
        log.atInfo().setMessage("Sending updates").addKeyValue("from", from).log();
        updateServices.stream()
                .flatMap(updateService -> updateService.getAllUpdates(from).stream())
                .forEach(update -> {
                    try {
                        botRestClient.sendLinkUpdate(update);
                    } catch (BotRestClientApiErrorResponseException ignored) {
                    }
                });
    }
}
