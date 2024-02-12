package edu.java.bot.model.command_utils.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.command_utils.checks.CheckTrack;
import edu.java.bot.model.database_utils.User;
import edu.java.bot.model.url_utils.ParseURL;
import edu.java.bot.repository.UserBase;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component("/untrack")
public final class UntrackCommand implements Command {
    private final UserBase userBase;

    public UntrackCommand(UserBase userBase) {
        this.userBase = userBase;
    }

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Untrack your URL";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), messageFromCheckURL(userBase, update));
    }

    private String messageFromCheckURL(UserBase userBase, Update update) {
        String[] message = update.message().text().split(" ");
        Optional<User> userOptional = userBase.findById(update);
        String output = CommandsOutputMessage.USER_NOT_IN_BASE;
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<URL> uriList = userBase.getUserMap().get(update.message().chat().id()).getUriList();
            if (ParseURL.parseURL(message[1])) {
                try {
                    URL url = new URL(message[1]);
                    if (CheckTrack.checkURLInBase(uriList, url)) {
                        uriList.remove(url);
                        user.setUriList(uriList);
                        userBase.putUserToBase(user);
                        output = CommandsOutputMessage.URL_DELETE;
                    } else {
                        output = CommandsOutputMessage.URL_NOT_IN_BASE;
                    }
                } catch (Exception ignored) {
                }
            } else {
                output = CommandsOutputMessage.URL_INCORRECT;
            }
        }
        return output;
    }
}
