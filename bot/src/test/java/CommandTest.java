import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.BotApplication;
import java.util.Map;
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
public class CommandTest {
    @MockBean Update update;
    @Autowired UserBase userBase;
    @Autowired Map<String, Command> commands;

    @Order(0)
    @Test
    @DisplayName("Check base")
    void testBase() {
        var id_user2 = 2L;
        mockChat(id_user2);
        assertThat(userBase.findById(update)).isEmpty();
    }

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

    @Order(2)
    @Test
    @DisplayName("Start command 2")
    void testStart2() {
        var id_user2 = 2L;
        mockChat(id_user2);
        SendMessage message = commands.get("/start").handle(update);
        SendMessage result_message = new SendMessage(update.message().chat().id(), CommandsOutputMessage.START_MESSAGE);
        assertThat(message.getParameters()).isEqualTo(result_message.getParameters());
    }

    @Order(3)
    @Test
    @DisplayName("Track command 1")
    void testTrack1() {
        var id_user2 = 3L;
        mockChatWithText(id_user2, "1");
        SendMessage message = commands.get("/track").handle(update);
        SendMessage result_message =
            new SendMessage(update.message().chat().id(), CommandsOutputMessage.USER_NOT_IN_BASE);
        assertThat(message.getParameters()).isEqualTo(result_message.getParameters());
    }

    @Order(4)
    @Test
    @DisplayName("Track command 2")
    void testTrack2() {
        var id_user2 = 2L;
        mockChatWithText(id_user2, "/track 1");
        SendMessage message = commands.get("/track").handle(update);
        SendMessage result_message = new SendMessage(update.message().chat().id(), CommandsOutputMessage.URL_INCORRECT);
        assertThat(message.getParameters()).isEqualTo(result_message.getParameters());
    }

    @Order(5)
    @Test
    @DisplayName("Track command 3")
    void testTrack3() {
        var id_user2 = 2L;
        mockChatWithText(id_user2, "/track https://github.com/clockbel/Java-Tinkoff");
        SendMessage message = commands.get("/track").handle(update);
        SendMessage result_message = new SendMessage(update.message().chat().id(), CommandsOutputMessage.URL_APPEND);
        assertThat(message.getParameters()).isEqualTo(result_message.getParameters());
    }

    @Order(6)
    @Test
    @DisplayName("Track command 4")
    void testTrack4() {
        var id_user2 = 2L;
        mockChatWithText(id_user2, "/track https://github.com/clockbel/Java-Tinkoff");
        SendMessage message = commands.get("/track").handle(update);
        SendMessage result_message = new SendMessage(update.message().chat().id(), CommandsOutputMessage.URL_IN_BASE);
        assertThat(message.getParameters()).isEqualTo(result_message.getParameters());
    }

    @Order(7)
    @Test
    @DisplayName("Untrack command 1")
    void testUntrack1() {
        var id_user2 = 3L;
        mockChatWithText(id_user2, "1");
        SendMessage message = commands.get("/untrack").handle(update);
        SendMessage result_message =
            new SendMessage(update.message().chat().id(), CommandsOutputMessage.USER_NOT_IN_BASE);
        assertThat(message.getParameters()).isEqualTo(result_message.getParameters());
    }

    @Order(8)
    @Test
    @DisplayName("Untrack command 2")
    void testUntrack2() {
        var id_user2 = 2L;
        mockChatWithText(id_user2, "/untrack 1");
        SendMessage message = commands.get("/untrack").handle(update);
        SendMessage result_message = new SendMessage(update.message().chat().id(), CommandsOutputMessage.URL_INCORRECT);
        assertThat(message.getParameters()).isEqualTo(result_message.getParameters());
    }

    @Order(9)
    @Test
    @DisplayName("Untrack command 3")
    void testUntrack3() {
        var id_user2 = 2L;
        mockChatWithText(id_user2, "/untrack https://github.com/clockbel/Java-Tinkoff");
        SendMessage message = commands.get("/untrack").handle(update);
        SendMessage result_message = new SendMessage(update.message().chat().id(), CommandsOutputMessage.URL_DELETE);
        assertThat(message.getParameters()).isEqualTo(result_message.getParameters());
    }

    @Order(10)
    @Test
    @DisplayName("Untrack command 4")
    void testUntrack4() {
        var id_user2 = 2L;
        mockChatWithText(id_user2, "/untrack https://github.com/clockbel/Java-Tinkoff");
        SendMessage message = commands.get("/untrack").handle(update);
        SendMessage result_message =
            new SendMessage(update.message().chat().id(), CommandsOutputMessage.URL_NOT_IN_BASE);
        assertThat(message.getParameters()).isEqualTo(result_message.getParameters());
    }

    @Order(11)
    @Test
    @DisplayName("List command 1")
    void testList1() {
        var id_user2 = 2L;
        mockChatWithText(id_user2, "/track https://github.com/clockbel/Java-Tinkoff");
        commands.get("/track").handle(update);
        SendMessage message = commands.get("/list").handle(update);
        SendMessage result_message = new SendMessage(update.message().chat().id(), "List URL:\n" +
            "https://github.com/clockbel/Java-Tinkoff\n");
        assertThat(message.getParameters()).isEqualTo(result_message.getParameters());
    }

    @Order(12)
    @Test
    @DisplayName("List command 2")
    void testList2() {
        var id_user2 = 3L;
        mockChatWithText(id_user2, "/track https://github.com/clockbel/Java-Tinkoff");
        SendMessage message = commands.get("/list").handle(update);
        SendMessage result_message =
            new SendMessage(update.message().chat().id(), CommandsOutputMessage.USER_NOT_IN_BASE);
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
                    /start - Start the link tracker bot
                    /help - List of command
                    /track - Track URL
                    /untrack - Untrack your URL
                    /list - List of tracking URL
                    """);
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
