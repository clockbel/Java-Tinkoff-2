package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.model.command_utils.commands.Command;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BotConfig {
    @Bean
    public TelegramBot telegramBot(ApplicationConfig applicationConfig, Map<String, Command> commands) {
        TelegramBot bot = new TelegramBot(applicationConfig.telegramToken());
        bot.execute(commandsMenu(commands));
        return bot;
    }

    private SetMyCommands commandsMenu(Map<String, Command> commands) {
        return new SetMyCommands(commands.entrySet().stream()
            .map(entry -> new BotCommand(entry.getKey(), entry.getValue().description()))
            .toArray(BotCommand[]::new));
    }
}

