package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.command_utils.CommandStatus;
import edu.java.bot.model.command_utils.commands.Command;
import edu.java.bot.repository.UserBase;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;

@Controller
public class BotController implements UpdatesListener {
    private TelegramBot telegramBot;
    private UserBase userBase;
    private Map<String, Command> commandMap;

    public BotController(TelegramBot telegramBot, UserBase userBase, Map<String, Command> commandMap) {
        telegramBot.setUpdatesListener(this);
        this.telegramBot = telegramBot;
        this.userBase = userBase;
        this.commandMap = commandMap;
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            if (update.message() != null && update.message().text() != null) {
                CommandStatus.commandStatus(update, telegramBot, userBase, commandMap);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
