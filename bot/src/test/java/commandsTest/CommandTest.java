package commandsTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.BotApplication;
import java.util.Map;
import edu.java.bot.model.command_utils.checks.CheckCommand;
import edu.java.bot.model.command_utils.commands.Command;
import edu.java.bot.repository.UserBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
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
public class CommandTest {
    @MockBean Update update;
    @Autowired UserBase userBase;
    @Autowired Map<String, Command> commands;
    @Test
    @DisplayName("Bad command 1")
    void testBadCommand1() {
        var id_user2 = 2L;
        mockChat(id_user2);
        commands.get("/start").handle(update);
        mockChatWithText(id_user2, "123");
        String[] checkMessage = CheckCommand.checkCommand(update.message());
        SendMessage message = new SendMessage(update.message().chat().id(), checkMessage[1]);
        SendMessage result_message =
            new SendMessage(update.message().chat().id(), "No such command was found, type /help to view available commands");
        assertThat(message.getParameters()).isEqualTo(result_message.getParameters());
    }
    @Test
    @DisplayName("Bad command 2")
    void testBadCommand2() {
        var id_user2 = 2L;
        mockChat(id_user2);
        commands.get("/start").handle(update);
        mockChatWithText(id_user2, "/track");
        String[] checkMessage = CheckCommand.checkCommand(update.message());
        SendMessage message = new SendMessage(update.message().chat().id(), checkMessage[1]);
        SendMessage result_message =
            new SendMessage(update.message().chat().id(), "No link entered after the command");
        assertThat(message.getParameters()).isEqualTo(result_message.getParameters());
    }
    @Test
    @DisplayName("Bad command 3")
    void testBadCommand3() {
        var id_user2 = 2L;
        mockChat(id_user2);
        commands.get("/start").handle(update);
        mockChatWithText(id_user2, "/track https://github.com/clockbel/Java-Tinkoff https://github.com/clockbel/Java-Tinkoff");
        String[] checkMessage = CheckCommand.checkCommand(update.message());
        SendMessage message = new SendMessage(update.message().chat().id(), checkMessage[1]);
        SendMessage result_message =
            new SendMessage(update.message().chat().id(), "More than 1 link entered");
        assertThat(message.getParameters()).isEqualTo(result_message.getParameters());
    }
    @Test
    @DisplayName("Correct command")
    void testCorrectCommand() {
        var id_user2 = 2L;
        mockChat(id_user2);
        commands.get("/start").handle(update);
        mockChatWithText(id_user2, "/track https://github.com/clockbel/Java-Tinkoff");
        String[] checkMessage = CheckCommand.checkCommand(update.message());
        SendMessage message = new SendMessage(update.message().chat().id(), checkMessage[1]);
        SendMessage result_message =
            new SendMessage(update.message().chat().id(), "OK");
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
