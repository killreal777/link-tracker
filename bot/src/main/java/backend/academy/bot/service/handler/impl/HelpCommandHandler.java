package backend.academy.bot.service.handler.impl;

import backend.academy.bot.service.ChatStateService;
import backend.academy.bot.service.handler.CommandHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelpCommandHandler implements CommandHandler {

    private final TelegramBot bot;
    private final ChatStateService chatStateService;
    private final String commandDescriptions;

    @Autowired
    public HelpCommandHandler(TelegramBot bot, ChatStateService chatStateService, Set<CommandHandler> commands) {
        this.bot = bot;
        this.chatStateService = chatStateService;
        this.commandDescriptions = composeDescriptions(commands);
    }

    private String composeDescriptions(Set<CommandHandler> commands) {
        StringBuilder stringBuilder = new StringBuilder();
        commands.add(this);
        commands.forEach(command -> stringBuilder
                .append(command.prefixedCommandName())
                .append(" - ")
                .append(command.description())
                .append('\n'));
        return stringBuilder.toString();
    }

    @Override
    public String commandName() {
        return "help";
    }

    @Override
    public String description() {
        return "вывести справку по командам";
    }

    @Override
    public void handle(Update update) {
        long chatId = update.message().chat().id();
        chatStateService.cleanChatState(chatId);
        bot.execute(new SendMessage(chatId, commandDescriptions));
    }
}
