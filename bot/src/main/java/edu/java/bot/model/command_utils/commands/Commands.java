package edu.java.bot.model.command_utils.commands;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Commands {
    private Commands() {

    }

    public static Map<String, Command> commands() {
        StartCommand startCommand = new StartCommand(null);
        HelpCommand helpCommand = new HelpCommand();
        TrackCommand trackCommand = new TrackCommand(null);
        UntrackCommand untrackCommand = new UntrackCommand(null);
        ListCommand listCommand = new ListCommand(null);
        Map<String, Command> commandMap = new LinkedHashMap<>();
        commandMap.put(startCommand.command(), startCommand);
        commandMap.put(helpCommand.command(), helpCommand);
        commandMap.put(trackCommand.command(), trackCommand);
        commandMap.put(untrackCommand.command(), untrackCommand);
        commandMap.put(listCommand.command(), listCommand);
        return commandMap;
    }
}
