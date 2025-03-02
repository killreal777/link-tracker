package backend.academy.bot.service.handler.registry;

import backend.academy.bot.service.handler.CommandHandler;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandHandlerRegistry extends UpdateHandlerRegistry<CommandHandler, String> {

    @Autowired
    public CommandHandlerRegistry(Set<CommandHandler> handlers) {
        super(handlers, CommandHandler::prefixedCommandName);
    }
}
