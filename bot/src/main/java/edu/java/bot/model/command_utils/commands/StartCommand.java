package edu.java.bot.model.command_utils.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.database_utils.User;
import edu.java.bot.repository.UserBase;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("/start")
@Qualifier("commands")
public final class StartCommand implements Command {
    private final UserBase userBase;
    private static final String COMMAND = "/start";
    private static final String DESCRIPTION = "Start the link tracker bot";

    public StartCommand(UserBase userBase) {
        this.userBase = userBase;
    }

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), checkRegistrationOfBot(update));
    }

    private String checkRegistrationOfBot(Update update) {
        if (userBase.findById(update).isEmpty()) {
            Long chatId = update.message().chat().id();
            List<URL> uriList = new ArrayList<>();
            User user = new User(chatId, uriList);
            userBase.putUserToBase(user);
            return CommandsOutputMessage.REGISTRATION_MESSAGE;
        }
        return CommandsOutputMessage.START_MESSAGE;
    }
}
