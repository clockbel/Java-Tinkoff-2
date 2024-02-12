package edu.java.bot.model.command_utils.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component("/help")
public final class HelpCommand implements Command {
    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "List of command";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), helpMessage());
    }

    private String helpMessage() {
        Map<String, Command> commands = Commands.commands();
        return commands.entrySet().stream().map(entry -> entry.getKey() + " - " + entry.getValue().description() + "\n")
            .collect(
                Collectors.joining());
    }
}
