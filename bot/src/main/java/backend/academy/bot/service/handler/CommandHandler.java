package backend.academy.bot.service.handler;

public interface CommandHandler extends UpdateHandler {

    String commandName();

    String description();

    default String prefixedCommandName() {
        return "/" + commandName();
    }
}
