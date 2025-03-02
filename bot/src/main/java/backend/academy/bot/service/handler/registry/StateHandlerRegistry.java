package backend.academy.bot.service.handler.registry;

import backend.academy.bot.model.CommandState;
import backend.academy.bot.service.handler.StateHandler;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StateHandlerRegistry extends UpdateHandlerRegistry<StateHandler, CommandState> {

    @Autowired
    public StateHandlerRegistry(Set<StateHandler> handlers) {
        super(handlers, StateHandler::handledState);
    }
}
