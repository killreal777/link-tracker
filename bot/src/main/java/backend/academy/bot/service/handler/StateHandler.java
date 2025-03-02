package backend.academy.bot.service.handler;

import backend.academy.bot.model.CommandState;

public interface StateHandler extends UpdateHandler {

    CommandState handledState();
}
