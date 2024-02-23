package edu.java.bot.model.command_utils.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.database_utils.User;
import edu.java.bot.repository.UserBase;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("/list")
@Qualifier("commands")
public final class ListCommand implements Command {
    private final UserBase userBase;
    private static final String COMMAND = "/list";
    private static final String DESCRIPTION = "List of tracking URL";
    private static final String LIST_URL_HEAD = "List URL:\n";
    private static final String LIST_URL_IS_EMPTY = "List URL is empty";

    public ListCommand(UserBase userBase) {
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
        return new SendMessage(update.message().chat().id(), messageList(userBase, update));
    }

    private String messageList(UserBase userBase, Update update) {
        Optional<User> userOptional = userBase.findById(update);
        StringBuilder stringBuilder = new StringBuilder();
        if (userOptional.isPresent()) {
            List<URL> uriList = userBase.getUserMap().get(update.message().chat().id()).getUriList();
            if (!uriList.isEmpty()) {
                stringBuilder.append(LIST_URL_HEAD);
                for (URL url : uriList) {
                    stringBuilder.append(url).append("\n");
                }
            } else {
                stringBuilder.append(LIST_URL_IS_EMPTY);
            }
        } else {
            stringBuilder.append(CommandsOutputMessage.USER_NOT_IN_BASE);
        }
        return stringBuilder.toString();
    }
}
