package edu.java.configuration;

import edu.java.client.BotClient;
import org.springframework.context.annotation.Bean;

public class BotClientConfiguration {
    @Bean
    public BotClient botClient(ApplicationConfig appConf) {
        return BotClient.create(appConf.botUrl());
    }
}
