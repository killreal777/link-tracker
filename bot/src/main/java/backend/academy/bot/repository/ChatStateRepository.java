package backend.academy.bot.repository;

import backend.academy.bot.model.ChatState;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class ChatStateRepository {

    private final Map<Long, ChatState<?>> data;

    public ChatStateRepository() {
        data = new ConcurrentHashMap<>();
    }

    public ChatState<?> save(ChatState<?> state) {
        data.put(state.tgChatId(), state);
        return state;
    }

    public Optional<ChatState<?>> findById(long chatId) {
        return Optional.ofNullable(data.get(chatId));
    }

    public void deleteById(long chatId) {
        data.remove(chatId);
    }
}
