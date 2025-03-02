package backend.academy.bot.model;

import lombok.With;

@With
public record ChatState<D>(Long tgChatId, CommandState commandState, D data) {

    public ChatState(Long chatId, CommandState commandState) {
        this(chatId, commandState, null);
    }

    public ChatState(Long chatId) {
        this(chatId, CommandState.Basic.NONE, null);
    }
}
