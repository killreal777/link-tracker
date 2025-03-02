package backend.academy.bot.service.handler;

import com.pengrad.telegrambot.model.Update;

public interface UpdateHandler {

    void handle(Update update);
}
