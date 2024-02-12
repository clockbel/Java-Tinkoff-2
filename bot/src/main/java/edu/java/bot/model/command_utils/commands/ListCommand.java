package edu.java.bot.model.command_utils.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.database_utils.User;
import edu.java.bot.repository.UserBase;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component("/list")
public final class ListCommand implements Command {
    private UserBase userBase;

    public ListCommand(UserBase userBase) {
        this.userBase = userBase;
    }

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "List of tracking URL";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), messageList(userBase, update));
    }

    private String messageList(UserBase userBase, Update update) {
        Optional<User> userOptional = userBase.findById(update);
        StringBuilder stringBuilder = new StringBuilder();
        if (userOptional.isPresent()) {
            stringBuilder.append("List URL:\n");
            List<URL> uriList = userBase.getUserMap().get(update.message().chat().id()).getUriList();
            if (!uriList.isEmpty()) {
                for (URL url : uriList) {
                    stringBuilder.append(url).append("\n");
                }
            }
        } else {
            stringBuilder.append("Please, write /start to registration");
        }
        return stringBuilder.toString();
    }
}
