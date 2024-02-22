package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.model.command_utils.checks.CheckCommand;
import edu.java.bot.model.command_utils.commands.Command;
import edu.java.bot.repository.UserBase;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
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
                BaseResponse response;
                String[] checkResult = CheckCommand.checkCommand(update.message());
                String inputCommand = checkResult[0];
                String checkMessage = checkResult[1];
                switch (inputCommand) {
                    case "/start", "/help", "/list", "/track", "/untrack" -> {
                        response = telegramBot.execute(commandMap.get(inputCommand).handle(update));
                    }
                    default -> {
                        response = telegramBot.execute(new SendMessage(update.message().chat().id(), checkMessage));
                    }
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
