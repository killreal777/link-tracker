package backend.academy.bot.service;

import backend.academy.bot.model.ChatState;
import backend.academy.bot.model.CommandState;
import backend.academy.bot.repository.ChatStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatStateService {

    private final ChatStateRepository chatStateRepository;

    public ChatState<?> saveChatState(ChatState<?> chatState) {
        return chatStateRepository.save(chatState);
    }

    public ChatState<?> getChatState(long chatId) {
        return chatStateRepository.findById(chatId).orElseGet(() -> saveChatState(new ChatState<>(chatId)));
    }

    public <D> ChatState<D> getChatState(Long chatId, Class<D> dataType) {
        ChatState<?> chatStateRaw = getChatState(chatId);
        return cast(chatStateRaw, dataType);
    }

    public void cleanChatState(Long chatId) {
        chatStateRepository.deleteById(chatId);
    }

    private <D> ChatState<D> cast(ChatState<?> chatStateRaw, Class<D> dataType) {
        if (!dataType.isInstance(chatStateRaw.data())) {
            throw new ClassCastException("Couldn't cast commandState to " + dataType.getName());
        }

        long chatId = chatStateRaw.tgChatId();
        CommandState commandState = chatStateRaw.commandState();
        D data = dataType.cast(chatStateRaw.data());

        return new ChatState<>(chatId, commandState, data);
    }
}
