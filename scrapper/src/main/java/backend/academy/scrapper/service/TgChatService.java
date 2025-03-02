package backend.academy.scrapper.service;

import backend.academy.scrapper.model.entity.TgChat;
import backend.academy.scrapper.repository.SubscriptionRepository;
import backend.academy.scrapper.repository.TgChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TgChatService {

    private final TgChatRepository tgChatRepository;
    private final SubscriptionRepository subscriptionRepository;

    public void deleteTgChat(Long tgChatId) {
        subscriptionRepository
                .findAllByTgChatId(tgChatId)
                .forEach(subscription -> subscriptionRepository.deleteById(subscription.id()));
        tgChatRepository.deleteById(tgChatId);
        log.atInfo()
                .setMessage("TG chat unregistered")
                .addKeyValue("tgChatId", tgChatId)
                .log();
    }

    public void registerTgChat(Long tgChatId) {
        if (tgChatRepository.findById(tgChatId).isEmpty()) {
            TgChat tgChat = new TgChat().id(tgChatId);
            tgChatRepository.save(tgChat);
            log.atInfo()
                    .setMessage("TG chat registered")
                    .addKeyValue("tgChatId", tgChatId)
                    .log();
        } else {
            log.atInfo()
                    .setMessage("TG chat not registered: TG chat already exists")
                    .addKeyValue("tgChatId", tgChatId)
                    .log();
        }
    }
}
