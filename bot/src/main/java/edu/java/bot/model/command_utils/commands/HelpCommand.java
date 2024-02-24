package edu.java.bot.model.command_utils.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("/help")
public final class HelpCommand implements Command {
    private final Map<String, Command> commands;
    private static final String COMMAND = "/help";
    private static final String DESCRIPTION = "List of command";
    private static final String DASH = " - ";

    public HelpCommand(@Qualifier("commands") Map<String, Command> commands) {
        this.commands = commands;
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
        return new SendMessage(update.message().chat().id(), helpMessage());
    }

    private String helpMessage() {
        return commands.entrySet().stream().map(entry -> entry.getKey() + DASH + entry.getValue().description() + "\n")
            .collect(
                Collectors.joining()) + command() + DASH + description();
    }
}
