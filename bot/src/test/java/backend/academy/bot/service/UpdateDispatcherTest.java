package backend.academy.bot.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.bot.service.handler.impl.UnknownCommandHandler;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class UpdateDispatcherTest {

    @Autowired
    private UpdateDispatcher updateDispatcher;

    @MockitoBean
    private UnknownCommandHandler unknownCommandHandler;

    @Test
    public void shouldHandleUnknownCommand() {
        String unknownCommand = "/getOfferForCtoPositionWithoutExperience";
        long chatId = 12345;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(unknownCommand);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        updateDispatcher.dispatchToHandler(update);
        verify(unknownCommandHandler).handle(update);
    }
}
