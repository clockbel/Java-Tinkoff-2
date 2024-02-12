package edu.java.bot.model.command_utils;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.model.command_utils.checks.CheckCommand;
import edu.java.bot.model.command_utils.commands.Command;
import edu.java.bot.repository.UserBase;
import java.util.Map;

public final class CommandStatus {
    private CommandStatus() {
    }

    public static void commandStatus(
        Update update,
        TelegramBot telegramBot,
        UserBase userBase,
        Map<String, Command> commands
    ) {
        BaseResponse response;
        String[] checkResult = CheckCommand.checkCommand(update.message());
        String inputCommand = checkResult[0];
        String checkMessage = checkResult[1];

        switch (inputCommand) {
            case "/start", "/help", "/list", "/track", "/untrack" -> {
                response = telegramBot.execute(commands.get(inputCommand).handle(update));
            }
            default -> {
                response = telegramBot.execute(new SendMessage(update.message().chat().id(), checkMessage));
            }
        }
    }
}
