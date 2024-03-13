package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScrapperClientConfig {
    @Bean
    public ScrapperClient scrapperClient(ApplicationConfig appConf) {
        return ScrapperClient.create(appConf.scrapperUrl());
    }
}
