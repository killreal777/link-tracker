package backend.academy.bot.service;

import backend.academy.bot.service.handler.UpdateHandler;
import backend.academy.bot.service.handler.impl.UnknownCommandHandler;
import backend.academy.bot.service.handler.registry.CommandHandlerRegistry;
import backend.academy.bot.service.handler.registry.StateHandlerRegistry;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateDispatcher {

    private final TelegramBot bot;
    private final ChatStateService chatStateService;
    private final CommandHandlerRegistry commandHandlerRegistry;
    private final StateHandlerRegistry stateHandlerRegistry;
    private final UnknownCommandHandler unknownCommandHandler;

    public void dispatchToHandler(Update update) {
        if (update.message() != null && update.message().text() != null) {
            String text = update.message().text();
            Long chatId = update.message().chat().id();
            commandHandlerRegistry
                    .getHandler(text)
                    .map(handler -> (UpdateHandler) handler) // Optional<UpdateHandler>
                    .or(() -> stateHandlerRegistry.getHandler(
                            chatStateService.getChatState(chatId).commandState()))
                    .ifPresentOrElse(handler -> handler.handle(update), () -> unknownCommandHandler.handle(update));
        }
    }

    @PostConstruct
    public void configureBot() {
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::dispatchToHandler);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
        bot.execute(new SetMyCommands(commandHandlerRegistry.handlers().entrySet().stream()
                .map(entry -> new BotCommand(entry.getKey(), entry.getValue().description()))
                .toArray(BotCommand[]::new)));
    }
}
