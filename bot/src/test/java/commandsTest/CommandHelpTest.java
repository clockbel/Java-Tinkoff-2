package commandsTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.BotApplication;
import java.util.Map;
import edu.java.bot.model.command_utils.checks.CheckCommand;
import edu.java.bot.model.command_utils.commands.Command;
import edu.java.bot.model.command_utils.commands.CommandsOutputMessage;
import edu.java.bot.repository.UserBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BotApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommandHelpTest {
    @MockBean Update update;
    @Autowired UserBase userBase;
    @Autowired Map<String, Command> commands;

    @Order(1)
    @Test
    @DisplayName("Start command 1")
    void testStart1() {
        var id_user2 = 2L;
        mockChat(id_user2);
        SendMessage message = commands.get("/start").handle(update);
        SendMessage result_message =
            new SendMessage(update.message().chat().id(), CommandsOutputMessage.REGISTRATION_MESSAGE);
        assertThat(message.getParameters()).isEqualTo(result_message.getParameters());
    }
    @Order(13)
    @Test
    @DisplayName("Help command 1")
    void testHelp1() {
        var id_user2 = 2L;
        mockChatWithText(id_user2, "/help");
        SendMessage message = commands.get("/help").handle(update);
        SendMessage result_message =
            new SendMessage(update.message().chat().id(), """
                    /list - List of tracking URL
                    /start - Start the link tracker bot
                    /track - Track URL
                    /untrack - Untrack your URL
                    /help - List of command""");
        assertThat(message.getParameters()).isEqualTo(result_message.getParameters());
    }
    private void mockChat(long id) {
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(id);
        when(message.chat().id()).thenReturn(id);
    }

    private void mockChatWithText(long id, String text) {
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);

        when(chat.id()).thenReturn(id);
        when(message.chat().id()).thenReturn(id);

        when(message.text()).thenReturn(text);
        when(update.message().text()).thenReturn(text);
    }
}
