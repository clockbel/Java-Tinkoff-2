package edu.java.bot.model.command_utils.checks;

import com.pengrad.telegrambot.model.Message;
import java.util.Objects;

public final class CheckCommand {
    private CheckCommand() {
    }

    private final static String CORRECT_COMMAND = "OK";
    private final static String INCORRECT_COMMAND =
        "Не обнаружено такой команды, для просмотра доступных комманд введите /help";
    private final static String EMPTY_INPUT_TRACK = "Не введена ссылка после комманды";
    private final static String MOST_INPUT_TRACK = "Введена больше 1 ссылки";

    public static String[] checkCommand(Message message) {
        String[] messageText = message.text().split(" ");
        String validateResult = validateCommand(messageText);
        if (!Objects.equals(validateResult, CORRECT_COMMAND)) {
            messageText[0] = "";
        }
        return new String[] {messageText[0], validateResult};
    }

    private static String validateCommand(String[] messageText) {
        String validateCheck;
        switch (messageText[0]) {
            case "/start", "/list", "/help" -> {
                if (messageText.length > 2) {
                    validateCheck = MOST_INPUT_TRACK;
                } else {
                    validateCheck = CORRECT_COMMAND;
                }
            }
            case "/track", "/untrack" -> {
                if (messageText.length > 2) {
                    validateCheck = MOST_INPUT_TRACK;
                } else if (messageText.length == 1) {
                    validateCheck = EMPTY_INPUT_TRACK;
                } else {
                    validateCheck = CORRECT_COMMAND;
                }
            }
            default -> {
                validateCheck = INCORRECT_COMMAND;
            }
        }

        return validateCheck;
    }
}
